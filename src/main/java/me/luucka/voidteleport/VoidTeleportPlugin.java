package me.luucka.voidteleport;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import lombok.Getter;
import me.luucka.extendlibrary.message.Message;
import me.luucka.voidteleport.command.VoidTeleportCommand;
import me.luucka.voidteleport.config.IConfig;
import me.luucka.voidteleport.listener.PlayerListener;
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

    @Getter
    private Message messaggiNuovi;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
                .shouldHookPaperReload(true)
        );
    }

    @Override
    public void onEnable() {
        if (LOGGER != this.getLogger()) LOGGER.setParent(this.getLogger());

        CommandAPI.onEnable();

        this.spawnLocationManager = new SpawnLocationManager(this);
        this.configList.add(this.spawnLocationManager);

        this.messages = new Messages(this);
        this.configList.add(this.messages);

        this.messaggiNuovi = new Message("messages");
        this.messaggiNuovi.addPrefix();

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        new VoidTeleportCommand(this);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }

    public void reload() {
        for (final IConfig iConfig : configList) {
            iConfig.reloadConfig();
        }
    }
}
