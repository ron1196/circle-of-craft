<h1 align="center">The Lion King Mod — Developer Guide</h1>

<p align="center">
  <a href="https://github.com/ron1196/circle-of-craft/releases"><img src="https://img.shields.io/github/v/release/ron1196/circle-of-craft?style=flat-square&color=orange&label=latest%20release" alt="Latest Release" /></a>
  <img src="https://img.shields.io/badge/minecraft-1.20.1-green?style=flat-square" alt="Minecraft 1.20.1" />
  <img src="https://img.shields.io/badge/mod%20loader-Forge%2047.4.18-blue?style=flat-square" alt="Forge 47.4.18" />
  <img src="https://img.shields.io/badge/java-17-red?style=flat-square" alt="Java 17" />
  <a href="https://github.com/ron1196/circle-of-craft/blob/main/LICENSE"><img src="https://img.shields.io/github/license/ron1196/circle-of-craft?style=flat-square" alt="License" /></a>
</p>

<p align="center">
  <a href="https://www.curseforge.com/minecraft/mc-mods/circle-of-craft"><img src="https://img.shields.io/curseforge/dt/circle-of-craft?style=flat-square&logo=curseforge&color=f16436&label=CurseForge" alt="CurseForge" /></a>
  <a href="https://modrinth.com/mod/circle-of-craft"><img src="https://img.shields.io/modrinth/dt/circle-of-craft?style=flat-square&logo=modrinth&color=00af5c&label=Modrinth" alt="Modrinth" /></a>
</p>

> Looking for the user-facing mod description (CurseForge / Modrinth listing copy)? See **[MOD_PAGE.md](MOD_PAGE.md)**.

---

## Stack

- **Minecraft:** 1.20.1
- **Mod loader:** Forge 47.4.18 (NeoForge migration planned for the 1.21.x jump)
- **Java:** 17
- **Mappings:** Official (Mojang)
- **Mod ID / package:** `circleofcraft` / `io.github.ron1196.circleofcraft`

## Runtime dependencies

Declared in `src/main/resources/META-INF/mods.toml` (the source of truth — `build.gradle` pins for compile, `mods.toml` enforces at boot, CF/Modrinth metadata declares for the storefront):

| Mod | Required? | Range | Notes |
|---|---|---|---|
| GeckoLib | ✅ required | `[4.8,)` | Animation runtime. Compiled against `4.8.3`. |
| JEI      | ❌ optional | `[15.20,)` | Recipe lookup; Grinding Bowl integration. Compiled against `15.20.0.105`. |
| Jade     | ❌ optional | `[11.6,)`  | HUD tooltips; AnimalFavorProvider integration. |

When bumping a compile-time version in `build.gradle`, raise the corresponding `mods.toml` `versionRange` lower bound to match, so missing-API surprises surface at boot rather than as a runtime `NoSuchMethodError`.

## Repository layout

```
src/main/java/io/github/ron1196/circleofcraft/
  block/        block classes (+ block/entity/ block entities)
  client/       renderers, models, particles, GUIs (Dist.CLIENT)
  command/      /coc command tree
  compat/       JEI + Jade integrations
  data/         WorldData, PlayerData, criteria triggers
  entity/       animals, hostiles, NPCs, projectiles, AI goals
  event/        Forge + mod-bus event handlers
  gametest/     Mojang Game Tests (in-world integration coverage)
  item/         items, armor, tools, projectile items
  mixin/        Mixin-based vanilla patches
  network/      packets
  quest/        questlines, stages, action helpers
  registry/     deferred-register holders (ModItems, ModBlocks, …)
  world/        biomes, dimensions, features, structures
src/main/resources/
  META-INF/mods.toml
  assets/circleofcraft/  textures, models, blockstates, lang, sounds, geo, animations
  data/circleofcraft/    advancements, recipes, loot_tables, worldgen, tags
src/test/java/         JUnit 5 unit tests (pure-Java logic)
old/                   read-only reference: the original MC 1.4–1.6 mod
```

`CLAUDE.md` is the canonical reference for code conventions and architectural rules (quest action helpers, `WorldData.get` routing, NBT contracts, NPC chat helpers, etc.). Read it before touching new areas.

## Build & run

Always export Java 17 first:

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home   # macOS
```

| Command | What it does |
| --- | --- |
| `./gradlew compileJava` | Fast compile-only check |
| `./gradlew build` | Full build + JUnit tests (artifact lands in `build/libs/`) |
| `./gradlew test` | JUnit only (pure-Java logic) |
| `./gradlew runGameTestServer` | Run Mojang Game Tests (in-world coverage) |
| `./gradlew runClient` | Launch Minecraft with the mod loaded |
| `./gradlew runServer` | Launch a dedicated server with the mod loaded |
| `./gradlew runData` | Run data generators |
| `./gradlew spotlessApply` | Auto-format Java with the Palantir style |

On macOS, `runGameTestServer` may need `--no-build-cache` to avoid a Gradle build-cache packer failure on class files carrying extended attributes (`Could not get file mode for ...`):

```bash
./gradlew runGameTestServer --no-build-cache
```

## Testing

The project ships **two complementary test frameworks** — see [`docs/AUTOMATED_TESTING.md`](docs/AUTOMATED_TESTING.md) for the full guide.

- **JUnit 5** (`src/test/java/`) — pure-Java logic. No Minecraft runtime. ~2 s.
- **Mojang Game Tests** (`src/main/java/.../gametest/`) — anything touching `Level`, `Entity`, `BlockState`, NBT, or Forge registries. ~30 s.

Quick picker: if the code under test touches Minecraft types or any class transitively loading `ForgeRegistries`, use a Game Test. Otherwise prefer JUnit.

CI runs format-check, build (incl. JUnit), and Game Tests on every push/PR (`.github/workflows/ci.yml`).

## Formatting

Java is auto-formatted by Spotless (Palantir). CI rejects unformatted code. Run `./gradlew spotlessApply` after Java edits.

## Conventions (the short list)

These are summaries — `CLAUDE.md` has the full rationale.

- **`@Override` methods** carry `@NotNull` / `@Nullable` (`org.jetbrains.annotations`) on every parameter and the return type.
- **Long method signatures** wrap one parameter per line at 8-space indent, with `) {` on its own line.
- **`WorldData.get(anyServerLevel)`** routes to the overworld; never call `level.getDataStorage()` directly. Quest state must be shared across dimensions.
- **Quest state is derived from `QuestlineManager.getStage()`** — never store boolean flags that duplicate it. Use `EnumSet` for stage ranges; never compare `ordinal()`.
- **Quest stage → world-state mapping** lives in `quest/actions/*QuestActions.ensureWorldState(level, stage)` — idempotent, called from `customTransition`, `/coc quest set`, and the ~100-tick entity fallback.
- **When inserting a new stage into `OutlandsQuestline.Stage`**, audit `TREE_OCCUPATION_STAGES` in `OutlandsQuestActions.java` — missing it causes Rafiki to spawn prematurely or Zira's tree corruption to toggle incorrectly.
- **NPC chat** goes through `ChatHelper.sendNpcMessage` / `broadcastNpcMessage` — never inline `§e<Name> §f` formatting.
- **Workaround policy:** every "for now" substitution must be filed as a GitHub issue. No silent TODOs.

## Quest testing commands

`/coc quest` bypasses triggers/items for fast iteration:

```
/coc quest info <questId>
/coc quest advance <questId>
/coc quest set <questId> <stageKey>
/coc quest reset <questId>
```

Quest IDs: `rafiki`, `outlands`. Stage names match the enum values (e.g., `FIND_RAFIKI`, `COLLECT_BONES`).

Use the `/quest-skip-check` Claude skill (see `.claude/`) to verify every stage follows the `ensureWorldState` idempotency pattern.

## Key files

| File | Purpose |
| --- | --- |
| `event/CommonEvents.java` | Entity attributes + spawn placement rules (mod bus) |
| `event/ModForgeEvents.java` | Forge bus events: combat, NPC interaction, breeding, chunk/world ticks |
| `data/WorldData.java` | World-level saved data (overworld-backed), quest-derived state |
| `data/ModCriteriaTriggers.java` | Custom advancement triggers |
| `quest/questline/QuestlineManager.java` | Single source of truth for quest stage transitions |
| `quest/actions/*QuestActions.java` | Stage → world-state side effects (one per questline) |

## Contributing

Issues and pull requests are welcome. Before opening a PR:

1. `./gradlew spotlessApply` — format
2. `./gradlew build` — compile + JUnit
3. `./gradlew runGameTestServer` — in-world coverage
4. Read `CLAUDE.md` if you're touching a new subsystem

See [GitHub Issues](https://github.com/ron1196/circle-of-craft/issues) for the active workaround / feature backlog.

## License

See [LICENSE](LICENSE) for details.
