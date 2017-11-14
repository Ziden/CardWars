package truco.plugin.data;

import truco.plugin.CardWarsPlugin;

/**
 *
 * @author Gabriel
 */
public class ConfGlobalManager {

    public static ConfigManager ConfGlobal;
    public static ConfigManager mysqlConf;

    public static void LoadConf() {
        try {
            mysqlConf = new ConfigManager(CardWarsPlugin._instance.getDataFolder() + "/mysql.yml");

            ConfGlobal = new ConfigManager(CardWarsPlugin._instance.getDataFolder() + "/config.yml");
            // Globais
            init("ServerType", "LOBBY");
            init("MatchMaking", "false");
            initsq("mysqlconn", "jdbc:mysql://localhost:3306/database");
            initsq("mysqluser", "batata");
            initsq("mysqlpass", "batata");
            mysqlConf.SaveConfig();
            ConfGlobal.SaveConfig();
        } catch (Throwable ex) {
            CardWarsPlugin.log.info("Error to load the Global config file");
        }
    }

    public static String getString(String node) {
        return ConfGlobal.getConfig().getString(node);
    }

    public static String getStringSq(String node) {
        return mysqlConf.getConfig().getString(node);
    }

    public static void init(String node, Object value) {
        if (!ConfGlobal.getConfig().contains(node)) {
            ConfGlobal.getConfig().set(node, value);
        }
        ConfGlobal.SaveConfig();
    }

    public static void initsq(String node, Object value) {
        if (!mysqlConf.getConfig().contains(node)) {
            mysqlConf.getConfig().set(node, value);
        }
        mysqlConf.SaveConfig();
    }

}
