/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truco.plugin.cards.skills.skilltypes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import truco.plugin.utils.ChatUtils;
import truco.plugin.data.MetaShit;

/**
 *
 * @author usuario
 */
public class TimedHit {
    
    public static TimedHit hit = new TimedHit();
    
    public boolean fazTimedHit(Player p, String nomeGolpe, int delay, int tempos) {
        
        if (p.hasMetadata("timed")) {
            ChatUtils.sendMessage(p,ChatColor.RED+"Voce esta preparando outro movimento");
            return false;
        }
        ChatUtils.sendMessage(p,ChatColor.GREEN+"Voce comecou a preparar "+nomeGolpe);
        Timed t = new Timed(p, delay, tempos, nomeGolpe);
        t.taskId =  Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, t, delay, delay);
        MetaShit.setMetaObject("timed",p,t);
        return true;
    }
    
    public boolean acertaTimed(Player p, String nomeGolpe) {
         if(p.hasMetadata("timed")) {
            Timed t = (Timed)MetaShit.getMetaObject("timed",p);
            if(!t.golpe.equalsIgnoreCase(nomeGolpe))
                return false;
            if(p.hasMetadata("timedCerto")) {
                Bukkit.getScheduler().cancelTask(t.taskId);
                p.removeMetadata("timed", CardWarsPlugin._instance);
                p.removeMetadata("timedCerto", CardWarsPlugin._instance);
                return true;
            } else {
                Bukkit.getScheduler().cancelTask(t.taskId);
                p.sendMessage(ChatColor.RED+"Voce afobou "+nomeGolpe+" !");
                p.removeMetadata("timed", CardWarsPlugin._instance);
                p.removeMetadata("timedCerto", CardWarsPlugin._instance);
                return false;
            }
         }
         return false;
    }
 
    public boolean playerAfobou(Player p, String nomeGolpe) {
        if(p.hasMetadata("timed")) {
            Timed t = (Timed)MetaShit.getMetaObject("timed",p);
            if(!t.golpe.equalsIgnoreCase(nomeGolpe))
                return false;
            Bukkit.getScheduler().cancelTask(t.taskId);
            ChatUtils.sendMessage(p,ChatColor.RED+"Voce afobou o golpe !");
            p.removeMetadata("timed", CardWarsPlugin._instance);
            p.removeMetadata("timedCerto", CardWarsPlugin._instance);
            return true;
        }
        return false;
    }
    
    public class Timed implements Runnable {
        
        public String golpe;
        public int delay;
        public int maxTimes;
        public int dur;
        public Player p;
        public int taskId;
        public int times = 0;
        
        public Timed(Player p,int delay, int maxTimes, String golpe) {
            this.p = p;
            this.delay=delay;
            this.maxTimes=maxTimes;
            this.golpe = golpe;
        }
        
        public void run() {
            times++;
            if(times == maxTimes) {
                p.sendMessage(ChatColor.GREEN+"Já!");
                MetaShit.setMetaObject("timedCerto", p, "1");
            } else if(times > maxTimes) {
                p.sendMessage(ChatColor.RED+"Voce demorou demais !");
                p.removeMetadata("timed", CardWarsPlugin._instance);
                p.removeMetadata("timedCerto", CardWarsPlugin._instance);
                Bukkit.getScheduler().cancelTask(taskId);
            } else {
                p.sendMessage(ChatColor.YELLOW+""+times);
            }
        }
    }

}


