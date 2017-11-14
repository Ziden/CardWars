package truco.plugin.arena.DeathMatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.libraryaddict.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import truco.plugin.cards.ControleCartas;
import truco.plugin.arena.Arena;
import truco.plugin.CardWarsPlugin;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.cards.stats.MaisVidaTdm;
import truco.plugin.cards.stats.Stats;
import truco.plugin.functions.ScoreCWs;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class TeamDeathMatch extends Arena {

    public HashMap<UUID, Integer> kills = new HashMap();
    public HashMap<UUID, Integer> vidas = new HashMap();
    public HashMap<UUID, Integer> mortes = new HashMap();
    public int vivosred = 0;
    public int vivosblue = 0;

    public int getKills(UUID uuid) {
        if (kills.containsKey(uuid)) {
            return kills.get(uuid);
        }
        return 0;
    }

    public List<Player> getLivesPlayers() {
        ArrayList<Player> vivos = new ArrayList<Player>();
        for (UUID p : getPlayers()) {
            if (vidas.containsKey(p) && getTeam(p) != null) {
                if (!specs.contains(p)) {
                    Player pl = Bukkit.getPlayer(p);
                    if (pl != null) {
                        vivos.add(pl);
                    }
                }
            }
        }
        return vivos;
    }

    public boolean canWin(Team t) {
        boolean ganhei = true;
        for (Player vivo : getLivesPlayers()) {
            if (getTeam(vivo.getUniqueId()) == Team.getTeamOposta(t)) {
                if (vivo.isOnline()) {
                    ganhei = false;
                }
            }
        }
        return ganhei;
    }

    @Override
    public void startPlayer(Player p) {
        Utils.setHeaderAndFooter(p, "§c§lCard Wars §7- §4Servidor TDM", "§ewww.instamc.com.br");

        if (!kills.containsKey(p.getUniqueId())) {
            kills.put(p.getUniqueId(), 0);
        }
        if (!vidas.containsKey(p.getUniqueId())) {
            if (getTeam(p.getUniqueId()) == Team.RED) {
                vivosred++;

            } else if (getTeam(p.getUniqueId()) == Team.BLUE) {
                vivosblue++;
            }
            int bonus = 0;
            for (Stats s : ControleCartas.getStats(p)) {
                if (s instanceof MaisVidaTdm) {
                    bonus += s.getX();
                }
            }
            vidas.put(p.getUniqueId(), 4 + bonus);
        }
        if (!mortes.containsKey(p.getUniqueId())) {

            mortes.put(p.getUniqueId(), 0);
        }

    }

    public void addKill(Player p) {
        if (p == null) {
            return;
        }
        MatchMaker.db.addGoldWithThread(p.getUniqueId(), 1, true,true);
        int tem = 0;
        if (kills.containsKey(p.getUniqueId())) {
            tem = kills.get(p.getUniqueId());
            kills.remove(p.getUniqueId());
        }

        kills.put(p.getUniqueId(), ++tem);
        //  SBScore.setScoreLine(pOn, 4, ChatColor.GOLD + "Vidas: " + ChatColor.YELLOW + "" + this.getVidas(pOn));
        ScoreCWs.setScoreLine(p, 3, ChatColor.GOLD + "Kills: " + ChatColor.YELLOW + "" + this.getKills(p.getUniqueId()));

    }

    public int getVidas(Player p) {
        if (vidas.containsKey(p.getUniqueId())) {
            return vidas.get(p.getUniqueId());

        }
        return -1;
    }

    public int getVidas(UUID p) {
        if (vidas.containsKey(p)) {
            return vidas.get(p);

        }
        return -1;
    }

    public void removedoJogo(Player p) {
        if (!specs.contains(p.getUniqueId())) {
            addSpec(p);
            vidas.remove(p.getUniqueId());
            vidas.put(p.getUniqueId(), 0);
            if (getTeam(p.getUniqueId()) == Team.RED) {
                vivosred--;
            } else if (getTeam(p.getUniqueId()) == Team.BLUE) {
                vivosblue--;
            }
            boolean ganhei = true;
            for (Player vivo : getLivesPlayers()) {

                if (getTeam(vivo.getUniqueId()) == getTeam(p.getUniqueId())) {
                    if (vivo.isOnline()) {
                        ganhei = false;
                    }
                }

            }
            if (ganhei) {
                win(Team.getTeamOposta(getTeam(p.getUniqueId())));
            }

        }
    }

   
    public void addMorte(Player p) {

        int tem = vidas.get(p.getUniqueId());

        int mor = mortes.get(p.getUniqueId());

        mortes.remove(p.getUniqueId());
        mortes.put(p.getUniqueId(), ++mor);

        vidas.remove(p.getUniqueId());
        tem = tem - 1;
        vidas.put(p.getUniqueId(), tem);
        if (vidas.get(p.getUniqueId()) <= 0) {
            removedoJogo(p);
        } else {
            ControleCartas.updateInventoryCards(p, false);
        }
        ScoreCWs.setScoreLine(p, 4, ChatColor.GOLD + "Vidas: " + ChatColor.YELLOW + "" + this.getVidas(p));
        //SBScore.setScoreLine(pOn, 3, ChatColor.GOLD + "Kills: " + ChatColor.YELLOW + "" + this.getKills(pOn.getUniqueId()));

    }

    public int getKillsTeam(Team t) {
        int x = 0;
        for (UUID p : getPlayers(t)) {
            x += kills.get(p);
        }
        return x;
    }

    public int getDeathsTeam(Team t) {
        int x = 0;
        for (UUID p : getPlayers(t)) {
            x += mortes.get(p);
        }
        return x;
    }

    public TeamDeathMatch(String name, Location redspawn, Location bluespawn) {
        super(name, redspawn, bluespawn);
        Bukkit.getPluginManager().registerEvents(new TeamDeathMatchListener(this), CardWarsPlugin._instance);

    }

    public TeamDeathMatch(String name) {
        super(name);
        Bukkit.getPluginManager().registerEvents(new TeamDeathMatchListener(this), CardWarsPlugin._instance);
    }

    @Override
    public String getDbName() {
        return "tdm";
    }

    @Override
    public void startm() {
    }

    @Override
    public String getDesc() {
        return "Mate seus inimigos ! Cada um tem 4 vidas !";
    }

    @Override
    public void createScore(Player pOn) {
        ScoreboardManager.setDisplayName(pOn, DisplaySlot.SIDEBAR, "§4§lTDM §7- §f" + Utils.getTimeToString(this.getTime()));

        ScoreCWs.setScoreLine(pOn, 4, ChatColor.GOLD + "Vidas: " + ChatColor.YELLOW + "" + this.getVidas(pOn));
        ScoreCWs.setScoreLine(pOn, 3, ChatColor.GOLD + "Kills: " + ChatColor.YELLOW + "" + this.getKills(pOn.getUniqueId()));

        ScoreCWs.setScoreLine(pOn, 2, ChatColor.BLUE + "Time Azul: " + vivosblue);
        ScoreCWs.setScoreLine(pOn, 1, ChatColor.RED + "Time Vermelho: " + vivosred);

        ScoreCWs.updateLifePlayers();

    }

    @Override
    public void SecPerSec() {
        for (Player pOn : Bukkit.getOnlinePlayers()) {
            ScoreboardManager.setDisplayName(pOn, DisplaySlot.SIDEBAR, "§4§lTDM §7- §f" + Utils.getTimeToString(this.getTime()));
        }
    }

    @Override
    public Team getWinTeam() {
        int vidasred = 0;
        int vidasbue = 0;
        for (UUID uuid : getPlayers(Arena.Team.RED)) {
            vidasred += getVidas(uuid);
        }
        for (UUID uuid : getPlayers(Arena.Team.BLUE)) {
            vidasbue += getVidas(uuid);
        }
        if (vidasred > vidasbue) {
            return Team.RED;
        }
        if (vidasbue > vidasred) {
            return Team.BLUE;
        }
        if (CardWarsPlugin.random.nextBoolean()) {
            return Team.RED;
        } else {
            return Team.BLUE;
        }
    }
}
