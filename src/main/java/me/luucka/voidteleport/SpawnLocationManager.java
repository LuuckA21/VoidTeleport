package me.luucka.voidteleport;

import me.luucka.extendlibrary.util.IReload;
import me.luucka.voidteleport.config.BaseConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

public class SpawnLocationManager implements IReload {
    
    private final File dataFolder;
    private final Set<SpawnLocation> spawnLocations = new HashSet<>();

    public SpawnLocationManager(final VoidTeleportPlugin plugin) {
        this.dataFolder = new File(plugin.getDataFolder(), "worlds");
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs();
        }
        reload();
    }

    public Optional<SpawnLocation> getSpawnLocationByWorld(final World world) {
        return spawnLocations.stream().filter(spawnLocation -> spawnLocation.getWorldName().equalsIgnoreCase(world.getName())).findFirst();
    }

    public Optional<SpawnLocation> getSpawnLocationWithStatusOnByWorld(final World world) {
        return spawnLocations.stream().filter(spawnLocation -> spawnLocation.getWorldName().equalsIgnoreCase(world.getName()) && spawnLocation.getStatus() == SpawnLocation.Status.ON).findFirst();
    }

    public void createSpawnLocation(final Location location) {
        final File file = new File(dataFolder, location.getWorld().getName() + ".yml");
        if (file.exists()) return;
        spawnLocations.add(new SpawnLocation(new BaseConfiguration(file), location));
    }

    public void remove(final SpawnLocation location) {
        spawnLocations.remove(location);
        location.delete();
    }

    @Override
    public void reload() {
        spawnLocations.clear();
        final File[] listOfFiles = dataFolder.listFiles();
        if (listOfFiles.length >= 1) {
            for (final File file : listOfFiles) {
                String fileName = file.getName();
                if (file.isFile() && fileName.endsWith(".yml")) {
                    try {
                        spawnLocations.add(new SpawnLocation(new BaseConfiguration(file)));
                    } catch (final Exception ex) {
                        Bukkit.getLogger().log(Level.WARNING, "World file " + fileName + " loading error!");
                    }
                }
            }
        }
    }
}
