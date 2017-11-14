package truco.plugin.menus;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MenuUtils {

    public static ItemStack getItemIlusorio(ItemStack itm) {
        ItemStack i = itm.clone();
        ItemMeta im = i.getItemMeta();
        if (!im.hasDisplayName()) {
            im.setDisplayName(StringUtils.capitalize(i.getType().toString().toLowerCase().replace("_", " ")));
        }
        im.setDisplayName("§bø§r " + im.getDisplayName());
        i.setItemMeta(im);
        return i;
    }

    public static boolean isIlusorio(ItemStack item) {

        if (item.getItemMeta() == null) {
            return false;
        }
        if (item.getItemMeta().getDisplayName() == null) {
            return false;
        }
        return item.getItemMeta().getDisplayName().startsWith("§bø§r ");

    }

    public static String getName(ItemStack s) {
        if (isIlusorio(s)) {
            String[] split = ChatColor.stripColor(s.getItemMeta().getDisplayName()).split("ø");

            return split[1].trim();
        } else {
            if (s.getItemMeta() == null || s.getItemMeta().getDisplayName() == null) {
                return null;
            }
            return s.getItemMeta().getDisplayName();
        }
    }

    public static ItemStack getMenuItem(Material m, String nome, List<String> list) {
        ItemStack i = new ItemStack(m);
        ItemMeta im = i.getItemMeta();
        if (list != null) {
            im.setLore(list);
        }
        im.setDisplayName(nome);
        i.setItemMeta(im);
        return i;

    }

}
