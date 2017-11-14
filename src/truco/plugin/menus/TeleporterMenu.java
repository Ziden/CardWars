/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.menus;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cmds.CmdSpawn;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.Cooldown;
import truco.plugin.managers.lobbys.ChangeLobbyMenu;
import truco.plugin.utils.Utils;

/**
 *
 * @author Júnior
 */
public class TeleporterMenu extends Menu {

    public static String nome = "§cTeleportes !";
    public static int custo = 100;
    public static String menuname = "Teleporter";

    public TeleporterMenu() {
        super(menuname, MenuType.LOBBY);
    }

    @Override
    public void openInventory(Player p) {
        Inventory i = Bukkit.createInventory(p, 27, nome);
        i.setItem(12, MenuUtils.getItemIlusorio(MenuUtils.getMenuItem(Material.DIAMOND_SWORD, "§cCaverna de Jogos", Arrays.asList("§7Teleporta para a caverna de jogos"))));
        i.setItem(14, MenuUtils.getItemIlusorio(MenuUtils.getMenuItem(Material.EMERALD, "§6Loja", Arrays.asList("§7Teleporta para a loja!"))));
        ItemStack jogar = MenuUtils.getMenuItem(Material.MONSTER_EGG, "§9Sala de Equipamentos", Arrays.asList("§7Teleporta para a sala de equipamentos"));
        jogar.setDurability((short) 98);
        i.setItem(4, MenuUtils.getItemIlusorio(jogar));
        i.setItem(22, MenuUtils.getItemIlusorio(MenuUtils.getMenuItem(Material.ENDER_PEARL, "§bSpawn", Arrays.asList("§7Teleporta para o spawn!"))));
        i.setItem(26, MenuUtils.getItemIlusorio(MenuUtils.getMenuItem(Material.BOOK, "§dTutorial", Arrays.asList("§7Teleporta para o tutorial!"))));
        i.setItem(18, MenuUtils.getItemIlusorio(MenuUtils.getMenuItem(Material.COMMAND, "§3§lMudar Lobby", Arrays.asList("§7Abre o menu de Lobbys!!"))));

        p.openInventory(i);

    }

    @Override
    public void clickInventory(InventoryClickEvent e) {
        if (e.getInventory().getTitle().equalsIgnoreCase(nome)) {

            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) {
                return;
            }

            String name = MenuUtils.getName(e.getCurrentItem());
            if (name != null) {
                name = ChatColor.stripColor(name);

                if (name.equalsIgnoreCase("Tutorial")) {
                    p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, Integer.MAX_VALUE, 1);
                    Utils.TeleportarTPBG("cws_Tutorial", p);
                    p.closeInventory();
                } else if (name.equalsIgnoreCase("Loja")) {
                    p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, Integer.MAX_VALUE, 1);
                    p.teleport(new Location(p.getWorld(), -36.5, 22, 86.5, 0, 0));
                    p.closeInventory();
                } else if (name.equalsIgnoreCase("Spawn")) {
                    p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, Integer.MAX_VALUE, 1);
                    p.teleport(CmdSpawn.getSpawnLocation(p));
                    p.closeInventory();
                } else if (name.equalsIgnoreCase("Sala de Equipamentos")) {
                    p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, Integer.MAX_VALUE, 1);
                    p.teleport(new Location(p.getWorld(), -139.5, 12, 20.5, 90, 0));
                    p.closeInventory();
                } else if (name.equalsIgnoreCase("Caverna de Jogos")) {

                    p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, Integer.MAX_VALUE, 1);
                    p.teleport(new Location(p.getWorld(), -75.5, 5, 69.5, 0, 0));
                    p.closeInventory();
                } else if (name.equalsIgnoreCase("Mudar Lobby")) {
                    Menu.menus.get(ChangeLobbyMenu.name).openInventory(p);
                }
            }

        }

    }

    @Override
    public void closeInventory(Player p, Inventory s) {
    }
}
