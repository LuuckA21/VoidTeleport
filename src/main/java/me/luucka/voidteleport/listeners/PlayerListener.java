package me.luucka.voidteleport.listeners;

import me.luucka.voidteleport.SpawnLocationManager;
import me.luucka.voidteleport.VoidTeleportPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerListener implements Listener {

    private final VoidTeleportPlugin plugin;
    private final SpawnLocationManager spawnLocationManager;

    private final Set<UUID> fallingPlayers = new HashSet<>();

    public PlayerListener(VoidTeleportPlugin plugin) {
        this.plugin = plugin;
        this.spawnLocationManager = plugin.getSpawnLocationManager();
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (event.getFrom().toVector().equals(event.getTo().toVector())) return;

        spawnLocationManager.getSpawnLocationOnByWorld(player.getWorld())
                .ifPresent(location -> {
                            if (location.canTeleport(event.getTo().getY())) {
                                (new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        location.teleport(player);
                                    }
                                }).runTaskLater(plugin, 0L);
                                fallingPlayers.add(player.getUniqueId());
                            }
                        }
                );
    }

    @EventHandler
    public void onPlayerFall(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (!fallingPlayers.contains(player.getUniqueId())) return;
        event.setCancelled(true);
        fallingPlayers.remove(player.getUniqueId());
    }

}
