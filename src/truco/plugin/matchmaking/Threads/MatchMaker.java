/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.matchmaking.Threads;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import truco.plugin.functions.Cooldown;
import truco.plugin.functions.groups.ControleGrupos;
import truco.plugin.functions.groups.EventosGrupos;
import truco.plugin.functions.groups.Grupo;
import truco.plugin.managers.lobbys.LobbyObject;
import truco.plugin.managers.lobbys.MultipleLobbysManager;
import truco.plugin.matchmaking.DBHandler;
import truco.plugin.matchmaking.DBHandler.PlayerStatus;
import truco.plugin.matchmaking.FreeServer;
import truco.plugin.matchmaking.PlayerBlock;
import truco.plugin.matchmaking.PossibleMatch;
import truco.plugin.matchmaking.Team;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.Utils;

/**
 *
 * @author usuario
 *
 */
// this will run at one place only
public class MatchMaker extends Thread {

    public static DBHandler db;

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(1000 * 30);
            } catch (InterruptedException ex) {
                CardWarsPlugin.log.info("Error on Thread Sleep of MatchMaker");
            }
            try {

                //     debuga();
                //   CardWarsPlugin.log.info("----------MATCHMAKING---------");
                HashMap<Integer, List<UUID>> groups = new HashMap<>();
                List<UUID> solo = new ArrayList<>();
                HashSet<PossibleMatch> possibleMatches = new HashSet<PossibleMatch>();
                possibleMatches.add(new PossibleMatch());

                db.st = db.connection.createStatement();

                ResultSet rs = db.st.executeQuery("select Q.uid, IFNULL(G.groupId,-1) as groupId from Queue Q left join QueueGroups G on G.groupId = Q.groupId order by Q.joinTime desc, groupId;");
                // filling groups
                while (rs.next()) {
                    UUID uid = UUID.fromString(rs.getString("uid"));
                    int groupId = rs.getInt("groupId");
                    if (groupId != -1) {
                        //         Utils.debug("added a player to group id " + groupId + " !");
                        if (groups.containsKey(groupId)) {
                            List<UUID> players = groups.get(groupId);
                            players.add(uid);
                        } else {
                            List<UUID> players = new ArrayList<UUID>();
                            players.add(uid);
                            groups.put(groupId, players);
                        }
                    } else {
                        //       Utils.debug("added a player " + uid.toString() + " to solo list");
                        solo.add(uid);
                    }
                }

                // first, fit the groups
                for (Integer groupId : groups.keySet()) {
                    boolean hasBeenFit = false;
                    // Utils.debug("fittin group id " + groupId);
                    List<UUID> players = groups.get(groupId);
                    // closed group yay makes things easy
                    if (players.size() == PossibleMatch.teamNumber) {
                        PossibleMatch m = new PossibleMatch();
                        m.players.add(new PlayerBlock(players));
                        possibleMatches.add(m);
                        //   Utils.debug("this group have " + players.size() + " players and has made a possible team");
                        continue;
                    }
                    //Utils.debug("this group have " + players.size() + " players");
                    // se if this group fits in any possible matches
                    for (PossibleMatch m : possibleMatches) {
                        //  Utils.debug("reading possible match to match group id =" + groupId);
                        int needPlayers = m.needPlayers();
                        //Utils.debug("this match needs " + needPlayers + " players and this group have " + players.size());
                        if (needPlayers >= players.size()) {
                            //  Utils.debug("ive placed group " + groupId + " on this possible match cause it fits !");
                            m.players.add(new PlayerBlock(players));
                            hasBeenFit = true;
                            break;
                        }
                    }
                    if (!hasBeenFit) {
                        //Utils.debug("no possible matches that would fit group " + groupId + ", creating new possible match");
                        PossibleMatch m = new PossibleMatch();
                        m.players.add(new PlayerBlock(players));
                        possibleMatches.add(m);
                    }
                }

                // then fit the solo players
                for (UUID u : solo) {
                    //  Utils.debug("fittin solo player");
                    boolean hasBeenFit = false;
                    for (PossibleMatch m : possibleMatches) {
                        if (m.needPlayers() > 0) {
                            //        Utils.debug("this player fits in a possible match !");
                            m.players.add(new PlayerBlock(Arrays.asList(new UUID[]{u})));
                            hasBeenFit = true;
                        }
                    }
                    if (!hasBeenFit) {
                        //  Utils.debug("this player didnt fit in any match, creating a new one !");
                        PossibleMatch m2 = new PossibleMatch();
                        m2.players.add(new PlayerBlock(Arrays.asList(new UUID[]{u})));
                        possibleMatches.add(m2);
                    }
                }

                // now we check if any match is closed
                for (PossibleMatch m : possibleMatches) {
                    // 5 ppl ! YAY !
                    if (m.needPlayers() == 0) {
                        // inserting to warn this players are ready to go !
                        db.st = db.connection.createStatement();
                        db.st.executeUpdate("insert into ReadyTeam (teamId) values (null)");

                        int teamId = 0;
                        db.st = db.connection.createStatement();
                        rs = db.st.executeQuery("select LAST_INSERT_ID() as id");
                        rs.next();
                        teamId = rs.getInt("id");
                        //Utils.debug("Created a ready to fight team with id =" + teamId + " !!!");
                        for (UUID p : m.getAllPlayers()) {
                            //    Utils.debug("adding player =" + p + " !!!");
                            db.st = db.connection.createStatement();
                            db.st.executeUpdate("insert into PlayerReadyTeam (teamId, uid) values (" + teamId + ",'" + p.toString() + "')");

                            // now deleting those players and theyr groups from the queue as theyr already ready
                            db.st = db.connection.createStatement();
                            db.st.executeUpdate("DELETE G.* FROM QueueGroups G INNER JOIN Queue Q ON Q.groupId = G.groupId where Q.uid = '" + p.toString() + "' ");
                            db.st = db.connection.createStatement();
                            db.st.executeUpdate("DELETE FROM Queue where uid = '" + p.toString() + "'");
                        }
                    }
                }

                // checking if we have teams ready to start the game
                Team[] readyToStart = db.getReadyTeams();
                if (readyToStart == null || readyToStart.length < 2 || readyToStart[0] == null || readyToStart[1] == null) {
                    //Utils.debug("There is no 2 teams waiting for the game to start !!!");
                    continue;
                }

                // checking if we have ready servers
                FreeServer server = db.getFreeServer();
                if (server == null) {
                    //Utils.debug("We have no free servers need to wait a game to finish !!!");
                    continue;
                }

                // starting the game
                // we place those teams into DB warning theyr ready to teleport !
                db.startGame(readyToStart, server);

            } catch (Exception ex) {
                CardWarsPlugin.log.info("Error in MatchMaker Thread:" + ex.getMessage());
                ex.printStackTrace();
            }

        }
    }

    // puts a player in the queue to join a game
    public static void addPlayerToSoloQueue(final Player p) {
        if (Cooldown.isCooldown(p, "entrarfila")) {
            ChatUtils.sendMessage(p, ChatColor.RED + "Espere um realizar esta ação novamente!!");
            return;
        }
        Cooldown.addCoolDown(p, "entrarfila", 5000);
        new Thread(new Runnable() {

            @Override
            public void run() {
                Grupo gp = ControleGrupos.getGrupo(p);
                if (gp == null) {
                    if (MatchMaker.db.getIngamePlayer(p) != null) {
                        p.sendMessage("§aVocê já está em jogo!");
                        return;

                    }
                    if (db.isPlayerInQueue(p.getUniqueId()) != PlayerStatus.NONE) {
                        ChatUtils.sendMessage(p, ChatColor.RED + "Voce ja esta na fila de espera !");
                        p.sendMessage("§aCaso queira sair use §c/sairfila");
                        return;
                    }
                    db.addPlayerToQueue(p.getUniqueId(), -1); // -1 means he have no group

                    ChatUtils.sendMessage(p, ChatColor.GREEN + "Voce entrou na fila de espera para jogar !");
                    p.sendMessage("§aCaso queira sair use §c/sairfila");
                } else {
                    if (gp.getLider() == p.getUniqueId()) {
                        boolean pode = true;
                        for (UUID uui : gp.getPlayers()) {

                            if (db.isPlayerInQueue(uui) != PlayerStatus.NONE || MatchMaker.db.getIngamePlayer(p) != null) {
                                pode = false;
                                break;
                            }
                        }
                        if (pode) {
                            gp.sendGroupMsg("§b§lSeu grupo entrou na fila!");
                            gp.sendGroupMsg("§aCaso queira sair use §c/sairfila");
                            EventosGrupos.addToQueue(gp);
                        } else {
                            gp.sendGroupMsg("§b§lAlgum membro do grupo já está na fila!");
                        }
                    } else {
                        ChatUtils.sendMessage(p, "§a§lSomente o lider do grupo pode colocar na fila!");
                    }
                }
            }
        }).start();

    }

    public static void debuga() {
        try {
            ResultSet rs = db.connection.createStatement().executeQuery("SELECT * FROM Queue");
            //==============================================================================
            CardWarsPlugin.log("====================== QUEUE =================");

            while (rs.next()) {
                CardWarsPlugin.log("uid=" + rs.getString("uid") + " - GROUPID=" + rs.getInt("groupId"));
            }
            CardWarsPlugin.log("====================== QUEUE =================");
            rs = db.connection.createStatement().executeQuery("SELECT * FROM QueueGroups");
            //==============================================================================
            CardWarsPlugin.log("====================== QUEUEGROUPS =================");

            while (rs.next()) {
                CardWarsPlugin.log("joinTime=" + rs.getTime("joinTime").toGMTString() + " - GROUPID=" + rs.getInt("groupId"));
            }

            CardWarsPlugin.log("====================== QUEUEGROUPS =================");
            //==============================================================================

            String ready = "|||||+= EQUIPES PRONTAS :";
            rs = db.connection.createStatement().executeQuery("SELECT * FROM ReadyTeam");
            while (rs.next()) {
                ready += rs.getInt("teamId");
            }
            CardWarsPlugin.log("\n" + ready);
            rs = db.connection.createStatement().executeQuery("SELECT * FROM PlayersIngame");
//============================================================================================================
            CardWarsPlugin.log("====================== PLAYERSINGAME =================");

            while (rs.next()) {
                CardWarsPlugin.log("uid=" + rs.getString("uid") + " - gameId=" + rs.getInt("gameId") + " - team=" + rs.getBoolean("team"));
            }

            CardWarsPlugin.log("====================== PLAYERSINGAME =================");
//--------------------------------------------------------------------------------------------=
            rs = db.connection.createStatement().executeQuery("SELECT * FROM Games");

            CardWarsPlugin.log("====================== GAMES =================");

            while (rs.next()) {
                CardWarsPlugin.log("gameId=" + rs.getInt("gameId") + " - startTime=" + rs.getTimestamp("startTime").toGMTString() + " - serverId=" + rs.getInt("serverId"));
            }
            CardWarsPlugin.log("====================== GAMES =================");
//===========================================================================================================

            rs = db.connection.createStatement().executeQuery("SELECT * FROM PlayerReadyTeam");

            CardWarsPlugin.log("====================== PLAYERREADYTEAM =================");

            while (rs.next()) {
                CardWarsPlugin.log("teamId=" + rs.getInt("teamId") + " - uid=" + rs.getString("uid"));
            }
            CardWarsPlugin.log("====================== PLAYERREADYTEAM =================");
//===========================================================================================================
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void clearServer() {
        try {

            Runnable r = new Runnable() {
                public void run() {

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        LobbyObject freeHub = MultipleLobbysManager.getPerfectLobby(p);
                        if (freeHub == null) {
                            p.kickPlayer("§4§lTodos os Lobbys estão lotados, tente entrar novamente!");
                        } else {
                            Utils.TeleportarTPBG(freeHub.getNome(), p);
                        }
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                        @Override
                        public void run() {
                            Bukkit.shutdown();
                        }
                    }, 20 * 5);
                }
            };

            Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, r, 20 * 10);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(ChatColor.GREEN + "Voce sera teleportado de volta em alguns segundos...");
            }
            if (CardWarsPlugin.server == ServerType.GAME) {
                try {
                    MatchMaker.db.freeServerPlayers(CardWarsPlugin.serverName);
                } catch (Exception ex) {
                    Logger.getLogger(CardWarsPlugin.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } catch (Exception e) {
            CardWarsPlugin.log.info(e.getMessage());
            e.printStackTrace();
        }
    }

}
