# VoidTeleport

Teleport a player to spawn when he falls into the void

# Commands

### /voidteleport

* **/voidteleport spawn** - Set or Update spawn in this world
* **/voidteleport yoffset <number>** - Set the Y-offset in this world"
* **/voidteleport remove** - Remove spawn from this world
* **/voidteleport on** - Activate teleport to spawn in this world
* **/voidteleport off** - Deactivate teleport to spawn in this world
* **/voidteleport reload** - Reload plugin

# Permissions

* **voidteleport.admin** - Permission for /voidteleport commands

# Language (messages.properties)

```properties
# This plugin uses MiniMessage as the component text format.
# LEGACY TEXT FORMATTING (&) IS NOT SUPPORTED
# Use https://webui.adventure.kyori.net/ to preview parsed components
# You can find full documentation on the format (including normal colors and formatting, rgb, click/hover events, and more)
# here: https://docs.adventure.kyori.net/minimessage/format
# Messages containing the placeholder "<prefix>" will have it replaced with the following string
prefix=<grey>[<#9403fc>Void<#0373fc>Teleport<grey>]
reload=<prefix> <#31e31e>Plugin reloaded!
spawn-not-set=<prefix> <#d63e4d>Void spawn location is not set in this world
spawn-set=<prefix> <#31e31e>Void spawn set successfully
spawn-update=<prefix> <#31e31e>Void spawn update successfully
spawn-remove=<prefix> <#d63e4d>Void spawn removed successfully
offset-set=<prefix> <#31e31e>Y-Offset set successfully
spawn-active=<prefix> <#31e31e>Void spawn teleport is now active in this world
spawn-inactive=<prefix> <#d63e4d>Void spawn teleport is now inactive in this world
```

[Other translations](https://hangar.papermc.io/LuuckA/AdvancedBooks/pages/Translations)

If you translate to another language, open an issue on [Github](https://github.com/LuuckA21/VoidTeleport/issues)

# Issue or Support

**Found an issue?** Report the issue on [Github](https://github.com/LuuckA21/VoidTeleport/issues) or join
the [Discord](https://discord.gg/HQZtzDjzgN)