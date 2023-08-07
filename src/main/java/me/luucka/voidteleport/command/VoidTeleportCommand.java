package me.luucka.voidteleport.command;

import me.luucka.voidteleport.Messages;
import me.luucka.voidteleport.SpawnLocation;
import me.luucka.voidteleport.SpawnLocationManager;
import me.luucka.voidteleport.VoidTeleportPlugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.luucka.voidteleport.util.MMColor.toComponent;

public class VoidTeleportCommand extends BaseCommand {

    private final VoidTeleportPlugin plugin;
    private final Messages messages;
    private final SpawnLocationManager spawnLocationManager;

    public VoidTeleportCommand(final VoidTeleportPlugin plugin) {
        super("voidteleport", "VoidTeleport main command", "voidteleport.admin");
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.spawnLocationManager = plugin.getSpawnLocationManager();
        this.setUsage("/voidteleport < spawn | yoffset | remove | on | off | reload >");
    }

    @Override
    public void execute(CommandSource sender, String[] args) throws Exception {
        if (!sender.isPlayer()) throw new Exception(messages.noConsole());
        if (!testPermissionSilent(sender.getSender())) throw new Exception(messages.noPermission());
        if (args.length < CommandType.getMinArgsNeeded()) throw new Exception(messages.commandUsage(getUsage()));

        final CommandType cmd;
        try {
            cmd = CommandType.valueOf(args[0].toUpperCase());
        } catch (final IllegalArgumentException ex) {
            throw new Exception(messages.commandUsage(getUsage()));
        }

        final Player player = sender.getPlayer();

        switch (cmd) {
            case SPAWN -> spawnLocationManager.getSpawnLocationByWorld(player.getWorld()).ifPresentOrElse(
                    location -> {
                        location.setLocation(player.getLocation());
                        player.sendMessage(toComponent(messages.spawnUpdate()));
                    },
                    () -> {
                        spawnLocationManager.createSpawnLocation(player.getLocation());
                        player.sendMessage(toComponent(messages.spawnSet()));
                    }
            );
            case YOFFSET -> {
                if (args.length < cmd.argsNeeded)
                    throw new Exception(messages.commandUsage("/voidteleport yoffset <y-offset>"));

                double yOffset;
                try {
                    yOffset = Double.parseDouble(args[1]);
                } catch (final NumberFormatException ex) {
                    yOffset = -100.0D;
                }
                double finalYOffset = yOffset;
                spawnLocationManager.getSpawnLocationByWorld(player.getWorld()).ifPresentOrElse(
                        location -> {
                            location.setYOffset(finalYOffset);
                            player.sendMessage(toComponent(messages.yOffsetSet()));
                        },
                        () -> {
                            player.sendMessage(toComponent(messages.worldNotSet()));
                        }
                );
            }
            case REMOVE -> spawnLocationManager.getSpawnLocationByWorld(player.getWorld())
                    .ifPresent(location -> {
                                spawnLocationManager.remove(location);
                                player.sendMessage(toComponent(messages.worldTpRemoved()));
                            }
                    );
            case ON -> spawnLocationManager.getSpawnLocationByWorld(player.getWorld())
                    .ifPresent(location -> {
                        location.setStatus(SpawnLocation.Status.ON);
                        player.sendMessage(toComponent(messages.tpActive()));
                    });
            case OFF -> spawnLocationManager.getSpawnLocationByWorld(player.getWorld())
                    .ifPresent(location -> {
                        location.setStatus(SpawnLocation.Status.OFF);
                        player.sendMessage(toComponent(messages.tpInactive()));
                    });
            case RELOAD -> {
                plugin.reload();
                player.sendMessage(toComponent(messages.reload()));
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args) {
        if (!testPermissionSilent(sender.getSender())) return Collections.emptyList();

        if (args.length == 1) {
            final List<String> options = new ArrayList<>();
            for (final CommandType ct : CommandType.values()) {
                options.add(ct.name().toLowerCase());
            }
            return options;
        } else {
            return Collections.emptyList();
        }
    }

    private enum CommandType {
        SPAWN(1),
        YOFFSET(2),
        REMOVE(1),
        ON(1),
        OFF(1),
        RELOAD(1);

        private final int argsNeeded;

        CommandType(int argsNeeded) {
            this.argsNeeded = argsNeeded;
        }

        public static int getMinArgsNeeded() {
            int min = values()[0].argsNeeded;
            for (CommandType ct : values()) {
                if (ct.argsNeeded < min) min = ct.argsNeeded;
            }
            return min;
        }
    }
}
