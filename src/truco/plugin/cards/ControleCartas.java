/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import truco.plugin.arena.Arena;
import truco.plugin.itens.CustomItem;
import truco.plugin.itens.Items;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.menus.ArmorSelectMenu;
import truco.plugin.signs.SignUtils;
import truco.plugin.cards.stats.Stats;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.Cooldown;
import truco.plugin.utils.ItemUtils;

/**
 *
 * @author usuario
 */
public class ControleCartas {

    public static Random rnd = CardWarsPlugin.random;
    public static final String ICONE_RARIDADE = "♚";

    public static ChatColor getCor(Raridade r) {
        if (r == Raridade.COMUM) {
            return ChatColor.YELLOW;
        } else if (r == Raridade.INCOMUM) {
            return ChatColor.GREEN;
        } else if (r == Raridade.RARO) {
            return ChatColor.BLUE;
        } else if (r == Raridade.EPICO) {
            return ChatColor.DARK_PURPLE;
        }
        return ChatColor.WHITE;
    }
    // o loader tem q por as carta aki
    public static final HashMap<String, Carta> todasCartas = new HashMap<String, Carta>();
    public static final HashMap<Raridade, List<Carta>> porRaridade = new HashMap<Raridade, List<Carta>>();
    public static final HashMap<Armadura, List<Carta>> porArmadura = new HashMap<Armadura, List<Carta>>();

    public static boolean canUseCard(Player p, Carta c) {
        Material boots = null;
        try {
            boots = p.getInventory().getBoots().getType();
        } catch (NullPointerException e) {
            boots = Material.AIR;
        }
        return c.getArmadura().boots.contains(boots);
    }

    // pega as cartas q tao equipadas
    public static ArrayList<Carta> getCartas(Player p) {
        ArrayList<Carta> cartas = new ArrayList<Carta>();
        for (int x = 0; x < 9; x++) {
            Carta c = getCarta(p.getEnderChest().getItem(x));

            if (c != null) {
                cartas.add(c);

            } else {
                cartas.add(null);
            }

        }
        return cartas;
    }

    public static List<Carta> getCards() {
        ArrayList<Carta> crts = new ArrayList<>();
        for (Carta c : todasCartas.values()) {
            crts.add(c);
        }
        return crts;

    }

    public static Carta getCardByName(String g) {
        if (todasCartas.containsKey(g)) {
            return todasCartas.get(g);
        }
        return null;
    }
    public static HashMap<UUID, List<Stats>> stats = new HashMap();

    public static List<Stats> getStats(Player p) {
        if (stats.containsKey(p.getUniqueId())) {
            return stats.get(p.getUniqueId());
        } else {
            return new ArrayList<Stats>();
        }
    }

    public static boolean hasStat(Player p, String nome) {
        for (Stats s : getStats(p)) {
            if (s.getNome().equalsIgnoreCase(nome)) {
                return true;
            }
        }
        return false;
    }

    public static void updateStats(Player p) {
        List<Stats> st = new ArrayList();
        for (Carta c : ControleCartas.getCartas(p)) {

            if (c != null && c.getStats() != null) {
                st.addAll(Arrays.asList(c.getStats()));
            }
        }
        if (ControleCartas.stats.containsKey(p.getUniqueId())) {
            ControleCartas.stats.remove(p.getUniqueId());
        }
        ControleCartas.stats.put(p.getUniqueId(), st);

    }

    public static Carta abrePacote(Raridade r) {
        List<Carta> cartas = porRaridade.get(r);
        Carta escolhida = cartas.get(rnd.nextInt(cartas.size()));
        return escolhida;

    }

    public static Carta abrePacote() {
        Raridade escolhida = Raridade.COMUM;
        int random = rnd.nextInt(1000);
        if (random == 0) {
            if (rnd.nextBoolean()&&rnd.nextBoolean()) {
                escolhida = Raridade.EPICO;
            }
        } else if (random > 0 && random < 20) {
            if (rnd.nextBoolean()) {
                escolhida = Raridade.RARO;
            }
        } else if (random > 20 && random < 150) {
            escolhida = Raridade.INCOMUM;
        }
        List<Carta> cards = porRaridade.get(escolhida);
        Carta carta = cards.get(rnd.nextInt(cards.size()));

        return carta;
    }

    public static Carta abrePacote(Armadura r) {

        Raridade escolhida = Raridade.COMUM;
        int random = rnd.nextInt(1000);
        if (random == 0) {
            if (rnd.nextBoolean() && rnd.nextBoolean()) {
                escolhida = Raridade.EPICO;
            }
        } else if (random > 0 && random < 20) {
            if (rnd.nextBoolean()) {
                escolhida = Raridade.RARO;
            }
        } else if (random > 20 && random < 300) {
            escolhida = Raridade.INCOMUM;
        }
        List<Armadura> ars = new ArrayList(r.getArmors());
        ars.add(r);
        Collections.shuffle(ars);
        List<Carta> roda = new ArrayList();
        for (Armadura arm : ars) {
            if (!porArmadura.containsKey(arm)) {

                continue;
            }

            for (Carta c : porArmadura.get(arm)) {
                if (c.getRaridade() == escolhida) {
                    roda.add(c);
                }
            }

        }
        if (roda.isEmpty()) {
            return abrePacote();
        }
        return roda.get(rnd.nextInt(roda.size()));

    }

    public static Carta abrePacote(Player p, Raridade r) {
        List<Carta> cartas = porRaridade.get(r);
        Carta escolhida = cartas.get(rnd.nextInt(cartas.size()));
        return escolhida;

    }

    public static Carta abrePacote(Player p) {
        Raridade escolhida = Raridade.COMUM;
        int random = rnd.nextInt(1000);

        if (random == 0) {
            if (rnd.nextBoolean()) {
                escolhida = Raridade.EPICO;
            }
        } else if (random > 0 && random < 20) {
            escolhida = Raridade.RARO;
        } else if (random > 20 && random < 300) {
            escolhida = Raridade.INCOMUM;
        }
        List<Carta> cards = porRaridade.get(escolhida);
        Carta carta = cards.get(rnd.nextInt(cards.size()));

        return carta;
    }

    public static void addCart(Carta c) {
        todasCartas.put(c.getNome(), c);
        c.onEnable();
        if (!porArmadura.containsKey(c.getArmadura())) {
            List<Carta> cards = new ArrayList<Carta>();
            cards.add(c);
            porArmadura.put(c.getArmadura(), cards);
        } else {
            porArmadura.get(c.getArmadura()).add(c);
        }
        if (!porRaridade.containsKey(c.getRaridade())) {
            List<Carta> cards = new ArrayList<Carta>();
            cards.add(c);
            porRaridade.put(c.getRaridade(), cards);
        } else {
            porRaridade.get(c.getRaridade()).add(c);
        }
    }

    public static void updateInventoryCards(final Player p, final boolean onlyarmor) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                final ArmorSelectMenu.Armadura r = MatchMaker.db.getArmor(p.getUniqueId());
                Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                    @Override
                    public void run() {
                        if (r != null) {
                            p.getInventory().setArmorContents(r.getArmorContents());
                        } else {
                            p.getInventory().setArmorContents(null);
                        }
                        if (onlyarmor) {
                            return;
                        }
                        if ((CardWarsPlugin.server == CardWarsPlugin.ServerType.GAME)) {
                            p.getInventory().clear();

                            p.getInventory().setItem(0, Items.icaster.geraItem(1));
                            if (CardWarsPlugin.mainarena != null) {
                                Color c = CardWarsPlugin.mainarena.getTeam(p.getUniqueId()) == Arena.Team.BLUE ? Color.BLUE : Color.RED;

                                ItemStack calca = new ItemStack(Material.LEATHER_LEGGINGS);
                                calca = ItemUtils.pinta(calca, c);
                                p.getInventory().setLeggings(calca);
                                p.getInventory().setHelmet(null);
                            }
                            for (Carta c : getCartas(p)) {
                                if (c != null) {
                                    if (c.getItems() == null) {
                                        continue;
                                    }
                                    for (ItemStack it : c.getItems()) {
                                        p.getInventory().addItem(it);
                                    }
                                }

                            }
                        }

                        if (CardWarsPlugin.server == ServerType.TUTORIAL) {

                            ItemStack io = null;
                            if (p.getInventory().getItem(0) != null) {
                                io = p.getInventory().getItem(0);
                            }
                            p.getInventory().setItem(0, Items.icaster.geraItem(1));
                            if (io != null) {

                                p.getInventory().addItem(io);
                            }

                        }

                        p.updateInventory();
                    }
                });
            }
        }).start();

    }

    public static void calculaVida(Player p) {
        double vida = 20;
        for (int x = 0; x < 9; x++) {
            ItemStack ix = p.getEnderChest().getItem(x);
            if (ix == null) {
                continue;
            }
            Carta c = ControleCartas.getCarta(ix);
            if (c != null) {
                vida = c.alteraVida(vida);
            }
        }
        p.setMaxHealth(vida);
        p.setHealth(p.getMaxHealth());
    }

    public static Carta getCarta(ItemStack ss) {
        if (ss == null) {
            return null;
        }
        if (ss.getType() != Material.MONSTER_EGG) {
            return null;
        }
        if (ss.getItemMeta() == null || ss.getItemMeta().getDisplayName() == null) {
            return null;
        }

        String nome = ChatColor.stripColor(ss.getItemMeta().getDisplayName()).replace(ICONE_RARIDADE, "").trim();

        return todasCartas.get(nome);
    }
}
