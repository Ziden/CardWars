package truco.plugin.arena;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import truco.plugin.CardWarsPlugin;
import truco.plugin.arena.DeathMatch.TeamDeathMatch;
import truco.plugin.arena.Dominion.Dominion;
import truco.plugin.arena.KomQuista.KomQuista;
import truco.plugin.utils.LocUtils;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 *
 */

public class ArenaSave {

    public static void loadArenas() {
        try {
            ResultSet rs = DBArena.conn.createStatement().executeQuery("SELECT * FROM arenas");
            while (rs.next()) {
                String type = rs.getString("type");
                Location bluespawn = LocUtils.stringToLocation(rs.getString("bluespawn"));
                Location redspawnspawn = LocUtils.stringToLocation(rs.getString("redspawn"));

                if (type.equals("tdm")) {
                    CardWarsPlugin.mainarena = new TeamDeathMatch(rs.getString("name"), redspawnspawn, bluespawn);

                    break;
                } else if (type.equalsIgnoreCase("dominio")) {
                    if (redspawnspawn != null && bluespawn != null) {
                        CardWarsPlugin.mainarena = new Dominion(rs.getString("name"), redspawnspawn, bluespawn);
                        break;
                    } else {
                        new Dominion(rs.getString("name"));
                    }
                }else if (type.equalsIgnoreCase("komquista")) {
                    if (redspawnspawn != null && bluespawn != null) {
                        CardWarsPlugin.mainarena = new KomQuista(rs.getString("name"), redspawnspawn, bluespawn);
                        break;
                    } else {
                        new KomQuista(rs.getString("name"));
                    }

                }
            }
        } catch (SQLException ex) {
            CardWarsPlugin.log.info("ERRO NO SAVE DA ARENA:"+ex.getMessage());
            ex.printStackTrace();
        }

    }

    public static boolean containsSaveArena(Arena r) {
        try {
            ResultSet rs = DBArena.conn.createStatement().executeQuery("SELECT * FROM arenas WHERE name ='" + r.getName() + "'");
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(ArenaSave.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static void saveArena(Arena r) {
        try {
            String g = "";
            if (containsSaveArena(r)) {
                g = "UPDATE arenas set `bluespawn`='" + LocUtils.locationToString(r.getBluespawn()) + "',`redspawn`='" + LocUtils.locationToString(r.getRedspawn()) + "'" + " WHERE `name` = '" + r.getName() + "'";

            } else {
                g = "INSERT INTO arenas (name,type,bluespawn,redspawn) VALUES('" + r.getName() + "','" + r.getDbName() + "','" + LocUtils.locationToString(r.getBluespawn()) + "','" + LocUtils.locationToString(r.getRedspawn()) + "')";
            }
            DBArena.conn.createStatement().execute(g);
        } catch (SQLException ex) {
            Logger.getLogger(ArenaSave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void removeArena(Arena r) {
        if (containsSaveArena(r)) {
            try {
                DBArena.conn.createStatement().execute("DELETE FROM arenas WHERE name='" + r.getName() + "'");
            } catch (SQLException ex) {
                Logger.getLogger(ArenaSave.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (Arena.todasarenas.contains(r)) {
                Arena.todasarenas.remove(r);
            }

        }

    }

}
