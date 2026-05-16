<p align="center">
  <img src="banner.jpg" alt="The Lion King Mod" />
</p>

<h1 align="center">The Lion King Mod</h1>

<p align="center">
  <b>The Lion King meets Minecraft.</b><br/>
  Explore the Pride Lands, journey through the Outlands, and complete quests in an entirely new Lion King-themed adventure.
</p>

<p align="center">
  <a href="https://github.com/ron1196/TheLionKing/releases"><img src="https://img.shields.io/github/v/release/ron1196/TheLionKing?style=flat-square&color=orange&label=latest%20release" alt="Latest Release" /></a>
  <img src="https://img.shields.io/badge/minecraft-1.20.1-green?style=flat-square" alt="Minecraft 1.20.1" />
  <img src="https://img.shields.io/badge/mod%20loader-NeoForge-blue?style=flat-square" alt="NeoForge" />
  <img src="https://img.shields.io/badge/java-17-red?style=flat-square" alt="Java 17" />
  <a href="https://github.com/ron1196/TheLionKing/blob/main/LICENSE"><img src="https://img.shields.io/github/license/ron1196/TheLionKing?style=flat-square" alt="License" /></a>
</p>

---

## About

The Lion King Mod brings the world of Disney's The Lion King into Minecraft. It adds **three new dimensions**, dozens of animals and NPCs, unique ores, tools, armor, a full quest line, custom music, and much more.

This is a **ground-up port** of the classic Lion King Mod (originally for Minecraft 1.4–1.6) to modern NeoForge 1.20.1.

## Features

### Dimensions
- **Pride Lands** — A vast savannah realm with unique biomes, trees, flowers, ores, and wildlife. Fully survivable — everything you need can be found within.
- **Outlands** — A dark and dangerous dimension, home to hyenas and other threats. Unlocked through Rafiki's quest.
- **Upendi** — A magical twilight realm.

### Mobs & NPCs
- **Animals** — Lions, lionesses, zebras, giraffes, crocodiles, bugs, and more — all breedable.
- **Hostile mobs** — Hyenas, vultures, the Termite Queen boss, and more.
- **NPCs** — Rafiki, Simba, Scar, Zira, and the Ticket Lion, each with unique interactions.

### Quest System
- **Rafiki's Quest** — A multi-stageKey adventure. Collect hyena bones, earn Rafiki's Stick, hunt down Scar in his cave, and ultimately summon your own companion Simba who fights for you and carries your items.
- **Outlands Quest** — A 10-stageKey quest line in the dangerous Outlands.
- **Book of Quests** — Tracks your progress, describes items, and shows crafting recipes.

### Blocks, Items & Crafting
- **106 new blocks** — Pridestone, kingswood, mango wood, rainforest wood, passion fruit, and more.
- **147+ new items** — Tools, armor, food, quest items, and decorative items across 5 tool tiers and 5 armor materials.
- **Grinding Bowl** — A custom crafting station for grinding items into powders.
- **8 creative tabs** to browse everything.

### World Generation
- **14 biomes** across the three dimensions.
- **5 landmark structures** including the Ticket Booth and Rafiki's Tree.
- **Custom ores and trees** with full worldgen integration.

### Audio
- Custom **music tracks** for each dimension.
- Unique **mob sounds** for all entities.

### Advancements
- **33 custom advancements** to track your journey through the Pride Lands and beyond.

## Getting Started

1. **Find a Ticket Booth** — They spawn naturally in your Overworld, most commonly in plains, deserts, and swamps.
2. **Buy a ticket** — Give the Ticket Lion a gold ingot to receive a Lion King Ticket.
3. **Enter the portal** — Use the ticket on the portal inside the booth to activate it and step through.
4. **Find Rafiki** — Head to the center of the Pride Lands (coordinates 0, 0) and speak with Rafiki to begin your quest.

> **Tip:** Stepping into an activated Lion Portal sets your spawn to the portal. If you die in the Pride Lands or Outlands, you'll respawn there. Beds also work in the Pride Lands — craft one with lion fur in place of wool.

## Installation

1. Install [NeoForge for Minecraft 1.20.1](https://neoforged.net/).
2. Download the latest mod JAR from [Releases](https://github.com/ron1196/TheLionKing/releases).
3. Place the JAR in your `.minecraft/mods/` folder.
4. Launch Minecraft with the NeoForge profile.

## Building from Source

Requires **Java 17**.

```bash
git clone https://github.com/ron1196/TheLionKing.git
cd TheLionKing
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home  # macOS
./gradlew build
```

The built JAR will be in `build/libs/`.

To run the mod in a development environment:

```bash
./gradlew runClient   # Launch the game client
./gradlew runServer   # Launch a dedicated server
```

### Running tests

```bash
./gradlew runGameTestServer --no-build-cache
```

`--no-build-cache` avoids a known Gradle issue on macOS where the build-cache packer fails on class files carrying extended attributes (`Could not get file mode for ...`). See [`docs/AUTOMATED_TESTING.md`](docs/AUTOMATED_TESTING.md) for the full testing guide (JUnit + Mojang Game Tests).

## Screenshots

*Coming soon — contributions welcome!*

## Credits

- **Original mod** by [redrosewarrior1](https://www.curseforge.com/minecraft/mc-mods/the-lion-king-mod) — the classic Lion King Mod for Minecraft 1.4–1.6.
- **NeoForge port** by [ron1196](https://github.com/ron1196).

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests. See the project structure in [CLAUDE.md](CLAUDE.md) for an overview of the codebase.

## License

See [LICENSE](LICENSE) for details.
