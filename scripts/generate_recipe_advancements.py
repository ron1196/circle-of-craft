#!/usr/bin/env python3
"""Generate recipe-unlock advancement JSONs from existing recipe JSONs.

Each recipe in src/main/resources/data/<ns>/recipes/*.json gets a companion
advancement at src/main/resources/data/<ns>/advancements/recipes/<name>.json
that fires `recipe_unlocked` once the player obtains any input ingredient.

The companion advancement is what makes the recipe show up in the in-game
recipe book — without it, modded recipes stay hidden until the player has
"discovered" them, which they never do because nothing unlocks them.
"""
from __future__ import annotations

import json
from pathlib import Path
from typing import Any

MOD_ID = "thelionking"
ROOT = Path(__file__).resolve().parent.parent
RECIPES_DIR = ROOT / "src/main/resources/data" / MOD_ID / "recipes"
ADV_DIR = ROOT / "src/main/resources/data" / MOD_ID / "advancements/recipes"

# The vanilla recipe book only displays these recipe types. Custom types
# (e.g. thelionking:grinding) have their own GUIs and don't need unlock advs.
VANILLA_RECIPE_BOOK_TYPES = frozenset({
    "minecraft:crafting_shaped",
    "minecraft:crafting_shapeless",
    "minecraft:smelting",
    "minecraft:blasting",
    "minecraft:smoking",
    "minecraft:campfire_cooking",
})


def extract_ingredients(recipe: dict[str, Any]) -> list[dict[str, str]]:
    """Return ingredient specs from any recipe type as a list of {item|tag: id}.

    Order is "input slots first" so the first element is a deterministic choice
    for the unlock trigger.
    """
    rtype = recipe.get("type", "")
    out: list[dict[str, str]] = []

    if rtype == "minecraft:crafting_shaped":
        for entry in (recipe.get("key") or {}).values():
            out.extend(_flatten(entry))
    elif rtype == "minecraft:crafting_shapeless":
        for entry in recipe.get("ingredients") or []:
            out.extend(_flatten(entry))
    elif rtype in {
        "minecraft:smelting",
        "minecraft:blasting",
        "minecraft:smoking",
        "minecraft:campfire_cooking",
    }:
        ing = recipe.get("ingredient")
        if ing is not None:
            out.extend(_flatten(ing))
    return out


def _flatten(entry: Any) -> list[dict[str, str]]:
    """Ingredient entries can be a dict or a list of dicts. Normalize."""
    if isinstance(entry, list):
        result: list[dict[str, str]] = []
        for e in entry:
            result.extend(_flatten(e))
        return result
    if isinstance(entry, dict):
        if "item" in entry:
            return [{"item": entry["item"]}]
        if "tag" in entry:
            return [{"tag": entry["tag"]}]
    return []


def trigger_name(spec: dict[str, str]) -> str:
    raw = spec.get("item") or spec.get("tag") or "input"
    short = raw.split(":")[-1].replace("/", "_")
    return f"has_{short}"


def make_advancement(recipe_id: str, ingredients: list[dict[str, str]]) -> dict[str, Any]:
    if not ingredients:
        raise ValueError(f"no ingredients found for {recipe_id}")
    primary = ingredients[0]
    item_condition = {"items": [primary["item"]]} if "item" in primary else {"tag": primary["tag"]}
    crit_name = trigger_name(primary)
    return {
        "parent": "minecraft:recipes/root",
        "criteria": {
            crit_name: {
                "trigger": "minecraft:inventory_changed",
                "conditions": {"items": [item_condition]},
            },
            "has_the_recipe": {
                "trigger": "minecraft:recipe_unlocked",
                "conditions": {"recipe": recipe_id},
            },
        },
        "requirements": [[crit_name, "has_the_recipe"]],
        "rewards": {"recipes": [recipe_id]},
        "sends_telemetry_event": False,
    }


def main() -> None:
    ADV_DIR.mkdir(parents=True, exist_ok=True)
    written = 0
    skipped_custom = 0
    skipped_problems: list[str] = []
    for recipe_path in sorted(RECIPES_DIR.rglob("*.json")):
        rel = recipe_path.relative_to(RECIPES_DIR)
        recipe_id_path = rel.with_suffix("").as_posix()
        recipe_id = f"{MOD_ID}:{recipe_id_path}"
        with recipe_path.open() as fh:
            recipe = json.load(fh)
        if recipe.get("type") not in VANILLA_RECIPE_BOOK_TYPES:
            skipped_custom += 1
            continue
        try:
            ingredients = extract_ingredients(recipe)
            adv = make_advancement(recipe_id, ingredients)
        except ValueError as exc:
            skipped_problems.append(f"{recipe_id_path}: {exc}")
            continue
        out_path = ADV_DIR / rel
        out_path.parent.mkdir(parents=True, exist_ok=True)
        with out_path.open("w") as fh:
            json.dump(adv, fh, indent=2)
            fh.write("\n")
        written += 1
    print(f"wrote {written} advancements to {ADV_DIR.relative_to(ROOT)}")
    print(f"skipped {skipped_custom} custom-type recipes (not shown in vanilla recipe book)")
    if skipped_problems:
        print(f"skipped {len(skipped_problems)} with problems:")
        for s in skipped_problems:
            print(f"  - {s}")


if __name__ == "__main__":
    main()
