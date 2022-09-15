package me.luucka.voidteleport.commands;

import me.luucka.voidteleport.VoidTeleport;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.luucka.voidteleport.utils.Color.colorize;

public class VoidTeleportCommand extends BaseCommand {

    private final VoidTeleport plugin;

    public VoidTeleportCommand(final VoidTeleport plugin) {
        super("voidteleport", "VoidTeleport main command", "voidteleport.admin", "voidtp", "vtp");
        this.plugin = plugin;
        this.setUsage("/voidteleport <spawn | yoffset | remove | reload>");
    }

    @Override
    public void execute(CommandSource sender, String[] args) throws Exception {
        if (!testPermissionSilent(sender.getSender())) throw new Exception(plugin.getMessages().noPermission());

        if (args.length < 1) throw new Exception(plugin.getMessages().commandUsage(this.getUsage()));

        final CommandType cmd;
        try {
            cmd = CommandType.valueOf(args[0].toUpperCase());
        } catch (final IllegalArgumentException ex) {
            throw new Exception(plugin.getMessages().commandUsage(this.getUsage()));
        }

        switch (cmd) {
            case SPAWN -> {
                if (!sender.isPlayer()) throw new Exception(plugin.getMessages().noConsole());
                final Player player = sender.getPlayer();

                final String worldName = player.getWorld().getName();
                final Location location = player.getLocation();

                this.plugin.getVoidSpawnManager().setSpawnLocation(worldName, location);

                player.sendMessage(colorize(plugin.getMessages().spawnSet()));
            }
            case YOFFSET -> {
                if (!sender.isPlayer()) throw new Exception(plugin.getMessages().noConsole());

                if (args.length < 2)
                    throw new Exception(plugin.getMessages().commandUsage("/voidteleport yoffset <y-offset>"));

                final Player player = sender.getPlayer();

                final String worldName = player.getWorld().getName();

                double yOffset;
                try {
                    yOffset = Double.parseDouble(args[1]);
                } catch (final NumberFormatException ex) {
                    yOffset = -100.0D;
                }

                plugin.getVoidSpawnManager().setYOffset(worldName, yOffset);

                player.sendMessage(colorize(plugin.getMessages().yOffsetSet()));
            }
            case REMOVE -> {
                if (!sender.isPlayer()) throw new Exception(plugin.getMessages().noConsole());
                final Player player = sender.getPlayer();

                final String worldName = player.getWorld().getName();

                this.plugin.getVoidSpawnManager().remove(worldName);

                player.sendMessage(colorize(plugin.getMessages().worldTpRemoved()));
            }
            case RELOAD -> {
                this.plugin.reload();
                sender.sendMessage(plugin.getMessages().reload());
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
        SPAWN,
        YOFFSET,
        REMOVE,
        RELOAD
    }
}
