package me.luucka.voidteleport;

import lombok.Getter;
import me.luucka.voidteleport.commands.BaseCommand;
import me.luucka.voidteleport.commands.VoidTeleportCommand;
import me.luucka.voidteleport.config.IConfig;
import me.luucka.voidteleport.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class VoidTeleportPlugin extends JavaPlugin {

    private static final Logger LOGGER = Logger.getLogger("VoidTeleport");

    private final List<IConfig> configList = new ArrayList<>();

    @Getter
    private SpawnLocationManager spawnLocationManager;

    @Getter
    private Messages messages;

    @Override
    public void onEnable() {
        if (LOGGER != this.getLogger()) LOGGER.setParent(this.getLogger());

        this.spawnLocationManager = new SpawnLocationManager(this);
        this.configList.add(this.spawnLocationManager);

        this.messages = new Messages(this);
        this.configList.add(this.messages);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        new VoidTeleportCommand(this);
        BaseCommand.registerHelpMap("VoidTeleport", "VoidTP", "voidteleport.admin", "VoidTeleport Help page");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reload() {
        for (final IConfig iConfig : configList) {
            iConfig.reloadConfig();
        }
    }
}
