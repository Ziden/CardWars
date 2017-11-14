/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.functions.elo;

import org.bukkit.ChatColor;
import truco.plugin.utils.IconLib;

/**
 *
 * @author usuario
 */
public class Ligas {

    public static String c = IconLib.PLAYERNORMAL + "";

    public static String getLiga(int elo) {
        return getCor(elo) + c;
    }

    public static ChatColor getCor(int elo) {
        if (elo > 15000) {
            return ChatColor.DARK_RED;//INSANO
        } else if (elo > 8000) {
            return ChatColor.DARK_PURPLE;//ENDER
        } else if (elo > 7000) {
            return ChatColor.RED;//NETHER
        } else if (elo > 6000) {
            return ChatColor.BLUE;//OBSIDIAN
        } else if (elo > 5500) {
            return ChatColor.AQUA;//diamante
        } else if (elo > 5000) {
            return ChatColor.GREEN;//esmeralda
        } else if (elo > 4000) {
            return ChatColor.GOLD;//Ouro    
        } else if (elo > 3300) {
            return ChatColor.GRAY;//ferro
        } else if (elo >= 3000) {
            return ChatColor.DARK_AQUA;//pedra
        } else {
            return ChatColor.YELLOW;//noob
        }
    }

}
