/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.matchmaking.Threads;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import truco.plugin.CardWarsPlugin;
import truco.plugin.matchmaking.DBHandler.PlayerStatus;
import truco.plugin.matchmaking.PlayerIngame;
import truco.plugin.socket.SocketManager;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.Utils;

/**
 *
 * @author usuario
 *
 */
public class PlayerManagerThread {

    public static boolean roda = false;

    public PlayerManagerThread() {

        if (CardWarsPlugin.matchmaking) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    mandaRodar();
                }
            }, 20 * 10, 20 * 10);
        }
    
    }

    public static void mandaRodar() {
        SocketManager.sendMessage(SocketManager.LOBBY_VARIABLE, "playermanagerthread", "roda");
        roda();
    }

    public static void roda() {
       new mandaJogoThread().start();
    }
    public static ArrayList<UUID> avisados = new ArrayList();

    public static class mandaJogoThread extends Thread {

        @EventHandler
        public void run() {

            try {
                Set<Player> players = new HashSet<Player>();

                int playersNaFila = MatchMaker.db.getPlayersNaFila();
                int timesProntos = MatchMaker.db.getTimesProntos();
                Player[] onli = new Player[Bukkit.getOnlinePlayers().size()];
                Bukkit.getOnlinePlayers().toArray(onli);
                players.addAll(Arrays.asList(onli));
                for (final Player p : players) {
                    if (p != null) {
                        PlayerIngame ingame = MatchMaker.db.getIngamePlayer(p);
                    
                        if (ingame != null) { //
                            //////////
                            // TODO //
                            //////////
                            final String serverName = ingame.serverName;
                            int team = ingame.team;
                            String name = team == 0 ? "§cVermelha" : "§9Azul";
                            if (avisados.contains(p.getUniqueId())) {
                                p.closeInventory();
                                p.sendMessage("§aVocê não poderá jogar outra partida até está acabar!");
                                Utils.TeleportarTPBG(serverName, p);
                                avisados.remove(p.getUniqueId());
                            } else {
                                p.closeInventory();
                                avisados.add(p.getUniqueId());
                                ChatUtils.sendMessage(p, "§bEm 10 segundos você será teleportado para o jogo. Você está na equipe " + name + "§b!");
                            }
                            // he is already in queue but not ready yet
                        } else if (MatchMaker.db.isPlayerInQueue(p.getUniqueId()) != PlayerStatus.NONE) {
                            if (p != null) {
                                p.sendMessage(ChatColor.GREEN + "Aguardando formador de equipes...");
                                p.sendMessage(ChatColor.GREEN + "Temos " + playersNaFila + " players na fila e " + timesProntos + " times prontos !");
                                Utils.sendTitle(p, "§aAguarde", "§7Você está na fila para jogar", 5, 15, 5);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }
}
