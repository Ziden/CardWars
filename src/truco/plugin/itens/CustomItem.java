package truco.plugin.itens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import truco.plugin.menus.MenuUtils;
import truco.plugin.cards.skills.skilltypes.CustomPotion;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CustomItem {

    protected String nome = null;
    protected Material ma = null;
    protected List<String> lore = null;
    protected String descr = null;
   protected ChatColor color = null;
   protected char simbolo = ' ';
    protected ItemRaridade raridade;
   protected static HashMap<String, CustomItem> allItems = new HashMap();
    int data = 0;
    public Color corfoguete = null;

    public static int getSlots(int s) {
        if (s % 9 == 0) {
            return s;
        }
        int x = s;
        while (x % 9 != 0) {
            x++;
        }
        return x;

    }

    public static void openInv(Player p) {
        int size = allItems.size() + CustomPotion.potions.size();
        Inventory i = Bukkit.createInventory(p, getSlots(size), "§7Itens");
        for (CustomItem c : CustomItem.allItems.values()) {
            i.addItem(c.geraItem(1));
        }
        for (CustomPotion cp : CustomPotion.potions.values()) {
            i.addItem(cp.toItemStack());
        }
        p.openInventory(i);

    }

    public static enum ItemRaridade {

        COMUM(ChatColor.YELLOW), INCOMUM(ChatColor.GREEN), RARO(ChatColor.BLUE), LENDARIO(ChatColor.GOLD), EPICO(ChatColor.DARK_PURPLE);
        ChatColor cor;

        public ChatColor getCor() {
            return cor;
        }

        private ItemRaridade(ChatColor cor) {
            this.cor = cor;
        }

    }

    public CustomItem(String nomes, Material mat, String desc, ChatColor cor, char simbol, ItemRaridade raridade) {
        this.nome = nomes;
        this.ma = mat;
        this.descr = desc;
        color = cor;
        this.lore = new ArrayList<>();
        this.lore.add("§0§k:" + nome);
        this.lore.add("§r§7- " + desc);
        simbolo = simbol;
        allItems.put(nomes, this);
        this.raridade = raridade;
    }

    public CustomItem(String nomes, Material mat, String desc, ChatColor cor, char simbol, int data, ItemRaridade raridade) {
        this.nome = nomes;
        this.ma = mat;
        this.descr = desc;
        color = cor;
        this.lore = new ArrayList<>();
        this.lore.add("§0§k:" + nome);
        this.lore.add("§r§7- " + desc);
        allItems.put(nomes, this);
        simbolo = simbol;
        this.data = data;
        this.raridade = raridade;

        this.raridade = raridade;
    }
      public CustomItem(String nomes, Material mat, String desc, ChatColor cor, char simbol, ItemRaridade raridade, Color c) {
        this.nome = nomes;
        this.ma = mat;
        this.descr = desc;
        color = cor;
        this.lore = new ArrayList<>();
        this.lore.add("§0§k:" + nome);
        this.lore.add("§r§7- " + desc);
        allItems.put(nomes, this);
        simbolo = simbol;
        this.raridade = raridade;
        corfoguete = c;
        this.raridade = raridade;
    }
    public CustomItem(String nomes, Material mat, String desc, ChatColor cor, char simbol, int data, ItemRaridade raridade, Color c) {
        this.nome = nomes;
        this.ma = mat;
        this.descr = desc;
        color = cor;
        this.lore = new ArrayList<>();
        this.lore.add("§0§k:" + nome);
        this.lore.add("§r§7- " + desc);
        allItems.put(nomes, this);
        simbolo = simbol;
        this.data = data;
        this.raridade = raridade;
        corfoguete = c;
        this.raridade = raridade;
    }

    public ItemStack geraItem(int x) {
        ItemStack i = new ItemStack(this.ma, x, (short) data);

        ItemMeta im = i.getItemMeta();
        if (ma == Material.FIREWORK_CHARGE && im instanceof FireworkEffectMeta) {
            if (corfoguete != null) {
                ((FireworkEffectMeta) im).setEffect(FireworkEffect.builder().withColor(corfoguete).build());
            }
        }
        im.setDisplayName(raridade.getCor() + "§l" + simbolo + "§r" + color + "§l " + this.nome);
        im.setLore(this.lore);
        i.setItemMeta(im);
        return i;
    }

    public String getName() {
        return this.nome;
    }

    public static CustomItem getCustomItem(String nome) {
        return allItems.get(nome);
    }

    public static boolean removeCustomItem(Inventory inv, CustomItem item, int qtd) {
        if (qtd == 0) {
            return true;
        }
        int x = 0;
        int falta = qtd - x;
        for (int y = 0; y < inv.getContents().length; y++) {
            ItemStack itm = inv.getItem(y);
            if (CustomItem.getItem(itm) != null && CustomItem.getItem(itm).equals(item.nome)) {
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
        return x >= qtd;
    }

    public static String getItem(ItemStack s) {
        if (s == null) {
            return null;
        }
        ItemMeta im = s.getItemMeta();
        if (MenuUtils.isIlusorio(s)) {
            return null;
        }
        if (s.getItemMeta() == null || im.getLore() == null || im.getLore().size() < 1) {
            return null;
        }
        if (!s.getItemMeta().getLore().get(0).contains(":")) {
            return null;
        }

        String[] d = s.getItemMeta().getLore().get(0).split(":");
        if (d.length != 2) {
            return null;
        }
        return d[1];

    }

    public static boolean containsInv(Inventory i, CustomItem item) {
        for (ItemStack it : i.getContents()) {
            if (getItem(it) != null && getItem(it).equalsIgnoreCase(item.nome)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsInv(Inventory i, CustomItem item, int qtd) {
        if (qtd == 0) {
            return true;
        }
        int x = 0;
        for (ItemStack it : i.getContents()) {
            if (getItem(it) != null && getItem(it).equalsIgnoreCase(item.nome)) {
                x += it.getAmount();
            }
        }
        return x >= qtd;
    }

    public void interact(Player p) {

    }

    public void interactLobby(Player p) {

    }

    public void interactGame(Player p) {

    }
}
