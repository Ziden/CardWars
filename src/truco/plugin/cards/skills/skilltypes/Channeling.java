/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cards.skills.skilltypes;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.data.MetaShit;
import truco.plugin.utils.Utils;

/**
 *
 * @author usuario
 */
public class Channeling {
    
    public static HashMap<UUID, Integer> conjurando = new HashMap();
    
    public static void playEffeitos() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {
            
            @Override
            public void run() {
                for (UUID uuid : conjurando.keySet()) {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p != null) {
                        p.getWorld().playEffect(p.getLocation().add(0, 1.4, 0), Effect.MAGIC_CRIT, 1);
                    }
                }
            }
        }, 5, 5);
        
    }
    
    public static void disrupt(Player p) {
        if (!p.hasMetadata("Channeling")) {
            return;
        }
        for (Entity e : p.getNearbyEntities(10, 10, 10)) {
            if (e.getType() == EntityType.PLAYER) {
                ((Player) e).sendMessage(p.getName() + " perdeu a concentracao");
            }
        }
        p.sendMessage(ChatColor.RED + "Voce perdeu sua concentracao");
        int taskId = (int) MetaShit.getMetaObject("Channeling", p);
        Bukkit.getScheduler().cancelTask(taskId);
        p.removeMetadata("Channeling", CardWarsPlugin._instance);
        if (conjurando.containsKey(p.getUniqueId())) {
            conjurando.remove(p.getUniqueId());
        }
    }
    
    public static boolean checkAlvo(long morreu, Player p,Player tentando) {
        if (p == null) {
            tentando.sendMessage("§cAlvo deslogou enquanto você tentava conjurar!");
            return false;
        }
        if (morreu != Utils.getDeathMillis(p)) {
            tentando.sendMessage("§cAlvo morreu enquando você tentava conjurar a habiliade!");
            return false;
        }
        return true;
    }

    public Channeling(final Player p, int seconds, Skill s, final Runnable acao) {
        if (p.hasMetadata("Channeling")) {
            return;
        }
        p.sendMessage(ChatColor.GREEN + "Começou a preparar " + s.getName() + " aguarde §c" + seconds + " segundos!");
        final UUID uid = p.getUniqueId();
        int taskid = Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {
            
            @Override
            public void run() {
                acao.run();
                if (conjurando.containsKey(uid)) {
                    conjurando.remove(uid);
                    p.removeMetadata("Channeling", CardWarsPlugin._instance);
                }
            }
        }, seconds * 20);
        conjurando.put(p.getUniqueId(), taskid);
        for (Entity e : p.getNearbyEntities(10, 10, 10)) {
            if (e.getType() == EntityType.PLAYER) {
                ((Player) e).sendMessage("§a" + p.getName() + " comecou a preparar " + s.getName());
            }
        }
        
        MetaShit.setMetaObject("Channeling", p, taskid);
        
    }
    
}
