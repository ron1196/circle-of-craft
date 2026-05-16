package io.github.ron1196.thelionking.data;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Guards that every vanilla-recipe-book recipe has a companion advancement
 * under {@code advancements/recipes/}. Without that advancement the recipe
 * stays hidden from players in the in-game recipe book.
 *
 * <p>To regenerate the advancements after adding new recipes, run
 * {@code python3 scripts/generate_recipe_advancements.py}.
 */
class RecipeAdvancementCoverageTest {

    private static final Path RECIPES_DIR = Path.of("src/main/resources/data/thelionking/recipes");
    private static final Path ADV_DIR = Path.of("src/main/resources/data/thelionking/advancements/recipes");

    private static final Set<String> VANILLA_RECIPE_BOOK_TYPES = Set.of(
            "minecraft:crafting_shaped",
            "minecraft:crafting_shapeless",
            "minecraft:smelting",
            "minecraft:blasting",
            "minecraft:smoking",
            "minecraft:campfire_cooking");

    private static final Pattern TYPE_PATTERN = Pattern.compile("\"type\"\\s*:\\s*\"([^\"]+)\"");

    @Test
    void everyVanillaRecipeHasUnlockAdvancement() throws IOException {
        List<Path> missing = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(RECIPES_DIR)) {
            stream.filter(p -> p.toString().endsWith(".json"))
                    .filter(Files::isRegularFile)
                    .forEach(recipe -> {
                        String type = firstTypeField(recipe);
                        if (!VANILLA_RECIPE_BOOK_TYPES.contains(type)) return;
                        Path expectedAdv = ADV_DIR.resolve(RECIPES_DIR.relativize(recipe));
                        if (!Files.exists(expectedAdv)) {
                            missing.add(expectedAdv);
                        }
                    });
        }
        if (!missing.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            msg.append(missing.size())
                    .append(" recipe(s) are missing their unlock advancement. ")
                    .append("Run: python3 scripts/generate_recipe_advancements.py\n");
            for (Path p : missing) {
                msg.append("  missing: ").append(p).append('\n');
            }
            fail(msg.toString());
        }
    }

    private static String firstTypeField(Path recipe) {
        try {
            String content = Files.readString(recipe);
            Matcher m = TYPE_PATTERN.matcher(content);
            return m.find() ? m.group(1) : "";
        } catch (IOException e) {
            throw new RuntimeException("failed to read " + recipe, e);
        }
    }
}
