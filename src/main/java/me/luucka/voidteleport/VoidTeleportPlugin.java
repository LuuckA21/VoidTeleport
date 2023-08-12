package me.luucka.voidteleport;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.luucka.extendlibrary.message.Message;
import me.luucka.extendlibrary.util.IReload;
import me.luucka.voidteleport.command.VoidTeleportCommand;
import me.luucka.voidteleport.listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class VoidTeleportPlugin extends JavaPlugin {

    private static final Logger LOGGER = Logger.getLogger("VoidTeleport");

    private final List<IReload> reloadList = new ArrayList<>();

    private SpawnLocationManager spawnLocationManager;

    private Message messages;

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
        this.reloadList.add(this.spawnLocationManager);

        this.messages = new Message(this, "messages");
        this.messages.addPrefix();
        this.reloadList.add(this.messages);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        new VoidTeleportCommand(this);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }

    public void reload() {
        for (final IReload iReload : reloadList) {
            iReload.reload();
        }
    }

    public SpawnLocationManager getSpawnLocationManager() {
        return spawnLocationManager;
    }

    public Message getMessages() {
        return messages;
    }
}
