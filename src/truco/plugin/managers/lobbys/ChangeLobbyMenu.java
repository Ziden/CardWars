/*

 */
package truco.plugin.managers.lobbys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import truco.plugin.CardWarsPlugin;
import truco.plugin.functions.Cooldown;
import truco.plugin.managers.PermManager;
import truco.plugin.menus.Menu;
import truco.plugin.menus.MenuUtils;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ChangeLobbyMenu extends Menu {

    public static String name = "ChangeLobby";
    public static String displayname = "§1Troca de Lobby!";
    private static Inventory inv;

    public ChangeLobbyMenu() {
        super(name, MenuType.LOBBY);
        inv = Bukkit.createInventory(null, 9, displayname);

    }

    @Override
    public void closeInventory(Player p, Inventory s) {

    }

    public static void update() {
        if (inv == null) {
            return;
        }
        inv.clear();
        List<LobbyObject> lobbys = new ArrayList(CardWarsPlugin.lobbymanager.getLobbys());
        Collections.sort(lobbys, new Comparator<LobbyObject>() {

            @Override
            public int compare(LobbyObject o1, LobbyObject o2) {
                int o1i = Integer.valueOf(o1.getNome().split("-")[1]);
                int o2i = Integer.valueOf(o2.getNome().split("-")[1]);
                return o1i - o2i;
            }

        });
        for (LobbyObject lo : lobbys) {
            inv.addItem(lo.getLobbyItem());
        }
    }

    @Override
    public void openInventory(Player p) {
        if (Cooldown.isCooldown(p, "trocahub")) {
            ChatUtils.sendMessage(p, "§7Espere um pouco para trocar de hub!");
            return;
        }
        p.openInventory(inv);
    }

    @Override
    public void clickInventory(InventoryClickEvent e) {

        if (e.getInventory().getTitle().equalsIgnoreCase(displayname)) {
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK) || e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                    if (!PermManager.LOGAFULL.playerHas(p) && e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                        ChatUtils.sendMessage(p, "§4Servidor lotado!");
                        return;
                    }

                    String svname = MenuUtils.getName(e.getCurrentItem());

                    if (svname != null) {
                        svname = "cws_" + svname;
                        if (CardWarsPlugin.lobbymanager.lobbyExiste(svname)) {
                            Utils.TeleportarTPBG(svname, p);
                            p.sendMessage("§eTeleportado §c" + svname + "!");
                        }
                    }
                    p.closeInventory();
                } else if (e.getCurrentItem().getType() == Material.LAPIS_BLOCK) {
                    ChatUtils.sendMessage(p, "§9Você já está nesse servidor!");
                    p.closeInventory();
                }
            }
        }
    }

}
