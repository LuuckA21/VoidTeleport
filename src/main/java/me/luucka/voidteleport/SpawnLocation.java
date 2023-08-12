package me.luucka.voidteleport;

import me.luucka.voidteleport.config.BaseConfiguration;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class SpawnLocation {

    private final BaseConfiguration config;

    public String getWorldName() {
        return this.location.getWorld().getName();
    }

    private Location location;

    private double yOffset;

    private Status status;

    public SpawnLocation(final BaseConfiguration config) {
        this.config = config;
        config.load();
        this.location = config.getLocation("world").location();
        this.yOffset = config.getDouble("y-offset", -100.0D);
        this.status = config.getBoolean("active", true) ? Status.ON : Status.OFF;
    }

    public SpawnLocation(final BaseConfiguration config, final Location location) {
        this.config = config;
        this.config.load();
        this.location = location;
        this.yOffset = -100.0D;
        this.status = Status.ON;
        save();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        save();
    }

    public double getyOffset() {
        return yOffset;
    }

    public void setYOffset(double yOffset) {
        this.yOffset = yOffset;
        save();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        this.config.setProperty("active", status.value);
        this.config.save();
    }

    public void delete() {
        config.getFile().delete();
    }

    public enum Status {
        ON(true),
        OFF(false);

        private final boolean value;

        Status(boolean value) {
            this.value = value;
        }
    }
}
