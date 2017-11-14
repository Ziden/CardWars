/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.managers.maniainventorysync.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Gabriel
 */
public class ConfigManager {

    private File dbFile;
    private FileConfiguration config = new YamlConfiguration();

    public ConfigManager(String arquivo) throws IOException, FileNotFoundException, InvalidConfigurationException {
        dbFile = new File(arquivo);
        if (!dbFile.exists()) {
            dbFile.getParentFile().mkdirs();
            dbFile.createNewFile();
        }
        config.load(dbFile);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public String GetString(String node) {
        return config.getString(node);
    }

    public void SaveConfig() {
        try {
            this.config.save(dbFile);
        } catch (IOException ex) {
            Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void init(String node, Object value) {
        if (!config.contains(node)) {
            config.set(node, value);
            SaveConfig();
        }
    }
}
