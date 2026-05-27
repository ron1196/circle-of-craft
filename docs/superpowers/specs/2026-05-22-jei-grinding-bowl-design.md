# JEI Integration — Grinding Bowl Recipe Category (Minimal Impl)

**Issue:** [#39](https://github.com/ron1196/TheLionKing/issues/39)
**Parent PRD:** [#22](https://github.com/ron1196/TheLionKing/issues/22)
**Date:** 2026-05-22

## Problem

The mod ships ~25 Grinding Bowl recipes that are undiscoverable without reading source. JEI is the de facto recipe-viewer UI on Forge 1.20.1, and the mod does not integrate with it.

## Goal

Expose every registered Grinding Bowl recipe to JEI via one dedicated `IRecipeCategory`, with the Grinding Bowl block-item bound as the catalyst.

## Non-goals (deferred to follow-up issues)

- Recipe transfer handler (`+` auto-fill button) → [#36](https://github.com/ron1196/TheLionKing/issues/36)
- In-GUI R/U key support inside `GrindingBowlScreen` → [#37](https://github.com/ron1196/TheLionKing/issues/37)
- Custom category background + arrow sprite → [#38](https://github.com/ron1196/TheLionKing/issues/38)
- Categories for any other blocks (Bongo Drum, Star Altar, Bug Trap — not recipe-based)

## Prerequisite (separate work, not part of this spec)

Four Grinding Bowl recipes from the original 2010-era mod (`old/code/common/LKGrindingRecipes.java`) are not yet ported because the target blocks were absent at the time. They are now registered:

| Old recipe | New recipe JSON | Output |
|---|---|---|
| `bananaLeaves → rugDye 6` | `banana_leaves_to_dye.json` | green dye |
| `lily meta 0 → rugDye 0` | `lily_white_to_dye.json` | white dye |
| `lily meta 1 → rugDye 8` | `lily_violet_to_dye.json` | light_gray dye (color chosen by maintainer) |
| `lily meta 2 → rugDye 3` | `lily_red_to_dye.json` | red dye |

Each needs the corresponding `data/circleofcraft/advancements/recipes/<name>.json`. The old `pridePillar` meta entries do not translate — they were rotational meta states, handled in the new mod by `RotatedPillarBlock` state properties, not separate items. These four recipes ship under a small separate change and are **not blockers for the JEI work**: the JEI plugin reads from `RecipeManager` at runtime, so whatever recipes are registered show up.

## Solution

A self-contained `compat.jei` package, hard-isolated from the rest of the codebase. Nothing outside this package imports any JEI class — so when JEI is absent at runtime the JVM never tries to load these classes.

### Architecture

```
src/main/java/io/github/ron1196/circleofcraft/compat/jei/
├── LionKingJeiPlugin.java          # @JeiPlugin: registers category, recipes, catalyst
└── GrindingBowlRecipeCategory.java # IRecipeCategory<GrindingBowlRecipe>
```

Both classes are loaded only by JEI's plugin scanner. No `Mod.EventBusSubscriber`, no static initializer in user-facing code, no references from `event/`, `registry/`, or anywhere else.

### `GrindingBowlRecipeCategory`

Single-row layout (82 × 26 px), matching the proportions of JEI's vanilla furnace category so it feels familiar.

```
+-------------------------------+
| [slot]   →→→→→   [slot]       |
+-------------------------------+
  input x=1,y=5    output x=61,y=5
  arrow region: 24×17 px, vanilla furnace texture (src x=79,y=35)
```

| Method | Behavior |
|---|---|
| `getRecipeType()` | Returns `RECIPE_TYPE = RecipeType.create("circleofcraft", "grinding_bowl", GrindingBowlRecipe.class)` (held as `public static final` for plugin reuse). |
| `getTitle()` | `Component.translatable("jei.circleofcraft.category.grinding_bowl")`. |
| `getBackground()` | `guiHelper.createBlankDrawable(82, 26)` — no custom art (per non-goals). |
| `getIcon()` | `guiHelper.createDrawableItemStack(new ItemStack(LionKingItems.GRINDING_BOWL_ITEM.get()))` — appears in JEI's left tab list. |
| `setRecipe(builder, recipe, focuses)` | `builder.addSlot(INPUT, 1, 5).addIngredients(recipe.getIngredient())`; `builder.addSlot(OUTPUT, 61, 5).addItemStack(recipe.getResult())`. |
| `draw(recipe, view, graphics, mx, my)` | Blits the static vanilla furnace arrow sprite. No animation — per-recipe progress doesn't exist in JEI's catalog view. |

**Why static, not animated arrow:** the in-world Grinding Bowl tracks per-instance progress (`GrindingBowlMenu.getGrindTime`), but JEI is showing the recipe catalog, not a live craft. Animation can come with the custom art in #38.

### `LionKingJeiPlugin`

```java
@JeiPlugin
public class LionKingJeiPlugin implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation("circleofcraft", "jei_plugin");

    @Override public ResourceLocation getPluginUid() { return ID; }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        reg.addRecipeCategories(new GrindingBowlRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        RecipeManager rm = Minecraft.getInstance().level.getRecipeManager();
        List<GrindingBowlRecipe> recipes = rm.getAllRecipesFor(RecipeTypes.GRINDING_TYPE.get());
        reg.addRecipes(GrindingBowlRecipeCategory.RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
        reg.addRecipeCatalyst(
            new ItemStack(LionKingItems.GRINDING_BOWL_ITEM.get()),
            GrindingBowlRecipeCategory.RECIPE_TYPE);
    }
}
```

`Minecraft.getInstance().level` is safe in `registerRecipes` — JEI invokes this hook after level load, when its catalog refreshes.

### Build & metadata

**`build.gradle`** — append the BlameJared maven and two JEI artifacts:

```groovy
repositories {
    maven {
        name = 'BlameJared (JEI)'
        url 'https://maven.blamejared.com/'
        content { includeGroup 'mezz.jei' }
    }
}

dependencies {
    compileOnly fg.deobf('mezz.jei:jei-1.20.1-forge-api:15.20.0.105')
    runtimeOnly fg.deobf('mezz.jei:jei-1.20.1-forge:15.20.0.105')
}
```

`compileOnly` provides the JEI API (`IModPlugin`, `IRecipeCategory`, `RecipeType`, etc.). `runtimeOnly` installs JEI into the dev launcher so `runClient` actually shows the recipe browser. Neither is shipped in the published jar.

**`src/main/resources/META-INF/mods.toml`** — append a fourth `[[dependencies.circleofcraft]]` block:

```toml
[[dependencies.circleofcraft]]
modId = "jei"
mandatory = false
versionRange = "[15,)"
ordering = "AFTER"
side = "BOTH"
```

`ordering = "AFTER"` makes JEI finish its setup before our plugin runs — relevant for catalyst registration and recipe iteration.

**`src/main/resources/assets/circleofcraft/lang/en_us.json`** — one new line:

```json
"jei.circleofcraft.category.grinding_bowl": "Grinding Bowl"
```

## Error handling

This is a read-only catalog plugin; failure modes are limited.

| Failure | Outcome | Mitigation |
|---|---|---|
| JEI absent at runtime | `@JeiPlugin` class never loaded; no integration code runs | Verified by no-JEI smoke test (below). |
| Recipe list empty | Category renders with zero entries | Acceptable — symptom of a broken datapack, not a plugin bug. |
| `Minecraft.getInstance().level == null` in `registerRecipes` | NPE in plugin init; JEI logs and skips | No defensive null-check: would mask a real JEI regression. JEI calls this post-level-load. |
| JEI version mismatch | Class-load failure at scan time; JEI logs and skips our plugin | `15.20.0.105` build pin + `[15,)` mods.toml range keeps this in-band. |

## Verification

1. `./gradlew build` → green.
2. `./gradlew runClient` → open inventory → JEI panel visible → scroll to the Grinding Bowl category → every currently-registered Grinding Bowl recipe renders with correct input/output.
3. In JEI, click the Grinding Bowl block-item in any inventory or in the JEI ingredient list → category opens focused on that catalyst.
4. **Soft-dep smoke test (one-time, manual):** comment out the `runtimeOnly fg.deobf('mezz.jei:jei-1.20.1-forge:15.20.0.105')` line in `build.gradle` → `./gradlew runClient` → confirm Minecraft reaches the main menu without crash, mod listed as loaded → uncomment and revert. Record the result in the PR description.

No automated game test: JEI is client-only, and the Mojang game-test framework runs an in-world server context.

## Open questions / follow-ups

None blocking. Tracked for later:

- **#36:** `IRecipeTransferHandler<GrindingBowlMenu, GrindingBowlRecipe>` so the `+` button auto-fills the bowl.
- **#37:** `IGuiContainerHandler<GrindingBowlScreen>` so R/U work inside the bowl GUI.
- **#38:** Themed background + animated arrow sprite for the category.
