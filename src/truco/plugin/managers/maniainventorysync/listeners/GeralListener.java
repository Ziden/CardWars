/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.managers.maniainventorysync.listeners;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import truco.plugin.managers.maniainventorysync.threads.ThreadJoinEvent;
import truco.plugin.managers.maniainventorysync.threads.ThreadQuitEvent;

/**
 *
 * @author Gabriel
 */
public class GeralListener implements Listener {

    public GeralListener() {
    }

    public static List<UUID> esperandoLoad = Collections.synchronizedList(new LinkedList<UUID>());

    @EventHandler(priority = EventPriority.NORMAL)
    public void PlayerJoinEvent(PlayerJoinEvent ev) {
        esperandoLoad.add(ev.getPlayer().getUniqueId());
        ev.getPlayer().getInventory().clear();
        new ThreadJoinEvent(ev.getPlayer()).start();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerQuitEvent(PlayerQuitEvent ev) {
        if (!esperandoLoad.contains(ev.getPlayer().getUniqueId())) {
            new ThreadQuitEvent(ev.getPlayer()).run();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerKickEvent(PlayerKickEvent ev) {
        if (!esperandoLoad.contains(ev.getPlayer().getUniqueId())) {
            new ThreadQuitEvent(ev.getPlayer()).run();
        }
    }

    private HashMap<UUID, Long> DelayCloseInv = new HashMap();

    @EventHandler(priority = EventPriority.MONITOR)
    public void CloseInventory(InventoryCloseEvent ev) {
        if (ev.getPlayer() instanceof Player) {
            if (DelayCloseInv.containsKey(ev.getPlayer().getUniqueId())) {
                if (System.currentTimeMillis() < DelayCloseInv.get(ev.getPlayer().getUniqueId())) {
                    return;
                }
            }
            DelayCloseInv.put(ev.getPlayer().getUniqueId(), System.currentTimeMillis() + 10000);
            // new ThreadQuitEvent((Player) ev.getPlayer()).start();
        }
    }
}
