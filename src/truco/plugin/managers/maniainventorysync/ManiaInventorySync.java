/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.managers.maniainventorysync;


import java.util.logging.Logger;

import org.bukkit.Bukkit;
import truco.plugin.CardWarsPlugin;
import truco.plugin.managers.maniainventorysync.listeners.GeralListener;
import truco.plugin.managers.maniainventorysync.mysql.Mysqlcontrol;
import truco.plugin.managers.maniainventorysync.threads.ThreadSalvaTodos;

/**
 *
 * @author Gabriel
 */
public class ManiaInventorySync {

    public static final Logger log = Logger.getLogger("Minecraft");

    public static void onEnable(CardWarsPlugin instance) {

        Mysqlcontrol.InitMysql();
        Bukkit.getServer().getPluginManager().registerEvents(new GeralListener(), instance);
        Runnable r = new Runnable() {

            @Override
            public void run() {
                new Thread(new ThreadSalvaTodos()).start();
            }
        };
        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, r, (long) (20 * 60) * 10, (long) (20 * 60) * 10);// Salva todos de 10 em 10 minutos
    }

    
    public static void onDisable() {
        new Thread(new ThreadSalvaTodos()).run();
    }

}
