package truco.plugin.data.mysql;

import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.functions.game.MultipleKills;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import truco.plugin.*;
import truco.plugin.functions.game.MultipleKills.KillType;
import truco.plugin.arena.Arena;
import static truco.plugin.matchmaking.DBHandler.connection;
import truco.plugin.menus.ArmorSelectMenu.Armadura;

/**
 *
 * @author usuario ( de drogas ) WTF
 *
 */
public class MatchHistoryDB {

    public static Connection connection;
    //public static String connStr = ""; //Botei pra config
    public static Statement st;

    public MatchHistoryDB() {
        try {
            connection = getConnection();
            if (connection == null) {
                CardWarsPlugin.log.log(Level.SEVERE,
                        "[MineDota] CONXEAUMMM VEIO NUUUUL");
            }

            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Jogadores (uuid VARCHAR(200),nome VARCHAR(30),vitorias INTEGER,derrotas INTEGER,kills INTEGER,mortes INTEGEr) ENGINE=InnoDB  DEFAULT CHARSET=utf8");

            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Historico_Partidas (id_historico INTEGER PRIMARY KEY AUTO_INCREMENT,timeGanhador TEXT,timePerdedor TEXT,dataInicial TIMESTAMP,dataFinal TIMESTAMP,tipo VARCHAR(20),serverName VARCHAR(40)) ENGINE=InnoDB  DEFAULT CHARSET=utf8");

            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS JogadoresMatando (quemUUID VARCHAR(200),matouQuemUUID VARCHAR(200),matouQuemNome VARCHAR(30),quemNome VARCHAR(30),data TIMESTAMP,id_historico INTEGER,multiplo INTEGER) ENGINE=InnoDB  DEFAULT CHARSET=utf8");

            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS PlayerHistory (uuid VARCHAR(200),nome VARCHAR(30),id_historico INTEGER,equipe VARCHAR(20),armor VARCHAR(20),cartas TEXT) ENGINE=InnoDB  DEFAULT CHARSET=utf8");

            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS CardList (nome VARCHAR(50) PRIMARY KEY,armor VARCHAR(60),skillname VARCHAR(40),raridade VARCHAR(30),descr VARCHAR(200)) ENGINE=InnoDB  DEFAULT CHARSET=utf8");

            st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS ExtraInfos (uuid VARCHAR(200) PRIMARY KEY,nome VARCHAR(30),doublekills INTEGER,triplekills INTEGER,quadrakills INTEGER,pentakills INTEGER,firstbloods INTEGER) ENGINE=InnoDB  DEFAULT CHARSET=utf8");
            //conn.close();
        } catch (SQLException e) {
            CardWarsPlugin.log.log(Level.SEVERE,
                    "[MineDota] Connection closed");
            CardWarsPlugin.log.log(Level.SEVERE, "[MineDota] {0}", e);
        }
    }//99344599068 Lucinei rosa de moraes    1' 

    public static void updateName(final String uid, final String name) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    connection.createStatement().executeUpdate("UPDATE ExtraInfos set `nome` ='" + name + "' where `uuid` ='" + uid + "' and `nome` !='" + name + "'");
                    connection.createStatement().executeUpdate("UPDATE Jogadores set `nome` ='" + name + "' where `uuid` ='" + uid + "' and `nome` !='" + name + "'");
                    connection.createStatement().executeUpdate("UPDATE JogadoresMatando set `quemNome` ='" + name + "' where `quemUUID` ='" + uid + "' and `quemNome` !='" + name + "'");
                    connection.createStatement().executeUpdate("UPDATE JogadoresMatando set `matouQuemNome` ='" + name + "' where `matouQuemUUID` ='" + uid + "' and `matouQuemNome` !='" + name + "'");
                    connection.createStatement().executeUpdate("UPDATE shop set `nomevendedor` ='" + name + "' where `vendedor` ='" + uid + "' and `nomevendedor` !='" + name + "'");
                    connection.createStatement().executeUpdate("UPDATE Players set name = '" + name + "' where uid = '" + uid + "'");

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        }).start();

    }

    public static void reloadCards() {
        try {
            if (connection == null) {
                System.out.println("NULL NO CONEXU!");
            }
            st = connection.createStatement();
            st.executeUpdate("DELETE FROM CardList");
            //connection.commit();

            for (Carta c : ControleCartas.getCards()) {
                st = connection.createStatement();
                String desc = "";
                String[] oridesc = c.getDesc();
                for (String s : oridesc) {
                    desc += s;
                    if (!s.equals(oridesc[oridesc.length - 1])) {
                        desc += ";";
                    }
                }

                String armadura = c.getArmadura().nomebanco;

                st.executeUpdate("INSERT INTO CardList (`nome`,`armor`,`raridade`,`descr`,`skillname`) VALUES('" + c.getNome() + "','" + armadura + "','" + c.getRaridade().name() + "','" + desc + "','" + (c.getSkill() != null ? c.getSkill().getName() : "Sem Habilidade") + "')");
                //connection.commit();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MatchHistoryDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static int addPartida() {
        try {
            st = connection.createStatement();
            st.executeUpdate("INSERT INTO Historico_Partidas (`dataInicial`,`tipo`,`serverName`) VALUES(CURRENT_TIMESTAMP,'" + CardWarsPlugin.getArena().getDbName() + "','" + CardWarsPlugin.serverName + "')");
            //connection.commit();
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("select LAST_INSERT_ID() as id_historico");
            rs.next();
            int gameId = rs.getInt("id_historico");
            return gameId;
        } catch (SQLException ex) {
            Logger.getLogger(MatchHistoryDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public static void acabaPartida(Arena r, Arena.Team ganhadora) {
        try {
            String equipeganhadora = "";
            String perdedora = "";
            List<UUID> reduuids = r.getPlayers(Arena.Team.RED);
            for (UUID pl : reduuids) {
                updateExtraKills(pl);
                String v = "";
                if (pl != reduuids.get(reduuids.size() - 1)) {
                    v = ";";
                }
                if (Arena.Team.RED == ganhadora) {
                    equipeganhadora += Bukkit.getOfflinePlayer(pl).getName() + v;
                } else {
                    perdedora += Bukkit.getOfflinePlayer(pl).getName() + v;
                }
            }

            List<UUID> blueuuids = r.getPlayers(Arena.Team.BLUE);
            for (UUID pl : blueuuids) {

                updateExtraKills(pl);
                String v = "";
                if (pl != blueuuids.get(blueuuids.size() - 1)) {
                    v = ";";
                }
                if (Arena.Team.BLUE == ganhadora) {
                    equipeganhadora += Bukkit.getOfflinePlayer(pl).getName() + v;
                } else {
                    perdedora += Bukkit.getOfflinePlayer(pl).getName() + v;
                }
            }
            st = connection.createStatement();
            st.executeUpdate("UPDATE Historico_Partidas SET `dataFinal` =CURRENT_TIMESTAMP,timeGanhador ='" + equipeganhadora + "',timePerdedor ='" + perdedora + "' WHERE id_historico ='" + r.getGameId() + "'");
            //connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MatchHistoryDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void addKill(OfflinePlayer matou, OfflinePlayer morreu, Arena r, int mp) {
        try {
            st = connection.createStatement();
            st.executeUpdate("INSERT INTO JogadoresMatando (`quemUUID`,`quemNome`,`matouQuemUUID`,`matouQuemNome`,`data`,`id_historico`,`multiplo`) VALUES('" + matou.getUniqueId().toString() + "','" + matou.getName() + "','" + morreu.getUniqueId().toString() + "','" + morreu.getName() + "',CURRENT_TIMESTAMP,'" + r.getGameId() + "','" + mp + "')");
            //connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MatchHistoryDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    //st.executeUpdate("CREATE TABLE IF NOT EXISTS PlayerHistory (uuid VARCHAR(200),nome VARCHAR(30),id_historico INTEGER,equipe VARCHAR(20),armor VARCHAR(20),cartas TEXT)");

    public static void addPlayerHistory(Player p, Arena.Team equi, int hist_id, Armadura r) {
        try {
            st = connection.createStatement();
            st.executeUpdate("INSERT INTO PlayerHistory (`uuid`,`nome`,`id_historico`,`equipe`,`armor`,`cartas`) VALUES('" + p.getUniqueId().toString() + "','" + p.getName() + "','" + hist_id + "','" + equi.getName() + "','" + r.name() + "','" + getCards(ControleCartas.getCartas(p)) + "')");
            //connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MatchHistoryDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    //CREATE TABLE IF NOT EXISTS Jogadores (uuid VARCHAR(200),nome VARCHAR(30),vitorias INTEGER,derrotas INTEGER,kills INTEGER,mortes INTEGEr)");

    public static void addWin(UUID uuid) {
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT vitorias FROM Jogadores WHERE uuid='" + uuid.toString() + "'");
            rs.next();
            int jatem = rs.getInt("vitorias");
            st = connection.createStatement();
            st.executeUpdate("UPDATE Jogadores SET vitorias ='" + (jatem + 1) + "' where uuid ='" + uuid.toString() + "'");
            //connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MatchHistoryDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addMorte(UUID uuid) {
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT mortes FROM Jogadores WHERE uuid='" + uuid.toString() + "'");
            rs.next();
            int jatem = rs.getInt("mortes");
            st = connection.createStatement();
            st.executeUpdate("UPDATE Jogadores SET mortes ='" + (jatem + 1) + "' where uuid ='" + uuid.toString() + "'");
            //connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MatchHistoryDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addKill(UUID uuid) {
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT kills FROM Jogadores WHERE uuid='" + uuid.toString() + "'");
            if (!rs.next()) {
                return;
            }
            int jatem = rs.getInt("kills");
            st = connection.createStatement();
            st.executeUpdate("UPDATE Jogadores SET kills ='" + (jatem + 1) + "' where uuid ='" + uuid.toString() + "'");
            //connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MatchHistoryDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addLose(UUID uuid) {
        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT derrotas FROM Jogadores WHERE uuid='" + uuid.toString() + "'");
            rs.next();
            int jatem = rs.getInt("derrotas");
            st = connection.createStatement();
            st.executeUpdate("UPDATE Jogadores SET derrotas ='" + (jatem + 1) + "' where uuid ='" + uuid.toString() + "'");
            //connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MatchHistoryDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void startPlayer(UUID uuid) {
        try {
            st = connection.createStatement();
            if (!st.executeQuery("SELECT 1 FROM Jogadores WHERE uuid='" + uuid.toString() + "'").next()) {
                OfflinePlayer of = Bukkit.getOfflinePlayer(uuid);
                //    st.executeUpdate("CREATE TABLE IF NOT EXISTS ExtraInfos (uuid VARCHAR(200) PRIMARY KEY,nome VARCHAR(30),doublekills INTEGER,triplekills,INTEGER,quadrakills INTEGER,pentakills INTEGER,firstbloods INTEGER)");

                st = connection.createStatement();

                st.executeUpdate("INSERT INTO Jogadores (`uuid`,`nome`,`vitorias`,`derrotas`,`kills`,`mortes`) VALUES('" + uuid.toString() + "','" + of.getName() + "','0','0','0','0')");
                //connection.commit();
                st = connection.createStatement();

                st.executeUpdate("INSERT INTO ExtraInfos (`uuid`,`nome`,`doublekills`,`triplekills`,`quadrakills`,`pentakills`,`firstbloods`) VALUES('" + uuid.toString() + "','" + of.getName() + "','0','0','0','0','0')");
                //connection.commit();

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void updateExtraKills(UUID uuid) {
        if (MultipleKills.getKillsTypes(uuid) != null) {
            try {

                st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT doublekills,triplekills,quadrakills,pentakills,firstbloods FROM ExtraInfos WHERE uuid = '" + uuid.toString() + "'");

                if (rs.next()) {
                    st = connection.createStatement();
                }
                {
                    HashMap<KillType, Integer> kills = MultipleKills.getKillsTypes(uuid);
                    int doublekills = kills.get(KillType.DOUBLEKILL) + rs.getInt("doublekills");

                    int triplekills = kills.get(KillType.TRIPLEKILL) + rs.getInt("triplekills");

                    int quadrakills = kills.get(KillType.QUADRAKILL) + rs.getInt("quadrakills");

                    int pentakills = kills.get(KillType.PENTAKILL) + rs.getInt("pentakills");

                    int firstbloods = kills.get(KillType.FIRSTBLOOD) + rs.getInt("firstbloods");
                    st.executeUpdate("UPDATE ExtraInfos set doublekills ='" + doublekills + "',triplekills='" + triplekills + "',"
                            + "quadrakills='" + quadrakills + "',pentakills='" + pentakills + "',firstbloods='" + firstbloods + "' WHERE uuid = '" + uuid.toString() + "'");
                    //connection.commit();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();

            }

        }

    }

    public static String getCards(List<Carta> cartas) {
        String retorna = "";
        for (int x = 0; x < cartas.size(); x++) {
            Carta c = cartas.get(x);
            if (c != null) {
                retorna += c.getNome();
            } else {
                retorna += "Sem Carta";
            }
            if (x != (cartas.size() - 1)) {
                retorna += ",";
            }
        }
        return retorna;
    }

    public synchronized java.sql.Connection getConnection() {
        if (connection == null) {
            connection = CardWarsPlugin.conn;

        }

        return connection;
    }

}
