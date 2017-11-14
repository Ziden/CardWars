package truco.plugin.menus;

import truco.plugin.menus.shop.CardSellerMenu;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import truco.plugin.cards.Carta;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.ControleCartas;
import truco.plugin.utils.ChatUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MenuAllCards extends Menu {

    public static void Click(InventoryClickEvent ev) {
       

    }

    public static String namem = "§c§l!Menu de Cartas";

    public static void OpenWithArmor(Player p, Armadura armor, int page) {
        if (page == 0) {
            return;
        }
        Inventory inv = Bukkit.createInventory(p, 54, "§a!C¬Armadura¬" + armor.name() + "¬" + page);
        ArrayList<ArrayList<ItemStack>> paginas = new ArrayList();
        {
            int x = 0;
            ArrayList<ItemStack> atual = new ArrayList();
            List<Carta> cartas = new ArrayList();
            for (Carta car : ControleCartas.getCards()) {
                if (car.getArmadura() == armor) {
                    cartas.add(car);
                }
            }
            for (Carta c : cartas) {

                atual.add(c.toItemStack());
                if (x == 51 || c.getNome().equals(cartas.get(cartas.size() - 1).getNome())) {
                    x = 0;
                    paginas.add(atual);
                    atual = new ArrayList();
                } else {
                    x++;
                }
            }
        }
        if (paginas.size() >= page) {
            inv.setItem(45, MenuUtils.getMenuItem(Material.REDSTONE_COMPARATOR, "§c§lVoltar", Arrays.asList("§9§lClique aqui para voltar!")));
            inv.setItem(53, MenuUtils.getMenuItem(Material.DIODE, "§a§lAvançar", Arrays.asList("§9§lClique aqui para avançar!")));
            List<ItemStack> cartas = paginas.get(page - 1);

            for (ItemStack c : cartas) {
                inv.addItem(c);
            }

            p.openInventory(inv);

        } else {
            ChatUtils.sendMessage(p, "§c§lNenhuma carta com esse filtro foi encontrado!!");
        }
    }

    public static void OpenAll(Player p, int page) {
        if (page == 0) {
            return;
        }
        final Inventory inv = Bukkit.createInventory(p, 54, "§a!C¬Todas¬" + "Todas" + "¬" + page);
        ArrayList<ArrayList<ItemStack>> paginas = new ArrayList();
        {
            int x = 0;
            ArrayList<ItemStack> atual = new ArrayList();
            List<Carta> cartas = new ArrayList(ControleCartas.getCards());

            for (Carta c : cartas) {

                atual.add(c.toItemStack());
                if (x == 51 || c.getNome().equals(cartas.get(cartas.size() - 1).getNome())) {
                    x = 0;
                    paginas.add(atual);
                    atual = new ArrayList();
                } else {
                    x++;
                }
            }
        }
        if (paginas.size() >= page) {
            inv.setItem(45, MenuUtils.getMenuItem(Material.REDSTONE_COMPARATOR, "§c§lVoltar", Arrays.asList("§9§lClique aqui para voltar!")));
            inv.setItem(53, MenuUtils.getMenuItem(Material.DIODE, "§a§lAvançar", Arrays.asList("§9§lClique aqui para avançar!")));
            List<ItemStack> cartas = paginas.get(page - 1);

            for (ItemStack c : cartas) {
                inv.addItem(c);
            }

            p.openInventory(inv);

        } else {
            ChatUtils.sendMessage(p, "§c§lNenhuma carta com esse filtro foi encontrado!!");
        }
    }

    public static void OpenWithRaridade(Player p, Raridade raridade, int page) {
        if (page == 0) {
            return;
        }
        Inventory inv = Bukkit.createInventory(p, 54, "§a!C¬Raridade¬" + raridade.name() + "¬" + page);
        ArrayList<ArrayList<ItemStack>> paginas = new ArrayList();
        {
            int x = 0;
            ArrayList<ItemStack> atual = new ArrayList();
            List<Carta> cartas = new ArrayList();
            for (Carta car : ControleCartas.getCards()) {
                if (car.getRaridade() == raridade) {
                    cartas.add(car);
                }
            }
            for (Carta c : cartas) {

                atual.add(c.toItemStack());
                if (x == 51 || c.getNome().equals(cartas.get(cartas.size() - 1).getNome())) {
                    x = 0;
                    paginas.add(atual);
                    atual = new ArrayList();
                } else {
                    x++;
                }
            }
        }
        if (paginas.size() >= page) {
            inv.setItem(45, MenuUtils.getMenuItem(Material.REDSTONE_COMPARATOR, "§c§lVoltar", Arrays.asList("§9§lClique aqui para voltar!")));
            inv.setItem(53, MenuUtils.getMenuItem(Material.DIODE, "§a§lAvançar", Arrays.asList("§9§lClique aqui para avançar!")));
            List<ItemStack> cartas = paginas.get(page - 1);

            for (ItemStack c : cartas) {
                inv.addItem(c);
            }

            p.openInventory(inv);

        } else {
            ChatUtils.sendMessage(p, "§c§lNenhuma carta com esse filtro foi encontrado!!");
        }
    }

      public static String menuname = "AllCards";
    public MenuAllCards() {
        super(menuname, MenuType.AMBOS);
    }

    @Override
    public void closeInventory(Player p, Inventory s) {
    
    }

    @Override
    public void openInventory(Player p) {
        Inventory inv = Bukkit.createInventory(p, 36, namem);
        for (Carta.Armadura r : Carta.Armadura.values()) {
            inv.addItem(r.getItemStackToMenu());
        }
        int x = 18;
        for (Raridade raridade : Raridade.values()) {
            ItemStack item = new ItemStack(Material.DIAMOND);
            ItemMeta itm = item.getItemMeta();
            itm.setDisplayName("§b§l~" + raridade.name());
            item.setItemMeta(itm);
            inv.setItem(x, item);
            x++;
        }
        inv.setItem(27, MenuUtils.getMenuItem(Material.EMERALD_BLOCK, "§a§lTodas as cartas ", null));
        p.openInventory(inv);
    }

    @Override
    public void clickInventory(InventoryClickEvent ev) {
     Inventory inv = ev.getInventory();
        final Player p = (Player) ev.getWhoClicked();
        ItemStack clicado = ev.getCurrentItem();
        if (inv.getTitle().equalsIgnoreCase(namem)) {
            ev.setCancelled(true);
            if (clicado != null) {
                if (clicado.getItemMeta() != null && clicado.getItemMeta().getDisplayName() != null) {

                    if (clicado.getItemMeta().getDisplayName().contains("~")) {
                        String[] split = clicado.getItemMeta().getDisplayName().split("~");
                        if (split.length == 2) {
                            OpenWithRaridade((Player) ev.getWhoClicked(), Raridade.valueOf(split[1]), 1);
                            return;
                        }

                    }
                    if (CardSellerMenu.getArmor(clicado) != null) {
                        OpenWithArmor(p, CardSellerMenu.getArmor(clicado), 1);
                    }
                }
                if (clicado.getType() == Material.EMERALD_BLOCK) {
                    OpenAll(p, 1);
                }

            }

        }
        if (inv.getTitle().startsWith("§a!C")) {

            if (clicado != null) {
                String[] sp = inv.getTitle().split("¬");
                Integer page = Integer.valueOf(sp[3]);
                String tipo = sp[2];
                if (clicado.getType() == Material.REDSTONE_COMPARATOR) {
                    ev.setCancelled(true);
                    if (page > 1) {
                        if (tipo.equalsIgnoreCase("Armadura")) {
                            OpenWithArmor(p, Armadura.valueOf(sp[2]), page - 1);
                        } else if (tipo.equalsIgnoreCase("Raridade")) {
                            OpenWithRaridade(p, Raridade.valueOf(sp[2]), page - 1);
                        } else {
                            OpenAll(p, page - 1);
                        }
                    } else {
                        openInventory(p);
                    }
                    return;
                }
                if (clicado.getType() == Material.DIODE) {
                    ev.setCancelled(true);
                    if (tipo.equalsIgnoreCase("Armadura")) {
                        OpenWithArmor(p, Armadura.valueOf(sp[2]), page + 1);
                    } else if (tipo.equalsIgnoreCase("Raridade")) {
                        OpenWithRaridade(p, Raridade.valueOf(sp[2]), page + 1);
                    } else {
                        OpenAll(p, page + 1);
                    }

                }
            }
        }
    }

}
