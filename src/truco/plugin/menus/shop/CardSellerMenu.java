/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.menus.shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.menus.shop.CartaAVenda;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.Cooldown;
import truco.plugin.utils.ItemUtils;
import truco.plugin.functions.LevelManager;
import truco.plugin.menus.Menu;
import truco.plugin.menus.MenuUtils;

/**
 *
 * @author Carlos
 */
public class CardSellerMenu extends Menu {

    public static ItemStack getSellItemStack(CartaAVenda cav) {
        ItemStack s = cav.c.toItemStack();
        s = MenuUtils.getItemIlusorio(s);
        ItemMeta m = s.getItemMeta();
        List<String> lore = m.getLore();
        lore.add("§c§lID:§0§k" + cav.id);
        lore.add("§b§lVendedor:" + cav.nomevendedor);
        lore.add("§a§l$Preço:" + cav.preco);
        m.setLore(lore);
        s.setItemMeta(m);
        return s;

    }

    public static int getId(ItemStack i) {
        if (i == null) {
            return -1;
        }
        if (i.getItemMeta() == null) {
            return -1;
        }
        ItemMeta im = i.getItemMeta();
        if (im.getLore() == null) {
            return -1;
        }
        if (im.getLore().size() < 3) {
            return -1;
        }
        for (String s : im.getLore()) {
            if (s.startsWith("§c§lID:§0§k")) {
                return Integer.valueOf(s.split("§k")[1]);
            }
        }
        return -1;
    }

    public static ArrayList<ArrayList<ItemStack>> getCartasRaridade(Raridade r, UUID uuid) {
        ArrayList<ArrayList<ItemStack>> paginas = new ArrayList();
        int x = 0;
        ArrayList<ItemStack> atual = new ArrayList();
        List<CartaAVenda> cartas = CardsDB.getCardsByRaridade(r, uuid);
        for (CartaAVenda c : cartas) {

            atual.add(getSellItemStack(c));
            if (x == 51 || c.id == cartas.get(cartas.size() - 1).id) {
                x = 0;
                paginas.add(atual);
                atual = new ArrayList();
            } else {
                x++;
            }
        }
        return paginas;
    }

    public static ArrayList<ArrayList<ItemStack>> getCartas(UUID uuid) {
        ArrayList<ArrayList<ItemStack>> paginas = new ArrayList();
        int x = 0;
        ArrayList<ItemStack> atual = new ArrayList();
        List<CartaAVenda> cartas = CardsDB.getCardsSell(uuid);
        for (CartaAVenda c : cartas) {

            atual.add(getSellItemStack(c));
            if (x == 51 || c.id == cartas.get(cartas.size() - 1).id) {
                x = 0;
                paginas.add(atual);
                atual = new ArrayList();
            } else {
                x++;
            }
        }
        return paginas;
    }

    public static ArrayList<ArrayList<ItemStack>> getCartasArmadura(Armadura r, UUID uuid) {
        ArrayList<ArrayList<ItemStack>> paginas = new ArrayList();
        int x = 0;
        ArrayList<ItemStack> atual = new ArrayList();
        List<CartaAVenda> cartas = CardsDB.getCardsByArmor(r, uuid);
        for (CartaAVenda c : cartas) {

            atual.add(getSellItemStack(c));
            if (x == 51 || c.id == cartas.get(cartas.size() - 1).id) {
                x = 0;
                paginas.add(atual);
                atual = new ArrayList();
            } else {
                x++;
            }
        }
        return paginas;
    }

    public static Armadura getArmor(ItemStack s) {
        for (Armadura r : Armadura.values()) {
            if (s.getType() == Material.MONSTER_EGG) {
                if (s.getItemMeta() == null) {
                    continue;
                }
                String nome = ChatColor.stripColor(s.getItemMeta().getDisplayName());
                if (nome.equalsIgnoreCase(r.desc)) {
                    return r;
                }
            }
        }

        return null;
    }

    public static void openRemoveMenu(final Player p) {

        final Inventory inv = Bukkit.createInventory(p, 54, "§c§l!Shop Remove");
        for (CartaAVenda cav : CardsDB.getCardsByVendor(p.getUniqueId())) {
            inv.addItem(getSellItemStack(cav));
        }

        p.openInventory(inv);

    }

    public static void openMenu(final Player p, final int page, final Armadura r) {
        if (page == 0) {
            p.closeInventory();
            return;
        }
        final Inventory inv = Bukkit.createInventory(p, 54, "§a!S¬Armadura¬" + r.name() + "¬" + page);

        ArrayList<ArrayList<ItemStack>> cartass = getCartasArmadura(r, p.getUniqueId());

        if (cartass.size() >= page) {
            inv.setItem(45, MenuUtils.getMenuItem(Material.REDSTONE_COMPARATOR, "§c§lVoltar", Arrays.asList("§9§lClique aqui para voltar!")));
            inv.setItem(53, MenuUtils.getMenuItem(Material.DIODE, "§a§lAvançar", Arrays.asList("§9§lClique aqui para avançar!")));
            List<ItemStack> cartas = cartass.get(page - 1);

            for (ItemStack c : cartas) {
                inv.addItem(c);
            }

            p.openInventory(inv);

        } else {
            ChatUtils.sendMessage(p, "§c§lNenhuma carta com esse filtro foi encontrado!!");
        }

    }

    public static void openMenu(final Player p, final int page, final Raridade r) {
        if (page == 0) {
            p.closeInventory();
            return;
        }
        final Inventory inv = Bukkit.createInventory(p, 54, "§a!S¬Raridade¬" + r.name() + "¬" + page);

        ArrayList<ArrayList<ItemStack>> cartass = getCartasRaridade(r, p.getUniqueId());
        if (cartass.size() >= page) {
            inv.setItem(45, MenuUtils.getMenuItem(Material.REDSTONE_COMPARATOR, "§c§lVoltar", Arrays.asList("§9§lClique aqui para voltar!")));
            inv.setItem(53, MenuUtils.getMenuItem(Material.DIODE, "§a§lAvançar", Arrays.asList("§9§lClique aqui para avançar!")));
            List<ItemStack> cartas = cartass.get(page - 1);

            for (ItemStack c : cartas) {
                inv.addItem(c);
            }

            p.openInventory(inv);

        } else {
            ChatUtils.sendMessage(p, "§c§lNenhuma carta com esse filtro foi encontrado!!");
        }

    }

    public static void openMenu(final Player p, final int page) {

        if (page == 0) {
            p.closeInventory();
            return;
        }
        final Inventory inv = Bukkit.createInventory(p, 54, "§a!S¬Todas¬" + "Todas" + "¬" + page);

        ArrayList<ArrayList<ItemStack>> cartass = getCartas(p.getUniqueId());
        if (cartass.size() > (page - 1)) {
            inv.setItem(45, MenuUtils.getMenuItem(Material.REDSTONE_COMPARATOR, "§c§lVoltar", Arrays.asList("§9§lClique aqui para voltar!")));
            inv.setItem(53, MenuUtils.getMenuItem(Material.DIODE, "§a§lAvançar", Arrays.asList("§9§lClique aqui para avançar!")));
            List<ItemStack> cartas = cartass.get(page - 1);

            for (ItemStack c : cartas) {
                inv.addItem(c);
            }

            p.openInventory(inv);

        } else {
            ChatUtils.sendMessage(p, "§c§lNenhuma carta com esse filtro foi encontrado!!");
        }

    }
    public static String menuname = "Seller";

    public CardSellerMenu() {
        super(menuname, MenuType.LOBBY);
    }

    @Override
    public void closeInventory(Player p, Inventory s) {
    }

    @Override
    public void openInventory(Player p) {
        if (!LevelManager.canUse(LevelManager.LevelBonus.SHOPLEVEL, p.getUniqueId())) {
            p.sendMessage("§cVocê ainda não pode abrir a loja!");
            return;
        }
        Inventory inv = Bukkit.createInventory(p, 36, "§c§l!Shop Filtro");
        for (Armadura r : Armadura.values()) {
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
        inv.setItem(27, MenuUtils.getMenuItem(Material.EMERALD_BLOCK, "§a§lTodas as cartas a venda!", null));
        inv.setItem(35, MenuUtils.getMenuItem(Material.REDSTONE_BLOCK, "§c§lRemove suas cartas da venda!", null));
        p.openInventory(inv);
    }

    @Override
    public void clickInventory(InventoryClickEvent ev) {

        Inventory inv = ev.getInventory();
        final Player p = (Player) ev.getWhoClicked();
        ItemStack clicado = ev.getCurrentItem();
        if (inv.getTitle().equalsIgnoreCase("§c§l!Shop Filtro")) {
            ev.setCancelled(true);
            if (clicado != null) {
                if (clicado.getItemMeta() != null) {
                    if (clicado.getItemMeta().getDisplayName().contains("~")) {
                        String[] split = clicado.getItemMeta().getDisplayName().split("~");
                        if (split.length == 2) {
                            openMenu((Player) ev.getWhoClicked(), 1, Raridade.valueOf(split[1]));
                            return;
                        }

                    }
                    if (getArmor(clicado) != null) {
                        openMenu(p, 1, getArmor(clicado));
                    }
                }
                if (clicado.getType() == Material.EMERALD_BLOCK) {
                    openMenu(p, 1);
                }
                if (clicado.getType() == Material.REDSTONE_BLOCK) {
                    openRemoveMenu(p);
                }
            }

        }
        if (inv.getTitle().equalsIgnoreCase("§c§l!Shop Remove")) {
            ev.setCancelled(true);
            if (clicado != null) {

                final int id = getId(clicado);
                if (id != -1) {

                    final CartaAVenda cav = CardsDB.getCartaAVenda(id);
                    if (cav != null) {
                        if (CardsDB.isAvenda(cav.id)) {
                            CardsDB.removeCartaAVenda(id);
                            ItemUtils.addCarta(p, cav.c.toItemStack());

                            p.closeInventory();

                            ChatUtils.sendMessage(p, "§eVocê cancelou a venda da carta  §c" + cav.c.getNome() + "§e por §c" + cav.preco + " §e!");

                        } else {
                            ChatUtils.sendMessage(p, "§eAlguem já comprou esta carta!");
                        }
                    } else {
                        ChatUtils.sendMessage(p, "§eAlguem já comprou esta carta!");
                    }

                }
            }
        }
        if (inv.getTitle().startsWith("§a!S")) {
            ev.setCancelled(true);
            if (clicado != null) {
                String[] sp = inv.getTitle().split("¬");
                Integer page = Integer.valueOf(sp[3]);
                String tipo = ChatColor.stripColor(sp[1]);
                if (clicado.getType() == Material.REDSTONE_COMPARATOR) {
                    if (tipo.equalsIgnoreCase("Armadura")) {
                        openMenu(p, page - 1, Armadura.valueOf(sp[2]));
                    } else if (tipo.equalsIgnoreCase("Raridade")) {
                        openMenu(p, page - 1, Raridade.valueOf(sp[2]));
                    } else {
                        openMenu(p, page - 1);

                    }
                    return;
                }
                if (clicado.getType() == Material.DIODE) {
                    if (tipo.equalsIgnoreCase("Armadura")) {
                        openMenu(p, page + 1, Armadura.valueOf(sp[2]));
                    } else if (tipo.equalsIgnoreCase("Raridade")) {
                        openMenu(p, page + 1, Raridade.valueOf(sp[2]));
                    } else {
                        openMenu(p, page + 1);
                    }
                    return;
                }
                final int id = getId(clicado);
                if (id != -1) {
                    final UUID uuid = p.getUniqueId();
                    if (!Cooldown.isCooldown(p, "bancotempo")) {

                        Cooldown.addCoolDown(p, "bancotempo", 50000);
                        p.closeInventory();

                        final CartaAVenda cav = CardsDB.getCartaAVenda(id);
                        if (cav != null) {

                            if (MatchMaker.db.hasGold(uuid, cav.preco)) {
                                if (CardsDB.isAvenda(id)) {
                                    CardsDB.removeCartaAVenda(id);
                                    MatchMaker.db.removeGold(uuid, cav.preco);
                                    MatchMaker.db.addGold(cav.vendedor, cav.preco);
                                    Player vendedor = Bukkit.getPlayer(cav.vendedor);

                                    ItemUtils.addCarta(p, cav.c.toItemStack());
                                    p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, Integer.MAX_VALUE, 2);
                                    if (vendedor != null) {
                                        ChatUtils.sendMessage(vendedor, "§eSua carta §c" + cav.c.getNome() + "§e que estava a venda foi vendida para §c" + p.getName() + " §epor §c" + cav.preco + " §emoedas!");
                                    }

                                    ChatUtils.sendMessage(p, "§eVocê comprou a carta §c" + cav.c.getNome() + "§e de §c" + cav.nomevendedor + "§e por §c" + cav.preco + " §emoedas!");

                                } else {
                                    p.sendMessage("§a§lEssa carta já foi vendida!");
                                }
                            } else {
                                ChatUtils.sendMessage(p, "§4Você não tem dinheiro suficiente!");
                            }
                        }

                    } else {
                        ChatUtils.sendMessage(p, "§a§lEspere um pouco para comprar outra carta!");
                    }
                }
            }
        }
    }

}
