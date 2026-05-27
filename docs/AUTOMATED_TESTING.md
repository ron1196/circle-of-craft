# Automated Testing Guide

This mod uses two separate, complementary automated test frameworks. Pick the right one for what you're verifying.

---

## TL;DR — which framework do I use?

| Question | Answer |
|---|---|
| Is the thing under test pure Java with no Minecraft types? | **JUnit** |
| Does it touch `Level`, `Entity`, `BlockState`, AI goals, or NBT? | **Game Test** |
| Both? | Test the pure-Java seam with JUnit, the in-world wiring with a Game Test |

| Run | Command | Speed | Where it lives |
|---|---|---|---|
| Compile only | `./gradlew compileJava` | seconds | — |
| JUnit (pure-Java) | `./gradlew test` | ~2 s | `src/test/java/...` |
| Game Tests (in-world) | `./gradlew runGameTestServer` | ~30 s | `src/main/java/.../gametest/...` |
| Full build (compiles + JUnit) | `./gradlew build` | ~30 s | runs both compile and `test` |

Always set `JAVA_HOME` first:

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
```

---

## Framework 1 — JUnit 5 (pure-Java unit tests)

### What it's for

Logic that has zero dependency on Minecraft. Predicates, parsers, mathematical helpers, anything you can prove correct with input → output assertions.

**Concrete example in this codebase:** `BreedingRules.canBreed(Gender, Gender)` — a pure function that returns `a != b`. Four test cases cover every input pair. No Minecraft runtime needed.

### Where it lives

```
src/test/java/io/github/ron1196/circleofcraft/entity/animal/BreedingRulesTest.java
```

Mirror the production package path under `src/test/java/`. JUnit dependency is wired in `build.gradle`:

```groovy
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.9.2'
}

test {
    useJUnitPlatform()
}
```

### Skeleton

```java
package io.github.ron1196.circleofcraft.entity.animal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BreedingRulesTest {

    @Test
    void maleAndFemaleCanBreed() {
        assertTrue(BreedingRules.canBreed(Gender.MALE, Gender.FEMALE));
    }

    @Test
    void twoMalesCannotBreed() {
        assertFalse(BreedingRules.canBreed(Gender.MALE, Gender.MALE));
    }
}
```

Naming convention: `<Subject>Test`, package-private class, methods named after the *behaviour* being verified (not "test1", "test2"). Methods take no arguments and return `void`.

### How to run

```bash
./gradlew test
```

Silent on success. Failures print the assertion error and the failing test name. Reports land in `build/reports/tests/test/index.html`.

### When NOT to use JUnit

- Anything that requires `Level`, `EntityType.create(level)`, `BlockEntity`, NBT save/load round-trip, or AI ticking.
- Anything that needs a registry to be loaded (any `RegistryObject.get()` call inside the production code being tested).
- Anything that needs item/block tags resolved.

If you try, you'll hit `NullPointerException` because Minecraft's static state is uninitialised. That's your signal to switch to a Game Test.

---

## Framework 2 — Mojang Game Tests (in-world integration tests)

### What it's for

Behaviour that requires a running Minecraft server: entity AI, gender NBT round-trips, breeding mechanics, mane visibility, multi-tick interactions.

**Concrete examples in this codebase** (all in `LionGameTests.java`):

- `babyMaleIsManeless` / `adultMaleIsManed` — verify `LionEntity.shouldShowMane()` with a real entity instance
- `oppositeGenderBreeds` — spawn a male + female, set both in love, run the world until a cub appears
- `sameGenderDoesNotBreed` — same setup with two males, verify *no* cub after a bounded wait
- `maleCubGainsManeOnAgeUp` — call `setAge(0)` on a cub, assert the mane visibility flips

### How it works under the hood

`./gradlew runGameTestServer` boots a special headless server (`GameTestServer`). For every `@GameTest`-annotated method:

1. The server spawns an instance of the test's `template` structure to act as a fresh arena.
2. It calls your test method, passing a `GameTestHelper` scoped to that arena.
3. It ticks the world for up to `timeoutTicks`. Your code calls `helper.succeed()` (pass), `helper.fail(...)` (terminal fail), or one of the deferred patterns described below.
4. The arena is torn down before the next test.

Multiple tests run in parallel arenas spread across the world. The status string `[+_++++_]` in the log shows each arena's state: `+` passed, `_` pending, `X` failed.

### Where it lives

| Piece | Path | Notes |
|---|---|---|
| Test holder class | `src/main/java/.../gametest/<Feature>Tests.java` | Note: `src/main/java`, not `src/test/java` — Forge needs it on the runtime classpath |
| Arena structure | `src/main/resources/data/circleofcraft/structures/<name>.nbt` | **Vanilla `data/<ns>/structures/` path — NOT a `gametest/` subfolder** |
| Run config | `build.gradle` `gameTestServer` block | Generates the `runGameTestServer` task |
| Namespace enable | `build.gradle` `forge.enabledGameTestNamespaces` property | Already set on `client`, `server`, and `gameTestServer` runs |

### Required class-level annotations

```java
@GameTestHolder("circleofcraft")
@PrefixGameTestTemplate(false)
public class LionGameTests {
    private static final String EMPTY = "empty";    // unqualified — namespace comes from @GameTestHolder
    ...
}
```

| Annotation | Why |
|---|---|
| `@GameTestHolder("circleofcraft")` | Forge auto-discovers this class because the namespace is enabled in `forge.enabledGameTestNamespaces`. No manual registration needed. |
| `@PrefixGameTestTemplate(false)` | **Required.** Without it, Forge auto-prepends the lowercased class name to every template path — so `"empty"` becomes `"liongametests.empty"`, which fails to load. |

### Skeleton — a trivial mane-rule test

```java
@GameTest(template = EMPTY, timeoutTicks = 40)
public void babyMaleIsManeless(GameTestHelper helper) {
    LionEntity lion = helper.spawn(EntityTypes.LION.get(), new BlockPos(2, 2, 2));
    lion.setBaby(true);
    lion.setGender(Gender.MALE);
    if (lion.shouldShowMane()) {
        helper.fail("baby male should be maneless", lion);
    }
    helper.succeed();
}
```

### Three deferred-completion patterns — pick the right one

Most non-trivial Game Tests need to wait for something to happen across multiple ticks. There are three patterns; using the wrong one is the most common bug.

**Pattern 1 — `helper.succeed()` (immediate, synchronous test):** the test does its setup, asserts, and passes in one call. Use when nothing needs to tick. Example: the mane-rule tests above.

**Pattern 2 — `helper.runAfterDelay(ticks, runnable)` (wait-then-check):** wait exactly N ticks, then run a single check. Use when you want to verify *no* state change occurs over a window — for negative tests.

```java
@GameTest(template = EMPTY, timeoutTicks = 200)
public void sameGenderDoesNotBreed(GameTestHelper helper) {
    LionEntity a = helper.spawn(...); a.setInLoveTime(600);
    LionEntity b = helper.spawn(...); b.setInLoveTime(600);

    helper.runAfterDelay(180, () -> {
        if (countCubsInArena(helper) > 0) helper.fail("two males should not produce a cub");
        helper.succeed();
    });
}
```

**Pattern 3 — `helper.succeedWhen(runnable)` (poll-until-true):** run the body *every tick*. The test passes the moment the body completes without throwing; it fails if `timeoutTicks` expires first. Use when a positive event will eventually happen but you don't know exactly when.

```java
@GameTest(template = EMPTY, timeoutTicks = 600)
public void oppositeGenderBreeds(GameTestHelper helper) {
    LionEntity male = helper.spawn(...); male.setGender(Gender.MALE);   male.setInLoveTime(600);
    LionEntity female = helper.spawn(...); female.setGender(Gender.FEMALE); female.setInLoveTime(600);

    helper.succeedWhen(() ->
        helper.assertTrue(countCubsInArena(helper) >= 1, "expected at least 1 cub")
    );
}
```

### CRITICAL: `assertTrue` vs `fail` inside `succeedWhen`

`succeedWhen` runs the body each tick. The body's exception type decides what happens:

| Body throws | Outcome |
|---|---|
| Nothing (returns normally) | Test passes |
| `GameTestAssertException` (via `helper.assertTrue(false, ...)`) | Test silently *retries* on the next tick |
| Anything else (including `helper.fail(...)`) | Test fails *immediately, terminally* |

**Use `helper.assertTrue(condition, message)` inside `succeedWhen`, never `helper.fail(...)`.** Calling `fail` permanently kills the test on the first tick before the awaited event has had a chance to occur.

### Querying the arena

`GameTestHelper.getBounds()` is `private`. Use `absolutePos` to construct your own AABB:

```java
private static long countCubsInArena(GameTestHelper helper) {
    BlockPos min = helper.absolutePos(new BlockPos(0, 0, 0));
    BlockPos max = helper.absolutePos(new BlockPos(4, 4, 4));
    AABB bounds = new AABB(min, max);
    return helper.getLevel()
            .getEntitiesOfClass(LionEntity.class, bounds, LionEntity::isBaby)
            .size();
}
```

### Generating an empty arena structure NBT

There is no built-in "empty arena" template; you must ship one. The 5×5×5 air arena `data/circleofcraft/structures/empty.nbt` was generated with this Python (run once, commit the binary):

```python
from nbtlib import Compound, File, Int, List, String

File({
    "DataVersion": Int(3465),                            # 1.20.1
    "size": List[Int]([Int(5), Int(5), Int(5)]),
    "palette": List[Compound]([Compound({"Name": String("minecraft:air")})]),
    "blocks": List[Compound]([]),
    "entities": List[Compound]([]),
}).save("src/main/resources/data/circleofcraft/structures/empty.nbt", gzipped=True)
```

Future tests can reuse the same `EMPTY` template — no need to ship one per test unless you need actual blocks in the arena.

### How to run

```bash
./gradlew runGameTestServer
```

Final output line is the verdict:

```
========= 7 GAME TESTS COMPLETE ======================
All 7 required tests passed :)
```

Or:

```
1 required tests failed :(
   - oppositegenderbreeds
```

Working directory is `run-gametest/` (separate from the manual `runServer`'s `run/` so playtest worlds aren't disturbed). Crash reports land in `run-gametest/crash-reports/` and the full server log is at `run-gametest/logs/latest.log`.

---

## When the same feature needs both

If a feature has both a pure-Java seam and an in-world wiring, write *both* a JUnit test and a Game Test. The lion-merge feature is the example to follow:

- `BreedingRulesTest` (JUnit) — proves `canBreed(Gender, Gender)` is correct in isolation. Catches typos like `==` vs `!=` instantly.
- `LionGameTests.oppositeGenderBreeds` / `.sameGenderDoesNotBreed` (Game Test) — proves the AI loop actually calls `canBreedWith` at the right point. Catches wiring regressions like "we forgot to override `canMate`".

Each test type catches bugs the other can't see. Together they're cheap insurance.

---

## CI / pre-merge checklist

Run all three before opening a PR:

```bash
./gradlew compileJava            # cheap, catches obvious breakage
./gradlew test                   # JUnit, ~2 s
./gradlew runGameTestServer      # in-world, ~30 s
```

If any fail, fix before pushing. Game Tests are slow enough that you'd typically wire them into nightly CI rather than every commit, but they're fast enough to run locally on every meaningful change.

---

## Lessons learned (don't repeat these)

These were the bugs we hit while building `LionGameTests`. Each is a one-line gotcha but cost time to diagnose:

1. **Forge auto-prefixes the class name to template paths.** `"empty"` becomes `"liongametests.empty"`. Fix: `@PrefixGameTestTemplate(false)` on the holder class.
2. **`@GameTestHolder("circleofcraft")` already provides the namespace.** Don't write `template = "circleofcraft:empty"` — Forge will produce `circleofcraft:circleofcraft:empty`. Use `template = "empty"`.
3. **Structure NBTs go at `data/<ns>/structures/<name>.nbt`.** Not `data/<ns>/gametest/structures/`. Vanilla's `StructureManager` only checks the standard path.
4. **`succeedWhen` retries on `GameTestAssertException`, not on `helper.fail()`.** Use `assertTrue(cond, msg)` inside `succeedWhen`, never `fail(msg)`.
5. **`GameTestHelper.getBounds()` is `private`.** Use `absolutePos(BlockPos)` to construct an AABB yourself.
