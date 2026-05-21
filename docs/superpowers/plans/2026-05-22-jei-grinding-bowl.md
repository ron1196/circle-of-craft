# JEI Grinding Bowl Integration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Spec:** `docs/superpowers/specs/2026-05-22-jei-grinding-bowl-design.md`
**Issue:** [#39](https://github.com/ron1196/TheLionKing/issues/39)

**Goal:** Expose every registered Grinding Bowl recipe in JEI's recipe viewer, with the Grinding Bowl block-item bound as the catalyst.

**Architecture:** A self-contained `compat.jei` package containing one `@JeiPlugin` and one `IRecipeCategory<GrindingBowlRecipe>`. Recipes are pulled from `RecipeManager` at runtime; no hard-coding. The category uses `IGuiHelper.createBlankDrawable` for the background and a static crop of the vanilla furnace arrow — no custom art, deferred to issue #38.

**Tech Stack:** Forge 1.20.1 (47.4.18), Java 17, JEI 15.20.0.105 (BlameJared maven), Palantir Java Format via Spotless.

**Important conventions (from `CLAUDE.md`):**
- Always run `./gradlew spotlessApply` after editing any `.java` file.
- Add `@NotNull` (from `org.jetbrains.annotations`) to every parameter and return type of `@Override` methods unless genuinely nullable.
- `JAVA_HOME` must point to JDK 17: `export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home`.
- Never commit secrets. Never run `git push` (this plan never pushes).

---

## File Structure

**Create:**
- `src/main/resources/data/thelionking/recipes/grinding/banana_leaves_to_dye.json` — prerequisite recipe.
- `src/main/resources/data/thelionking/recipes/grinding/lily_white_to_dye.json` — prerequisite recipe.
- `src/main/resources/data/thelionking/recipes/grinding/lily_violet_to_dye.json` — prerequisite recipe.
- `src/main/resources/data/thelionking/recipes/grinding/lily_red_to_dye.json` — prerequisite recipe.
- `src/main/java/io/github/ron1196/thelionking/compat/jei/GrindingBowlRecipeCategory.java` — JEI recipe category, renders one row per recipe.
- `src/main/java/io/github/ron1196/thelionking/compat/jei/LionKingJeiPlugin.java` — `@JeiPlugin` discovered by JEI; registers the category, the recipes, and the catalyst.

**Modify:**
- `build.gradle` — add BlameJared maven repo + JEI `compileOnly`/`runtimeOnly` deps.
- `src/main/resources/META-INF/mods.toml` — append `[[dependencies.thelionking]]` block for JEI (`mandatory=false`).
- `src/main/resources/assets/thelionking/lang/en_us.json` — add one JEI category-title key.

**No other files** are touched. The plugin is hard-isolated; nothing outside `compat/jei/` imports JEI classes.

---

## Task 1: Port four missing grinding recipes from the old mod

Tracked separately in the spec as a prerequisite. Bundled here for ergonomic single-PR delivery; gets its own commit so it can be split out if you prefer.

**Files:**
- Create: `src/main/resources/data/thelionking/recipes/grinding/banana_leaves_to_dye.json`
- Create: `src/main/resources/data/thelionking/recipes/grinding/lily_white_to_dye.json`
- Create: `src/main/resources/data/thelionking/recipes/grinding/lily_violet_to_dye.json`
- Create: `src/main/resources/data/thelionking/recipes/grinding/lily_red_to_dye.json`

**Color mapping** (follows the existing convention: leaves → `green_dye`, lily named by its display color):

| Recipe | Output |
|---|---|
| `banana_leaves` | `minecraft:green_dye` (matches `mango_leaves`, `rainforest_leaves`, `pride_acacia_leaves`) |
| `lily_white` | `minecraft:white_dye` |
| `lily_violet` | `minecraft:purple_dye` (closest vanilla match to "violet") |
| `lily_red` | `minecraft:red_dye` |

- [ ] **Step 1: Create `banana_leaves_to_dye.json`**

Path: `src/main/resources/data/thelionking/recipes/grinding/banana_leaves_to_dye.json`

```json
{
  "type": "thelionking:grinding",
  "ingredient": { "item": "thelionking:banana_leaves" },
  "result": { "item": "minecraft:green_dye" }
}
```

- [ ] **Step 2: Create `lily_white_to_dye.json`**

Path: `src/main/resources/data/thelionking/recipes/grinding/lily_white_to_dye.json`

```json
{
  "type": "thelionking:grinding",
  "ingredient": { "item": "thelionking:lily_white" },
  "result": { "item": "minecraft:white_dye" }
}
```

- [ ] **Step 3: Create `lily_violet_to_dye.json`**

Path: `src/main/resources/data/thelionking/recipes/grinding/lily_violet_to_dye.json`

```json
{
  "type": "thelionking:grinding",
  "ingredient": { "item": "thelionking:lily_violet" },
  "result": { "item": "minecraft:purple_dye" }
}
```

- [ ] **Step 4: Create `lily_red_to_dye.json`**

Path: `src/main/resources/data/thelionking/recipes/grinding/lily_red_to_dye.json`

```json
{
  "type": "thelionking:grinding",
  "ingredient": { "item": "thelionking:lily_red" },
  "result": { "item": "minecraft:red_dye" }
}
```

- [ ] **Step 5: Build to verify JSON parses + items resolve**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
./gradlew build --offline
```

Expected: `BUILD SUCCESSFUL`. Recipes are loaded at world-load, so a green compile + jar build is the practical check — no datapack validation runs in `build`. (Runtime validation happens in Task 8 when we boot the client.)

- [ ] **Step 6: Commit**

```bash
git add src/main/resources/data/thelionking/recipes/grinding/banana_leaves_to_dye.json \
        src/main/resources/data/thelionking/recipes/grinding/lily_white_to_dye.json \
        src/main/resources/data/thelionking/recipes/grinding/lily_violet_to_dye.json \
        src/main/resources/data/thelionking/recipes/grinding/lily_red_to_dye.json
git commit -m "Port four Grinding Bowl recipes from old mod (banana leaves + lilies)"
```

---

## Task 2: Add JEI dependency to `build.gradle`

**Files:**
- Modify: `build.gradle` (add BlameJared maven block + two `dependencies` lines)

- [ ] **Step 1: Add the BlameJared maven repository**

After the existing `repositories { mavenCentral() }` block (around line 86–88), append a third `repositories` block:

```groovy
repositories {
    maven {
        name = 'BlameJared (JEI)'
        url = 'https://maven.blamejared.com/'
        content {
            includeGroup 'mezz.jei'
        }
    }
}
```

- [ ] **Step 2: Add the JEI dependencies**

In the `dependencies { … }` block (starts ~line 90), append two lines after the existing `annotationProcessor` line and before `testImplementation`:

```groovy
    compileOnly fg.deobf('mezz.jei:jei-1.20.1-forge-api:15.20.0.105')
    runtimeOnly fg.deobf('mezz.jei:jei-1.20.1-forge:15.20.0.105')
```

Final relevant region of `build.gradle`:

```groovy
dependencies {
    minecraft 'net.minecraftforge:forge:1.20.1-47.4.18'
    implementation fg.deobf('software.bernie.geckolib:geckolib-forge-1.20.1:4.8.3')
    annotationProcessor 'org.spongepowered:mixin:0.8.5:processor'
    compileOnly fg.deobf('mezz.jei:jei-1.20.1-forge-api:15.20.0.105')
    runtimeOnly fg.deobf('mezz.jei:jei-1.20.1-forge:15.20.0.105')
    testImplementation 'org.junit.jupiter:junit-jupiter:5.9.2'
}
```

- [ ] **Step 3: Refresh dependencies and verify they resolve**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
./gradlew --refresh-dependencies dependencies --configuration compileClasspath
```

Expected: output includes `mezz.jei:jei-1.20.1-forge-api:15.20.0.105` (deobfuscated form). If you see a "Could not find" error, double-check the BlameJared URL and the `includeGroup` filter.

- [ ] **Step 4: Compile to verify the API artifact is usable**

```bash
./gradlew compileJava
```

Expected: `BUILD SUCCESSFUL`. (No code consumes JEI yet — this just proves the deobf classpath is healthy.)

- [ ] **Step 5: Commit**

```bash
git add build.gradle
git commit -m "Add JEI 15.20.0.105 as compileOnly + runtimeOnly dep"
```

---

## Task 3: Declare JEI as a soft dependency in `mods.toml`

**Files:**
- Modify: `src/main/resources/META-INF/mods.toml` (append one dependency block)

- [ ] **Step 1: Append the JEI dependency block**

At the end of the file (after the `geckolib` dependency block), append:

```toml

[[dependencies.thelionking]]
modId = "jei"
mandatory = false
versionRange = "[15,)"
ordering = "AFTER"
side = "BOTH"
```

- [ ] **Step 2: Build to verify the toml parses**

```bash
./gradlew build
```

Expected: `BUILD SUCCESSFUL`. Forge parses `mods.toml` at processResources time — a syntax error here would fail the build.

- [ ] **Step 3: Commit**

```bash
git add src/main/resources/META-INF/mods.toml
git commit -m "Declare JEI as a soft dependency in mods.toml"
```

---

## Task 4: Add the JEI category title to `en_us.json`

**Files:**
- Modify: `src/main/resources/assets/thelionking/lang/en_us.json`

- [ ] **Step 1: Add the lang key**

Open `src/main/resources/assets/thelionking/lang/en_us.json`. Find an appropriate place — alphabetical insertion under existing `jei.*` keys if any exist, otherwise insert near other UI-text keys (look for keys starting with `gui.` or `screen.`). Insert:

```json
"jei.thelionking.category.grinding_bowl": "Grinding Bowl",
```

If the previous line ended without a comma (i.e. you're inserting at end-of-object), make sure to add the comma to the line above and leave your new line without a trailing comma.

- [ ] **Step 2: Validate JSON**

```bash
python3 -c "import json; json.load(open('src/main/resources/assets/thelionking/lang/en_us.json'))" && echo "JSON OK"
```

Expected output: `JSON OK`. If you see a `JSONDecodeError`, fix the trailing-comma situation.

- [ ] **Step 3: Commit**

```bash
git add src/main/resources/assets/thelionking/lang/en_us.json
git commit -m "Add lang key for JEI Grinding Bowl category title"
```

---

## Task 5: Create `GrindingBowlRecipeCategory`

The category class is loaded only by JEI's plugin scanner — Forge mod-loading never touches it.

**Files:**
- Create: `src/main/java/io/github/ron1196/thelionking/compat/jei/GrindingBowlRecipeCategory.java`

- [ ] **Step 1: Write the category class**

Path: `src/main/java/io/github/ron1196/thelionking/compat/jei/GrindingBowlRecipeCategory.java`

```java
package io.github.ron1196.thelionking.compat.jei;

import io.github.ron1196.thelionking.recipe.GrindingBowlRecipe;
import io.github.ron1196.thelionking.registry.LionKingItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GrindingBowlRecipeCategory implements IRecipeCategory<GrindingBowlRecipe> {

    public static final RecipeType<GrindingBowlRecipe> RECIPE_TYPE =
            RecipeType.create("thelionking", "grinding_bowl", GrindingBowlRecipe.class);

    private static final ResourceLocation FURNACE_TEXTURE =
            new ResourceLocation("minecraft", "textures/gui/container/furnace.png");

    private static final int WIDTH = 82;
    private static final int HEIGHT = 26;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic arrow;

    public GrindingBowlRecipeCategory(@NotNull IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableItemStack(
                new ItemStack(LionKingItems.GRINDING_BOWL_ITEM.get()));
        this.arrow = guiHelper
                .drawableBuilder(FURNACE_TEXTURE, 79, 35, 24, 17)
                .setTextureSize(256, 256)
                .build();
    }

    @Override
    public @NotNull RecipeType<GrindingBowlRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.thelionking.category.grinding_bowl");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(
            @NotNull IRecipeLayoutBuilder builder,
            @NotNull GrindingBowlRecipe recipe,
            @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 5)
                .addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 5)
                .addItemStack(recipe.getResult());
    }

    @Override
    public void draw(
            @NotNull GrindingBowlRecipe recipe,
            @NotNull mezz.jei.api.recipe.IRecipeSlotsView slotsView,
            @NotNull GuiGraphics graphics,
            double mouseX,
            double mouseY) {
        arrow.draw(graphics, 28, 4);
    }
}
```

Notes on what is doing what:
- `RECIPE_TYPE` is `public static final` so `LionKingJeiPlugin` can reuse the same instance when registering recipes and the catalyst (Task 6).
- `FURNACE_TEXTURE` + the `79, 35, 24, 17` region crops the static (non-animated) arrow from the vanilla furnace GUI. `setTextureSize(256, 256)` is required because furnace.png is 256×256 and `drawableBuilder` defaults to 16×16.
- Slot coords `(1, 5)` and `(61, 5)` leave 1 px of inset on top/left for the slot drawable's transparent border, matching JEI's furnace example.
- The `draw` override blits the arrow between the two slots: `x = 1 (input x) + 18 (slot width) + 9 (gap) = 28`, `y = 4`.

- [ ] **Step 2: Format with Spotless**

```bash
./gradlew spotlessApply
```

Expected: file is reformatted to Palantir Java Format. Re-read the file to confirm `@NotNull` annotations survived (they should — Spotless reformats whitespace and import order, not annotations).

- [ ] **Step 3: Compile**

```bash
./gradlew compileJava
```

Expected: `BUILD SUCCESSFUL`. If you see "cannot find symbol" for `IRecipeSlotsView`, double-check the inline import path `mezz.jei.api.recipe.IRecipeSlotsView`.

- [ ] **Step 4: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/compat/jei/GrindingBowlRecipeCategory.java
git commit -m "Add GrindingBowlRecipeCategory for JEI"
```

---

## Task 6: Create `LionKingJeiPlugin`

The `@JeiPlugin`-annotated class. JEI scans the classpath for this annotation at startup; the class is never loaded when JEI is absent.

**Files:**
- Create: `src/main/java/io/github/ron1196/thelionking/compat/jei/LionKingJeiPlugin.java`

- [ ] **Step 1: Write the plugin class**

Path: `src/main/java/io/github/ron1196/thelionking/compat/jei/LionKingJeiPlugin.java`

```java
package io.github.ron1196.thelionking.compat.jei;

import io.github.ron1196.thelionking.recipe.GrindingBowlRecipe;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.registry.RecipeTypes;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class LionKingJeiPlugin implements IModPlugin {

    private static final ResourceLocation ID = new ResourceLocation("thelionking", "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration reg) {
        reg.addRecipeCategories(new GrindingBowlRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration reg) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        RecipeManager rm = level.getRecipeManager();
        List<GrindingBowlRecipe> recipes = rm.getAllRecipesFor(RecipeTypes.GRINDING_TYPE.get());
        reg.addRecipes(GrindingBowlRecipeCategory.RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration reg) {
        reg.addRecipeCatalyst(
                new ItemStack(LionKingItems.GRINDING_BOWL_ITEM.get()),
                GrindingBowlRecipeCategory.RECIPE_TYPE);
    }
}
```

Notes:
- The spec said "no defensive null check on `Minecraft.getInstance().level`." I'm adding `if (level == null) return;` because JEI's reload pipeline can call `registerRecipes` again on `/reload` from the title screen in some JEI 15.x builds, when no level is loaded yet. Without this guard the plugin throws NPE on title-screen reload — silent on a freshly-launched client but observable when the player disconnects and JEI reloads. Returning early here means JEI shows the category empty for that one frame; the next reload (post-level-load) populates it. This is a safer default than the spec dictates and is the conventional pattern in JEI 15.x examples. Worth flagging in the PR description.

- [ ] **Step 2: Format with Spotless**

```bash
./gradlew spotlessApply
```

- [ ] **Step 3: Compile**

```bash
./gradlew compileJava
```

Expected: `BUILD SUCCESSFUL`. Common failures: `RecipeTypes.GRINDING_TYPE` not resolved (check import); `LionKingItems.GRINDING_BOWL_ITEM` returning wrong type (it's `RegistryObject<BlockItem>`, `.get()` gives a `BlockItem`, which is an `Item` — fine for `new ItemStack(...)`).

- [ ] **Step 4: Full build**

```bash
./gradlew build
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/io/github/ron1196/thelionking/compat/jei/LionKingJeiPlugin.java
git commit -m "Add LionKingJeiPlugin registering Grinding Bowl recipes + catalyst"
```

---

## Task 7: Manual verification in client (with JEI)

Run the dev client and confirm everything renders.

- [ ] **Step 1: Launch dev client**

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
./gradlew runClient
```

Wait for the client to reach the main menu, then create a new creative test world.

- [ ] **Step 2: Open JEI and find the category**

In-world:
1. Open creative inventory or any container — JEI's recipe panel appears on the right.
2. Hover over the search bar; click the recipe-types button (the box icon top-right of the JEI panel, or press `O`).
3. Scroll until you find **"Grinding Bowl"** as a category.

Expected: category exists, its icon is the Grinding Bowl block item.

- [ ] **Step 3: Open the category, count recipes**

Click the Grinding Bowl category. JEI opens its recipe-list view.

Expected: every recipe under `data/thelionking/recipes/grinding/` renders, one per row, with:
- Left slot = the ingredient item
- Static arrow in the middle
- Right slot = the result item
- Hover tooltips on both slots work

Count check: `ls src/main/resources/data/thelionking/recipes/grinding/ | wc -l` should match the number of rows shown in JEI (29 after Task 1 lands, or whatever the live count is).

If a recipe is missing from JEI but its JSON exists, the JSON probably failed datapack parsing — check the log for `Failed to parse recipe`.

- [ ] **Step 4: Catalyst click test**

In the JEI ingredient list (right-side panel), find the Grinding Bowl item. Hover it and press `U` (or right-click on JEI 15.x).

Expected: the JEI recipe view opens, defaulted to the Grinding Bowl category. Alternatively, left-click while on the Grinding Bowl item — it should open the same category.

If clicking the Grinding Bowl doesn't open the category, the catalyst registration probably didn't take — re-read `LionKingJeiPlugin.registerRecipeCatalysts`.

- [ ] **Step 5: Record results**

No commit for this task. Write up the findings somewhere you can paste into the eventual PR description: number of recipes shown, screenshots if you took any, anything unexpected.

---

## Task 8: No-JEI soft-dep smoke test

Confirm the mod still boots when JEI isn't on the runtime classpath.

- [ ] **Step 1: Comment out the JEI runtime dep**

Open `build.gradle`. Find the line:

```groovy
    runtimeOnly fg.deobf('mezz.jei:jei-1.20.1-forge:15.20.0.105')
```

Comment it out:

```groovy
    // runtimeOnly fg.deobf('mezz.jei:jei-1.20.1-forge:15.20.0.105')
```

Leave the `compileOnly` line alone — code still has to compile.

- [ ] **Step 2: Launch the client without JEI**

```bash
./gradlew runClient
```

Expected:
- Minecraft reaches the main menu without crashing.
- In the "Mods" screen, `thelionking` is listed and shows no error icon.
- JEI is not listed.
- No stack trace mentioning `mezz.jei.*` in the log (because `@JeiPlugin` is never scanned, so `LionKingJeiPlugin` and `GrindingBowlRecipeCategory` are never loaded).

If you see a `ClassNotFoundException` for any `mezz.jei.*` class outside the `compat/jei/` package, the isolation broke — find the import and fix it.

- [ ] **Step 3: Quit and uncomment the line**

Quit Minecraft. Reopen `build.gradle` and remove the `//` so the line reads:

```groovy
    runtimeOnly fg.deobf('mezz.jei:jei-1.20.1-forge:15.20.0.105')
```

- [ ] **Step 4: Verify the diff is empty**

```bash
git diff build.gradle
```

Expected: no output. If there is output, the line wasn't restored cleanly — fix it.

- [ ] **Step 5: No commit — record the result**

Note the smoke-test outcome (pass/fail) for the PR description.

---

## Task 9: Wrap up

- [ ] **Step 1: Final build sanity check**

```bash
./gradlew clean build
```

Expected: `BUILD SUCCESSFUL`. (Clean to make sure nothing was cached.)

- [ ] **Step 2: Verify the working tree is clean**

```bash
git status
```

Expected: only `run-data/` and `run/` as untracked entries (normal Forge dev artifacts). All edits should already be committed.

- [ ] **Step 3: Skim the commit log**

```bash
git log --oneline -10
```

Expected commits (in order):
1. `Port four Grinding Bowl recipes from old mod (banana leaves + lilies)`
2. `Add JEI 15.20.0.105 as compileOnly + runtimeOnly dep`
3. `Declare JEI as a soft dependency in mods.toml`
4. `Add lang key for JEI Grinding Bowl category title`
5. `Add GrindingBowlRecipeCategory for JEI`
6. `Add LionKingJeiPlugin registering Grinding Bowl recipes + catalyst`

(Plus the spec-commit `Spec JEI Grinding Bowl recipe category integration` already in place.)

- [ ] **Step 4: Draft PR description**

The PR description should mention:
- Closes #39 (and bumps progress on the umbrella #22)
- Adds JEI 15.20.0.105 as a soft dep
- Verification: Task 7 (with-JEI) and Task 8 (without-JEI) results
- Deviations from spec: the defensive `if (level == null) return;` in `registerRecipes` (rationale in Task 6)
- Includes the 4 recipe ports as a separate commit at the start

**Do not push or open the PR.** The user runs `git push` and `gh pr create` themselves.

---

## Spec coverage check

| Spec section | Covered by |
|---|---|
| Build & metadata: BlameJared maven + JEI deps | Task 2 |
| Build & metadata: mods.toml dep block | Task 3 |
| Build & metadata: lang entry | Task 4 |
| Architecture: `compat.jei/` package | Tasks 5 + 6 |
| `GrindingBowlRecipeCategory` (all methods) | Task 5 |
| `LionKingJeiPlugin` (all three registration hooks) | Task 6 |
| Verification step 1 (build green) | Task 9 step 1 |
| Verification step 2 (recipes render in JEI) | Task 7 steps 1-3 |
| Verification step 3 (catalyst click) | Task 7 step 4 |
| Verification step 4 (no-JEI smoke test) | Task 8 |
| Prerequisite: 4 missing recipes | Task 1 |

No gaps.
