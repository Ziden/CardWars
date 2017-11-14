/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.managers.maniainventorysync.threads;

import org.bukkit.entity.Player;
import truco.plugin.managers.maniainventorysync.mysql.Mysqlcontrol;

/**
 *
 * @author Gabriel
 */
public class ThreadQuitEvent extends Thread {

    Player p;

    public ThreadQuitEvent(Player pl) {
        this.p = pl;
    }

    @Override
    public void run() {
        Mysqlcontrol.UpdateInfo(p);
    }
}
