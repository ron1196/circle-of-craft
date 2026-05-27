---
name: animate-entity
description: Use when creating, editing, or previewing GeckoLib entity animations via Blockbench MCP. Triggers on animation work for any mod entity - idle, walk, attack, death, or custom animations.
---

# Animate Entity

Create and edit GeckoLib entity animations through Blockbench MCP tools.

## Usage

`/animate-entity <entity_name> <animation_type>`

Examples:
- `/animate-entity termite_queen death`
- `/animate-entity rafiki idle`
- `/animate-entity hyena walk`

## Creative Process

**This is the core workflow. Follow it for EVERY animation.**

### Phase 1: Discovery

1. Get bone structure from the open Blockbench project:
```js
// risky_eval
Group.all.map(g => ({name: g.name, parent: g.parent ? g.parent.name : null}))
```

2. Cross-reference with geo.json at `src/main/resources/assets/circleofcraft/geo/entity/{entity_name}.geo.json`

3. List existing animations:
```js
// risky_eval
Animation.all.map(a => ({name: a.name, length: a.length, loop: a.loop}))
```

### Phase 2: Generate 5 Unique Variations

Create 5 distinctly different animations in Blockbench, each with a unique creative concept. **Do NOT make generic/cookie-cutter animations.** Each variation must have a clear identity.

For each animation type, think about what makes it unique. Examples of differentiation:

**Death animations might vary by:**
- Collapse style (forward crash, sideways tip, straight down, rear-up-then-crash, slow crumble)
- Leg behavior (curl inward like dead spider, splay outward, one side gives out first)
- Dramatic pacing (instant, slow fade, violent convulsion, defiant last stand)
- Body physics (bounce on impact, ragdoll, stiff topple)

**Idle animations might vary by:**
- Personality (alert/nervous, lazy/sleepy, aggressive/territorial, curious/searching, regal/calm)
- Focus of motion (head-dominant, body-breathing, limb-fidgeting, tail/appendage-focused)
- Rhythm (slow and smooth, twitchy and irregular, rhythmic swaying)

**Walk animations might vary by:**
- Speed feel (heavy trudge, light skitter, confident stride, cautious creep, charging rush)
- Body language (head low hunting, head high alert, swaying relaxed, rigid tense)
- Gait pattern (symmetric, asymmetric limp, tripod insect, gallop)

**Attack animations might vary by:**
- Style (bite, slam, charge, projectile windup, tail sweep, grab)
- Tempo (quick jab, heavy windup, multi-hit combo, feint-then-strike)
- Body commitment (head-only snap, full-body lunge, stationary swipe)

Name them `animation.{entity_name}.{type}1` through `animation.{entity_name}.{type}5`.

Present the user a table:

| # | Name | Duration | Description |
|---|------|----------|-------------|
| 1 | ... | ...s | One-line concept |
| 2 | ... | ...s | One-line concept |
| 3 | ... | ...s | One-line concept |
| 4 | ... | ...s | One-line concept |
| 5 | ... | ...s | One-line concept |

### Phase 3: Merge into 6th

Send ALL 5 animation descriptions (detailed, with bone-by-bone breakdown of what each does) to a **sonnet subagent** via the Agent tool. Ask it to design ONE optimal animation merging the best elements. The subagent should explain WHY each element was chosen or cut.

Create the merged animation as `animation.{entity_name}.{type}6`.

### Phase 4: User Chooses

Present all 6 options. The user picks one (or asks for tweaks). Rename the chosen animation to the final name `animation.{entity_name}.{type}`.

Delete the other 5 variations from Blockbench:
```js
// risky_eval: clean up variations
Animation.all.filter(a => /\.(type)\d$/.test(a.name)).forEach(a => a.remove());
```

### Phase 5: Export

Export all animations to JSON:
```js
// risky_eval: export and save
(function() {
    var res = {};
    Animation.all.forEach(function(anim) {
        var ad = {animation_length: anim.length, bones: {}};
        if (anim.loop === 'loop') ad.loop = true;
        for (var bId in anim.animators) {
            var an = anim.animators[bId];
            var grp = Group.all.find(function(g) { return g.uuid === bId; });
            if (!grp) continue;
            var bd = {};
            ['rotation', 'position', 'scale'].forEach(function(ch) {
                var kfs = an[ch];
                if (!kfs || kfs.length === 0) return;
                kfs = kfs.slice().sort(function(a,b){return a.time - b.time});
                var cd = {};
                kfs.forEach(function(kf) {
                    var dp = kf.data_points[0];
                    cd[String(kf.time)] = [parseFloat(dp.x)||0, parseFloat(dp.y)||0, parseFloat(dp.z)||0];
                });
                bd[ch] = cd;
            });
            if (Object.keys(bd).length > 0) ad.bones[grp.name] = bd;
        }
        res[anim.name] = ad;
    });
    var json = JSON.stringify({format_version: "1.8.0", animations: res}, null, '\t');
    var path = "/Users/ronmizrachi/private-dev/TheLionKing/src/main/resources/assets/circleofcraft/animations/entity/ENTITY_NAME.animation.json";
    require('fs').writeFileSync(path, json);
    return "Saved " + Object.keys(res).length + " animations";
})()
```

Replace `ENTITY_NAME` with the actual entity name.

## Blockbench Operations

### Create Animation

Use `mcp__blockbench__create_animation`. The tool prepends `animation.` automatically, so fix after:

```js
// risky_eval: fix doubled prefix
Animation.all.forEach(a => {
    if (a.name.startsWith('animation.animation.'))
        a.name = a.name.replace('animation.animation.', 'animation.');
});
```

### Preview

```js
// risky_eval: select animation and scrub to time
var anim = Animation.all.find(a => a.name === 'animation.termite_queen.death');
if (anim) { anim.select(); Timeline.setTime(1.5); }
```

Then use `mcp__blockbench__capture_screenshot` and `mcp__blockbench__set_camera_angle` (requires `projection: "perspective"`).

### Delete

```js
// risky_eval
var anim = Animation.all.find(a => a.name === 'animation.termite_queen.death');
if (anim) anim.remove();
```

## Animation Fundamentals

- **Use rotation for limbs, not position** — rotation looks natural
- `loop: true` for idle/walk, `false` for attack/death
- Asymmetric timing on paired parts (left vs right) feels organic
- Secondary motion (abdomen lag, antenna follow-through) adds weight
- A secondary bounce on impact sells heaviness
- `thenPlayAndHold` in Java for death (holds final frame)
- `thenLoop` for idle/walk
- Large entities may need `getMotionAnimThreshold()` override (return `0.001f`) for walk detection

## Java Integration

```java
// Constants
private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.entity_name.idle");
private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.entity_name.walk");
private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("animation.entity_name.attack");
private static final RawAnimation DEATH_ANIM = RawAnimation.begin().thenPlayAndHold("animation.entity_name.death");

// Controller
private PlayState mainController(AnimationState<MyEntity> state) {
    if (this.dead) return state.setAndContinue(DEATH_ANIM);
    if (state.isMoving()) return state.setAndContinue(WALK_ANIM);
    return state.setAndContinue(IDLE_ANIM);
}

// Triggerable (attack) — register + fire
controllers.add(new AnimationController<>(this, "main", 5, this::mainController)
    .triggerableAnim("animation.entity_name.attack", ATTACK_ANIM));
triggerAnim("main", "animation.entity_name.attack"); // in doHurtTarget
```

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Legs animated with position only | Use rotation — much more natural |
| Animation name doubled (`animation.animation.`) | Fix with risky_eval after create_animation |
| Walk not playing (large entities) | Override `getMotionAnimThreshold()` in renderer to return `0.001f` |
| Export missing bones | Ensure bone names match Group.all UUIDs, not Cube names |
| Death doesn't hold | Use `thenPlayAndHold`, not `thenPlay` |
| All 5 variations look similar | Each MUST have a distinct concept/personality — not just parameter tweaks |