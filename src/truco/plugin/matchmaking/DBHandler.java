package truco.plugin.matchmaking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import truco.plugin.managers.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.arena.Arena;
import truco.plugin.arena.EloPlayer;
import truco.plugin.data.mysql.MatchHistoryDB;
import truco.plugin.menus.ArmorSelectMenu;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.LevelManager;
import truco.plugin.functions.ScoreCWs;
import truco.plugin.managers.lobbys.LobbyObject;
import truco.plugin.managers.lobbys.MultipleLobbysManager;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.socket.SocketManager;
import truco.plugin.utils.Utils;

/**
 *
 * @author usuario ( de drogas ) WTF
 *
 */
public class DBHandler {

    public static Connection connection;
    //public static String connStr = ""; //Botei pra config
    public static Statement st;

    public DBHandler() {
        try {
            connection = getConnection();
            if (connection == null) {
                CardWarsPlugin.log.log(Level.SEVERE,
                        "[MineDota] CONXEAUMMM VEIO NUUUUL");
            }

            // needs to create FOREIGN KEYS
            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Players (uid VARCHAR(200) PRIMARY KEY, gold INTEGER, elo INTEGER, name varchar(200), armor varchar(20), level INTEGER, exp INTEGER,tutorial BOOLEAN) DEFAULT CHARSET=latin1;");

            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Queue (uid VARCHAR(200) PRIMARY KEY, groupId INTEGER, joinTime TIMESTAMP, gameData varchar(200))ENGINE=InnoDB  DEFAULT CHARSET=utf8;");

            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS QueueGroups (groupId INTEGER PRIMARY KEY AUTO_INCREMENT, joinTime datetime)ENGINE=InnoDB  DEFAULT CHARSET=utf8;");

            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS PlayersIngame (uid VARCHAR(200), gameId INTEGER, team TINYINT(1), PRIMARY KEY(uid, gameId))ENGINE=InnoDB  DEFAULT CHARSET=utf8;");

            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Games (gameId INTEGER PRIMARY KEY AUTO_INCREMENT, startTime TIMESTAMP, serverId INTEGER)ENGINE=InnoDB  DEFAULT CHARSET=utf8;");

            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Servers (serverId INTEGER PRIMARY KEY AUTO_INCREMENT, serverName varchar(200))ENGINE=InnoDB  DEFAULT CHARSET=utf8;");

            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS ReadyTeam (teamId INTEGER PRIMARY KEY AUTO_INCREMENT)ENGINE=InnoDB  DEFAULT CHARSET=utf8;");

            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS PlayerReadyTeam (teamId INTEGER, uid varchar(200), PRIMARY KEY(teamId, uid))ENGINE=InnoDB  DEFAULT CHARSET=utf8;");

            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS RankingElo (posicao INTEGER, uid varchar(200), elo INTEGER)ENGINE=InnoDB  DEFAULT CHARSET=utf8;");

            if ((server == ServerType.LOBBY) && CardWarsPlugin.matchmaking) {
                st = connection.createStatement();
                st.executeUpdate("DELETE FROM PlayerReadyTeam");

                st = connection.createStatement();
                st.executeUpdate("DELETE FROM ReadyTeam");

                st = connection.createStatement();
                st.executeUpdate("DELETE FROM Queue");

                st = connection.createStatement();
                st.executeUpdate("DELETE FROM QueueGroups");
            }
            //connection.commit();

            //conn.close();
        } catch (SQLException e) {
            CardWarsPlugin.log.log(Level.SEVERE,
                    "[MineDota] Connection closed");
            CardWarsPlugin.log.log(Level.SEVERE, "[MineDota] {0}", e);
        }
    }

    public void showRanking(Player p) {
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select posicao, uid, elo from RankingElo");
            while (rs.next()) {
                OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("uid")));
                p.sendMessage(ChatColor.GREEN + "" + rs.getInt("posicao") + " - " + op.getName() + " [" + rs.getInt("elo") + "]");
            }
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
    }
    public WeakHashMap<UUID, ArmorSelectMenu.Armadura> armors = new WeakHashMap<UUID, ArmorSelectMenu.Armadura>();

    public List<EloPlayer> getRanking() {

        List<EloPlayer> guildas = new ArrayList();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT elo,uid,name from Players ORDER BY `elo` DESC limit 5");
            while (rs.next()) {

                guildas.add(new EloPlayer(UUID.fromString(rs.getString("uid")), rs.getInt("elo"), rs.getString("name")));
            }

            return guildas;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return guildas;

    }

    public List<String> getGameServers() {
        List<String> svs = new ArrayList();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT Servers.serverName FROM `Games` INNER JOIN `Servers` on Games.serverId = Servers.serverId");
            while (rs.next()) {
               svs.add(rs.getString("serverName"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return svs;
    }

    public ArmorSelectMenu.Armadura getArmor(UUID p) {
        try {
            if (this.armors.containsKey(p)) {
                return armors.get(p);
            }
            ResultSet rs = connection.createStatement().executeQuery("SELECT armor FROM Players WHERE uid='" + p.toString() + "'");
            if (rs.next()) {
                ArmorSelectMenu.Armadura r = ArmorSelectMenu.Armadura.valueOf(rs.getString("armor"));
                armors.put(p, r);
                return r;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void setArmor(UUID p, ArmorSelectMenu.Armadura arm) {
        try {

            connection.createStatement().executeUpdate("UPDATE Players set armor='" + arm.name() + "' WHERE uid = '" + p.toString() + "'");

            if (armors.containsKey(p)) {
                armors.remove(p);
            }
            armors.put(p, arm);
        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateEloRanking(List<EloPlayer> lista) {
        try {
            st = connection.createStatement();
            st.executeUpdate("delete from RankingElo");
            //connection.commit();
            for (int x = 0; x < lista.size(); x++) {
                EloPlayer ep = lista.get(x);
                st = connection.createStatement();
                st.executeUpdate("insert into RankingElo (posicao, uid, elo) values (" + (x + 1) + ",'" + ep.u.toString() + "', " + ep.elo + ");");
            }
            //connection.commit();
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
    }

    public List<PlayerIngame> getPlayersIngame(String serverName) {
        List<PlayerIngame> lista = new ArrayList<PlayerIngame>();
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select G.gameId, P.uid, P.team from Servers S inner join Games G on G.serverId = S.serverId inner join PlayersIngame P on P.gameId = G.gameId where serverName='" + serverName + "'");
            while (rs.next()) {
                PlayerIngame player = new PlayerIngame(rs.getInt("gameId"), serverName, rs.getInt("team"));
                lista.add(player);
            }
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
        return lista;
    }

    public boolean hasGold(UUID p, int qtd) {
        return getGold(p) >= qtd;
    }

    public void resetBuffers(UUID uuid) {
        if (armors.containsKey(uuid)) {
            armors.remove(uuid);
        }
        if (levels.containsKey(uuid)) {
            levels.remove(uuid);
        }
    }

    public void setGold(UUID uuid, int qtd) {
        try {
            if (qtd < 0) {
                qtd = 0;
            }
            if ((server == ServerType.LOBBY || server == ServerType.TUTORIAL)) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) {
                    ScoreCWs.updategold(qtd, p);
                }
            }
            connection.createStatement().executeUpdate("UPDATE Players set Gold ='" + qtd + "' WHERE uid = '" + uuid.toString() + "'");
            //connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getGold(UUID uuid) {

        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Players WHERE uid = '" + uuid.toString() + "'");
            if (rs.next()) {
                return rs.getInt("Gold");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public PlayerInfo getPlayerInfo(UUID uuid) {
        PlayerInfo pin = new PlayerInfo(3000, 1, 0, 0, uuid);
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT gold,elo,level,exp FROM Players WHERE uid = '" + uuid.toString() + "'");
            if (rs.next()) {
                pin = new PlayerInfo(rs.getInt("elo"), rs.getInt("level"), rs.getInt("gold"), rs.getInt("exp"), uuid);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pin;
    }

    public void setExp(UUID uuid, int qtd) {
        try {
            if (qtd < 0) {
                qtd = 0;
            }
            Player p = Bukkit.getPlayer(uuid);
            if (p != null && (server == ServerType.LOBBY || server == ServerType.TUTORIAL)) {
                ScoreCWs.updateexp(qtd, p);
            }
            connection.createStatement().executeUpdate("UPDATE Players set exp =" + qtd + " WHERE uid = '" + uuid.toString() + "'");
            //connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getExp(UUID uuid) {

        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT exp FROM Players WHERE uid = '" + uuid.toString() + "'");
            if (rs.next()) {
                return rs.getInt("exp");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void setLevel(UUID uuid, int qtd) {
        try {
            if (qtd < 0) {
                qtd = 0;
            }
            if (levels.containsKey(uuid)) {
                levels.remove(uuid);
            }
            levels.put(uuid, qtd);
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {

                LevelManager.levelUp(p, qtd);

                Utils.sendTitle(p, "§aParabens você passou de level!", "§4Seu novo level : " + qtd, 10, 30, 10);
                if ((server == ServerType.LOBBY || server == ServerType.TUTORIAL)) {
                    ScoreCWs.updatelvl(qtd, p);
                }
            }
            connection.createStatement().executeUpdate("UPDATE Players set level ='" + qtd + "' WHERE uid = '" + uuid.toString() + "'");
            //connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addExp(UUID u, int qtd) {
        try {
            int lvl = getLevel(u);
            int xp = getExp(u);
            int qtdForProxLevel = 100 * (lvl / 2);
            xp += qtd;
            // upou
            if (xp > qtdForProxLevel) {
                int sobro = xp - qtdForProxLevel;
                setExp(u, sobro);
                setLevel(u, lvl + 1);
                Player p = Bukkit.getPlayer(u);
                if (p != null) {
                    ChatUtils.sendMessage(p, ChatColor.DARK_AQUA + "§lVocê passou de nivel parabens!");
                }
            } else {
                setExp(u, xp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public WeakHashMap<UUID, Integer> levels = new WeakHashMap<UUID, Integer>();

    public int getLevel(UUID uuid) {
        if (levels.containsKey(uuid)) {
            return levels.get(uuid);
        }
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT level FROM Players WHERE uid = '" + uuid.toString() + "'");
            if (rs.next()) {
                return rs.getInt("level");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void freeServerGame(String nome) throws Exception {
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select serverId from Servers where serverName='" + nome + "'");
            CardWarsPlugin.log.info("select serverId from Servers where serverName='" + nome + "'");
            int serverId = 0;
            if (rs.next()) {
                serverId = rs.getInt("serverId");
            } else {
                throw new Exception("Não existia esse servidor na tabela!");
            }
            st = connection.createStatement();
            ResultSet rss = st.executeQuery("select gameId from Games where serverId=" + serverId + "");
            //Main.log.info("select gameId from Games where serverId="+serverId+"");
            int gameId = 0;
            if (rss.next()) {
                gameId = rss.getInt("gameId");
            } else {
                Utils.debug("Servidor já estáva limpo!");

            }
            st = connection.createStatement();
            st.executeUpdate("delete from Games where serverId = " + serverId);
            // Main.log.info("delete from Games where serverId = "+serverId);
            //st = connection.createStatement();
            //st.executeUpdate("delete from PlayersIngame where gameId = " + gameId);
            // Main.log.info("delete from PlayersIngame where gameId = "+gameId);
            //connection.commit();
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
    }

    public void freeServerPlayers(String nome) throws Exception {
        try {
            ResultSet rs = st.executeQuery("select serverId from Servers where serverName='" + nome + "'");
            CardWarsPlugin.log.info("select serverId from Servers where serverName='" + nome + "'");
            int serverId = 0;
            if (rs.next()) {
                serverId = rs.getInt("serverId");
            } else {
                Utils.debug("Servidor já estáva limpo!");

            }
            st = connection.createStatement();
            ResultSet rss = st.executeQuery("select gameId from Games where serverId=" + serverId + "");
            //Main.log.info("select gameId from Games where serverId="+serverId+"");
            int gameId = 0;
            if (rss.next()) {
                gameId = rss.getInt("gameId");
            } else {
                Utils.debug("Servidor já estáva limpo!");

            }
            st = connection.createStatement();
            CardWarsPlugin.log.info("delete from PlayersIngame where gameId = " + gameId);
            st.executeUpdate("delete from PlayersIngame where gameId = " + gameId);
            // Main.log.info("delete from PlayersIngame where gameId = "+gameId);
            //connection.commit();
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
    }

    //usado pra pegar os negos juntos pra tirar da fila qnd 
    public List<UUID> getGroup(UUID u) {
        try {
            List<UUID> players = new ArrayList<UUID>();
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select uid from Queue where groupId = (select groupId from Queue where uid = '" + u.toString() + "' and groupId <> -1) ");
            while (rs.next()) {
                players.add(UUID.fromString(rs.getString("uid")));
            }
            // se nao tinha grupo verifica se ja tem time pronto
            if (players.size() == 0) {
                st = connection.createStatement();
                rs = st.executeQuery("select teamId from PlayerReadyTeam where uid = '" + u.toString() + "'");
                if (rs.next()) {
                    int teamId = rs.getInt("teamId");

                    st = connection.createStatement();
                    rs = st.executeQuery("select uid from PlayerReadyTeam where teamId = " + teamId);
                    while (rs.next()) {
                        players.add(UUID.fromString(rs.getString("uid")));
                    }
                }
            }
            // o grupo do cara pode estar na readyteams
            return players;
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
        return new ArrayList<UUID>();
    }

    public void removeFromQueue(UUID u) {
        try {
            int groupId = -1;
            int teamId = -1;
            // procurando o cara na queue
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select groupId from Queue Q where Q.uid='" + u.toString() + "'");
            if (rs.next()) {
                groupId = rs.getInt("groupId");
            }

            st = connection.createStatement();
            rs = st.executeQuery("select teamId from PlayerReadyTeam where uid='" + u.toString() + "'");
            if (rs.next()) {
                teamId = rs.getInt("teamId");
            }
            if (groupId != -1) {
                st = connection.createStatement();
                st.executeUpdate("DELETE FROM QueueGroups where groupId = " + groupId);

                st = connection.createStatement();
                st.executeUpdate("DELETE from Queue where groupId = " + groupId);
            }
            // if hes in a readyteam we have to cancell this ready team as well
            if (teamId != -1) {
                st = connection.createStatement();
                st.executeUpdate("DELETE from ReadyTeam where teamId = " + teamId + "");

                st = connection.createStatement();
                st.executeUpdate("DELETE from PlayerReadyTeam where teamId = " + teamId + "");
            }
            st = connection.createStatement();
            st.executeUpdate("DELETE from Queue where uid = '" + u.toString() + "'");
            //connection.commit();
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
    }

    public void sai(final Player pc, final boolean msg) {
        final UUID quitter = pc.getUniqueId();
        if (MatchMaker.db.isPlayerInQueue(pc.getUniqueId()) != PlayerStatus.NONE) {
            List<UUID> playerGroup = MatchMaker.db.getGroup(pc.getUniqueId());
            if (playerGroup.size() > 0) {
                for (UUID u : playerGroup) {

                    SocketManager.sendMessagetoPlayer(u.toString(), ChatColor.RED + "Um jogador do seu grupo desconectou e voce saiu da fila de espera !");

                }
            }
            Thread t = new Thread() {

                @Override
                public void run() {
                    MatchMaker.db.removeFromQueue(quitter);
                    if (pc != null && msg) {

                        ChatUtils.sendMessage(pc, "§a§lVocê saiu da fila para jogar!");
                    }
                }
            };
            t.start();
        } else {
            if (msg) {
                ChatUtils.sendMessage(pc, "§a§lVocê não está na fila para jogar!!");
            }

        }
    }
//UPDATE `gems` set `gems` = `gems` + 35 WHERE `uuid` = 'ea33ca55-1587-45fd-b8a1-baea6e0186f9';

    public void addGold(UUID uuid, int qtd) {
        setGold(uuid, getGold(uuid) + qtd);
    }

    public void addGoldWithThread(final UUID uid, final int qt, final boolean msg, final boolean checkvip) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                int tem = getGold(uid);
                int qtd = qt;
                if (msg) {
                    Player of = Bukkit.getPlayer(uid);
                    if (of != null) {
                        if (checkvip) {
                            qtd *= Arena.getMultiplicadorGold(of);
                        }
                        String s = qtd > 1 ? "s" : "";
                        ChatUtils.sendMessage(of, "§fVocê ganhou §5" + qtd + " §fmoeda" + s + "§f agora você tem §5" + (tem + qtd) + "§f moedas!");

                    }
                }

                setGold(uid, qtd + tem);
            }

        }).start();
    }

    public void removeGold(UUID uuid, int qtd) {
        setGold(uuid, getGold(uuid) - qtd);
    }

    public void addGroupQueue(List<UUID> players) {
        try {
            st = connection.createStatement();
            st.executeUpdate("insert into QueueGroups (joinTime) values (CURRENT_TIMESTAMP)");

            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select LAST_INSERT_ID() as id");
            rs.next();
            int groupId = rs.getInt("id");
            for (UUID p : players) {
                addPlayerToQueue(p, groupId);
            }
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
    }

    public PlayerIngame getIngamePlayer(Player p) {
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select G.gameId, S.serverName, P.team team from PlayersIngame P inner join Games G on G.gameId = P.gameId inner join Servers S on S.serverId = G.serverId where P.uid = '" + p.getUniqueId().toString() + "'");
            if (rs.next()) {
                //    System.out.println(rs.getInt("gameId") + "-" + rs.getString("serverName") + "-" + rs.getInt("team"));
                return new PlayerIngame(rs.getInt("gameId"), rs.getString("serverName"), rs.getInt("team"));
            }
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
        return null;
    }

    // gets a server that is not running a game
    public void startGame(Team[] teams, FreeServer server) {
        try {
            // Utils.debug("Starting game on server " + server.serverName);
            st = connection.createStatement();
            // creating the game
            st.executeUpdate("insert into Games (startTime, serverId) values(CURRENT_TIMESTAMP, " + server.serverId + ")");
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select LAST_INSERT_ID() as id");
            rs.next();
            int gameId = rs.getInt("id");
            // puttin players ingame
            for (UUID pTeam1 : teams[0].players) {
                st = connection.createStatement();
                st.executeUpdate("insert into PlayersIngame (uid, gameId, team) values('" + pTeam1.toString() + "', " + gameId + ", 0)");
            }
            for (UUID pTeam2 : teams[1].players) {
                st = connection.createStatement();
                st.executeUpdate("insert into PlayersIngame (uid, gameId, team) values('" + pTeam2.toString() + "', " + gameId + ", 1)");
            }

            // removing ready teams
            st = connection.createStatement();
            st.executeUpdate("delete from ReadyTeam where teamId in (" + teams[0].teamId + "," + teams[1].teamId + ")");
            // removing ready players
            st = connection.createStatement();
            st.executeUpdate("delete from PlayerReadyTeam where teamId in (" + teams[0].teamId + "," + teams[1].teamId + ")");
            //connection.commit();
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
    }

    // gets a server that is not running a game
    public FreeServer getFreeServer() {
        FreeServer server = null;
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select S.serverName, S.serverId from Servers S left join Games G on G.serverId = S.serverId where G.gameId is null order by rand() limit 1");
            if (rs.next()) {
                server = new FreeServer(rs.getInt("serverId"), rs.getString("serverName"));
            }
            //connection.commit();
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
        return server;
    }

    public Team[] getReadyTeams() {
        try {
            Team[] teams = new Team[2];
            int ct = 0;
            int initialTeamId = -1;
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select P.uid, P.teamId from PlayerReadyTeam P inner join ReadyTeam R on R.teamId = P.teamId order by teamId limit 10");
            while (rs.next()) {
                if (ct >= 2) {
                    break;
                }
                int teamId = rs.getInt("teamId");
                UUID u = UUID.fromString(rs.getString("uid"));
                if (initialTeamId == -1) {

                    initialTeamId = teamId;
                    Team t = new Team();
                    teams[ct] = t;
                    teams[ct].players.add(u);
                    teams[ct].teamId = teamId;
                } else if (teamId != initialTeamId) {
                    ct++;
                    initialTeamId = teamId;
                    Team t = new Team();
                    teams[ct] = t;
                    teams[ct].players.add(u);
                    teams[ct].teamId = teamId;
                } else {
                    teams[ct].players.add(u);
                    teams[ct].teamId = teamId;
                }
            }
            return teams;
        } catch (SQLException ex) {

            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
        return null;
    }

    // called on joinevent   0 FOI REGISTRADO 1 TENQ FAZER TUTORIAL 2 TUDO OK
    public int initPlayer(UUID u, String name) {
        int re = -1;
        try {

            ResultSet rs = connection.createStatement().executeQuery("select tutorial from Players where uid = '" + u.toString() + "'");
            if (!rs.next()) {

                PreparedStatement ps = connection.prepareStatement("insert into Players (uid, gold,name,elo,armor, level, exp,tutorial) values ('" + u.toString() + "',0,'" + name + "','3000','" + ArmorSelectMenu.Armadura.PELADO.name() + "',1,0,?);");
                ps.setBoolean(1, false);
                ps.executeUpdate();
                re = 0;
                //connection.commit();
            } else {
                if (rs.getBoolean("tutorial")) {

                    re = 2;
                } else {

                    re = 1;
                }

                MatchHistoryDB.updateName(u.toString(), name);
                //connection.commit();
            }

        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
        return re;
    }

    public void terminaTutorial(UUID u) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE Players set tutorial = ? where uid = '" + u.toString() + "'");
            ps.setBoolean(1, true);
            ps.executeUpdate();
            //connection.commit();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean fezTutorial(UUID u) {
        try {

            ResultSet rs = connection.createStatement().executeQuery("select tutorial from Players where uid = '" + u.toString() + "'");
            rs.next();
            return rs.getBoolean("tutorial");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;

    }

    public int getPlayersNaFila() {
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select count(uid) as ct from Queue");
            rs.next();
            return rs.getInt("ct");
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    public int getTimesProntos() {
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select count(teamId) as ct from ReadyTeam");
            rs.next();
            return rs.getInt("ct");
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    public void setElo(UUID u, int elo) {
        try {
            st = connection.createStatement();
            st.executeUpdate("update Players set elo = " + elo + " where uid = '" + u.toString() + "'");
            //connection.commit();
            Player of = Bukkit.getPlayer(u);
            if (of != null && (server == ServerType.LOBBY || server == ServerType.TUTORIAL)) {
                ScoreCWs.updateelo(elo, of);
                ScoreCWs.updateliga(elo, of);
            }
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public int getElo(UUID u) {
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select elo from Players where uid = '" + u.toString() + "'");
            if (rs.next()) {
                return rs.getInt("elo");
            }
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    public List<EloPlayer> getElos() {
        List<EloPlayer> list = new ArrayList();
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select uid,elo,name from Players");
            while (rs.next()) {
                list.add(new EloPlayer(UUID.fromString(rs.getString("uid")), rs.getInt("elo"), rs.getString("name")));
            }
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
        }
        return list;
    }

    public enum PlayerStatus {

        QUEUE,
        READYTEAM,
        NONE
    }

    public PlayerStatus isPlayerInQueue(UUID u) {
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select 1 as result from Queue where uid = '" + u.toString() + "'"
                    + " UNION "
                    + "select 2 as result from PlayerReadyTeam T where T.uid ='" + u.toString() + "';");
            if (rs.next()) {
                int resposta = rs.getInt("result");
                if (resposta == 1) {
                    return PlayerStatus.QUEUE;
                } else if (resposta == 2) {
                    return PlayerStatus.READYTEAM;
                }
            } else {
                return PlayerStatus.NONE;
            }
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
        return PlayerStatus.NONE;
    }

    public void addPlayerToQueue(UUID u, int groupId) {
        try {
            st = connection.createStatement();
            st.executeUpdate("insert into Queue (uid, groupId, joinTime, gameData) values ('" + u.toString() + "'," + groupId + ", CURRENT_TIMESTAMP, 'normal')");
            //connection.commit();
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("Database Error:" + ex.getMessage());
            ex.printStackTrace();
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Shuttin it Down !");
        }
    }

    public void fechaConexao() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
        }
    }

    public synchronized java.sql.Connection getConnection() {
        if (connection == null) {
            connection = CardWarsPlugin.conn;

        }

        return connection;
    }
}
