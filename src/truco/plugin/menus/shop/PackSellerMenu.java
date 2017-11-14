package truco.plugin.menus.shop;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.itens.CustomItem;
import truco.plugin.itens.Items;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.Cooldown;
import truco.plugin.menus.Menu;
import truco.plugin.menus.MenuUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PackSellerMenu extends Menu {

    public static String nome = "§9Vendedor de Pacotes!";
    public static int custo = 150;
    public static String menuname = "Pacote";

    public static ItemStack setLime(ItemStack is) {

        ItemMeta im = is.getItemMeta();
        ((FireworkEffectMeta) im).setEffect(FireworkEffect.builder().withColor(Color.LIME).build());
        is.setItemMeta(im);
        return is;
    }

    public static void compraPacote(final Player p, final int qts) {
        if (p.getInventory().firstEmpty() == -1) {
            ChatUtils.sendMessage(p, "§aInventario lotado esvazie ele primeiro!");
            return;
        }
        if (!Cooldown.isCooldown(p, "pacoteshop")) {
            Cooldown.addCoolDown(p, "pacoteshop", 5000);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    if (p != null) {
                        final int tem = MatchMaker.db.getGold(p.getUniqueId());
                        final int preço = custo * qts;

                        if (tem >= preço) {

                            MatchMaker.db.setGold(p.getUniqueId(), tem - preço);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                                @Override
                                public void run() {
                                    CardWarsPlugin.log(p.getName() + " comprou " + qts + " pacotes de carta por " + preço + " ele tinha " + tem + " de coins!");
                                    ChatUtils.sendMessage(p, "§aVocê comprou " + qts + " pacote" + (qts > 1 ? "s" : "") + " de cartas por " + preço + " !");
                                    p.getInventory().addItem(Items.pacote.geraItem(1 * qts));
                                    p.updateInventory();
                                    p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, Integer.MAX_VALUE, 2);
                                }
                            });

                        } else {
                            ChatUtils.sendMessage(p, "§2Você precisa de §c" + preço + "§2 moedas para comprar isso!");

                        }

                    }
                }
            }).start();
        }
    }

    public PackSellerMenu() {
        super(menuname, MenuType.LOBBY);
    }

    @Override
    public void closeInventory(Player p, Inventory s) {
    }

    @Override
    public void openInventory(Player p) {
        Inventory i = Bukkit.createInventory(p, 9, nome);
        i.addItem(setLime(MenuUtils.getItemIlusorio(MenuUtils.getMenuItem(Material.FIREWORK_CHARGE, "§aUm Pacote de Cartas", Arrays.asList("§aPACOTE ALEATORIO", "§6Custo:" + custo + " moedas")))));
        i.addItem(setLime(MenuUtils.getItemIlusorio(MenuUtils.getMenuItem(Material.FIREWORK_CHARGE, "§aCinco Pacotes de Cartas", Arrays.asList("§aPACOTES ALEATORIOS", "§6Custo:" + (custo * 5) + " moedas")))));
        i.addItem(setLime(MenuUtils.getItemIlusorio(MenuUtils.getMenuItem(Material.FIREWORK_CHARGE, "§aDez Pacotes de Cartas", Arrays.asList("§aPACOTES ALEATORIOS", "§6Custo:" + custo * 10 + " moedas")))));
        i.addItem(setLime(MenuUtils.getItemIlusorio(MenuUtils.getMenuItem(Material.FIREWORK_CHARGE, "§aVinte Pacotes de Cartas", Arrays.asList("§aPACOTES ALEATORIOS", "§6Custo:" + custo * 20 + " moedas")))));
        p.openInventory(i);

    }

    @Override
    public void clickInventory(InventoryClickEvent e) {
        if (e.getInventory().getTitle().equalsIgnoreCase(nome)) {

            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            Player p = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) {
                return;
            }
            if (p.getInventory().firstEmpty() == -1) {
                ChatUtils.sendMessage(p, "§aEsvazie seu inventario para comprar pacotes!");
                return;
            }
            if (Cooldown.isCooldown(p, "pacoteshop")) {
                p.sendMessage("§aEspere um pouco para comprar outro pacote!");
                return;
            }
            String name = MenuUtils.getName(e.getCurrentItem());
            if (name != null) {
                name = ChatColor.stripColor(name);
                if (name.equalsIgnoreCase("Um Pacote de Cartas")) {
                    compraPacote(p, 1);
                    p.closeInventory();
                } else if (name.equalsIgnoreCase("Cinco Pacotes de Cartas")) {
                    compraPacote(p, 5);
                    p.closeInventory();
                } else if (name.equalsIgnoreCase("Dez Pacotes de Cartas")) {
                    compraPacote(p, 10);
                    p.closeInventory();
                } else if (name.equalsIgnoreCase("Vinte Pacotes de Cartas")) {
                    compraPacote(p, 20);
                    p.closeInventory();
                }
            }
        }
    }
}
