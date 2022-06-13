# Transparent

[![GitHub Releases](https://img.shields.io/github/v/release/trikzon/transparent?include_prereleases&style=flat-square)](https://github.com/trikzon/transparent/releases)
[![](http://cf.way2muchnoise.eu/full_377582_fabric.svg)](https://www.curseforge.com/minecraft/mc-mods/transparent-fabric)
[![](http://cf.way2muchnoise.eu/full_410507_forge.svg)](https://www.curseforge.com/minecraft/mc-mods/transparent-forge)
[![Discord Server](https://img.shields.io/discord/450018397657235460.svg?color=blueviolet&style=flat-square)](https://discord.gg/aqXkvbJ)

Allows resource packs to make entities support transparency.

![Promo Image](https://i.imgur.com/C0PElij.png)

Resource pack in above image: [BwW Texturepack](https://www.curseforge.com/minecraft/texture-packs/bww-texturepack)

## Supported Entities:

Entities with a '*' are disabled by default.

- Painting
- Item Frame
- Beacon Beam*

Want an entity added to that list? Contact me on discord: https://discord.gg/aqXkvbJ

## Config

Configuration is done through resource packs.

If you only want the defaults, you don't need a configuration file.

Note: Defaults may change in future updates if

| Entity      | Enabled By Default | Reason |
| ----------- | ------------------ | ------ |
| Painting    | true               |
| Item Frame  | true               |
| Beacon Beam | false              | Enabling transparency causes render layer bugs. |

### Default configuration file

Place in your resource pack in `/assets/transparent/transparent.json`.

```json
{
    "painting": true,
    "item_frame": true,
    "beacon_beam": false
}
```

[<img src="https://user-images.githubusercontent.com/14358394/115450238-f39e8100-a21b-11eb-89d0-fa4b82cdbce8.png" width="200">](https://ko-fi.com/trikzon)
