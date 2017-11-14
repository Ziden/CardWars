/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cmds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.CardWarsPlugin;
import truco.plugin.managers.PermManager;
import truco.plugin.menus.Menu;
import truco.plugin.menus.MenuUtils;
import truco.plugin.utils.ChatUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CmdVerCartas extends Menu implements CommandExecutor {

    public CmdVerCartas() {
        super("VerCartas", MenuType.AMBOS);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if (args.length == 0) {

            openMenu(p, p);
        } else if (args.length == 1) {
            Player alvo = Bukkit.getPlayer(args[0]);
            if (!PermManager.VERCARTAS.playerHas(p)) {
                ChatUtils.sendMessage(sender, "§cSomente vips podem ver cartas de outros jogadores!");
                return false;
            }
            if (alvo == null) {
                ChatUtils.sendMessage(sender, "§cJogador Offline!");
                return false;

            }

            openMenu(p, alvo);
        }
        return false;
    }

    public static void openMenu(Player p, Player dequem) {
        Inventory i = Bukkit.createInventory(p, 45, "§5Ver Cartas!");
        Inventory enderchest = dequem.getEnderChest();
        List<Carta> cartas = new ArrayList();
        for (int x = 0; x < 18; x++) {
            if (x < 9) {
                ItemStack item = enderchest.getItem(x);
                if (item != null) {
                    Carta c = ControleCartas.getCarta(item);
                    if (c != null) {
                        cartas.add(c);
                    }
                    i.setItem(x, MenuUtils.getItemIlusorio(item));
                } else {
                    i.setItem(x, MenuUtils.getMenuItem(Material.INK_SACK, "§cNenhuma Carta", null));
                }
            } else {
                ItemStack s = MenuUtils.getMenuItem(Material.STAINED_GLASS_PANE, "§bItens das Cartas", Arrays.asList("§a➘➘➘➘"));
                i.setItem(x, s);
                s.setDurability((short)4);

            }

        }
        for (Carta c : cartas) {
            if (c.getItems() != null) {
                for (ItemStack item : c.getItems()) {
                    i.addItem(MenuUtils.getItemIlusorio(item));
                }
            }
        }
        p.openInventory(i);
    }

    @Override
    public void closeInventory(Player p, Inventory s) {
    }

    @Override
    public void openInventory(Player p) {

    }

    @Override
    public void clickInventory(InventoryClickEvent ev) {
        if (ev.getInventory().getName().equalsIgnoreCase("§5Ver Cartas!")) {
            ev.setCancelled(true);
        }
    }
}
