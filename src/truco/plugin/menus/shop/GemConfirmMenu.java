/*

 */
package truco.plugin.menus.shop;

import br.pj.newlibrarysystem.cashgame.GemManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import truco.plugin.CardWarsPlugin;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.functions.Cooldown;
import truco.plugin.menus.MenuUtils;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class GemConfirmMenu {

    Player p;
    GemShop.GemItem item;
    Inventory inv;

    public static List<GemConfirmMenu> lista = new ArrayList();

    public static ItemStack getGemItem(int qt) {
        String s = Utils.getS(qt);
        ItemStack is = MenuUtils.getMenuItem(Material.RECORD_10, "§a§l" + qt + " Gema" + s, null);
        return MenuUtils.getItemIlusorio(is);
    }

    public GemConfirmMenu(Player p, GemShop.GemItem item) {
        this.p = p;
        this.item = item;
        String nome = "§2" + p.getName() + " - §0#" + CardWarsPlugin.random.nextInt(100) + "!";
        inv = Bukkit.createInventory(p, 36, nome);
        inv.setItem(0, getGemItem(item.preco));
        inv.setItem(8, item.geraItem(p));
        ItemStack seta = MenuUtils.getItemIlusorio(MenuUtils.getMenuItem(Material.VINE, "§d>>>>>>", null));

        for (int x = 1; x < 8; x++) {

            inv.setItem(x, seta);
        }
        ItemStack aceita = MenuUtils.getMenuItem(Material.WOOL, "§a§lAceitar", null);
        aceita.setDurability((short) 5);
        aceita = MenuUtils.getItemIlusorio(aceita);
        inv.setItem(18, aceita);
        inv.setItem(19, aceita);
        inv.setItem(27, aceita);
        inv.setItem(28, aceita);
        ItemStack nega = MenuUtils.getMenuItem(Material.WOOL, "§c§lNegar", null);
        nega.setDurability((short) 14);
        nega = MenuUtils.getItemIlusorio(nega);
        inv.setItem(26, nega);
        inv.setItem(35, nega);
        inv.setItem(25, nega);
        inv.setItem(34, nega);
        ItemStack vazio = MenuUtils.getItemIlusorio(MenuUtils.getMenuItem(Material.STAINED_GLASS_PANE, " ", null));

        for (int x = 0; x < inv.getSize(); x++) {
            if (inv.getItem(x) == null) {
                inv.setItem(x, vazio);
            }
        }
        p.openInventory(inv);
        lista.add(this);
    }

    public void close() {
        if (lista.contains(this)) {
            lista.remove(this);
        }
    }

    public void click(InventoryClickEvent ev) {

        ev.setCancelled(true);
        ev.setResult(Event.Result.DENY);
        if (p == null) {
            ev.getWhoClicked().closeInventory();
            lista.remove(this);
        }
        if (ev.getCurrentItem() != null) {
            if (ev.getCurrentItem().getType() == Material.WOOL) {
                short durability = ev.getCurrentItem().getDurability();
                if (durability == 14) {
                    p.playSound(p.getLocation(), Sound.FIZZ, Integer.MAX_VALUE, 1);
                    ChatUtils.sendMessage(p, "§4Você negou a troca!");
                    p.closeInventory();
                } else if (durability == 5) {
                    int tem = GemManager.GetGems(p.getUniqueId());

                    if (tem >= item.preco) {
                        if (item.click(p)) {
                            final UUID puid = p.getUniqueId();

                            if (GemManager.RemoveGem(puid, item.preco)) {
                                CardsDB.addAction(p.getUniqueId(), p.getName(), "gastou com " + ChatColor.stripColor(item.nome), item.preco);
                                p.playSound(p.getLocation(), Sound.NOTE_PLING, Integer.MAX_VALUE, 1);
                                ChatUtils.sendMessage(p, "§2Você comprou " + item.nome + "§r§2 por §a" + item.preco + " gemas!");
                            } else {
                                CardsDB.addAction(p.getUniqueId(), p.getName(), "ocorreu erro com " + item.nome, item.preco);
                                p.sendMessage("§c§lOcorreu algum erro contactar a staff!");
                                p.sendMessage("§c§lOcorreu algum erro contactar a staff!");
                                p.sendMessage("§c§lOcorreu algum erro contactar a staff!");
                                p.sendMessage("§c§lOcorreu algum erro contactar a staff!");

                            }
                            p.closeInventory();
                            Cooldown.addCoolDown(p, "gemshop", 5000);
                        } else {
                            p.closeInventory();
                            Cooldown.addCoolDown(p, "gemshop", 5000);
                        }
                    } else {

                        p.playSound(p.getLocation(), Sound.FIZZ, Integer.MAX_VALUE, 1);
                        p.closeInventory();
                        Cooldown.addCoolDown(p, "gemshop", 5000);
                        ChatUtils.sendMessage(p, "§aVocê não tem gemas suficientes para comprar este pacote!");
                    }

                }
            }
        }
    }

}
