package me.luucka.voidteleport;

import me.luucka.voidteleport.config.BaseConfiguration;
import me.luucka.voidteleport.config.IConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VoidSpawnManager implements IConfig {

    private static final Logger LOGGER = Logger.getLogger("VoidTeleport");

    private final VoidTeleport plugin;

    private final File dataFolder;

    private final Map<String, SpawnLocation> worlds = new HashMap<>();

    public VoidSpawnManager(final VoidTeleport plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(this.plugin.getDataFolder(), "worlds");
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs();
        }
        reloadConfig();
    }

    public void setSpawnLocation(final String worldName, final Location location) {
        final SpawnLocation spawnLocation = worlds.get(worldName);
        if (spawnLocation == null) {
            final File file = new File(dataFolder, worldName + ".yml");
            if (file.exists()) return;
            worlds.put(worldName, new SpawnLocation(new BaseConfiguration(file), location));
            return;
        }
        spawnLocation.setLocation(location);
    }

    public void setYOffset(final String worldName, final double yOffset) throws Exception {
        final SpawnLocation spawnLocation = worlds.get(worldName);
        if (spawnLocation == null) {
            throw new Exception(plugin.getMessages().worldNotSet());
        }
        spawnLocation.setYOffset(yOffset);
    }

    public void teleport(final String worldName, final Player player, final Location toLocation) throws Exception {
        final SpawnLocation spawnLocation = worlds.get(worldName);
        if (spawnLocation == null) {
            throw new Exception(plugin.getMessages().worldNotSet());
        }
        if (!spawnLocation.canTeleport(toLocation.getY())) {
            throw new Exception(plugin.getMessages().worldNotSet());
        }
        (new BukkitRunnable() {
            @Override
            public void run() {
                spawnLocation.teleport(player);
            }
        }).runTaskLater(plugin, 0L);
    }

    public void remove(final String worldName) throws Exception {
        final SpawnLocation spawnLocation = worlds.get(worldName);
        if (spawnLocation == null) {
            throw new Exception(plugin.getMessages().worldNotSet());
        }
        spawnLocation.remove();
        worlds.remove(worldName);
    }

    @Override
    public void reloadConfig() {
        worlds.clear();
        final File[] listOfFiles = dataFolder.listFiles();
        if (listOfFiles.length >= 1) {
            for (final File file : listOfFiles) {
                String fileName = file.getName();
                if (file.isFile() && fileName.endsWith(".yml")) {
                    try {
                        worlds.put(fileName.substring(0, fileName.length() - 4), new SpawnLocation(new BaseConfiguration(file)));
                    } catch (final Exception ex) {
                        LOGGER.log(Level.WARNING, "World file " + fileName + " loading error!");
                    }
                }
            }
        }
    }
}
