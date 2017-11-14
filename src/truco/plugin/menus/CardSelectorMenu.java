package truco.plugin.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.Cooldown;
import truco.plugin.utils.ItemUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 *
 */
public class CardSelectorMenu extends Menu {

    public static String nome = "§c§lCard Selector Menu";

    public static String menuname = "Selector";

    public CardSelectorMenu() {
        super(menuname, MenuType.AMBOS);
    }

    @Override
    public void closeInventory(Player p, Inventory s) {

        if (s.getTitle().equals(nome)) {

            // validando tudo se tiver alguma merda ele tira e bota no inventario do player de volta   
            for (int x = 0; x < 9; x++) {
                ItemStack ix = s.getItem(x);
                if (ix == null) {
                    continue;
                }
                Carta c = ControleCartas.getCarta(ix);

                // verificando se eh algum item que naum eh carta
                if (c == null) {
                    p.getInventory().addItem(ix);
                    s.remove(ix);
                    p.updateInventory();
                    p.sendMessage(ChatColor.RED + "Items que nao eram cartas foram removidos !");
                    continue;
                }

                // verificando armaduras erradas
                Material armadura;
                if (p.getInventory().getBoots() == null) {
                    armadura = Material.AIR;
                } else {
                    armadura = p.getInventory().getBoots().getType();
                }

                if (!c.getArmadura().boots.contains(armadura)) {
                    p.sendMessage(ChatColor.RED + "Voce tinha cartas que nao pertencem a sua armadura, e elas foram removidas !");
                    ItemUtils.addCarta(p, ix);
                    s.setItem(x, null);
                    p.updateInventory();
                }

                // verificando copias de cartas
                for (int y = 0; y < 9; y++) {

                    if (y == x) {
                        continue;
                    }

                    ItemStack iy = s.getItem(y);
                    if (iy == null) {
                        continue;
                    }
                    ItemMeta mx = ix.getItemMeta();
                    ItemMeta my = iy.getItemMeta();
                    if (mx.getDisplayName() != null && my.getDisplayName() != null) {
                        // se ja tem essa carta
                        if (mx.getDisplayName().equals(my.getDisplayName())) {
                            s.remove(ix);
                            ItemUtils.addCarta(p, ix);

                            p.sendMessage(ChatColor.RED + "Uma carta duplicada foi removida das suas cartas !");
                        }
                    }
                }
            }

            Inventory ender = p.getEnderChest();
            ArrayList<ItemStack> itens = new ArrayList<>();
            for (int x = 0; x < 9; x++) {
                itens.add(null);
                if (s.getItem(x) != null) {
                    ender.setItem(x, s.getItem(x));
                    itens.set(x, s.getItem(x));
                } else {
                    ender.setItem(x, null);
                }
            }
            
            CardsDB.saveEnderchest(itens, p.getUniqueId());
            ControleCartas.calculaVida(p);
            ControleCartas.updateStats(p);
            p.updateInventory();
        }
    }

    @Override
    public void openInventory(Player p) {
        if (CardsDB.enderchest.contains(p.getUniqueId())) {
            return;
        }
  
        Inventory i = Bukkit.createInventory(p, 9, nome);
        for (int x = 0; x < 9; x++) {
            if (p.getEnderChest().getItem(x) != null) {
                i.setItem(x, p.getEnderChest().getItem(x));
            }
        }
        p.openInventory(i);

    }

    @Override
    public void clickInventory(InventoryClickEvent e) {
        if (e.getInventory().getTitle().equalsIgnoreCase(nome)) {
            if (e.isShiftClick()) {
                e.setCancelled(true);
                ChatUtils.sendMessage((Player) e.getWhoClicked(), "§cVocê não pode equipar cartas com shift!");
                e.getWhoClicked().closeInventory();
                return;
            }
            if (e.getRawSlot() > 9) {
                return;
            }
            //((Player)e.getWhoClicked()).sendMessage("CARTA CURRENT ITEM "+String.valueOf(ControleCartas.getCarta(e.getCurrentItem())!=null));
            //((Player)e.getWhoClicked()).sendMessage("CARTA CURSOR "+String.valueOf(ControleCartas.getCarta(e.getCursor())!=null));
            //((Player)e.getWhoClicked()).sendMessage("ITEM NO CURSOR "+e.getCursor());
            if (e.getCurrentItem() == null) {
                return;
            }

            final Carta estouPondo = ControleCartas.getCarta(e.getCursor());
            final Player equipando = (Player) e.getWhoClicked();

            if (e.getCursor().getType() != Material.AIR && estouPondo == null) {
                equipando.sendMessage(ChatColor.RED + "Carta inválida.");
                e.setCancelled(true);
                equipando.closeInventory();
                return;
            }

            if (Cooldown.isCooldown(equipando, "equipCarta")) {
                equipando.sendMessage(ChatColor.RED + "Espere para poder fazer uma ação novamente.");
                e.setCancelled(true);
                equipando.closeInventory();

                return;
            }

            if (estouPondo != null) {
                Material armadura;
                if (equipando.getInventory().getBoots() == null) {
                    armadura = Material.AIR;
                } else {
                    armadura = equipando.getInventory().getBoots().getType();
                }
                if (!estouPondo.getArmadura().boots.contains(armadura)) {
                    equipando.sendMessage(ChatColor.RED + "Esta carta nao pode ser equipada usando sua armadura !");

                    e.setCancelled(true);
                    equipando.closeInventory();
                    return;

                }
            }

            boolean jaTemCarta = false;
            // pra kd carta q tem no meu equip
            for (ItemStack s : e.getInventory().getContents()) {

                if (s == null || s.getItemMeta() == null || s.getItemMeta().getDisplayName() == null) {
                    continue;
                }
                // se ali ja tem uma carta
                if (ControleCartas.getCarta(s) != null && estouPondo != null) {
                    // se eh igual a q eu to colocando
                    if (s.getItemMeta().getDisplayName().equals(e.getCursor().getItemMeta().getDisplayName())) {
                        jaTemCarta = true;
                    }
                }
            }
            if (jaTemCarta) {
                e.setCancelled(true);
                equipando.closeInventory();
                equipando.sendMessage(ChatColor.RED + "Voce nao pode ter duas cartas iguais.");
                return;
            }

            if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                equipando.sendMessage(ChatColor.GREEN + " Voce equipou a carta !");
                equipando.playSound(equipando.getLocation(), Sound.ANVIL_LAND, 1, 0);
            } else if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                equipando.sendMessage(ChatColor.RED + " Voce removeu a carta !");
                equipando.playSound(equipando.getLocation(), Sound.ANVIL_LAND, 1, 0);
            }

            Cooldown.addCoolDown(equipando, "equipCarta", 200);
        }
    }

}
