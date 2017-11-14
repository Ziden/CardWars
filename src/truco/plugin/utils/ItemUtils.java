/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.itens.CustomItem;
import truco.plugin.itens.Items;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ItemUtils {
    
    public static ItemStack pinta(ItemStack s, Color c) {
        if (s.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta cam = (LeatherArmorMeta) s.getItemMeta();
            cam.setColor(c);
            s.setItemMeta(cam);
            return s;
        }
        return s;
        
    }
    
    public static boolean containsInv(Player p, Material m, int qtd) {
        if (qtd == 0) {
            return true;
        }
        Inventory i = p.getInventory();
        int x = 0;
        for (ItemStack it : i.getContents()) {
            if (it != null && it.getType() == m && it.getItemMeta().getLore() == null && it.getItemMeta().getDisplayName() == null) {
                x += it.getAmount();
            }
        }
        return x >= qtd;
    }
    
    public static boolean isArm(ItemStack s) {
        return s == null || s.getType() == Material.AIR;
    }
    
    public static boolean isClaw(ItemStack s) {
        String c = CustomItem.getItem(s);
        if (c != null) {
            CustomItem ci = CustomItem.getCustomItem(c);
            if (ci != null) {
                return ci == Items.garraouro || ci == Items.garramadeira || ci == Items.garrapedra || ci == Items.garraferro;
            }
        }
        return false;
    }
    
    public static void consumeItem(Player p, Material m, int qtd) {
        Inventory inv = p.getInventory();
        if (qtd == 0) {
            return;
        }
        int x = 0;
        int falta = qtd - x;
        for (int y = 0; y < inv.getContents().length; y++) {
            ItemStack itm = inv.getItem(y);
            if (itm == null) {
                continue;
            }
            
            if (itm.getType() == m && itm.getItemMeta().getDisplayName() == null && itm.getItemMeta().getLore() == null) {
                if (x >= qtd) {
                    break;
                }
                if (itm.getAmount() > falta) {
                    itm.setAmount(itm.getAmount() - falta);
                    x += qtd;
                } else if (itm.getAmount() < falta) {
                    x += itm.getAmount();
                    inv.setItem(y, null);
                } else if (itm.getAmount() == falta) {
                    x += itm.getAmount();
                    inv.setItem(y, null);
                    
                }
                
            }
        }
        
    }
    
    public static boolean isAxe(Material m) {
        return m == Material.DIAMOND_AXE || m == Material.GOLD_AXE || m == Material.IRON_AXE || m == Material.STONE_AXE || m == Material.WOOD_AXE;
    }
    
    public static boolean isSword(Material m) {
        return m == Material.DIAMOND_SWORD || m == Material.GOLD_SWORD || m == Material.IRON_SWORD || m == Material.STONE_SWORD || m == Material.WOOD_SWORD;
    }
    
    public static boolean isSpade(Material m) {
        return m == Material.DIAMOND_SPADE || m == Material.GOLD_SPADE || m == Material.IRON_SPADE || m == Material.STONE_SPADE || m == Material.WOOD_SPADE;
    }
    
    public static boolean isArmor(Material s) {
        if (s == Material.IRON_BOOTS || s == Material.IRON_CHESTPLATE || s == Material.IRON_HELMET || s == Material.IRON_LEGGINGS) {
            return true;
        }
        if (s == Material.GOLD_BOOTS || s == Material.GOLD_CHESTPLATE || s == Material.GOLD_HELMET || s == Material.GOLD_LEGGINGS) {
            return true;
        }
        if (s == Material.CHAINMAIL_BOOTS || s == Material.CHAINMAIL_CHESTPLATE || s == Material.CHAINMAIL_HELMET || s == Material.CHAINMAIL_LEGGINGS) {
            return true;
        }
        if (s == Material.DIAMOND_BOOTS || s == Material.DIAMOND_CHESTPLATE || s == Material.DIAMOND_HELMET || s == Material.DIAMOND_LEGGINGS) {
            return true;
        }
        return s == Material.LEATHER_BOOTS || s == Material.LEATHER_CHESTPLATE || s == Material.LEATHER_HELMET || s == Material.LEATHER_LEGGINGS;
    }
    
    public static void consumeItemInHand(Player p) {
        if (p.getItemInHand().getAmount() > 1) {
            p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
        } else {
            p.setItemInHand(null);
        }
    }
    
    public static void addCarta(Player p, ItemStack carta) {
        if (p.getInventory().firstEmpty() != -1) {
            p.getInventory().addItem(carta);
            p.updateInventory();
        } else {
            p.sendMessage("§7§oComo seu inventário está lotado a carta foi adicionada ao estoque!");
            CardsDB.addCard(p.getUniqueId(), carta);
        }
    }
    
    public static ItemStack getHead(String name) {
        ItemStack s = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta sm = (SkullMeta) s.getItemMeta();
        sm.setOwner(name);
        s.setItemMeta(sm);
        return s;
    }
}
