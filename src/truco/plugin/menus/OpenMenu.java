package truco.plugin.menus;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import truco.plugin.itens.CustomItem;
import truco.plugin.matchmaking.Threads.MatchMaker;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class OpenMenu extends Menu {

    public static String nome = "§e§c§lCardWars";

    public static String menuname = "MenuPrincipal";

    public OpenMenu() {
        super(menuname, MenuType.AMBOS);
    }

    @Override
    public void closeInventory(Player p, Inventory s) {
    }

    @Override
    public void openInventory(Player p) {
        if (!p.isOp()) {
            return;
        }
        boolean pode = true;
        if (CardWarsPlugin.server == ServerType.LOBBY && MatchMaker.db.getIngamePlayer(p) != null) {
            pode = false;
        }
        if (pode) {
            Inventory i = Bukkit.createInventory(p, 9, nome);
            i.addItem(MenuUtils.getMenuItem(Material.PAPER, "§9§lEquipar Cartas", Arrays.asList("§7Clique aqui para equipar suas cartas!")));
            if (CardWarsPlugin.server == ServerType.LOBBY) {
                i.addItem(MenuUtils.getMenuItem(Material.MAP, "§a§lJogar", Arrays.asList("§7Clique aqui para jogar!")));
            }

            i.addItem(MenuUtils.getMenuItem(Material.BED, "§7§lMenu de armaduras", Arrays.asList("§7Clique aqui para mudar sua armadura!")));
            i.addItem(MenuUtils.getMenuItem(Material.GHAST_TEAR, "§2§lSuas cartas", Arrays.asList("§7Clique aqui para guardar suas cartas!")));
            if (p.isOp()) {
                i.addItem(MenuUtils.getMenuItem(Material.DIAMOND, "§a(OP) CustomItems", Arrays.asList("§7Clique aqui para pegar os fodendo itens!")));
                i.addItem(MenuUtils.getMenuItem(Material.POTATO_ITEM, "§e§l(OP)Todas as cartas", Arrays.asList("§7Clique aqui para ver todas as cartas!")));
            }
            p.openInventory(i);
        }
    }

    @Override
    public void clickInventory(InventoryClickEvent e) {
        if (e.getInventory().getTitle().equalsIgnoreCase(nome)) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getType().equals(Material.PAPER)) {
                    Menu.menus.get(CardSelectorMenu.menuname).openInventory((Player) e.getWhoClicked());
                } else if (e.getCurrentItem().getType() == Material.BED) {
                    Menu.menus.get(ArmorSelectMenu.menuname).openInventory((Player) e.getWhoClicked());
                } else if (e.getCurrentItem().getType() == Material.MAP) {
                    MatchMaker.addPlayerToSoloQueue((Player) e.getWhoClicked());
                } else if (e.getCurrentItem().getType() == Material.GHAST_TEAR) {
                    Menu.menus.get(CardStockMenu.menuname).openInventory((Player) e.getWhoClicked());
                } else if (e.getCurrentItem().getType() == Material.DIAMOND) {
                    CustomItem.openInv((Player) e.getWhoClicked());
                } else if (e.getCurrentItem().getType() == Material.POTATO_ITEM) {
                    Menu.menus.get(MenuAllCards.menuname).openInventory((Player) e.getWhoClicked());
                }

            }
        }
    }

}
