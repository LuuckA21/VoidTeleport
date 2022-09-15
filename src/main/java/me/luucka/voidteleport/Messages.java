package me.luucka.voidteleport;

import me.luucka.voidteleport.config.BaseConfiguration;
import me.luucka.voidteleport.config.IConfig;

import java.io.File;

public class Messages implements IConfig {

    private final VoidTeleport plugin;

    private final BaseConfiguration config;

    private String prefix;

    private String noPermission;

    private String noConsole;

    private String reload;

    private String commandUsage;

    private String worldNotSet;

    private String spawnSet;

    private String worldTpRemoved;

    private String yOffsetSet;

    public Messages(VoidTeleport plugin) {
        this.plugin = plugin;
        this.config = new BaseConfiguration(new File(plugin.getDataFolder(), "messages.yml"), "/messages.yml");
        reloadConfig();
    }

    public String noPermission() {
        return noPermission.replace("{PREFIX}", prefix);
    }

    public String noConsole() {
        return noConsole.replace("{PREFIX}", prefix);
    }

    public String reload() {
        return reload.replace("{PREFIX}", prefix);
    }

    public String commandUsage(final String usage) {
        return commandUsage.replace("{PREFIX}", prefix).replace("{COMMAND_USAGE}", usage);
    }

    public String worldNotSet() {
        return worldNotSet.replace("{PREFIX}", prefix);
    }

    public String spawnSet() {
        return spawnSet.replace("{PREFIX}", prefix);
    }

    public String worldTpRemoved() {
        return worldTpRemoved.replace("{PREFIX}", prefix);
    }

    public String yOffsetSet() {
        return yOffsetSet.replace("{PREFIX}", prefix);
    }

    @Override
    public void reloadConfig() {
        config.load();
        prefix = config.getString("prefix", "");
        noPermission = config.getString("no-permission", "");
        noConsole = config.getString("no-console", "");
        reload = config.getString("reload", "");
        commandUsage = config.getString("command-usage", "");
        worldNotSet = config.getString("world-not-set", "");
        spawnSet = config.getString("spawn-set", "");
        worldTpRemoved = config.getString("world-tp-removed", "");
        yOffsetSet = config.getString("yoffset-set", "");
    }
}
