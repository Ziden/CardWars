package truco.plugin.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.functions.Cooldown;
import truco.plugin.managers.PlayerManager;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.ItemUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ArmorSelectMenu extends Menu {
    
    public static String titulo = "§c§lArmor Selector";
    public static List<Armadura> menui = new ArrayList<Armadura>();
    public static String menuname = "Armor";
    
    public static String traduzItem(Material m) {
        String resultado = m.name();
        String nome = m.name();

// Ouro //
        if (nome.equalsIgnoreCase("gold_boots")) {
            resultado = "Botas de Ouro";
        } else if (nome.equalsIgnoreCase("gold_leggings")) {
            resultado = "Calças de Ouro";
        } else if (nome.equalsIgnoreCase("gold_chestplate")) {
            resultado = "Peitoral de Ouro";
        } // Malha //
        else if (nome.equalsIgnoreCase("chainmail_boots")) {
            resultado = "Botas de Malha";
        } else if (nome.equalsIgnoreCase("chainmail_leggings")) {
            resultado = "Calças de Malha";
        } else if (nome.equalsIgnoreCase("chainmail_chestplate")) {
            resultado = "Peitoral de Malha";
        } // Couro //
        else if (nome.equalsIgnoreCase("leather_boots")) {
            resultado = "Botas de Couro";
        } else if (nome.equalsIgnoreCase("leather_leggings")) {
            resultado = "Calças de Couro";
        } else if (nome.equalsIgnoreCase("leather_chestplate")) {
            resultado = "Peitoral de Couro";
        } // Ferro //
        else if (nome.equalsIgnoreCase("iron_boots")) {
            resultado = "Botas de Ferro";
        } else if (nome.equalsIgnoreCase("iron_leggings")) {
            resultado = "Calças de Ferro";
        } else if (nome.equalsIgnoreCase("iron_chestplate")) {
            resultado = "Peitoral de Ferro";
        } // diamante //
        else if (nome.equalsIgnoreCase("diamond_boots")) {
            resultado = "Botas de Diamante";
        } else if (nome.equalsIgnoreCase("diamond_leggings")) {
            resultado = "Calças de Diamante";
        } else if (nome.equalsIgnoreCase("diamond_chestplate")) {
            resultado = "Peitoral de Diamante";
        }
        
        return resultado;
    }
    
    public ArmorSelectMenu() {
        super(menuname, MenuType.AMBOS);
    }
    
    @Override
    public void closeInventory(Player p, Inventory s) {
    }
    
    @Override
    public void openInventory(Player p) {
        if (Cooldown.isCooldown(p, "Trocar armadura")) {
            p.sendMessage("§c§lEspere um pouco para trocar de armadura novamente!");
            return;
        }
        Cooldown.addCoolDown(p,"Trocar armadura",10000);
        Inventory i = Bukkit.createInventory(p, 9, titulo);
        
        for (Armadura a : Armadura.values()) {
            i.addItem(a.getDisplayItem(p));
        }
        p.openInventory(i);
        
    }
    
    @Override
    public void clickInventory(InventoryClickEvent ev) {
        
        if (ev.getInventory().getTitle().equalsIgnoreCase(titulo)) {
            ev.setCancelled(true);
            if (ev.getCurrentItem() == null) {
                return;
            }
            final Player p = (Player) ev.getWhoClicked();
            final Armadura arm = Armadura.getArmor(ev.getCurrentItem());
            if (arm != null) {
                PlayerManager.setArmor(p, arm);
                
            }
        }
    }
    
    public static enum Armadura {
        
        DIAMOND(new ItemStack[]{
            new ItemStack(Material.DIAMOND_BOOTS),
            new ItemStack(Material.DIAMOND_LEGGINGS),
            new ItemStack(Material.DIAMOND_CHESTPLATE), //  new ItemStack(Material.AIR)
        }, "Armadura de Diamante", Material.DIAMOND_CHESTPLATE, false,
                Arrays.asList(new Carta.Armadura[]{Carta.Armadura.DIMA, Carta.Armadura.FERRO_DIMA, Carta.Armadura.OURO_DIMA}), "Aguentar pohhada"),
        IRON(new ItemStack[]{
            new ItemStack(Material.IRON_BOOTS),
            new ItemStack(Material.IRON_LEGGINGS),
            new ItemStack(Material.IRON_CHESTPLATE), //  new ItemStack(Material.AIR)
        }, "Armadura de Ferro", Material.IRON_CHESTPLATE, true,
                Arrays.asList(new Carta.Armadura[]{Carta.Armadura.FERRO, Carta.Armadura.FERRO_DIMA, Carta.Armadura.CHAIN_FERRO}), "Dar dano em curta distancia e ainda aguentar pohhada"),
        GOLD(new ItemStack[]{
            new ItemStack(Material.GOLD_BOOTS),
            new ItemStack(Material.GOLD_LEGGINGS),
            new ItemStack(Material.GOLD_CHESTPLATE), // new ItemStack(Material.AIR)
        }, "Armadura de Ouro", Material.GOLD_CHESTPLATE, false,
                Arrays.asList(new Carta.Armadura[]{Carta.Armadura.OURO, Carta.Armadura.OURO_DIMA, Carta.Armadura.OURO_LEATHER}), "Conceder suporte para seus aliados"),
        CHAIN(new ItemStack[]{
            new ItemStack(Material.CHAINMAIL_BOOTS),
            new ItemStack(Material.CHAINMAIL_LEGGINGS),
            new ItemStack(Material.CHAINMAIL_CHESTPLATE), // new ItemStack(Material.AIR)
        }, "Armadura de Malha", Material.CHAINMAIL_CHESTPLATE, true,
                Arrays.asList(new Carta.Armadura[]{Carta.Armadura.CHAIN, Carta.Armadura.CHAIN_FERRO, Carta.Armadura.CHAIN_LEATHER}), "Dar dano a longa distancia"),
        LEATHER(new ItemStack[]{
            new ItemStack(Material.LEATHER_BOOTS),
            new ItemStack(Material.LEATHER_LEGGINGS),
            new ItemStack(Material.LEATHER_CHESTPLATE), // new ItemStack(Material.AIR)
        }, "Armadura de Couro", Material.LEATHER_CHESTPLATE, true,
                Arrays.asList(new Carta.Armadura[]{Carta.Armadura.LEATHER, Carta.Armadura.LEATHER_PELADO, Carta.Armadura.CHAIN_LEATHER, Carta.Armadura.OURO_LEATHER}), "Dar dano a media distancia"),
        PELADO(new ItemStack[]{ // new ItemStack(Material.AIR),
        // new ItemStack(Material.AIR),
        // new ItemStack(Material.AIR),
        //  new ItemStack(Material.AIR)
        }, "Pelado", Material.SKULL_ITEM, false,
                Arrays.asList(new Carta.Armadura[]{Carta.Armadura.PELADO, Carta.Armadura.LEATHER_PELADO,}), "Dar dano em curta distancia");
        
        ItemStack[] armor;
        String name;
        Material itemd;
        boolean padrao;
        List<Carta.Armadura> armadurac;
        String funcionalidade;
        
        public ItemStack getDisplayItem(Player p) {
            ItemStack i = new ItemStack(itemd);
            ItemMeta im = i.getItemMeta();
            if (itemd == Material.SKULL_ITEM) {
                ((SkullMeta) im).setOwner(p.getName());
                i.setDurability((short) 3);
            }
            
            im.setDisplayName("§a§l" + name);
            String s = "";
            ArrayList<String> lore = new ArrayList<>();
            lore.add("§d[FUNCAO] " + funcionalidade);
            lore.add("§r§6Armor:");
            for (ItemStack it : armor) {
                if (it.getType() != Material.AIR) {
                    lore.add("§r" + ChatColor.AQUA + traduzItem(it.getType()));
                }
            }
            s = s.trim();
            im.setLore(lore);
            i.setItemMeta(im);
            return i;
        }
        
        public ItemStack[] getArmorContents() {
            ItemStack[] armadura = new ItemStack[3];
            for (int x = 0; x < armor.length; x++) {
                ItemStack item = armor[x];
                ItemMeta im = item.getItemMeta();
                im.setLore(Arrays.asList("§0:armor:"));
                item.setItemMeta(im);
                armadura[x] = item;
            }
            return armadura;
        }
        
        public static Armadura getArmor(ItemStack i) {
            if (i == null || i.getItemMeta() == null) {
                return null;
            }
            for (Armadura ar : Armadura.values()) {
                if (ar.name.equalsIgnoreCase(ChatColor.stripColor(i.getItemMeta().getDisplayName()))) {
                    return ar;
                }
            }
            return null;
        }
        
        private Armadura(ItemStack[] items, String nome, Material display, boolean p, List<Carta.Armadura> ar, String funcionalidade) {
            armor = items;
            name = nome;
            List<Carta.Armadura> arli = new ArrayList<>();
            for (Carta.Armadura armt : ar) {
                arli.add(armt);
            }
            armadurac = arli;
            itemd = display;
            this.funcionalidade = funcionalidade;
            padrao = p;
            menui.add(this);
        }
        
        public String getName() {
            return name;
        }
        
    }
    
    public static boolean canEquip(Armadura arm, Player p) {
        return true;
    }
    
}
