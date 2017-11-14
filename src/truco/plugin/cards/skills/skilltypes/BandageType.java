/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cards.skills.skilltypes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import truco.plugin.data.MetaShit;

/**
 *
 * @author usuario
 */
public class BandageType {

    public static boolean applyBandage(Player p) {
        if (p.hasMetadata("bandagem")) {
            p.sendMessage("§cVocê só pode aplicar uma bandagem por vez!");
            return false;
        }
        p.sendMessage(ChatColor.GREEN + "Voce comecou a aplicar as bandagens");
        for (Entity e : p.getNearbyEntities(15, 15, 15)) {
            if (e.getType() == EntityType.PLAYER) {
                ((Player) e).sendMessage(ChatColor.GREEN + p.getName() + " comecou aplicar bandagens");
            }
        }
        Bands b = new Bands(p, 7, 10);
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, b, 20, 20);
        b.taskId = taskId;
        MetaShit.setMetaObject("bandagem", p, b);
        return true;
    }

    public static class Bands implements Runnable {

        public int taskId = 0;
        public int numero = 0;
        public int numeroMax = 5;
        public double cura = 0;
        Player p;

        public void run() {
            if (numero >= numeroMax) {
                if (p == null) {
                    stop();
                    return;
                }
                p.sendMessage(ChatColor.GREEN + "Voce terminou de aplicar as bandagens");
                if (p.getHealth() + cura > p.getMaxHealth()) {
                    p.setHealth(p.getMaxHealth());
                } else {
                    p.setHealth(p.getHealth() + cura);
                }
                stop();
                CardWarsPlugin.log.info("cancelei task id " + taskId);
            } else {
                numero++;
                p.sendMessage(ChatColor.GREEN + "Aplicando as bandagens em " + (numeroMax - numero));

            }
        }

        public Bands(Player p, int segundos, double cura) {
            this.p = p;
            this.numeroMax = segundos;
            this.cura = cura;
        }

        public void stop() {
            Bukkit.getScheduler().cancelTask(taskId);
            p.removeMetadata("bandagem", CardWarsPlugin._instance);
        }
    }

}
