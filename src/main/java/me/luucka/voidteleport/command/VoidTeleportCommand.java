package me.luucka.voidteleport.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import me.luucka.voidteleport.Messages;
import me.luucka.voidteleport.SpawnLocation;
import me.luucka.voidteleport.SpawnLocationManager;
import me.luucka.voidteleport.VoidTeleportPlugin;

public class VoidTeleportCommand {

    private final VoidTeleportPlugin plugin;
    private final Messages messages;
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
                                                player.sendRichMessage(messages.spawnUpdate());
                                            },
                                            () -> {
                                                spawnLocationManager.createSpawnLocation(player.getLocation());
                                                player.sendRichMessage(messages.spawnSet());
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
                                                player.sendRichMessage(messages.yOffsetSet());
                                            },
                                            () -> player.sendRichMessage(messages.worldNotSet())
                                    );
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("remove")
                                .withUsage("/voidteleport remove")
                                .withShortDescription("Remove spawn from this world")
                                .executesPlayer((player, args) -> {
                                    spawnLocationManager.getSpawnLocationByWorld(player.getWorld()).ifPresent(
                                            location -> {
                                                spawnLocationManager.remove(location);
                                                player.sendRichMessage(messages.worldTpRemoved());
                                            }
                                    );
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("on")
                                .withUsage("/voidteleport on")
                                .withShortDescription("Activate spawn in this world")
                                .executesPlayer((player, args) -> {
                                    spawnLocationManager.getSpawnLocationByWorld(player.getWorld()).ifPresent(
                                            location -> {
                                                location.setStatus(SpawnLocation.Status.ON);
                                                player.sendRichMessage(messages.tpActive());
                                            });
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("off")
                                .withUsage("/voidteleport off")
                                .withShortDescription("Deactivate spawn in this world")
                                .executesPlayer((player, args) -> {
                                    spawnLocationManager.getSpawnLocationByWorld(player.getWorld()).ifPresent(
                                            location -> {
                                                location.setStatus(SpawnLocation.Status.OFF);
                                                player.sendRichMessage(messages.tpInactive());
                                            });
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("reload")
                                .withUsage("/voidteleport reload")
                                .withShortDescription("Reload plugin")
                                .executesPlayer((player, args) -> {
                                    plugin.reload();
                                    player.sendRichMessage(messages.reload());
                                })
                );

        voidteleportCommand.withUsage(
                voidteleportCommand.getSubcommands().stream().map(command -> command.getUsage()[0] + " - " + command.getShortDescription()).toArray(String[]::new)
        ).register();
    }

}
