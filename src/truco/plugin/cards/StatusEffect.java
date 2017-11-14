/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cards;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import truco.plugin.CardWarsPlugin;
import truco.plugin.damage.DamageManager;
import truco.plugin.data.MetaShit;
import truco.plugin.functions.Cooldown;

/**
 *
 * @author usuario
 */
// status effect eh um bagui que vem e sai rapidin
// se for eterno (efeito eterno de cartas e talz) usar o ControleCartas.addStatus()
public class StatusEffect {

    public enum StatusMod {

        SILENCE("Silence"),
        STUN("Stun"),
        CONGELADO("Congelado"),
        SNARE("Snare"),
        MOLHADO("Molhado"),
        ELETRIZADO("Eletrizado");
        public String desc;

        private StatusMod(String desc) {
            this.desc = desc;
        }

    }

    private static HashMap<StatusMod, HashSet<UUID>> affected = new HashMap<StatusMod, HashSet<UUID>>();

    public StatusEffect() {
        for (StatusMod m : StatusMod.values()) {
            affected.put(m, new HashSet<UUID>());
        }
        Runnable r = new Runnable() {
            public void run() {
                playStatusEffectAnimations();
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, r, 20, 20);
    }

    public static boolean hasStatusEffect(Player p, StatusMod effect) {
        return affected.get(effect).contains(p.getUniqueId());
    }

    public static void removeStatusEffect(Player p, StatusMod effect) {
        if (affected.get(effect).contains(p.getUniqueId())) {
            affected.get(effect).remove(p.getUniqueId());
            if (effect == StatusMod.CONGELADO) {
                if (p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getType() == Material.ICE) {
                    p.getInventory().setHelmet(null);
                }
               
            }
            int taskId = (int) MetaShit.getMetaObject("Task" + effect.desc, p);
            Bukkit.getScheduler().cancelTask(taskId);
            p.removeMetadata("Task" + effect.desc, CardWarsPlugin._instance);
        }
    }

    public static void playStatusEffectAnimations() {
        for (StatusMod m : affected.keySet()) {
            for (UUID u : affected.get(m)) {
                Player p = Bukkit.getPlayer(u);
                if (p != null) {
                    if (m == m.SILENCE) {
                        p.getWorld().playEffect(p.getLocation().add(0, 1.1, 0), Effect.VILLAGER_THUNDERCLOUD, 1);
                    } else if (m == m.STUN) {
                        p.getWorld().playEffect(p.getLocation().add(0, 1.1, 0), Effect.INSTANT_SPELL, 1);
                        p.getWorld().playEffect(p.getLocation().add(0, 1.8, 0), Effect.INSTANT_SPELL, 1);
                    } else if (m == m.MOLHADO) {
                        p.getWorld().playEffect(p.getLocation().add(0, 0.6, 0), Effect.WATERDRIP, 1);
                    } else if (m == m.CONGELADO) {
                        p.getWorld().playEffect(p.getLocation().add(0, 0.6, 0), Effect.HEART, 1);
                    }
                }
            }
        }
    }

    public static void addStatusEffect(final Player p, final StatusMod effect, int seconds) {
        if (Cooldown.isCooldown(p, "nodamage")) {
            return;
        }
        affected.get(effect).add(p.getUniqueId());
        p.sendMessage(ChatColor.BLUE + "+ " + effect.desc);
        DamageManager.dim.showDamageIndicator(p, "§e§l+ " + effect.desc);
        // se tomou o mermo efeito tira o antigo pra nao remover
        if (p.hasMetadata("Task" + effect.desc)) {
            int taskId = (int) MetaShit.getMetaObject("Task" + effect.desc, p);
            Bukkit.getScheduler().cancelTask(taskId);
        }
        Runnable r = new Runnable() {
            public void run() {
                affected.get(effect).remove(p.getUniqueId());
                p.sendMessage(ChatColor.BLUE + "- " + effect.desc);
                p.removeMetadata("Task" + effect.desc, CardWarsPlugin._instance);
                // removing effects
                if (effect == StatusMod.CONGELADO) {
                    if (p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getType() == Material.ICE) {
                        p.getInventory().setHelmet(null);
                    }
                  
                }
            }
        };
        int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, r, seconds * 20);
        MetaShit.setMetaObject("Task" + effect.desc, p, taskId);

        // special effects
        if (effect == StatusMod.CONGELADO) {
            if (p.getInventory().getHelmet() == null) {
                p.getInventory().setHelmet(new ItemStack(Material.ICE));

            }
            
            p.teleport(new Location(p.getWorld(), p.getLocation().getBlockX() + 0.5, p.getLocation().getBlockY(), p.getLocation().getBlockZ() + 0.5));
        }
    }

}
