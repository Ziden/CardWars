package truco.plugin.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.CardWarsPlugin;
import truco.plugin.utils.ChatUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CardStockMenu extends Menu {

    public static String nome = "§c§lDep¬";

    public static void openInv(final Player p, final int page, String dequem) {
        final OfflinePlayer of;
        if (dequem == null) {
            of = p;
        } else if (Bukkit.getOfflinePlayer(dequem) == null) {
            p.sendMessage("§aNão existe registro!");
            return;
        } else {
            of = Bukkit.getOfflinePlayer(dequem);
        }

        final UUID uuid = of.getUniqueId();
        new Thread(new Runnable() {

            @Override
            public void run() {
                final Inventory i = Bukkit.createInventory(p, 54, nome + of.getName() + "¬" + page);

                final List<ItemStack> cartas = CardsDB.getCartas(uuid, page);
                Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                    @Override
                    public void run() {

                        if (cartas != null) {
                            for (int x = 0; x < cartas.size(); x++) {
                                //FIZ ISSO CASO MUDE A DESC DE ALGUMA CARTA
                                ItemStack is = cartas.get(x);
                                if (ControleCartas.getCarta(is) != null) {
                                    is = ControleCartas.getCarta(is).toItemStack();
                                }
                                i.setItem(x, is);
                            }
                        }
                        i.setItem(45, MenuUtils.getMenuItem(Material.REDSTONE_COMPARATOR, "§c§lVoltar", Arrays.asList("§9§lClique aqui para voltar!")));
                        i.setItem(53, MenuUtils.getMenuItem(Material.DIODE, "§a§lAvançar", Arrays.asList("§9§lClique aqui para avançar!")));

                        p.openInventory(i);
                    }
                }
                );

            }
        }
        ).start();
    }

    public static String menuname = "Estoque";

    public CardStockMenu() {
        super(menuname, MenuType.AMBOS);
    }

    @Override
    public void closeInventory(Player p, final Inventory s) {
        if (s.getTitle().startsWith(nome)) {

            final int pagina = Integer.valueOf(s.getTitle().split("¬")[2]);
            final String name = s.getTitle().split("¬")[1];
            /*
             for (int x = 0; x < s.getSize(); x++) {
             if (s.getItem(x) != null) {
             if (ControleCartas.getCarta(s.getItem(x)) == null) {
             if (s.getItem(x).getType() != Material.DIODE && s.getItem(x).getType() != Material.REDSTONE_COMPARATOR) {

             p.getInventory().addItem(s.getItem(x));
             s.setItem(x, null);
             }
             }
             }
             }
 
             */
            p.updateInventory();

            final UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();

            CardsDB.saveCards(uuid, Arrays.asList(s.getContents()), pagina);

        }

    }

    @Override
    public void openInventory(Player p) {
        openInv(p, 1, null);
    }

    @Override
    public void clickInventory(InventoryClickEvent e) {

        if (e.getInventory().getTitle().startsWith(nome)) {
            if (e.getInventory().getTitle().split("¬").length != 3) {
                return;
            }
            int pagina = Integer.valueOf(e.getInventory().getTitle().split("¬")[2]);
            String name = e.getInventory().getTitle().split("¬")[1];
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getType() == Material.DIODE) {
                    e.setCancelled(true);
                    if (e.getClick() == ClickType.LEFT) {
                        if (pagina != CardsDB.stocksize) {
                            ((Player) e.getWhoClicked()).closeInventory();
                            openInv((Player) e.getWhoClicked(), pagina + 1, name);
                            return;
                        } else {
                            ChatUtils.sendMessage((Player) e.getWhoClicked(), "§c§lVocê já está na ultima pagina!");

                        }
                    }

                }

                if (e.getCurrentItem().getType() == Material.REDSTONE_COMPARATOR) {
                    e.setCancelled(true);
                    if (pagina != 1) {
                        if (e.getClick() == ClickType.LEFT) {
                            ((Player) e.getWhoClicked()).closeInventory();

                            openInv((Player) e.getWhoClicked(), pagina - 1, name);
                            return;
                        }
                    } else {
                        ChatUtils.sendMessage((Player) e.getWhoClicked(), "§cVocê já está na primeira pagina!");
                    }

                }

            }
            /*  final Carta estouPondo = ControleCartas.getCarta(e.getCursor());
             final Player equipando = (Player) e.getWhoClicked();
             LIBERADO TUTO !
             if (e.getCursor().getType() == Material.AIR || estouPondo == null) {
             if (ControleCartas.getCarta(e.getCurrentItem()) == null) {
             equipando.sendMessage(ChatColor.RED + "Carta inválida.");
             e.setCancelled(true);
             equipando.closeInventory();
             e.setResult(Event.Result.DENY);
             return;
             }
             }
             */
        }

    }

}
