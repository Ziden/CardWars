/*

 */
package truco.plugin.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import truco.plugin.CardWarsPlugin;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MenuGames extends Menu {

    Inventory inv = null;

    public static MenuGames ins;

    public MenuGames() {
        super("GamesMenu", MenuType.LOBBY);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                update();
            }
        }, 200, 200);
    }

    public void update() {
        if (inv == null) {
            inv = Bukkit.createInventory(null, 54, "§2Todos os Jogos!");
        } else {
            inv.clear();
        }
        for (String sv : MatchMaker.db.getGameServers()) {
            inv.addItem(MenuUtils.getItemIlusorio(MenuUtils.getMenuItem(Material.BEACON, "§b" + sv, null)));
        }
    }

    @Override
    public void closeInventory(Player p, Inventory s) {

    }

    @Override
    public void openInventory(Player p) {
        if (inv == null) {
            inv = Bukkit.createInventory(null, 54, "§2Todos os Jogos!");
            for (String sv : MatchMaker.db.getGameServers()) {
                inv.addItem(MenuUtils.getItemIlusorio(MenuUtils.getMenuItem(Material.BEACON, "§b" + sv, null)));
            }

        }
        p.openInventory(inv);
    }

    @Override
    public void clickInventory(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().equalsIgnoreCase("§2Todos os Jogos!")) {

            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                String name = MenuUtils.getName(e.getCurrentItem());
                if (name != null) {
                    Utils.TeleportarTPBG(name, p);
                }
            }
        }
    }

}
