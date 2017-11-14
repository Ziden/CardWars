/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.arena;



import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.WeakHashMap;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;


/**
 *
 * @author usuario
 */
public class DBArena {



    private static WeakHashMap<Player, String> buffer = new WeakHashMap<>();
    public static Connection conn = null;


    public static void createTables() {
	try {
	    conn.createStatement().execute("CREATE TABLE IF NOT EXISTS arenas (name VARCHAR(30) PRIMARY KEY,type VARCHAR(15),redspawn VARCHAR(200),bluespawn VARCHAR(200))");
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
    }


    public static void startDatabase() {
	if (conn == null) {
	    createConnection();
	    createTables();
	}

    }


    public static void createConnection() {
	try {
	    try {
		Class.forName("org.sqlite.JDBC");
	    } catch (ClassNotFoundException ex) {
		ex.printStackTrace();
	    }
	    conn = DriverManager.getConnection("jdbc:sqlite:" + CardWarsPlugin._instance.getDataFolder().getPath() + File.separator + "ArenasDB.db");
	    conn.setAutoCommit(true);
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
    }




}
