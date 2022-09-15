package me.luucka.voidteleport;

import lombok.Getter;
import me.luucka.voidteleport.config.BaseConfiguration;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnLocation {

    private final BaseConfiguration config;

    @Getter
    private Location location;

    @Getter
    private double yOffset;

    public SpawnLocation(final BaseConfiguration config) {
        this.config = config;
        this.config.load();
        this.location = this.config.getLocation("world").location();
        this.yOffset = this.config.getDouble("y-offset", -100.0D);
    }

    public SpawnLocation(final BaseConfiguration config, final Location location) {
        this.config = config;
        this.config.load();
        this.location = location;
        this.yOffset = -100.0D;
        save();
    }

    public void setLocation(Location location) {
        this.location = location;
        save();
    }

    public void setYOffset(double yOffset) {
        this.yOffset = yOffset;
        save();
    }

    public boolean canTeleport(final double toYOffset) {
        return toYOffset < yOffset;
    }

    public void teleport(final Player player) {
        player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public void save() {
        this.config.setProperty("world", location);
        this.config.setProperty("y-offset", yOffset);
        this.config.save();
    }

    public void remove() {
        config.getFile().delete();
    }
}
