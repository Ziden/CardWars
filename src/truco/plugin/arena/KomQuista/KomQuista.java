/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.arena.KomQuista;

import java.util.UUID;
import me.libraryaddict.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scoreboard.DisplaySlot;
import truco.plugin.CardWarsPlugin;
import truco.plugin.arena.Arena;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.ScoreCWs;
import truco.plugin.utils.Utils;

/**
 *
 * @author usuario
 *
 */
public class KomQuista extends Arena {

    public KomQuista(String name, Location redspawn, Location bluespawn) {
        super(name, redspawn, bluespawn);
        Bukkit.getPluginManager().registerEvents(new KomQuistaListener(this), CardWarsPlugin._instance);
    }

    public KomQuista(String name) {
        super(name);
        Bukkit.getPluginManager().registerEvents(new KomQuistaListener(this), CardWarsPlugin._instance);
    }

    @Override
    public String getDesc() {
        return "Conquiste o castelo ou defenda ele de invasores !";
    }

    @Override
    public void startm() {

    }

    // qnd algm dominar
    public void domina(Team t) {
        Bukkit.broadcastMessage("§e§lO time §l" + t.getCor() + "§e dominou o castelo !");
        win(t);
    }

    @Override
    public String getDbName() {
        return "komquista";
    }

    @Override
    public void startPlayer(Player p) {

        Utils.setHeaderAndFooter(p, "§c§lCard Wars §7- §2Servidor de Conquista", "§ewww.instamc.com.br");
    }

    @Override
    public void createScore(Player pOn) {

        ScoreboardManager.setDisplayName(pOn, DisplaySlot.SIDEBAR, "§2§lConquista §7- §f" + Utils.getTimeToString(getTime()));
        ScoreCWs.setScoreLine(pOn, 2, "§c§lVida: " + ChatColor.YELLOW + "" + KomQuistaListener.vidared);
        ScoreCWs.setScoreLine(pOn, 1, "§9§lVida: " + ChatColor.YELLOW + "" + KomQuistaListener.vidablue);
        ScoreCWs.updateLifePlayers();
        ScoreCWs.updateVivos();

    }

    @Override
    public void SecPerSec() {
        for (Player pOn : Bukkit.getOnlinePlayers()) {
            ScoreboardManager.setDisplayName(pOn, DisplaySlot.SIDEBAR, "§2§lConquista §7- §f" + Utils.getTimeToString(getTime()));
        }
    }

    @Override
    public Team getWinTeam() {
        if (KomQuistaListener.vidablue > KomQuistaListener.vidared) {
            return Team.BLUE;
        }
        if (KomQuistaListener.vidared > KomQuistaListener.vidablue) {
            return Team.RED;
        }
        if (CardWarsPlugin.random.nextBoolean()) {
            return Team.RED;
        } else {
            return Team.BLUE;
        }
    }

}
