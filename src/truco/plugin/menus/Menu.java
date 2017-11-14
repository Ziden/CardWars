/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.menus;

import truco.plugin.menus.shop.GemShop;
import truco.plugin.menus.shop.PackSellerMenu;
import truco.plugin.menus.shop.CardSellerMenu;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import truco.plugin.cmds.CmdVerCartas;
import truco.plugin.managers.lobbys.ChangeLobbyMenu;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public abstract class Menu {

    public static HashMap<String, Menu> menus = new HashMap();

    public abstract void closeInventory(Player p, Inventory s);

    public abstract void openInventory(Player p);

    public abstract void clickInventory(InventoryClickEvent e);

    String nome;
    MenuType tipo;

    public Menu(String nome, MenuType tipo) {
        this.nome = nome;
        this.tipo = tipo;
        menus.put(nome, this);
    }

    public MenuType getTipo() {
        return tipo;
    }

    public static void Start() {
        new ArmorSelectMenu();
        new CardSelectorMenu();
        new CardSellerMenu();
        new CardStockMenu();
        new TeleporterMenu();
        new PackSellerMenu();
        new MenuAllCards();
        new OpenMenu();
        new GemShop();
        new CmdVerCartas();
        if (CardWarsPlugin.server == ServerType.LOBBY) {
            new ChangeLobbyMenu();
            new MenuGames();
        }
    }

    public static enum MenuType {

        AMBOS, LOBBY, JOGO
    }
}
