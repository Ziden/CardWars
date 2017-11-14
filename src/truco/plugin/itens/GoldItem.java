/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.itens;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.signs.SignUtils;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.Cooldown;
import truco.plugin.utils.ItemUtils;

/**
 *
 * @author Júnior
 */
public class GoldItem extends CustomItem {

    public GoldItem() {
        super("Vale Moedas", Material.EMERALD, "Ao ativar ganha uma quantia de gold!", ChatColor.GOLD, '$', CustomItem.ItemRaridade.COMUM);
    }

    @Override
    public ItemStack geraItem(int x) {
        ItemStack i = new ItemStack(ma, 1, (short) data);

        ItemMeta im = i.getItemMeta();
        if (ma == Material.FIREWORK_CHARGE && im instanceof FireworkEffectMeta) {
            if (corfoguete != null) {
                ((FireworkEffectMeta) im).setEffect(FireworkEffect.builder().withColor(corfoguete).build());
            }
        }
        String plural = x > 1 ? "s" : "";
        im.setDisplayName(raridade.getCor() + "§l" + simbolo + "§r" + color + "§l " + "Vale " + x + " gold" + plural + "");
        List<String> newlore = new ArrayList(lore);
        newlore.add("§5Vale:" + x);
        im.setLore(newlore);
        i.setItemMeta(im);
        return i;
    }

    @Override
    public void interactLobby(final Player p) {
        if (Cooldown.isCooldown(p, "golditem")) {
            return;
        }
        ItemStack item = p.getItemInHand();
        if (item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
            if (item.getItemMeta().getLore().size() == 3) {
                String gold = item.getItemMeta().getLore().get(2);

                gold = gold.split(":")[1];
                if (SignUtils.isInt(gold)) {
                    ItemUtils.consumeItemInHand(p);
                    final UUID uid = p.getUniqueId();
                    final int quanto = Integer.valueOf(gold);
                    p.updateInventory();
                    Cooldown.addCoolDown(p, "golditem", 4000);
                    MatchMaker.db.addGoldWithThread(uid, quanto, true,false);

                }

            }
        }

    }
}
