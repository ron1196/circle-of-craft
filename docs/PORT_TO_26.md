# Port Plan: NeoForge 1.21.1 → 26.x

Everything that has to happen to move `mc/1.21.1` onto Minecraft 26.x, in the order it
has to happen. Written before any port work started; update it as reality disagrees.

**Update — Phase 1 spiked on `mc/26.1`, then a first mechanical-fixes pass done on top:**
toolchain bump done, dependencies resolved, and a chunk of the safe/mechanical errors fixed.
See "Spike results" and "Mechanical fixes pass" below for real numbers in place of the
estimates. **Correction:** the first spike's "100 errors / 47 files" figure undercounted —
javac's default `-Xmaxerrs 100` was silently truncating the real error list. The true
starting count was **1383 errors across 209 of 319 files (~65%)**. `build.gradle` now sets
`-Xmaxerrs 100000` so this can't happen again.

---

## TL;DR

| | |
|---|---|
| Shipping target | **26.2** |
| Intermediate checkpoint | **26.1** — built, run, and playtested before continuing |
| Route | `mc/1.21.1` → `mc/26.1` → `mc/26.2` (two branches, two ports) |
| Biggest single lever | **Write datagen providers first, on `mc/1.21.1`** — converts ~1400 hand-written JSONs into code |
| Hard prerequisite | Land or abandon all in-flight feature branches first |
| Realistic effort | 90–160 focused hours |

---

## Why 26.1 as a checkpoint, and 26.2 as the target

The renderer rewrite is **not** avoidable by stopping at 26.1. The immediate-mode →
submission rendering model (`render(...)` → `submit(EntityRenderState, PoseStack,
SubmitNodeCollector, CameraRenderState)`) landed in **1.21.9**, which is upstream of 26.1.
Every one of our 21 entity renderers and 6 block-entity renderers gets rewritten no matter
which of the two we land on.

So 26.1 is not a way to spend less. It is a way to spend it **in two verifiable halves**:

- 26.1 is the deobfuscation boundary. Everything below it is obfuscated with a decade of
  accumulated community porting knowledge; everything above it is a different world.
  Landing exactly on that line gives a clean "the logic layer is proven correct" checkpoint.
- The 26.1 ecosystem is where packs and reference mods are consolidating right now, so
  when something breaks there's a good chance someone has already hit it.
- The 26.1 → 26.2 delta is large but **narrow**: Vulkan/OpenGL abstraction split, vertex
  formats built dynamically, font drawing via `PreparedText`/`GlyphVisitor`, GUI state moved
  off `Minecraft` onto `Gui`, item/block collections consolidated. It is almost entirely
  client-side and almost entirely mechanical once the game logic is known-good.

Debugging a 12-version logic port *and* a graphics-API abstraction simultaneously is how
ports die. Checkpoint at 26.1.

**Reconsider this if:** by the time we start, 26.2 has clearly displaced 26.1 in the pack
ecosystem. Then go straight to 26.2 and accept the harder debug.

---

## What we're actually crossing

Twelve breaking versions. Only the ones that hit *this* codebase are listed.

| Version | What breaks for us | Blast radius |
|---|---|---|
| **1.21.2** | `EntityRenderState` architecture — renderers need `createRenderState()`/`extractRenderState()`; `EntityModel` generics take a render state, `setupAnim()` takes state only, `prepareMobModel()`/`copyPropertiesTo()` gone. Block/Item `Properties` must `setId()`. `Tier` → `ToolMaterial`. `ArmorMaterial` → record + `EQUIPPABLE` component. Food → `Consumable` component. Recipes become a datapack registry. | 22 models, 21 renderers, 40 blocks, 36 items |
| **1.21.4** | Client Items system — item models move to `assets/circleofcraft/items/*.json` with `ItemModel$Unbaked` types. `ItemColors`, `ItemOverride`, `BakedOverrides`, `BlockEntityWithoutLevelRenderer` all removed. Particles render via `RenderType`. | 595 model JSONs, `HyenaHeadItemRenderer`, `ColoredPortalParticle` |
| **1.21.5** | Equipment overhaul, `SimpleEquipmentLayer`, `ItemSubPredicate` → `DataComponentPredicate` (package move `critereon` → `core.component.predicates`), entity variants become datapack registry objects, `ClientAsset` replaces raw IDs. | advancements (225 files), `GiraffeOverlayLayer` |
| **1.21.9** | **The render rewrite.** `MultiBufferSource` → `SubmitNodeCollector`; two-phase submit/dispatch. Block entities get their own `BlockEntityRenderState`. Model part *visibility* removed — parts become separate model instances. `TextureSheetParticle` → `SingleQuadParticle`. `GuiGraphics.renderX()` → `submitX()`. `PlayerRenderer` → `AvatarRenderer`. | all 21 renderers, 6 BER, 22 models, 11 GUI files, particles |
| **1.21.11** | `ResourceLocation` → `Identifier` everywhere. `critereon` → `criterion`. ~150 entity/model classes reorganized into subpackages. `RenderType` built via `RenderSetup`/`RenderPipeline`. Item textures on their own atlas. | 53 files (mechanical), all model imports |
| **26.1** | **Java 25.** Deobfuscation — all parameter names change. `ItemStack` split into mutable `ItemStack` / immutable `ItemStackTemplate` + `ItemInstance`. Loot types unrolled to bare `MapCodec`, `getType()` → `codec()`. `RecipeSerializer` becomes a record. Villager trades become a data registry. Day/time → world clocks via `ClockManager`. `Validatable` replaces `CriterionValidator`. Data components lazily init via `DataComponentInitializers`. | **109 files use ItemStack**, 8 use `getDayTime/getGameTime`, loot/recipe/advancement data, `ModCriteriaTriggers` |
| **26.2** | Vulkan parallel class hierarchy under `com.mojang.blaze3d.vulkan`, `FeatureRenderer`/`SubmitNode`/`FeatureRenderPhase` finish replacing `MultiBufferSource`, dynamic vertex format construction, font pipeline via `PreparedText`, GUI state on `Gui`, item/block constants consolidated into collections. | client layer again, narrower |

---

## Phase 0 — Prerequisites (do these BEFORE touching the toolchain)

Nothing here is port work. All of it makes the port possible.

### 0.1 Clear the runway

Unmerged work that will become unportable the moment `mc/1.21.1` stops moving:

- `origin/feat/zebra-boots-1.21.1` — 4 commits ahead (current branch)
- `origin/feature/scar-locating-and-ancestors-sight` — 1 commit ahead
- `origin/mc/1.21.1` — 5 commits ahead of local

**Action:** merge these into `mc/1.21.1` or explicitly abandon them. A feature branch written
against 1.21.1 APIs cannot be cherry-picked across the port — it has to be rewritten from
scratch. Decide now, while the diff is small.

### 0.2 Decide the fate of `mc/1.20.1`

`feat/zebra-boots-1.20.1` and `feat/banana-ripening-stages-1.20.1` are 31 and 27 commits ahead.
Maintaining three Minecraft versions by hand is not realistic alongside a port of this size.
**Recommendation:** land what's outstanding on 1.20.1, ship a final release, then freeze it.
Say so in `docs/RELEASES.md` so future-you doesn't wonder.

### 0.3 Close the issues the port would otherwise multiply

- **#13** — "1.21.2 Migration: replace deprecated `BlockBehaviour.onRemove()`
  (`affectNeighborsAfterRemoval`)". This is literally the first version we cross. Do it now,
  on 1.21.1, where it's a 3-block change against a working build.
- **#26** — "Post-v1 migration: helper libraries to evaluate on NeoForge 1.21.x". Resolve
  *before* porting. Adopting a helper library mid-port means two moving targets; adopting one
  after means porting code you're about to delete.
- **#57** — "replace 1135-line static ItemInfo map with per-item lang/data files". A 1135-line
  static map full of `ItemStack`/component references is exactly the shape that the 26.1
  `ItemStack`/`ItemStackTemplate` split will make painful. Fixing it first shrinks the port.
- **#35** (Simba Blender model) and **#78** (Dart Quiver, currently commented out) — either
  land them on 1.21.1 or accept they get rewritten against the new model API. Don't leave
  commented-out code to port.

### 0.4 Baseline the current behaviour

The gametests are the only thing that will tell us the quest state machine survived. Before
starting:

- Confirm all 12 gametest classes and 13 JUnit tests are green on `mc/1.21.1`.
- Record a manual playtest checklist (see Phase 5) *from the working 1.21.1 build*, so there's
  a known-good reference to diff against. Screenshot every custom GUI and every entity.
- Note: `src/main/resources/golden/` exists — confirm what it covers and whether those golden
  files survive the JSON format migrations.

### 0.5 The big lever: write datagen providers on 1.21.1 first

**This is the highest-value thing on the entire list.**

Right now `runData` is wired up in `build.gradle` and `src/generated/resources` is registered
as a resource source dir — but **`src/generated/` is empty and there is not a single
`DataProvider` implementation in the codebase** (the only `*Provider*` classes are Jade compat).
All ~1400 data files are hand-written:

| Kind | Files | Format changes ahead |
|---|---|---|
| `assets/.../models` | 595 | 1.21.4 client items, 1.21.11 atlas split |
| `assets/.../blockstates` | 159 | minor |
| `data/.../advancement` | 225 | 1.21.5 predicates, 26.1 `Validatable` |
| `data/.../recipe` | 219 | 1.21.2 datapack registry, 26.1 serializer records |
| `data/.../loot_table` | 167 | 26.1 loot type unrolling |
| `data/.../worldgen` | 88 | moderate |
| `tags` (ours + minecraft) | 54 | minor |

Hand-migrating 1400 JSONs across six format changes is somewhere between miserable and
impossible. Generating them means each format change is a provider edit plus `runData`.

**Do this on `mc/1.21.1`, before the port**, so it can be validated against a build that
already works — generate, then diff the output against the checked-in files until they match
byte-for-byte, then delete the hand-written originals. That diff-until-identical step is what
makes it safe.

Order: models + blockstates (biggest win) → recipes → loot tables → advancements → tags.
Worldgen JSON is the one place hand-authoring may still win; judge it when you get there.

Realistically **20–35 hours**. It will feel like a detour. It is not — it's most of the port's
data work, done against a compiler and a test suite that currently pass.

---

## Phase 1 — Toolchain (`mc/26.1` branch, day one)

```bash
git checkout mc/1.21.1
git checkout -b mc/26.1
```

1. **JDK 25.** Install it, update `java.toolchain.languageVersion`, and update the
   `JAVA_HOME` line in `CLAUDE.md` (currently pins `openjdk@21`).
2. **`gradle.properties`** — `minecraft_version=26.1`, `minecraft_version_range`,
   `neo_version` (four-component now: `26.1.0.<build>`), `neo_version_range`.
   `docs/RELEASES.md` says this file is the branch's source of truth; the release workflow
   validates tags against it, so get it right before the first tag.
3. **Build plugin decision: NeoGradle vs ModDevGradle.** We're on
   `net.neoforged.gradle.userdev` 7.0.192. Both plugins are still endorsed for 26.1; MDG is
   the simpler/streamlined one, NeoGradle is for multi-version projects — which, given
   branch-per-version, we are explicitly *not*. **Lean ModDevGradle**, but pull a 26.1 MDK
   from the NeoForge mod generator for both and compare against our `runs {}` block first;
   we have five run configs (`client`, `client2`, `server`, `gameTestServer`, `data`) that all
   need equivalents.
4. **Gradle wrapper** — 8.14.4 today; bump to whatever the 26.1 MDK ships with.
5. **Spotless / Palantir** — 6.25.0 / palantir 2.39.0 almost certainly can't parse Java 25.
   Bump both. Expect one large reformat commit; **do it as its own commit** so it doesn't
   pollute port diffs.
6. **The mixin AP workaround block can probably go.** The `-AMSG_NO_OBFDATA_*=note` compiler
   args exist because the SpongePowered AP demands obf mappings that Mojmap doesn't provide.
   With obfuscation removed entirely in 26.1, re-test whether that block is still needed —
   and if it is, whether `OutlandsFluidMixin`'s `@WrapOperation` target
   (`Aquifer$NoiseBasedAquifer#computeSubstance`) even still exists. **Verify this early**;
   the Outlands dimension's cave generation depends on it, and if the target moved, that's a
   design problem, not a rename.
7. **CI** — `.github/workflows/ci.yml` pins `java-version: '21'` in all three jobs. Bump.
   `release.yml` too. Both workflows trigger on `mc/**`, so `mc/26.1` is picked up automatically.
8. **Dependency bumps:**
   - GeckoLib `4.8.4` → `5.5.2` (NeoForge 26.1.2). Note the **major** version jump — the
     GeckoLib 4 → 5 API changed independently of Minecraft. Only 3 files touch it, but check
     the 2 files in `assets/circleofcraft/animations/` and 3 in `geo/` for format changes.
   - JEI `19.27.0.340` → `29.21.0.66` (NeoForge 26.1.2)
   - Jade `15.10.0+neoforge` → `26.1.8`
   - MixinExtras, JUnit — routine.
9. **`neoforge.mods.toml`** — check for schema changes; it's templated through
   `ProcessResources`, so the `replaceProperties` map may need new keys.

**Gate: `./gradlew compileJava` runs and produces errors from *our* code, not from Gradle.**

---

## Phase 2 — Common layer (do not open a renderer yet)

Port in dependency order. The rule for this whole phase: **if it imports anything from
`net.minecraft.client`, skip it.** The goal is a compiling, testable server-side mod.

1. **Mechanical sweep first.** Script the renames — `ResourceLocation` → `Identifier`,
   `critereon` → `criterion`, entity/model subpackage moves, `net.minecraft.util` moves.
   These are ~53 files plus imports and should be one reviewable commit that changes no logic.
2. **`registry/`** (17 files) — `DeferredRegister`/`DeferredHolder` shape, and the 1.21.2
   `setId()` requirement on block and item `Properties` (`DeferredRegister.Blocks#registerBlock`
   handles it if we adopt it).
3. **`block/`** (40) — properties, `affectNeighborsAfterRemoval` (issue #13).
4. **`item/`** (36) — the heavy one. `ToolMaterial` replacing `Tier`, `ArmorMaterial` as a
   record + `EQUIPPABLE`, food → `Consumable` component, and then the 26.1
   `ItemStack`/`ItemStackTemplate` split rippling through **109 files**. Expect this to be
   the largest single chunk of the port. Work out the `ItemInstance` pattern on two or three
   items, write it down, then apply it uniformly.
5. **`data/`** (5) — `WorldData`/`SavedData`. Keep the `WorldData.get(anyServerLevel)` overworld
   routing invariant from `CLAUDE.md` intact; verify `SavedData` codec registration changed shape.
   `ModCriteriaTriggers` needs the `Validatable` rework.
6. **`world/`** (23) — worldgen, dimensions, the Outlands. Verify the mixin here.
7. **`entity/`** (59) — attributes (25 files use `AttributeSupplier`), `SpawnPlacements`, goals.
   `CommonEvents.java` holds attributes + spawn placement rules.
8. **`menu/`** (8) + **`network/`** (11) — `AbstractContainerMenu`, `CustomPacketPayload`.
   Note the May 2026 NeoForge advisory on network object allocation — read it while touching
   packet handling.
9. **`recipe/`** (2) — `RecipeSerializer` as a record, `Recipe$CommonInfo`.
10. **`quest/`** (15) + **`event/`** (4) + **`command/`** (1) — mostly our own logic; should
    survive. `getDayTime()`/`getGameTime()` (8 files) → `ClockManager`.
11. **Datagen providers** — regenerate everything from Phase 0.5 against the new formats.

**Gate: `./gradlew build` green, `runServer` boots, `runGameTestServer` passes.**
This is the moment the port stops being speculative.

---

## Phase 3 — Client layer

The concentrated pain. Good news from the survey: renderers are thin and share base classes
(`AnimalRenderer`, `MobRenderer`, `ScaledMobRenderer`, `NpcRenderer`), so the render-state and
submission rework mostly lands in 4 base classes rather than 21 leaf ones. Models are vanilla-style
and small — 22 files, 2654 lines total.

1. **Render state.** Define render state classes and `extractRenderState()` on the 4 base
   renderers first. `LionModel<T extends ModAnimal>` and friends currently take an entity
   generic — that generic becomes the render state.
2. **Submission rendering.** Convert `render(...)` → `submit(...)` across all 21 renderers and
   6 block-entity renderers (which also need their own `BlockEntityRenderState`).
3. **Model part visibility is gone** (1.21.9). Anything that toggles `part.visible` — the
   lion mane, the giraffe overlay layer — has to become separate model instances. Check
   `LionModel` (`mane`, `headwear`) and `GiraffeOverlayLayer` specifically.
4. **Item rendering** — `HyenaHeadItemRenderer` was a `BlockEntityWithoutLevelRenderer`; that
   class is gone. Becomes a `SpecialModelRenderer` registered through `SpecialModelRenderers`.
5. **GUI** (11 files) — `GuiGraphics.renderX()` → `submitX()`, screen/HUD reorganization.
   `HudOverlays` and `QuestBookMenuButton` are the exposed ones. Issue **#61** (quest book
   tooltip drift at non-1.0 render scale) may well fix or worsen itself here — check it.
6. **Particles** — `ColoredPortalParticle`: `TextureSheetParticle` → `SingleQuadParticle`,
   `ParticleProvider#createParticle()` gains a `RandomSource` param.
7. **`compat/`** (11) — JEI plugin + 6 Jade providers against the new APIs.
8. **`DimensionMusicHandler`, `CompassWobble`** — smaller, but client-side; don't forget them.

**Gate: `runClient` launches, every entity renders, every GUI opens, manual checklist passes.**

---

## Phase 4 — 26.1 → 26.2

Only start once 26.1 is playtested and tagged.

```bash
git checkout mc/26.1 && git checkout -b mc/26.2
```

Narrower and almost entirely client: Vulkan/OpenGL parallel hierarchies, `FeatureRenderer`/
`SubmitNode`/`FeatureRenderPhase` finishing the `MultiBufferSource` replacement, dynamically
constructed vertex formats, font drawing via `PreparedText`/`GlyphVisitor`, GUI state moved
from `Minecraft` onto `Gui`, and consolidated item/block collections (e.g. `Items#_*DYE` →
`Items#DYE`) which will hit recipe and datagen code. Bump deps again (GeckoLib 5.5.3, JEI
30.15.x, Jade 26.2.8).

---

## Phase 5 — Verification

Gametests cover logic. They cover **none** of what's most likely to break here — rendering,
GUI, particles, sound. Both are needed.

**Automated:**
- 13 JUnit tests (pure Java — should mostly survive)
- 12 gametest classes. These themselves use APIs that change; budget time to port the tests
  before they can validate anything. Only 1 structure NBT file, which limits the damage.
- `./gradlew runGameTestServer` in CI on `mc/**` — already wired.

**Manual checklist** (record from the working 1.21.1 build in Phase 0.4, diff after):
- Every custom mob renders, at correct scale, adult and baby, with correct texture variant
  (lion mane/maneless, giraffe overlay)
- Every custom GUI opens and lays out: quest book, bug trap, grinding bowl, bongo drum,
  quiver, Simba inventory, Timon merchant
- HUD overlays, compass wobble
- Outlands dimension: travel in and out, **cave generation is dry, not flooded** (the mixin)
- Both questlines end to end via `/coc quest set` on every stage
- Breeding, taming, favor system
- JEI shows the Grinding Bowl category; Jade shows all 6 providers
- Custom particles and dimension music
- **World upgrade path** — decide explicitly whether 1.21.1 saves are expected to load. Given
  the data component and registry changes across 12 versions, the honest answer is almost
  certainly no. Say so in the release notes rather than letting players discover it.

---

## Phase 6 — Release

Per `docs/RELEASES.md`, nothing needs changing structurally — the workflows read
`gradle.properties` and trigger on `mc/**`.

- Ship `mc/26.1` as `v<X>.0.0-beta.1-mc26.1` first. A port is a MAJOR bump.
- CurseForge/Modrinth game-version metadata comes from `gradle.properties` — verify both
  platforms accept the new `26.1` / `26.2` version strings.
- Note the version-scheme change in release notes; users will not expect `mc26.1` after `mc1.21.1`.
- Update `CLAUDE.md`: JDK path, `minecraft_version`, and the build-plugin name if we move to MDG.

---

## Effort

| Phase | Hours |
|---|---|
| 0 — prerequisites incl. **datagen (20–35)** | 30–50 |
| 1 — toolchain | 6–12 |
| 2 — common layer (now known to include: `ArmorMaterial` redesign, `ValueIO` save-data rework across `WorldData`/`PlayerData`/block entities, 16-file `Item#use` review, `RecipeSerializer` record) | 40–65 |
| 3 — client layer (GUI screens' mechanical shape is now proven fast to port; the render-state generic rework across 16 renderers is the remaining unknown) | 25–45 |
| 4 — 26.1 → 26.2 | 15–25 |
| 5 — verification & fixes, incl. porting the gametest suite to the new registry-based system | 20–30 |
| **Total** | **~135–225** |

Phase 0.5 (datagen) is the one to protect. Every hour skipped there comes back as three hours
of hand-editing JSON in Phase 2, without a compiler to catch mistakes.

Revised upward from the original 120–200 now that the true (uncapped) error count — 1383
across 65% of files, not the originally-reported 100/47 — is known. The mechanical fixes done
this session (Level accessor methods, GUI/JEI compat layer, several package moves — 177
errors resolved) came in cheap and fast, as expected; the remaining buckets are the ones the
original plan already flagged as real design work (armor, serialization, render state, item
interaction, gametests), just confirmed now to be wider than a first glance suggested.

---

## Spike results (Phase 1, `mc/26.1`)

Actually done, not estimated: JDK 25 installed, `gradle.properties` and `build.gradle`
bumped, Gradle wrapper moved to 9.6.1, dependencies re-pointed at 26.1 coordinates,
`./gradlew compileJava` run to completion.

**Toolchain resolved cleanly.** NeoGradle 7.1.38 decompiled, patched, and recompiled vanilla
26.1.2 against JDK 25 without incident — confirms the plan's plugin choice (stay on
NeoGradle, skip the ModDevGradle rewrite) was right for this spike.

**Toolchain-level findings not in any primer** (these needed the real build, not docs):

- The `data` run type is gone — split into **`clientData`** and **`serverData`** in the
  userdev config (`net.neoforged.fml.startup.DataClient` / `DataServer`). Confirmed by
  extracting `config.json` from the `neoforge-26.1.2.94-userdev.jar`. `build.gradle`'s
  `runs {}` block now declares both instead of one `data {}`.
- GeckoLib's Maven **groupId changed** from `software.bernie.geckolib` to `com.geckolib`
  (version stayed `geckolib-neoforge-<full-mc-version>:5.5.2`, e.g. `26.1.2`, not `26.1`).
  The `GeckoLib` repo block's `content {}` filter needs the new group added alongside the
  old one (kept for the 1.21.1 branch's benefit if ever diffed).
- Resolved dependency coordinates for this branch: `com.geckolib:geckolib-neoforge-26.1.2:5.5.2`,
  `mezz.jei:jei-26.1.2-neoforge(-api):29.21.0.66`, `maven.modrinth:jade:26.1.8+neoforge`.
- The Outlands `Aquifer$NoiseBasedAquifer#computeSubstance` mixin target **still exists** at
  26.1 (verified against NeoForm's decompiled source) — unknown #1 from the original plan is
  resolved. The mixin itself hasn't been re-enabled/tested yet; its `BlockState.is(Block)`
  call-site ordinal still needs re-verification once mixins compile again.

**Compile results (corrected):** `compileJava` completes (not crashes) with the real count of
**1383 errors across 209 of 319 files (~65%)** — not the 100/47 the capped run reported.

| Cause | Approx. sites | Matches plan phase |
|---|---|---|
| `ResourceLocation` → `Identifier` rename, package-level | ~20 files, 29 sites | Phase 2, mechanical sweep |
| `GameTestHolder`/`PrefixGameTestTemplate`/`GameTest` — whole annotation-scanning gametest system replaced by imperative `RegisterGameTestsEvent#registerTest(...)` + `GameTestInstance`/`FunctionGameTestInstance` registry objects | all 12 gametest classes | Phase 5 (test port) — **architectural, not a rename** |
| `GuiGraphics` renamed to `GuiGraphicsExtractor`; `Screen`/`AbstractContainerScreen` render lifecycle rewritten from `render()`/`renderBg()`/`renderLabels()`/`renderTooltip()` to `extractBackground()`/`extractRenderState()`/`extractLabels()` (auto-tooltip); mouse events now take a `MouseButtonEvent` record; `imageWidth`/`imageHeight` are now constructor-only `final` fields; `graphics.pose()` returns a 2D `Matrix3x2fStack`, not `PoseStack` | 11 GUI files + 2 JEI category files | Phase 3 — **fixed this pass, see below** |
| `MobRenderer`/`AnimalRenderer`/etc. now take **3** type args, not 2 (adds the render-state generic) | all 16 renderer classes | Phase 3, confirms the `EntityRenderState` prediction — **not yet fixed, needs render-state class design** |
| `RecipeSerializer` is now a **record**, not an interface | `GrindingBowlRecipeSerializer` | Phase 2 — **not yet fixed** |
| `Item#use()` returns plain `InteractionResult`, not `InteractionResultHolder<ItemStack>` — stack mutation model changed from copy-and-return to in-place | 16 item classes | Phase 2 — **not yet fixed, needs per-item review, not a blind rename** |
| `INBTSerializable`/`BlockEntity#saveAdditional`/`loadAdditional` — NBT (`CompoundTag`) serialization replaced by a `ValueOutput`/`ValueInput` (`ValueIO`) contract | `PlayerData`, `WorldData`, multiple block entities | Phase 2 — **not yet fixed, resolves unknown #4 below (yes, it does reach WorldData)** |
| `ArmorItem` removed — `ArmorItem.Type` → `net.minecraft.world.item.equipment.ArmorType`; `ArmorMaterial` reshaped | `ModArmorMaterials`, `ModItems` | Phase 2 — **not yet fixed, needs the new `ArmorMaterial` shape understood first** |
| `Level.random` (field, `protected`) and `Level.isClientSide` (field, now `private`) both need their existing public accessor methods called instead — `.getRandom()` / `.isClientSide()` | **64 files, ~168 sites** | Mechanical — **fixed this pass, the single largest lever found** |
| `EntityModel` no-arg constructor removed; `no suitable method found for create(ServerLevel/Level)`; `spawnAtLocation`/`teleportTo` signature changes; several accessors now return `Optional<T>` instead of `T`/primitive | spread across entity/particle files | Phase 2/3 — **not yet fixed**, each needs the call site read, not a blind substitution |
| `net.minecraft.advancements.critereon` package renamed to `criterion` | `ModCriteriaTriggers` and others | Phase 2 — **not yet fixed** |

**What this confirms about the estimate:** even at the true 65%-of-files count, the errors
are concentrated exactly where the primers said — renderers, gametests, item interaction,
serialization, armor. Nothing showed up that wasn't anticipated in kind, only in volume.
1383 real errors before any manual porting is still consistent with the 90–160 hour range;
if anything it argues for the upper half of that range now that the true count is known.

### Mechanical fixes pass (this session, on top of the spike)

Applied and verified by recompiling after each:

- **`ResourceLocation` → `Identifier`**: global rename across ~54 files (import + type refs).
  Confirmed no compound identifiers existed, so a plain substring replace was safe.
- **`Level.random` → `Level.getRandom()`, `Level.isClientSide` → `Level.isClientSide()`**:
  the single biggest lever found this pass — 64 files, ~168 call sites, entirely mechanical
  because both accessor methods already existed unchanged; only the field access needed
  parentheses. Done via a Python pass with an explicit Level-typed-receiver allowlist
  (`level`, `serverLevel`, `outlandsLevel`, `attacker.level()`, `entity.level()`) to avoid
  touching `Entity`'s own unrelated `this.random` field or `Math.random()`.
- **GUI/JEI-category compat layer fully ported**: all 11 `client/gui/*Screen.java` +
  `QuestBookMenuButton` + `HudOverlays` + 2 JEI category files now compile clean against the
  new `GuiGraphicsExtractor`/`extractBackground`/`extractRenderState`/`MouseButtonEvent` API.
  `QuestBookScreen`'s custom pose-stack scaling (0.65x book render + inverse mouse-coordinate
  mapping for hit-testing) was reconstructed against the new `Matrix3x2fStack` API and the
  split `extractBackground`/`extractRenderState` harness call — **this one needs `runClient`
  visual verification before trusting it**, since its old behavior already had a known bug
  (issue #61, tooltip drift at non-1.0 scale) that may have shifted rather than disappeared.
- **Package moves**: `AbstractArrow` → `.projectile.arrow`, `ThrowableItemProjectile` →
  `.projectile.throwableitemprojectile`, `Cow` → `.animal.cow`, `MobSpawnType` →
  `EntitySpawnReason` (same package), `DimensionTransition` → `TeleportTransition` (the
  override method itself renamed `changeDimension` → `teleport` too, confirmed safe to
  rename blindly only because the body just delegates to `super` and returns its result
  unchanged).
- **`build.gradle`**: added `-Xmaxerrs 100000` so this cap never hides the true count again.

Net effect: **1383 → 1206 unique errors** (177 resolved, dominated by the random/isClientSide
sweep). Remaining errors are concentrated in named architectural buckets above — each is a
real design task (new `ArmorMaterial` shape, `ValueIO` serialization, render-state classes,
gametest registration, per-item `InteractionResult` review), not more free mechanical wins.

**Not yet done:** GeckoLib/JEI/Jade actual Java API compatibility beyond dependency
resolution, datagen provider authoring (Phase 0.5), `runClient`, and the bulk of Phases 2–6.

---

## Known unknowns — resolve early, they can change the plan

1. ~~Does `Aquifer$NoiseBasedAquifer#computeSubstance` still exist in 26.1?~~ **Resolved by
   the spike: yes**, the method and its `BlockState.is(Block)` call site are both present in
   NeoForm's decompiled 26.1.2 source. Re-verify the call-site ordinal once mixins recompile.
2. **GeckoLib 4 → 5** is a major API break independent of Minecraft, **and** its Maven
   groupId changed (`software.bernie.geckolib` → `com.geckolib` — found by the spike, not
   documented anywhere upstream). Only 3 files touch it in code, but the `geo/` and
   `animations/` JSON formats still need checking, and its actual Java API against 26.1 is
   unverified — only dependency resolution was tested.
3. ~~ModDevGradle vs NeoGradle~~ **Resolved by the spike: staying on NeoGradle** (7.1.38
   resolved and built cleanly against 26.1.2 with no DSL rewrite beyond the `data` →
   `clientData`/`serverData` run-type split). Revisit only if a concrete NeoGradle limitation
   shows up later.
4. **Does `ItemStack`/`ItemStackTemplate` reach `WorldData` NBT serialization?** **Partially
   resolved by the spike: yes, but broader than just ItemStack.** `INBTSerializable` itself is
   gone — NBT (`CompoundTag`) serialization is replaced by a `ValueOutput`/`ValueInput`
   (`ValueIO`) contract, and it hits `PlayerData`, `WorldData`, and every custom
   `BlockEntity#saveAdditional`/`loadAdditional` override (18 sites found so far). This is a
   real Phase 2 redesign of the save-data layer, not a rename — budget for it explicitly.
5. **`src/main/resources/golden/`** — established by the spike prep: it's the
   `ItemInfoGameTests` golden-file snapshot (plain text). Low risk — regenerate after the
   port, not a blocker.
6. **JEI and Jade's actual Java APIs against 26.1** — only their Maven coordinates were
   verified to resolve. `compat/jei/` (3 files) and `compat/jade/` (6 providers) are in the
   47 files that fail to compile, but none of their errors are JEI/Jade-API errors yet — the
   failures seen are all `ResourceLocation`/`GuiGraphics` fallout. The real compat-API delta
   is still unknown until those upstream errors are fixed first.
