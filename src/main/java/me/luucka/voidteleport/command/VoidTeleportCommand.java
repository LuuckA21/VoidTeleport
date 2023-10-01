package me.luucka.voidteleport.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import me.luucka.extendlibrary.message.Message;
import me.luucka.voidteleport.SpawnLocation;
import me.luucka.voidteleport.SpawnLocationManager;
import me.luucka.voidteleport.VoidTeleportPlugin;

public class VoidTeleportCommand {

    private final VoidTeleportPlugin plugin;
    private final Message messages;
    private final SpawnLocationManager spawnLocationManager;

    public VoidTeleportCommand(final VoidTeleportPlugin plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.spawnLocationManager = plugin.getSpawnLocationManager();
        register();
    }

    private void register() {
        CommandAPICommand voidteleportCommand = new CommandAPICommand("voidteleport")
                .withHelp("VoidTeleport main command", "VoidTeleport main command")
                .withPermission("voidteleport.admin")
                .withSubcommand(
                        new CommandAPICommand("spawn")
                                .withUsage("/voidteleport spawn")
                                .withShortDescription("Set or Update spawn in this world")
                                .executesPlayer((player, args) -> {
                                    spawnLocationManager.getSpawnLocationByWorld(player.getWorld()).ifPresentOrElse(
                                            location -> {
                                                location.setLocation(player.getLocation());
                                                messages.from("spawn-update").send(player);
                                            },
                                            () -> {
                                                spawnLocationManager.createSpawnLocation(player.getLocation());
                                                messages.from("spawn-set").send(player);
                                            }
                                    );
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("yoffset")
                                .withUsage("/voidteleport yoffset <number>")
                                .withArguments(new DoubleArgument("offset"))
                                .withShortDescription("Set the Y-offset in this world")
                                .executesPlayer((player, args) -> {
                                    spawnLocationManager.getSpawnLocationByWorld(player.getWorld()).ifPresentOrElse(
                                            location -> {
                                                location.setYOffset((double) args.get("offset"));
                                                messages.from("offset-set").send(player);
                                            },
                                            () -> messages.from("spawn-not-set").send(player)
                                    );
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("remove")
                                .withUsage("/voidteleport remove")
                                .withShortDescription("Remove spawn and teleport from this world")
                                .executesPlayer((player, args) -> {
                                    spawnLocationManager.getSpawnLocationByWorld(player.getWorld()).ifPresentOrElse(
                                            location -> {
                                                spawnLocationManager.remove(location);
                                                messages.from("spawn-tp-removed").send(player);
                                            },
                                            () -> messages.from("spawn-not-set").send(player)
                                    );
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("on")
                                .withUsage("/voidteleport on")
                                .withShortDescription("Activate teleport to spawn in this world")
                                .executesPlayer((player, args) -> {
                                    spawnLocationManager.getSpawnLocationByWorld(player.getWorld()).ifPresentOrElse(
                                            location -> {
                                                location.setStatus(SpawnLocation.Status.ON);
                                                messages.from("tp-active").send(player);
                                            },
                                            () -> messages.from("spawn-not-set").send(player)
                                    );
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("off")
                                .withUsage("/voidteleport off")
                                .withShortDescription("Deactivate teleport to spawn in this world")
                                .executesPlayer((player, args) -> {
                                    spawnLocationManager.getSpawnLocationByWorld(player.getWorld()).ifPresentOrElse(
                                            location -> {
                                                location.setStatus(SpawnLocation.Status.OFF);
                                                messages.from("tp-inactive").send(player);
                                            },
                                            () -> messages.from("spawn-not-set").send(player)
                                    );
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("reload")
                                .withUsage("/voidteleport reload")
                                .withShortDescription("Reload plugin")
                                .executesPlayer((player, args) -> {
                                    plugin.reload();
                                    messages.from("reload").send(player);
                                })
                );

        voidteleportCommand.withUsage(
                voidteleportCommand.getSubcommands().stream().map(command -> command.getUsage()[0] + " - " + command.getShortDescription()).toArray(String[]::new)
        ).register();
    }

}
