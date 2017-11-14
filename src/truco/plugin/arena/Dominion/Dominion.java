/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.arena.Dominion;

import truco.plugin.functions.ScoreCWs;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.libraryaddict.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import truco.plugin.CardWarsPlugin;
import truco.plugin.arena.Arena;
import truco.plugin.utils.*;

/**
 *
 * @author Carlos
 */
public class Dominion extends Arena {

    public static String[] regioes = {"barraca", "caverna", "relogio", "ruina", "zeppelin"};
    public ArrayList<Base> bases = new ArrayList<Base>();
    int RedPoints = 0;
    int bluePoints = 0;
    int task = -1;
    int secs = 0;

    public ArrayList<Base> getBases() {
        return bases;
    }

    public Dominion(String name, Location redspawn, Location bluespawn) {
        super(name, redspawn, bluespawn);
        for (String s : regioes) {
            World mundo = redspawn.getWorld();
            Map<String, ProtectedRegion> pr = CardWarsPlugin.worldGuard.getRegionManager(mundo).getRegions();
            if (pr != null) {

                if (pr.get(s) != null) {
                    PlayerDominando.trocaCor(null, pr.get(s), mundo);
                    bases.add(new Base(pr.get(s), this));
                }
                Utils.debug("ACHEI A BASE " + s);
            }
        }
        Bukkit.getPluginManager().registerEvents(new DominionListener(this), CardWarsPlugin._instance);
    }

    public Dominion(String name) {
        super(name);
        Bukkit.getPluginManager().registerEvents(new DominionListener(this), CardWarsPlugin._instance);
    }

    public void addPoints(Team t, int points) {
        if (t == Team.BLUE) {
            bluePoints += points;
        } else if (t == Team.RED) {
            RedPoints += points;
        }
        int size = getBases().size();

        for (Player p : Bukkit.getOnlinePlayers()) {
            ScoreCWs.setScoreLine(p, size + 3, "§c§lPontos: " + ChatColor.YELLOW + "" + getPoints(Arena.Team.RED));
            ScoreCWs.setScoreLine(p, size + 2, "§9§lPontos: " + ChatColor.YELLOW + "" + getPoints(Arena.Team.BLUE));

        }
        if (getPoints(t) >= 40) {

            win(t);

            if (task != -1) {
                Bukkit.getScheduler().cancelTask(task);
            }
        }
    }

    public void timer() {
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {

                secs++;

                for (Player p : Bukkit.getOnlinePlayers()) {
                    ScoreboardManager.setDisplayName(p, DisplaySlot.SIDEBAR, "§5§lDominio §a- §f" + Utils.getTimeToString(secs));
                }

                if (secs % 30 == 0) {
                    int rp = 0;
                    int bp = 0;
                    for (Base b : bases) {

                        if (b.getDona() != null) {

                            if (b.getDona() == Team.BLUE) {
                                bp++;
                            } else {
                                rp++;
                            }
                        }
                    }
                    if (CardWarsPlugin.random.nextBoolean()) {
                        addPoints(Team.RED, rp);
                        addPoints(Team.BLUE, bp);
                    } else {
                        addPoints(Team.BLUE, bp);
                        addPoints(Team.RED, rp);

                    }

                    ChatUtils.broadcastMessage("§eA equipe §9azul §eganhou §a§n" + bp + "§e pontos e a equipe §cvermelha §eganhou §a§n" + rp + "§e pontos!");
                }
            }
        }, 20, 20);
    }

    public int getPoints(Team t) {
        if (t == Team.RED) {
            return RedPoints;
        }
        if (t == Team.BLUE) {
            return bluePoints;
        }
        return -1;
    }

    @Override
    public String getDbName() {
        return "dominio";
    }

    @Override
    public void startm() {
        timer();
    }

    @Override
    public void startPlayer(Player p) {

        Utils.setHeaderAndFooter(p, "§c§lCard Wars §7- §5Servidor de Dominion", "§ewww.instamc.com.br");
    }

    @Override
    public String getDesc() {
        return "Domine as bases e ganhe pontos com 40 pontos você ganha a partida!!";
    }

    @Override
    public void createScore(Player pOn) {
        Dominion tm = this;
        int size = tm.getBases().size();
        ScoreboardManager.setDisplayName(pOn, DisplaySlot.SIDEBAR, "§5§lDominio §a- §f" + Utils.getTimeToString(getTime()));
        ScoreCWs.setScoreLine(pOn, size + 3, "§c§lPontos: " + ChatColor.YELLOW + "" + tm.getPoints(Arena.Team.RED));
        ScoreCWs.setScoreLine(pOn, size + 2, "§9§lPontos: " + ChatColor.YELLOW + "" + tm.getPoints(Arena.Team.BLUE));
        for (int x = 0; x < size; x++) {
            Base b = tm.getBases().get(x);
            String s = "§f";
            if (b.getDona() == Team.RED) {
                s = "§c";
            }
            if (b.getDona() == Team.BLUE) {
                s = "§9";
            }
            String nome = b.regiao.getId().toUpperCase();
            while (nome.length() < 8) {
                nome += " ";
            }
            ScoreCWs.setScoreLine(pOn, x + 1, "§b" + nome + "§7:" + s + IconLib.DOMINIONBASE);
        }
        ScoreCWs.updateLifePlayers();
        ScoreCWs.updateVivos();
    }

    @Override
    public void SecPerSec() {
    }

    @Override
    public Team getWinTeam() {
        int rp = getPoints(Arena.Team.RED);
        int bp = getPoints(Arena.Team.BLUE);
        if (rp > bp) {
            return Team.RED;
        }
        if (bp > rp) {
            return Team.BLUE;
        }
        if (CardWarsPlugin.random.nextBoolean()) {
            return Team.RED;
        } else {
            return Team.BLUE;
        }
    }
}
