package me.luucka.voidteleport.listeners;

import lombok.RequiredArgsConstructor;
import me.luucka.voidteleport.VoidTeleport;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final VoidTeleport plugin;

    private final Set<UUID> fallingPlayers = new HashSet<>();

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final String worldName = player.getWorld().getName();
        final Location toLocation = event.getTo();

        if (event.getFrom().toVector().equals(toLocation.toVector())) return;

        try {
            plugin.getVoidSpawnManager().teleport(worldName, player, toLocation);
            fallingPlayers.add(player.getUniqueId());
        } catch (Exception ignored) {
        }
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
