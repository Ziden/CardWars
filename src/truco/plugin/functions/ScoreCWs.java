 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.functions;

import truco.plugin.functions.elo.Ligas;
import java.util.HashMap;
import java.util.UUID;
import me.libraryaddict.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.arena.Arena.Team;
import truco.plugin.managers.PermManager;
import truco.plugin.utils.IconLib;

/**
 *
 * @author usuario
 */
public class ScoreCWs {

    public static HashMap<UUID, HashMap<Integer, String>> ScoreCache = new HashMap<UUID, HashMap<Integer, String>>();

    public static HashMap<Integer, String> getScoreCache(Player p) {
        if (!ScoreCache.containsKey(p.getUniqueId())) {
            ScoreCache.put(p.getUniqueId(), new HashMap<Integer, String>());
        }
        return ScoreCache.get(p.getUniqueId());
    }

    public static String getScoreLine(Player p, int line) {
        return ScoreCWs.getScoreCache(p).get(line);
    }

    public static void setScoreLine(Player p, int line, String score) {
        if (ScoreCWs.getScoreLine(p, line) != null) {
            ScoreboardManager.hideScore(p, DisplaySlot.SIDEBAR, ScoreCWs.getScoreLine(p, line));
        }
        ScoreCWs.getScoreCache(p).put(line, score);
        ScoreboardManager.makeScore(p, DisplaySlot.SIDEBAR, score, line);
    }

    public static void globalSetScoreLine(int line, String score) {
        for (Player pOn : Bukkit.getOnlinePlayers()) {
            ScoreCWs.setScoreLine(pOn, line, score);
        }
    }

    public static void updateDisplayRoundTracker() {
        // ScoreboardManager.setDisplayName(DisplaySlot.SIDEBAR, ControleTimes.getTeamColor(Jogo.Time.AZUL) + "" + Jogo.getRoundTracker(Jogo.Time.AZUL) + "  " + IconLib.BOPE.getIcon() + ChatColor.GRAY + " / " + ControleTimes.getTeamColor(Jogo.Time.VERMELHO) + IconLib.FAVELA.getIcon() + "  " + Jogo.getRoundTracker(Jogo.Time.VERMELHO));
    }

    public static int getLifeOfPlayer(Player p) {

        return (int) p.getHealth();
    }

    public static void updateLifePlayers() {
        for (Player pOn : Bukkit.getOnlinePlayers()) {
            ScoreboardManager.makeScore(DisplaySlot.BELOW_NAME, pOn.getName(), (int) pOn.getHealth());
        }
    }

    public static void updateLifePlayer(Player p) {
        ScoreboardManager.makeScore(p, DisplaySlot.BELOW_NAME, p.getName(), (int) p.getHealth());

    }

    public static void updateCreditoPlayer(Player p) {
        //SBScore.setScoreLine(p, 9, "Créd.: " + ChatColor.GOLD + "$" + Jogo.getCredito(p));
    }

    public static void updateVivos() {
        // for (Player pOn : Bukkit.getOnlinePlayers()) {
        //      Player pOn = Bukkit.getPlayer(uuidp);
        //     SBScore.setScoreLine(pOn, 4, ChatColor.RED+ + IconLib.FAVELA.getIcon() + ChatColor.RESET + " Vivos: " + ControleTimes.getTeamColor(Jogo.Time.VERMELHO) + Jogo.getVivos(Jogo.Time.VERMELHO));
        //      SBScore.setScoreLine(pOn, 3, ControleTimes.getTeamColor(Jogo.Time.AZUL) + IconLib.BOPE.getIcon() + ChatColor.RESET + " Vivos: " + ControleTimes.getTeamColor(Jogo.Time.AZUL) + Jogo.getVivos(Jogo.Time.AZUL));
        //  }
    }

    public static void updatePing(Player pOn) {
        int ping = ((CraftPlayer) pOn).getHandle().ping;
        String pst;
        if (ping < 150) {
            pst = "§a||||";
        } else if (ping < 250) {
            pst = "§a|||§c|";
        } else if (ping < 400) {
            pst = "§a||§c||";
        } else {
            pst = "§a|§c|||";
        }
        ScoreCWs.setScoreLine(pOn, 14, "§4§lServer §d§lPing: §r" + pst);
    }

    public static void CriarObjetivos(Player pOn, int lvl, int gold, int exp, int elo, int gemas) {
        ScoreCWs.updateDisplayRoundTracker();

        updategold(gold, pOn);
        updateelo(elo, pOn);
        updateliga(elo, pOn);
        updateexp(exp, pOn);
        updatelvl(lvl, pOn);
        updateGems(gemas, pOn);
        ScoreCWs.setScoreLine(pOn, 1, "----------------");

        updatePing(pOn);
        ScoreCWs.setScoreLine(pOn, 15, "     ");

        ScoreCWs.setScoreLine(pOn, 13, "§f§l" + CardWarsPlugin.serverName.substring(CardWarsPlugin.serverName.lastIndexOf("_") + 1));
        ScoreCWs.setScoreLine(pOn, 12, "   ");
        ScoreCWs.setScoreLine(pOn, 7, "  ");
        ScoreCWs.setScoreLine(pOn, 4, "    ");

        ScoreCWs.updateLifePlayers();
    }

    public static void updateliga(int elo, Player of) {
        ScoreCWs.setScoreLine(of, 11, ChatColor.YELLOW + "§lLiga: §r" + Ligas.getLiga(elo));

    }

    public static void updateelo(int gold, Player p) {
        ScoreCWs.setScoreLine(p, 10, ChatColor.DARK_PURPLE + "§lElo: §f" + gold);

    }

    public static void updatelvl(int gold, Player p) {
        ScoreCWs.setScoreLine(p, 9, ChatColor.AQUA + "§lLevel: §f" + gold);

    }

    public static void updateexp(int gold, Player p) {
        ScoreCWs.setScoreLine(p, 8, ChatColor.DARK_GREEN + "§lExp: §f" + gold);

    }

    public static void updategold(int gold, Player p) {
        String s = ChatColor.GOLD + "§lMoedas";

        ScoreCWs.setScoreLine(p, 6, s);
        ScoreCWs.setScoreLine(p, 5, gold + "  ");

    }

    //
    public static void updateGems(int gems, Player p) {
        ScoreCWs.setScoreLine(p, 3, ChatColor.GREEN + "§lGemas");
        ScoreCWs.setScoreLine(p, 2, gems + "   ");
    }

    public static String getSuffix(int elo, Player p) {
        String bonus = "";
        ChatColor cor = Ligas.getCor(elo);
        if (PermissionsEx.getUser(p).inGroup("youtuber")) {
            bonus = (cor + "" + IconLib.YOUTUBER);
        } else if (p.hasPermission("cardwars.staff")) {
            bonus = (cor + "" + IconLib.STAFFICON);
        } else if (PermManager.BEAPRO.playerHas(p)) {
            bonus = (cor + "" + IconLib.BEAPRO);
        } else if (PermManager.CARDHERO.playerHas(p)) {
            bonus = (cor + "" + IconLib.CARDHERO);
        } else if (PermManager.CARDMASTER.playerHas(p)) {
            bonus = (cor + "" + IconLib.CARDMASTER);
        } else {
            bonus = (Ligas.getLiga(elo));
        }
        return bonus;
    }

    public static String getColorName(int elo, Player p) {
        if (PermissionsEx.getUser(p).inGroup("youtuber")) {
            return ChatColor.RED + "§l ";
        }
        if (PermManager.BEAPRO.playerHas(p)) {
            return ChatColor.AQUA + "";
        }
        if (PermManager.CARDHERO.playerHas(p)) {
            return ChatColor.BLUE + "";
        }
        if (PermManager.CARDMASTER.playerHas(p)) {
            return ChatColor.GREEN + "";
        }
        return ChatColor.WHITE + "";
    }

    public static boolean isNegrito(Player p) {

        return false;
    }

    public static void setIngameName(int elo, Player p, Team t) {

        ScoreboardManager.addToTeam(p, p.getName(), getSuffix(elo, p) + "§r " + t.getCor(), true);

    }

    public static void setLobbyName(int elo, Player p) {

        String prefix;

        String c = IconLib.STAFFICON.toString();
        String colored = null;

        PermissionUser puser = PermissionsEx.getUser(p);
        if (server == ServerType.TUTORIAL) {
            prefix = "§3§lTUTO";
            colored = "§f§l";
        } else if (puser.inGroup("diretor")) {
            prefix = ChatColor.DARK_AQUA + c;
        } else if (puser.inGroup("subdiretor")) {
            prefix = ChatColor.DARK_GRAY + c;
        } else if (puser.inGroup("coord")) {
            prefix = "§5" + c;
        } else if (puser.inGroup("adm")) {
            prefix = "§4" + c;
        } else if (puser.inGroup("mod")) {
            prefix = "§2" + c;
        } else if (puser.inGroup("mapper")) {
            prefix = "§d" + c;
        } else if (puser.inGroup("dir")) {
            prefix = "§8" + c;
        } else if (puser.inGroup("youtuber")) {
            prefix = "§c" + IconLib.YOUTUBER.toString();
        } else if (p.isOp()) {
            prefix = "§1" + c;

        } else {
            if (elo == -1) {
                return;
            }
            colored = getColorName(elo, p);
            prefix = getSuffix(elo, p);
        }

        ScoreboardManager.addToTeam(p, p.getName(), prefix + "§r " + (colored != null ? colored : "§l"), true);

    }

}
