/*

 */
package truco.plugin.utils;

import br.pj.newlibrarysystem.cashgame.GemManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import me.libraryaddict.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import truco.plugin.CardWarsPlugin;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.data.mysql.MatchHistoryDB;
import truco.plugin.functions.ScoreCWs;
import truco.plugin.managers.PlayerInfo;
import truco.plugin.matchmaking.Threads.MatchMaker;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PlayerUtils {

    public static void someNegadaTuto(Player pv) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != pv) {
                if (!p.isOp()) {
                    p.hidePlayer(pv);
                }
                if (!pv.isOp()) {
                    pv.hidePlayer(p);
                }
            }
        }
    }

    public static void startaCoisasVisuais(final Player p) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                final UUID uuid = p.getUniqueId();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        final PlayerInfo pinf = MatchMaker.db.getPlayerInfo(p.getUniqueId());

                        final int gems = GemManager.GetGems(uuid);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                            @Override
                            public void run() {
                                if (p != null) {
                                    ScoreboardManager.registerScoreboard(p);
                                    ScoreboardManager.setDisplayName(p, DisplaySlot.SIDEBAR, "§f▂▃▄§4§lCardWars§r§f▄▃▂");

                                    ScoreCWs.setLobbyName(pinf.getElo(), p);
                                    ScoreCWs.CriarObjetivos(p, pinf.getLvl(), pinf.getGold(), pinf.getExp(), pinf.getElo(), gems);
                                    Utils.setHeaderAndFooter(p, "§e" + IconLib.STAFFICON + " §fBem vindo ao §4§lCard Wars§e " + IconLib.STAFFICON, "§ewww.instamc.com.br");

                                }
                            }
                        });
                    }
                };
                new Thread(r).start();

            }

        }
        ).start();

    }

    public static void startaConta(final Player p) {
        Runnable run = new Runnable() {

            @Override
            public void run() {

                int resposta = MatchMaker.db.initPlayer(p.getUniqueId(), p.getName());
                if (resposta == 0) {
                    CardsDB.startPlayer(p);

                    List<String> cartas = Arrays.asList("Aprendiz epico", "Arco Tosco", "Bainha de Couro", "Endurecer", "Espada de Treino", "Garra De Bhefos", "Machado de Batalha", "Maos de Fogo");
                    for (String s : cartas) {
                        Carta c = ControleCartas.getCardByName(s);
                        if (c != null) {

                            CardsDB.addCard(p.getUniqueId(), c.toItemStack());
                        }

                    }

                    MatchHistoryDB.startPlayer(p.getUniqueId());

                } else if (resposta == 2) {
                    startaCoisasVisuais(p);
                }

                if (resposta == 0 || resposta == 1) {
                    p.sendMessage("§aSendo teleportado para o tutorial!");
                    Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                        @Override
                        public void run() {

                            if (p != null) {
                                Utils.TeleportarTPBG("cws_Tutorial", p);
                                Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                                    @Override
                                    public void run() {
                                        if (p != null) {
                                            p.kickPlayer("§bTutoriais lotados aguarde!");
                                        }
                                    }
                                }, 20 * 10);
                            }
                        }
                    }, 20 * 5);

                }
            }
        };
        new Thread(run).start();

    }

    public static void sendWelcomeMessage(final Player p) {
        PermissionUser u = PermissionsEx.getPermissionManager().getUser(p);
        String prefixo = u.getPrefix();
        prefixo = ChatColor.translateAlternateColorCodes('&', prefixo);
        if (p.hasPermission("cardwars.vip")) {
            ChatUtils.broadcastMessage(prefixo + "§f§l" + p.getName() + " §6entrou no servidor!");

        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                if (p != null) {
                    Utils.sendTitle(p, "§fBem vindo ao §4§lCard Wars", "§fTenha um bom jogo!", 10, 80, 10);
                    p.sendMessage("§lAcesse o site do servidor: §chttp://instamc.com.br/cardwars");
                }
            }
        }, 20 * 2);
    }

}
