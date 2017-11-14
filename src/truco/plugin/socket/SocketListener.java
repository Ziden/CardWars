/*

 */
package truco.plugin.socket;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import truco.plugin.events.SocketReceivedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.matchmaking.Threads.PlayerManagerThread;
import truco.plugin.utils.ChatUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class SocketListener implements Listener {
//BOM SEPARADOR 疅 POIS É COMPLICADO ALGUM PLAYER ACHAR ELE 

    @EventHandler
    public void receiveSocket(SocketReceivedEvent ev) {
        
        if (ev.getCanal().equalsIgnoreCase("broadcast")) {
            Bukkit.broadcastMessage(ev.getMessage());
        } else if (ev.getCanal().equalsIgnoreCase("playermanagerthread")) {
            PlayerManagerThread.roda();
        } else if (ev.getCanal().equalsIgnoreCase("mensagemuuid")) {
            UUID uid = UUID.fromString(ev.getMessage().split("¨¨")[0]);
            
            String msg = ev.getMessage().split("¨¨")[1];
            Player p = Bukkit.getPlayer(uid);
            if (p != null) {
                p.sendMessage(msg);
            }
        } else if (ev.getCanal().equalsIgnoreCase("chatbb")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("cardwars.chatbigboss")) {
                    p.sendMessage(ev.getMessage());
                }
            }
        } else if (ev.getCanal().equalsIgnoreCase("lobbyinfo")) {
            String[] split = ev.getMessage().split("¬");
            String svname = split[0];
            int players = Integer.valueOf(split[1]);
            int maxplayers = Integer.valueOf(split[2]);
            CardWarsPlugin.lobbymanager.recebiInfo(svname, maxplayers, players);
        } else if (ev.getCanal().equalsIgnoreCase("dc")) {
            
            CardWarsPlugin.lobbymanager.tomadc(ev.getMessage());
            
        } else if (ev.getCanal().equalsIgnoreCase("ganhacarta")) {
            String player = ev.getMessage().split(";")[0];
            String carta = ev.getMessage().split(";")[1];
            Carta c = ControleCartas.getCardByName(carta);
            if (c != null) {
                ChatUtils.ganhou(player, c.toItemStack());
            }
        }
    }
}
