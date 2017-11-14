/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.managers.maniainventorysync.threads;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import truco.plugin.managers.maniainventorysync.mysql.Mysqlcontrol;

/**
 *
 * @author Gabriel
 */
public class ThreadSalvaTodos extends Thread {

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Mysqlcontrol.UpdateInfo(p);
        }
    }

}
