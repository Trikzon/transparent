# Transparent
[![](http://cf.way2muchnoise.eu/full_377582_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/transparent)
[![](https://img.shields.io/modrinth/dt/transparent?logo=modrinth&style=flat)](https://www.modrinth.com/mod/transparent)
[![](http://cf.way2muchnoise.eu/versions/377582.svg)](https://www.curseforge.com/minecraft/mc-mods/transparent)

Transparent allows resource packs to give certain entities textures with transparent (or translucent) pixels. To use, simply install the mod and use or create a resource pack that gives one of the supported entities a transparent texture.

![Promotional Image](promo.png)

Resource pack used in the above image: [BwW Texturepack](https://www.curseforge.com/minecraft/texture-packs/bww-texturepack).

Officially supports [Paintings++ Mod](https://www.curseforge.com/minecraft/mc-mods/paintings)!

## Supported Entities
- Painting
- Item Frame
- End Crystal
- Beacon Beam (disabled by default)

If you want another entity to be supported by this mod, please let me know on [Discord](https://discord.gg/aqXkvbJ) or [GitHub Issues](https://github.com/Trikzon/transparent/issues).

## Configuration
| Entity      | Enabled By Default | Reason                                          |
|-------------|--------------------|-------------------------------------------------|
| Painting    | true               |                                                 |
| Item Frame  | true               |                                                 |
| End Crystal | true               |                                                 |
| Beacon Beam | false              | Enabling transparency causes render layer bugs. |

In order to change the default configuration, paste the following json file content into a file in your resource pack located at `/assets/transparent/transparent.json`.

```json
{
    "beacon_beam": false,
    "end_crystal": true,
    "item_frame": true,
    "painting": true
}
```

---

Report any issues on [GitHub](https://github.com/Trikzon/transparent/issues). Chat on [Discord](https://discord.gg/aUwZKagWh2) in the `#mc-mods` channel.

Support the development of my mods on [Ko-fi](https://ko-fi.com/X7X8D56YI).

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/X7X8D56YI)
