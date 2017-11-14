/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.functions.groups;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import static truco.plugin.functions.groups.ControleGrupos.convites;
import truco.plugin.matchmaking.Threads.MatchMaker;

/**
 *
 * @author Carlos
 */
public class EventosGrupos {

    public static void addToQueue(Grupo g) {
        MatchMaker.db.addGroupQueue(g.getPlayers());
        g.removeGroup();
    }

    public static void Quit(Player p) {
        for (Map.Entry<UUID, UUID> ent : new HashMap<UUID,UUID>(convites).entrySet()) {
            if (p != null) {
                if (ent.getKey().equals(p.getUniqueId())) {
                    p.sendMessage("§5§l[GRUPO]§r §b§lO usuario que você estáva chamando saiu!!");
                    convites.remove(ent.getKey());
                    break;
                }
                if (ent.getValue().equals(p.getUniqueId())) {
                    Player ch = Bukkit.getPlayer(ent.getKey());
                    if (ch != null) {
                        p.sendMessage("§5§l[GRUPO]§r Quem você estava convidadando saiu do jogo!");
                    }
                    convites.remove(ent.getKey());
                }
            }
        }
        Grupo grupo = ControleGrupos.getGrupo(p);
        if (grupo != null) {
            if (grupo.getLider() == p.getUniqueId() || (grupo.size() - 1) <= 1) {
                grupo.disband();
            } else {
                grupo.removeMember(p);
            }
        }
    }

}
