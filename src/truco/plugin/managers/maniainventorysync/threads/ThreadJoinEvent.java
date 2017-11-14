/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.managers.maniainventorysync.threads;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.listeners.LobbyListener;
import truco.plugin.managers.maniainventorysync.listeners.GeralListener;
import truco.plugin.managers.maniainventorysync.mysql.Mysqlcontrol;
import truco.plugin.utils.PlayerUtils;

/**
 *
 * @author Gabriel
 */
public class ThreadJoinEvent extends Thread {

    Player p;

    public ThreadJoinEvent(Player pl) {
        this.p = pl;
    }

    @Override
    public void run() {
        if (!Mysqlcontrol.TemSave(p)) {

            Mysqlcontrol.InsertInfo(p);
            GeralListener.esperandoLoad.remove(p.getUniqueId());

        } else {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadJoinEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
            Mysqlcontrol.RestauraInfo(p);

            GeralListener.esperandoLoad.remove(p.getUniqueId());
        }
    }
}
