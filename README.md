# MineMonTCG 🎮

A Trading Card Game plugin for Minecraft servers that brings the excitement of collecting, trading, and battling with cards to your server!

## Features ✨

- **Card System**
  - Unique cards with custom properties
  - Multiple types (Fire, Water, Grass, etc.)
  - Rarity system (Common to Legendary)
  - Shiny variants of cards
  - Custom card designs using resource packs

- **Card Packs**
  - Different pack types (Basic, Premium, Legendary)
  - Custom drop rates per card
  - Beautiful pack opening animations
  - Guaranteed rarity system

- **Trading System**
  - Safe player-to-player trading
  - Trade multiple cards at once
  - Trade confirmation system
  - Duplicate card protection

- **Battle System**
  - Player vs Player battles
  - AI battles for practice
  - Type advantages/disadvantages
  - Strategic gameplay mechanics

## Configuration 📝

### Cards (`cards.yml`)
```yaml
cards:
  blaziken:
    cardName: "Blaziken"
    types: ["fire", "dragon"]
    rarity: "legendary"
    set: "base_set"
    description:
      - "A fierce fire-type pokemon"
      - "that never backs down from a fight"
    canBeShiny: true
```

### Card Types (`types.yml`)
```yaml
types:
  fire:
    displayName: "<gradient:red:yellow>Fire</gradient>"
    weakAgainst: ["water", "rock", "ground"]
    strongAgainst: ["grass", "ice", "bug", "steel"]
```

### Card Sets (`sets.yml`)
```yaml
sets:
  base_set:
    displayName: "<gradient:gold:yellow>Base Set</gradient>"
    releaseDate: "01/01/2024"
    endDate: "12/31/2024"
```

### Card Packs (`packs.yml`)
```yaml
packs:
  basic_pack:
    displayName: "<gradient:yellow:gold>Basic Card Pack</gradient>"
    cards:
      blaziken: 5    # 5% chance
      aquadragon: 15 # 15% chance
      grassspirit: 80 # 80% chance
```

## Installation 🚀

1. Download the latest release from the releases page
2. Place the jar file in your server's `plugins` folder
3. Start/restart your server
4. Configure the plugin in the generated config files
5. Optional: Install a resource pack for custom card designs

## Commands 📜

- `/card view <card_id>` - View details of a specific card
- `/card list` - List all your cards
- `/pack open <pack_type>` - Open a card pack
- `/pack list` - List available pack types
- `/trade <player>` - Initiate a trade with another player
- `/battle <player>` - Challenge a player to a battle
- `/deck` - Manage your card deck

## Permissions 🔑

- `minemon.card.view` - Allow viewing cards
- `minemon.card.list` - Allow listing cards
- `minemon.pack.open` - Allow opening packs
- `minemon.pack.list` - Allow listing packs
- `minemon.trade` - Allow trading cards
- `minemon.battle` - Allow battling with cards
- `minemon.admin` - Admin permissions for all commands

## Dependencies 🔧

- Spigot/Paper 1.19+
- [Optional] PlaceholderAPI for advanced placeholders
- [Optional] Vault for economy integration

## Support 💬

If you encounter any issues or have suggestions:
1. Check the [Issues](../../issues) page
2. Create a new issue if yours isn't listed
3. Join our Discord server for direct support

## Contributing 🤝

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

## License 📄

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Credits 👏

- Developer: Person98
- Contributors: List of contributors
- Special thanks to the Minecraft plugin development community

---
Made with ❤️ by Person98 