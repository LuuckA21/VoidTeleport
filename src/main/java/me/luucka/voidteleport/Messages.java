package me.luucka.voidteleport;

import me.luucka.voidteleport.config.BaseConfiguration;
import me.luucka.voidteleport.config.IConfig;

import java.io.File;

public class Messages implements IConfig {

    private final BaseConfiguration config;

    private String prefix;

    private String reload;

    private String worldNotSet;

    private String spawnSet;

    private String spawnUpdate;

    private String worldTpRemoved;

    private String yOffsetSet;

    private String tpActive;

    private String tpInactive;

    public Messages(VoidTeleportPlugin plugin) {
        this.config = new BaseConfiguration(new File(plugin.getDataFolder(), "messages.yml"), "/messages.yml");
        reloadConfig();
    }

    public String reload() {
        return reload.replace("{PREFIX}", prefix);
    }

    public String worldNotSet() {
        return worldNotSet.replace("{PREFIX}", prefix);
    }

    public String spawnSet() {
        return spawnSet.replace("{PREFIX}", prefix);
    }

    public String spawnUpdate() {
        return spawnUpdate.replace("{PREFIX}", prefix);
    }

    public String worldTpRemoved() {
        return worldTpRemoved.replace("{PREFIX}", prefix);
    }

    public String yOffsetSet() {
        return yOffsetSet.replace("{PREFIX}", prefix);
    }

    public String tpActive() {
        return tpActive.replace("{PREFIX}", prefix);
    }

    public String tpInactive() {
        return tpInactive.replace("{PREFIX}", prefix);
    }

    @Override
    public void reloadConfig() {
        config.load();
        prefix = config.getString("prefix", "");
        reload = config.getString("reload", "");
        worldNotSet = config.getString("world-not-set", "");
        spawnSet = config.getString("spawn-set", "");
        spawnUpdate = config.getString("spawn-update", "");
        worldTpRemoved = config.getString("world-tp-removed", "");
        yOffsetSet = config.getString("yoffset-set", "");
        tpActive = config.getString("tp-active", "");
        tpInactive = config.getString("tp-inactive", "");
    }
}
