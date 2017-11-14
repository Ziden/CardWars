/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import truco.plugin.CardWarsPlugin;
import truco.plugin.managers.PermManager;
import truco.plugin.utils.ChatUtils;

/**
 *
 * @author Carlos
 */
public class SomeNegada {

    public static List<UUID> naoqueremosoutros = new ArrayList();

    public static void Join(PlayerJoinEvent ev) {
        if (naoqueremosoutros.contains(ev.getPlayer().getUniqueId())) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!PermManager.VISIVEL.playerHas(p)) {
                    if (p == ev.getPlayer()) {
                        continue;
                    }
                    ev.getPlayer().hidePlayer(p);
                }
            }
        }
        if (!PermManager.VISIVEL.playerHas(ev.getPlayer())) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p == ev.getPlayer()) {
                    continue;
                }
                if (naoqueremosoutros.contains(p.getUniqueId())) {
                    p.hidePlayer(ev.getPlayer());
                }
            }
        }

    }

    public static void usaCmd(Player p) {
        if (naoqueremosoutros.contains(p.getUniqueId())) {
            ChatUtils.sendMessage(p, "§aAgora você está vendo os outros jogadores no lobby");
            for (Player pf : Bukkit.getOnlinePlayers()) {
                if (pf == p) {
                    continue;
                }
                if (!p.canSee(pf)) {
                    p.showPlayer(pf);
                }

            }
            naoqueremosoutros.remove(p.getUniqueId());
        } else {
            ChatUtils.sendMessage(p, "§cAgora você não está vendo os outros jogadores no lobby");

            naoqueremosoutros.add(p.getUniqueId());
            for (Player pf : Bukkit.getOnlinePlayers()) {
                if (pf == p) {
                    continue;
                }
                if (!PermManager.VISIVEL.playerHas(pf)) {
                    p.hidePlayer(pf);
                }
            }
        }

    }
}
