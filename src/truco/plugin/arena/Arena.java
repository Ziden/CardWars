package truco.plugin.arena;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffectType;
import sun.security.krb5.internal.KDCOptions;
import truco.plugin.CardWarsPlugin;
import truco.plugin.functions.game.MultipleKills;
import truco.plugin.matchmaking.PossibleMatch;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.data.mysql.MatchHistoryDB;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.elo.Elo;
import truco.plugin.data.MetaShit;
import truco.plugin.functions.Cooldown;
import truco.plugin.functions.ScoreCWs;
import truco.plugin.managers.PermManager;
import truco.plugin.utils.SoundUtils;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 *
 */
public abstract class Arena {

    private String name;
    private Location redspawn;
    private Location bluespawn;

    public static ArrayList<Arena> todasarenas = new ArrayList();
    public HashMap<UUID, Team> players = new HashMap();
    private int time;
    private GameState state;
    public ArrayList<UUID> specs = new ArrayList();
    public int gameId = -1;

    public abstract String getDesc();

    public ArrayList<UUID> getSpecs() {
        return specs;
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    public void tp(Player p) {
        if (getTeam(p.getUniqueId()) == Team.RED) {
            p.teleport(redspawn);
        } else if (getTeam(p.getUniqueId()) == Team.BLUE) {
            p.teleport(bluespawn);
        } else {
            p.teleport(p.getWorld().getSpawnLocation());
        }
    }

    public Location getTpLocation(Player p) {
        if (getTeam(p.getUniqueId()) == Team.RED) {
            return (redspawn);
        } else if (getTeam(p.getUniqueId()) == Team.BLUE) {
            return (bluespawn);
        } else {
            return p.getLocation().getWorld().getSpawnLocation();
        }
    }

    public void addPlayer(final Player p, final Team t) {
        if (!players.containsKey(p.getUniqueId())) {
            players.put(p.getUniqueId(), t);
        }

    }

    public abstract void startm();

    public void soltaFogos(Team t) {
        final Team equi = t;
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                for (UUID uuid : getPlayers(equi)) {
                    Player pl = Bukkit.getPlayer(uuid);
                    if ((pl != null) && (pl.isOnline())) {
                        Utils.spawnRandomFirework(pl.getLocation());
                    }
                }
            }
        }, 0, 20);
    }

    public Team getTeam(UUID p) {
        if (getPlayers(Team.RED).contains(p)) {
            return Team.RED;
        }
        if (getPlayers(Team.BLUE).contains(p)) {
            return Team.BLUE;
        }
        if (getPlayers(Team.SPEC).contains(p)) {
            return Team.SPEC;
        }
        return null;
    }

    public List<UUID> getPlayers(Team t) {
        ArrayList<UUID> team = new ArrayList<UUID>();
        for (Entry<UUID, Team> ent : players.entrySet()) {
            if (ent.getValue() == t) {
                if (ent.getKey() != null) {
                    team.add(ent.getKey());
                }
            }
        }
        return team;
    }

    public List<UUID> getPlayers() {
        ArrayList<UUID> ps = new ArrayList<>();
        for (UUID p : getPlayers(Team.RED)) {
            if (p != null) {
                ps.add(p);
            }
        }
        for (UUID p : getPlayers(Team.BLUE)) {
            if (p != null) {
                ps.add(p);
            }
        }
        return ps;
    }

    public int getTime() {
        return time;
    }

    public void setGameId(int game) {
        this.gameId = game;

    }

    public abstract void createScore(Player pOn);

    public abstract Team getWinTeam();

    public abstract void SecPerSec();

    public int getGameId() {
        return gameId;
    }

    public GameState getState() {
        return state;
    }

    public void startGame() {

        if (state == GameState.ESPERANDO_JOGADORES) {

            int id = MatchHistoryDB.addPartida();

            setGameId(id);
            state = GameState.CARREGANDO;

            time = 0;

            Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    time++;
                    SecPerSec();
                    if (time < 10) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.playSound(p.getLocation(), Sound.NOTE_PLING, Integer.MAX_VALUE, 1);
                            Utils.sendTitle(p, "§a§l" + (10 - time), "§7§lO jogo começa em", 0, 25, 0);
                        }
                    }
                    if (time == 10) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.teleport(p.getWorld().getSpawnLocation());
                            for (Player pon : Bukkit.getOnlinePlayers()) {
                                if (pon != p) {
                                    if (getTeam(p.getUniqueId()) == Team.SPEC || getTeam(pon.getUniqueId()) == Team.SPEC) {
                                        continue;
                                    }
                                    pon.hidePlayer(p);
                                    p.hidePlayer(pon);

                                }
                            }
                        }

                        state = GameState.INGAME;
                        Bukkit.broadcastMessage("§b§lO jogo começou agora!!!");

                        for (UUID p : getPlayers()) {
                            Player pl = Bukkit.getPlayer(p);
                            if (pl != null) {
                                tp(pl);
                                for (Player pon : Bukkit.getOnlinePlayers()) {
                                    if (pon != pl) {
                                        pon.showPlayer(pl);
                                        pl.showPlayer(pon);
                                    }
                                }
                                Utils.sendTitle(pl, "§4§lO Jogo começou", "§7GOGOGOGO", 0, 20 * 3, 0);
                                pl.playSound(pl.getLocation(), Sound.EXPLODE, Integer.MAX_VALUE, 1);
                            }
                        }
                    }
                    if (time == (1200)) {

                        win(getWinTeam());
                    }

                }
            }, 20l, 20l);
            startm();

        }
    }

    public Location getRedspawn() {
        return redspawn;
    }

    public String getName() {
        return name;
    }

    public void setBluespawn(Location bluespawn) {

        this.bluespawn = bluespawn;

    }

    public static Arena getArenaByName(String name) {
        for (Arena r : todasarenas) {
            if (r.getName().equals(name)) {
                return r;
            }
        }
        return null;
    }

    public void setRedspawn(Location redspawn) {
        this.redspawn = redspawn;

    }

    public static int getMultiplicadorGold(Player p) {
        if (PermManager.GOLD3X.playerHas(p)) {
            return 3;
        }
        if (PermManager.GOLD2X.playerHas(p)) {
            return 2;
        }
        return 1;
    }

    public static int getMultiplicadorExp(Player p) {
        if (PermManager.EXP3X.playerHas(p)) {
            return 3;
        }
        if (PermManager.EXP2X.playerHas(p)) {
            return 2;
        }
        return 1;
    }

    public void win(Team t) {
        if (state != GameState.POSGAME) {
            final Arena ar = this;
            soltaFogos(t);
            state = GameState.POSGAME;
            ChatUtils.broadcastMessage("§e§lA equipe " + t.getCor() + "§l" + t.getName() + "§e§l ganhou a partida!");
            ChatUtils.broadcastMessage("§bServidor distribuindo recompensas aguarde!");
            int eloRed = 0;
            int eloBlue = 0;
            MatchHistoryDB.acabaPartida(this, t);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (getTeam(p.getUniqueId()) != null) {
                    if (t == getTeam(p.getUniqueId())) {
                        Utils.sendTitle(p, "§a§lFIM DA PARTIDA", "§dSUA EQUIPE GANHOU O JOGO!", 5, 20 * 10, 5);
                    } else if (t != Team.SPEC) {
                        Utils.sendTitle(p, "§4§lFIM DA PARTIDA", "§cSUA EQUIPE PERDEU O JOGO!", 5, 20 * 10, 5);

                    } else {
                        Utils.sendTitle(p, "§b§lFIM DA PARTIDA", t.getColoredName(true) + "§7 ganhou!", 5, 20 * 10, 5);
                    }
                }
            }
            // calculando ELO medio de cada time
            int dblue, dred;
            dred = 0;
            dblue = 0;
            for (Team time : Arrays.asList(Team.RED, Team.BLUE)) {
                List<UUID> pls = getPlayers(time);
                for (UUID p : pls) {
                    if (time == Team.BLUE) {
                        dblue++;
                        eloBlue += MatchMaker.db.getElo(p);
                    } else if (time == Team.RED) {
                        eloRed += MatchMaker.db.getElo(p);
                        dred++;
                    }
                }
            }
            if (dblue != 0 && dred != 0) {

                eloBlue = eloBlue / dblue;
                eloRed = eloRed / dred;
            }
            CardWarsPlugin.log.info("Elo Blue medio: " + eloBlue + " e red medio : " + eloRed);
            for (Team time : Arrays.asList(Team.RED, Team.BLUE)) {
                for (UUID p : getPlayers(time)) {

                    int elo = MatchMaker.db.getElo(p);
                    int eloFinal = elo;
                    if (time == t) { // vencedor
                        int gold = 33;
                        int exp = 24;
                        gold += MultipleKills.getBonusGold(p);
                        exp += MultipleKills.getBonusExp(p);
                        if (CardWarsPlugin.random.nextBoolean()) {
                            gold++;
                            exp++;
                        }
                        Player pl = Bukkit.getPlayer(p);

                        if (pl != null) {
                            gold *= getMultiplicadorGold(pl);
                            exp *= getMultiplicadorExp(pl);
                        }
                        if (dblue != 0 && dred != 0) {
                            if (t == Team.RED) {
                                eloFinal = Elo.ganhador(elo, eloBlue);
                            } else {
                                eloFinal = Elo.ganhador(elo, eloRed);
                            }
                        }
                        MatchHistoryDB.addWin(p);
                        MatchMaker.db.addGold(p, gold);
                        MatchMaker.db.addExp(p, exp);
                        if (pl != null) {

                            ChatUtils.sendMessage(pl, ChatColor.BLUE + "[Loot]" + ChatColor.GOLD + "Voce ganhou " + gold + " moeda" + Utils.getS(gold) + " !");
                            ChatUtils.sendMessage(pl, ChatColor.BLUE + "[Loot]" + ChatColor.GOLD + "Voce ganhou " + exp + " exp !");
                            if (dblue != 0 && dred != 0) {
                                ChatUtils.sendMessage(pl, ChatColor.BLUE + "[Loot]" + ChatColor.GREEN + "Voce ganhou " + (eloFinal - elo) + " pontos de ELO !");
                            }
                        }
                    } else {
                        int gold = 12;
                        int exp = 12;
                        Player pl = Bukkit.getPlayer(p);
                        if (dblue != 0 && dred != 0) {
                            if (t == Team.RED) {
                                eloFinal = Elo.perdedor(eloBlue, elo);
                            } else {
                                eloFinal = Elo.perdedor(eloRed, elo);
                            }
                        }

                        gold += MultipleKills.getBonusGold(p);
                        exp += MultipleKills.getBonusExp(p);
                        if (CardWarsPlugin.random.nextBoolean()) {
                            gold++;
                            exp++;
                        }
                        if (pl != null) {
                            gold *= getMultiplicadorGold(pl);
                            exp *= getMultiplicadorExp(pl);

                        }

                        MatchHistoryDB.addLose(p);
                        MatchMaker.db.addGold(p, gold);
                        MatchMaker.db.addExp(p, exp);
                        if (pl != null) {
                            if (dblue != 0 && dred != 0) {

                                ChatUtils.sendMessage(pl, ChatColor.RED + "Voce perdeu " + (elo - eloFinal) + " pontos de ELO !");
                            }
                            ChatUtils.sendMessage(pl, ChatColor.BLUE + "[Loot]" + ChatColor.GOLD + "Voce ganhou " + gold + " moeda" + Utils.getS(gold) + " !");
                            ChatUtils.sendMessage(pl, ChatColor.BLUE + "[Loot]" + ChatColor.GOLD + "Voce ganhou " + exp + " exp !");
                        }
                    }
                    if (dblue != 0 && dred != 0) {
                        MatchMaker.db.setElo(p, eloFinal);
                    }
                    Player pl = Bukkit.getPlayer(p);
                    if (pl != null) {

                        ChatUtils.sendMessage(pl, ChatColor.GREEN + "ELO FINAL:" + ChatColor.YELLOW + eloFinal);
                    }
                }
            }
            MatchMaker.clearServer();

        } else {

        }
    }

    public Location getBluespawn() {
        return bluespawn;
    }

    public Arena(String name, Location redspawn, Location bluespawn) {
        this.name = name;
        this.redspawn = redspawn;
        this.bluespawn = bluespawn;
        state = GameState.ESPERANDO_JOGADORES;
        todasarenas.add(this);
        Bukkit.getPluginManager().registerEvents(new ArenaListener(this), CardWarsPlugin._instance);
    }

    public void removeSpec(UUID uuid) {
        if (specs.contains(uuid)) {
            specs.remove(uuid);
        }
        Player p = Bukkit.getPlayer(uuid);

        if (p != null) {
            p.setGameMode(GameMode.SURVIVAL);

        } else {
            toremove.add(uuid);
        }

    }
    public ArrayList<UUID> toremove = new ArrayList();

    public void addSpec(Player p) {
        if (!specs.contains(p.getUniqueId())) {
            specs.add(p.getUniqueId());

            p.setGameMode(GameMode.SPECTATOR);

        }

    }

    public Arena(String name) {
        this.name = name;
        state = GameState.ESPERANDO_JOGADORES;
        todasarenas.add(this);
        Bukkit.getPluginManager().registerEvents(new ArenaListener(this), CardWarsPlugin._instance);
    }

    public abstract String getDbName();

    public abstract void startPlayer(Player p);

    void setPlayerTeam(Player p, Team t) {
        if (players.containsKey(p.getUniqueId())) {
            players.remove(p.getUniqueId());
        }
        players.put(p.getUniqueId(), t);

    }

    public static enum Team {

        RED(ChatColor.RED, "Vermelha"), BLUE(ChatColor.BLUE, "Azul"), SPEC(ChatColor.GRAY, "Espectador");
        ChatColor cor;
        String name;

        public String getColoredName(boolean negrito) {
            String b = negrito ? "§l" : "";
            return getCor() + b + getName();
        }

        public ChatColor getCor() {
            return cor;
        }

        public String getName() {
            return name;
        }

        private Team(ChatColor cor, String name) {
            this.cor = cor;
            this.name = name;
        }

        public void sendMessage(String msg) {
            if (CardWarsPlugin.getArena() != null) {
                for (Player uuid : Bukkit.getOnlinePlayers()) {
                    Team t = CardWarsPlugin.getArena().getTeam(uuid.getUniqueId());
                    if (t != null && t == this) {
                        ChatUtils.sendMessage(uuid, msg);
                    }
                }
            }

        }

        //BEST INGREIS
        public static Team getTeamOposta(Team t) {
            if (t == RED) {
                return BLUE;
            }
            if (t == BLUE) {
                return RED;
            }
            return null;

        }
    }
}
