# Quest Skip Check

## Overview

Verifies that the quest system follows its action helper pattern:
- **Quest action helpers** (`quest/actions/`): One idempotent `ensureWorldState(level, stage)` per questline — the single source of truth for stage → world state
- **CustomTransitions**: Call the helper + add presentation (particles, sounds, chat)
- **Debug commands**: Call the helper directly
- **Entity tick fallback**: Call the helper every ~100 ticks for chunk reload edge cases

This means `/lk quest set <questId> <stage>` works for every stage — instant, correct, no side effects needed from skipped stages.

## Usage

`/quest-skip-check`

No arguments needed — checks all questlines automatically.

## What It Checks

### 1. Trigger exists
Every stage (except COMPLETE) should have a trigger registered.

### 2. Action helper coverage
Every stage should have a case in its questline's `ensureWorldState()` switch. Each sub-method should be idempotent (check before acting).

**Check:** Read `quest/actions/RafikiQuestActions.java` and `quest/actions/OutlandsQuestActions.java`. Every stage enum value should appear in the switch.

### 3. CustomTransitions call the helper
Each `customTransition` should call `ensureWorldState()` for the new stage, then add presentation effects. It should NOT contain entity spawn/despawn or block mutation logic directly.

**Check:** Grep for `discard()`, `addFreshEntity()`, `setBlock()`, `destroyBlock()` in customTransition methods that don't go through the helper.

### 4. Debug command calls the helper
`LionKingCommands.questSet()` should call the appropriate `ensureWorldState()` after setting the stage.

### 5. Entity tick uses helper as fallback
Quest-relevant entities (Rafiki, Zira) should call `ensureWorldState()` every ~100 ticks as a lightweight fallback for chunk reload. They should NOT have inline quest stage checks that duplicate the helper logic.

**Check:** Grep for `getStage`, `isZiraOccupiesTree`, or quest stage enums in entity `tick()` methods outside of the helper call.

### 6. No boolean flags for quest state in WorldData
WorldData should not store booleans that duplicate quest stage information. Acceptable fields: quest manager, talk counters, tree position cache.

### 7. EnumSet for stage ranges
Stage range checks must use `EnumSet`, never `ordinal()` comparisons.

### 8. Helpers are idempotent
Each `ensure*` method should check current state before acting. Calling `ensureWorldState()` 100 times should produce the same result as calling it once.

## Flow

```
1. Read all questline definitions and action helper classes
2. For each stage:
   a. Verify trigger registration
   b. Verify helper covers this stage
   c. Verify customTransition calls helper (not inline mutations)
   d. Rate: ✅ correct, ⚠️ has issues, ❌ broken
3. Verify debug command calls helpers
4. Verify entity tick uses helpers (not inline logic)
5. Check WorldData for boolean quest-state flags
6. Check for ordinal() usage on stage enums
7. Output summary table
```

## Output Format

### Per-stage table:
```
| Stage | Trigger | Helper covers? | Transition calls helper? | Skip-safe? |
|---|---|---|---|---|
| FIND_RAFIKI | RAFIKI_TALK | ✅ | N/A (no transition) | ✅ |
| DEFEAT_SCAR | SCAR_KILLED | ✅ | ✅ helper + lightning VFX | ✅ |
```

### Architecture violations:
```
| File | Line | Issue |
|---|---|---|
| ZiraEntity.java:138 | inline stage check in tick() | Should use helper |
```

## Key Files to Check

### Where to look
- `quest/actions/` — action helper classes (one per questline, each has `ensureWorldState`)
- `quest/questline/` — questline definitions (customTransitions should call helpers)
- `command/` — debug commands (questSet should call helpers)
- `entity/npc/` — NPC entities (tick should use helper, not inline quest logic)
- `data/WorldData.java` — no boolean quest flags
- `block/` — quest-aware blocks (should be plain blocks if helper handles their state)
