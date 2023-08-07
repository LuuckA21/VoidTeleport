package me.luucka.voidteleport.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import me.luucka.voidteleport.Messages;
import me.luucka.voidteleport.SpawnLocation;
import me.luucka.voidteleport.SpawnLocationManager;
import me.luucka.voidteleport.VoidTeleportPlugin;

import static me.luucka.voidteleport.util.MMColor.toComponent;

public class VoidTeleportCommandNew {

    private final VoidTeleportPlugin plugin;
    private final Messages messages;
    private final SpawnLocationManager spawnLocationManager;

    public VoidTeleportCommandNew(final VoidTeleportPlugin plugin) {
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
                                                player.sendMessage(toComponent(messages.spawnUpdate()));
                                            },
                                            () -> {
                                                spawnLocationManager.createSpawnLocation(player.getLocation());
                                                player.sendMessage(toComponent(messages.spawnSet()));
                                            }
                                    );
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("yoffset")
                                .withUsage("/voidteleport yoffset <number>")
                                .withArguments(new IntegerArgument("offset"))
                                .withShortDescription("Set the Y-offset in this world (integer number)")
                                .executesPlayer((player, args) -> {
                                    spawnLocationManager.getSpawnLocationByWorld(player.getWorld()).ifPresentOrElse(
                                            location -> {
                                                location.setYOffset((Double) args.get("offset"));
                                                player.sendMessage(toComponent(messages.yOffsetSet()));
                                            },
                                            () -> player.sendMessage(toComponent(messages.worldNotSet()))
                                    );
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("remove")
                                .withUsage("/voidteleport remove")
                                .withShortDescription("Remove spawn from this world")
                                .executesPlayer((player, args) -> {
                                    spawnLocationManager.getSpawnLocationByWorld(player.getWorld())
                                            .ifPresent(location -> {
                                                        spawnLocationManager.remove(location);
                                                        player.sendMessage(toComponent(messages.worldTpRemoved()));
                                                    }
                                            );
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("on")
                                .withUsage("/voidteleport on")
                                .withShortDescription("Activate void teleport spawn in this world")
                                .executesPlayer((player, args) -> {
                                    spawnLocationManager.getSpawnLocationByWorld(player.getWorld())
                                            .ifPresent(location -> {
                                                location.setStatus(SpawnLocation.Status.ON);
                                                player.sendMessage(toComponent(messages.tpActive()));
                                            });
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("off")
                                .withUsage("/voidteleport off")
                                .withShortDescription("Deactivate void teleport spawn in this world")
                                .executesPlayer((player, args) -> {
                                    spawnLocationManager.getSpawnLocationByWorld(player.getWorld())
                                            .ifPresent(location -> {
                                                location.setStatus(SpawnLocation.Status.OFF);
                                                player.sendMessage(toComponent(messages.tpInactive()));
                                            });
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("reload")
                                .withUsage("/voidteleport reload")
                                .withShortDescription("Reload plugin")
                                .executesPlayer((player, args) -> {
                                    plugin.reload();
                                    player.sendMessage(toComponent(messages.reload()));
                                })
                );

        voidteleportCommand.withUsage(
                voidteleportCommand.getSubcommands().stream().map(command -> command.getUsage()[0] + " - " + command.getShortDescription()).toArray(String[]::new)
        ).register();
    }

}
