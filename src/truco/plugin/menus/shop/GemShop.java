/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.menus.shop;

import br.gabripj.newvipsystem.NewVipSystem;
import br.gabripj.newvipsystem.database.Mysqlcontrol;
import br.gabripj.newvipsystem.managers.VipManager;
import br.gabripj.newvipsystem.utils.Utils;
import br.pj.newlibrarysystem.cashgame.GemManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.backends.PermissionBackend;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.exceptions.PermissionBackendException;
import truco.plugin.cards.Carta;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.CardWarsPlugin;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.functions.Cooldown;
import truco.plugin.itens.Items;
import truco.plugin.itens.PacoteArmadura;
import truco.plugin.managers.PermManager;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.menus.Menu;
import truco.plugin.menus.MenuUtils;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.IconLib;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class GemShop extends Menu {

    public static String menuname = "GemShop";
    public static String menudisplay = "§2§lShop de Gemas!";

    public GemShop() {
        super(menuname, MenuType.AMBOS);

        GemItem s = new GemItem(5, "§5§lUm Pacote de Todas as Armaduras", Arrays.asList("§cUm pacote que vem", "§cpelo menos 1 carta", "*que pode ser usada por todas ", "armaduras"), 53, Material.FIREWORK_CHARGE) {

            @Override
            public boolean click(Player p) {
                if (p.getInventory().firstEmpty() == -1) {
                    ChatUtils.sendMessage(p, "§cVocê está com seu inventario lotado deixe pelo menos 1 slot vazio!");
                    return false;
                }
                p.getInventory().addItem(Items.pacotearmadura.geraItem(1, Armadura.TODOS));
                p.updateInventory();

                return true;
            }
        };
        s.setCorfo(PacoteArmadura.getCor(Carta.Armadura.TODOS));
//
        //
        //
        s = new GemItem(5, "§5§lUm Pacote de Ferro", Arrays.asList("§cUm pacote que vem", "§cpelo menos 1", "§c de ferro"), 52, Material.FIREWORK_CHARGE) {

            @Override
            public boolean click(Player p) {
                if (p.getInventory().firstEmpty() == -1) {
                    ChatUtils.sendMessage(p, "§cVocê está com seu inventario lotado deixe pelo menos 1 slot vazio!");
                    return false;
                }
                p.getInventory().addItem(Items.pacotearmadura.geraItem(1, Armadura.FERRO));
                p.updateInventory();

                return true;
            }
        };
        s.setCorfo(PacoteArmadura.getCor(Carta.Armadura.FERRO));
        //
        //
        //
        s = new GemItem(5, "§5§lUm Pacote de Malha", Arrays.asList("§cUm pacote que vem", "§cpelo menos 1", " de malha"), 51, Material.FIREWORK_CHARGE) {

            @Override
            public boolean click(Player p) {
                if (p.getInventory().firstEmpty() == -1) {
                    ChatUtils.sendMessage(p, "§cVocê está com seu inventario lotado deixe pelo menos 1 slot vazio!");
                    return false;
                }
                p.getInventory().addItem(Items.pacotearmadura.geraItem(1, Armadura.CHAIN));
                p.updateInventory();

                return true;
            }
        };
        s.setCorfo(PacoteArmadura.getCor(Carta.Armadura.CHAIN));
        //
        //
        //
        s = new GemItem(5, "§5§lUm Pacote de Couro", Arrays.asList("§cUm pacote que vem", "§cpelo menos 1", "§c de couro"), 50, Material.FIREWORK_CHARGE) {

            @Override
            public boolean click(Player p) {
                if (p.getInventory().firstEmpty() == -1) {
                    ChatUtils.sendMessage(p, "§cVocê está com seu inventario lotado deixe pelo menos 1 slot vazio!");
                    return false;
                }
                p.getInventory().addItem(Items.pacotearmadura.geraItem(1, Armadura.LEATHER));
                p.updateInventory();

                return true;
            }
        };
        s.setCorfo(PacoteArmadura.getCor(Carta.Armadura.LEATHER));

        //
        //
        //
        s = new GemItem(5, "§5§lUm Pacote de Ouro", Arrays.asList("§cUm pacote que vem", "§cpelo menos 1", "§ccarta de ouro"), 49, Material.FIREWORK_CHARGE) {

            @Override
            public boolean click(Player p) {
                if (p.getInventory().firstEmpty() == -1) {
                    ChatUtils.sendMessage(p, "§cVocê está com seu inventario lotado deixe pelo menos 1 slot vazio!");
                    return false;
                }
                p.getInventory().addItem(Items.pacotearmadura.geraItem(1, Armadura.OURO));
                p.updateInventory();

                return true;
            }
        };
        s.setCorfo(PacoteArmadura.getCor(Carta.Armadura.OURO));
        //
        //
        //
        s = new GemItem(5, "§5§lUm Pacote de Sem Armadura", Arrays.asList("§cUm pacote que vem", "§cpelo menos 1", "§cde sem armadura"), 48, Material.FIREWORK_CHARGE) {

            @Override
            public boolean click(Player p) {
                if (p.getInventory().firstEmpty() == -1) {
                    ChatUtils.sendMessage(p, "§cVocê está com seu inventario lotado deixe pelo menos 1 slot vazio!");
                    return false;
                }
                p.getInventory().addItem(Items.pacotearmadura.geraItem(1, Armadura.PELADO));
                p.updateInventory();

                return true;
            }
        };
        s.setCorfo(PacoteArmadura.getCor(Carta.Armadura.PELADO));
        //
        //
        //
        s = new GemItem(5, "§5§lUm Pacote de Diamante", Arrays.asList("§cUm pacote que vem", "§cpelo menos 1", "§ccarta de diamante"), 47, Material.FIREWORK_CHARGE) {

            @Override
            public boolean click(Player p) {
                if (p.getInventory().firstEmpty() == -1) {
                    ChatUtils.sendMessage(p, "§cVocê está com seu inventario lotado deixe pelo menos 1 slot vazio!");
                    return false;
                }
                p.getInventory().addItem(Items.pacotearmadura.geraItem(1, Armadura.DIMA));
                p.updateInventory();

                return true;
            }
        };
        s.setCorfo(PacoteArmadura.getCor(Carta.Armadura.DIMA));
        //
        //
        //
        s = new GemItem(30, "§5§lUm Pacote Epico", Arrays.asList("§dUm pacote que vem", "§dpelo menos 1 carta", "§depica"), 45, Material.FIREWORK_CHARGE) {

            @Override
            public boolean click(Player p) {
                if (p.getInventory().firstEmpty() == -1) {
                    ChatUtils.sendMessage(p, "§cVocê está com seu inventario lotado deixe pelo menos 1 slot vazio!");
                    return false;
                }
                p.getInventory().addItem(Items.pacoteepico.geraItem(1));
                p.updateInventory();

                return true;
            }
        };
        s.setCorfo(Color.PURPLE);

        s = new GemItem(15, "§9§lUm Pacote Raro", Arrays.asList("§dUm pacote que vem", "§dpelo menos 1 carta", "§drara"), 46, Material.FIREWORK_CHARGE) {

            @Override
            public boolean click(Player p) {
                if (p.getInventory().firstEmpty() == -1) {
                    ChatUtils.sendMessage(p, "§cVocê está com seu inventario lotado deixe pelo menos 1 slot vazio!");
                    return false;
                }
                p.getInventory().addItem(Items.pacoteraro.geraItem(1));
                p.updateInventory();

                return true;
            }
        };
        s.setCorfo(Items.corraro);
        new GemItem(10, "§6§l500 moedas", Arrays.asList("§bVoce ganha 500 moedas"), 31, Material.EMERALD) {

            @Override
            public boolean click(Player p) {
                MatchMaker.db.addGoldWithThread(p.getUniqueId(), 500, true, false);
                return true;
            }
        };
        new GemItem(35, "§6§l2000 moedas", Arrays.asList("§bVoce ganha 2000 moedas"), 22, Material.EMERALD) {

            @Override
            public boolean click(Player p) {

                MatchMaker.db.addGoldWithThread(p.getUniqueId(), 2000, true, false);

                return true;
            }
        };

        new GemItem(20, "§c[Perm] §eLogar lotado", Arrays.asList("§aVocê pode logar com o servidor lotado!", "§7Caso você compre algum pacote", "§7você não sera reembolsado"), 0, Material.REDSTONE) {

            @Override
            public boolean click(Player p) {
                if (PermManager.LOGAFULL.playerHas(p)) {
                    ChatUtils.sendMessage(p, "§eVocê já possui está permissão!");
                    return false;
                }
                PermissionUser puser = PermissionsEx.getUser(p);
                puser.addPermission(PermManager.LOGAFULL.getPermission());
                puser.save();
                return true;
            }
        };

        new GemItem(15, "§c[Perm] §eGrupo com 4+", Arrays.asList("§aVocê pode criar grupo com 4 ou mais pessoas!", "§7Caso você compre algum pacote", "§7você não sera reembolsado"), 1, Material.REDSTONE) {

            @Override
            public boolean click(Player p) {
                if (PermManager.GRUPO.playerHas(p)) {
                    ChatUtils.sendMessage(p, "§eVocê já possui está permissão!");
                    return false;
                }
                PermissionUser puser = PermissionsEx.getUser(p);
                puser.addPermission(PermManager.GRUPO.getPermission());
                puser.save();
                return true;
            }
        };
        new GemItem(20, "§c[Perm] §eFalar no global", Arrays.asList("§aVocê pode falar no chat global estando no lobby!", "§8global atinge todos os lobbys", "§7Caso você compre algum pacote", "§7você não sera reembolsado"), 2, Material.REDSTONE) {

            @Override
            public boolean click(Player p) {
                if (PermManager.GLOBAL.playerHas(p)) {
                    ChatUtils.sendMessage(p, "§eVocê já possui está permissão!");
                    return false;
                }
                PermissionUser puser = PermissionsEx.getUser(p);
                puser.addPermission(PermManager.GLOBAL.getPermission());
                puser.save();
                return true;
            }
        };
        new GemItem(10, "§c[Perm] §eVisivel no Lobby", Arrays.asList("§aVocê sempre fica visivel no lobby!", "§7Caso você compre algum pacote", "§7você não sera reembolsado"), 3, Material.REDSTONE) {

            @Override
            public boolean click(Player p) {
                if (PermManager.VISIVEL.playerHas(p)) {
                    ChatUtils.sendMessage(p, "§eVocê já possui está permissão!");
                    return false;
                }
                PermissionUser puser = PermissionsEx.getUser(p);
                puser.addPermission(PermManager.VISIVEL.getPermission());
                puser.save();
                return true;
            }
        };
        new GemItem(10, "§c[Perm] §eCores Chat", Arrays.asList("§aVocê pode usar cores no chat com a sigla &!", "§7Caso você compre algum pacote", "§7você não sera reembolsado"), 4, Material.REDSTONE) {

            @Override
            public boolean click(Player p) {
                if (PermManager.VISIVEL.playerHas(p)) {
                    ChatUtils.sendMessage(p, "§eVocê já possui está permissão!");
                    return false;
                }
                PermissionUser puser = PermissionsEx.getUser(p);
                puser.addPermission(PermManager.CORESCHAT.getPermission());
                puser.save();
                return true;
            }
        };
        new GemItem(35, "§c[VIP] §aCardMaster §7- §a30 dias", Arrays.asList(
                "§e- Ganha 2x mais xp e gold",
                "§e- Pode entrar no servidor lotado",
                "§e- Fazer grupo com mais de 4 pessoas",
                "§e- Usar cores no chat",
                "§e- Sempre vísivel no lobby",
                "§e- Icone especial no nome = '§c" + IconLib.CARDMASTER + "§e'",
                "§e- 3 pacotes de cartas normais",
                "§e- Pode vender até 10 cartas ao mesmo tempo no shop",
                "§e- Não paga para colocar cartas a venda",
                "§e- Pode usar chat global /g",
                "§e- Pode usar os comandos /vercartas JOGADOR e /mostrar"
        ), 5, Material.GLOWSTONE_DUST) {

            @Override
            public boolean click(Player p) {
                for (ItemStack it : p.getInventory().getContents()) {
                    if (it != null && (it.getType() != null || it.getType() != Material.AIR)) {
                        p.sendMessage(org.bukkit.ChatColor.RED + "Esvazie o inventorio conseguir comprar o pacote!");
                        return false;
                    }
                }
                if (Mysqlcontrol.AddVencimento(p, NewVipSystem.ListaPacotes.get("cws_master"), 30)) {
                    VipManager.GivePackToPlayer(p, NewVipSystem.ListaPacotes.get("cws_master"));
                    p.sendMessage("§2Voce comprou o pacote com sucesso!");
                    return true;
                } else {
                    p.sendMessage("§cOcorreu algum erro ao comprar o pacote! contate um ADM!");
                    CardsDB.addAction(p.getUniqueId(), p.getName(), "ocorreu erro com cardmaster", 40);
                    return false;
                }

            }
        };
        new GemItem(65, "§c[VIP] §9CardHero §7- §a90 dias", Arrays.asList(
                "§a- Mesmas vantagens do §cCardMaster §emais:",
                "§e- 7 pacotes de cartas normais",
                "§e- 1 pacote de cartas raras",
                "§e- Pode vender até 30 cartas ao mesmo tempo no shop",
                "§e- Icone especial no nome = '§c" + IconLib.CARDHERO + "§e'"
        ), 6, Material.GOLD_INGOT) {

            @Override
            public boolean click(Player p) {
                for (ItemStack it : p.getInventory().getContents()) {
                    if (it != null && (it.getType() != null || it.getType() != Material.AIR)) {
                        p.sendMessage(org.bukkit.ChatColor.RED + "Esvazie o inventorio conseguir comprar o pacote!");
                        return false;
                    }
                }
                if (Mysqlcontrol.AddVencimento(p, NewVipSystem.ListaPacotes.get("cws_hero"), 90)) {
                    VipManager.GivePackToPlayer(p, NewVipSystem.ListaPacotes.get("cws_hero"));
                    p.sendMessage("§2Voce comprou o pacote com sucesso!");
                    return true;
                } else {
                    p.sendMessage("§cOcorreu algum erro ao comprar o pacote! contate um ADM!");
                    CardsDB.addAction(p.getUniqueId(), p.getName(), "ocorreu erro com cardhero", 80);
                    return false;
                }

            }
        };
        new GemItem(100, "§c[VIP] §bBe a Pro §7- §aPermanente", Arrays.asList(
                "§a- Mesmas vantagens do §cCardHero §emais:",
                "§e- Ganha 3x mais xp e gold",
                "§e- 20 pacotes de cartas normais",
                "§e- 2 pacotes de cartas raras",
                "§e- 1 pacote de cartas epicas",
                "§e- Pode vender até 54 cartas ao mesmo tempo no shop",
                "§e- Icone especial no nome = '§c" + IconLib.BEAPRO + "§e'"
        ), 7, Material.DIAMOND) {

            @Override
            public boolean click(Player p) {
                for (ItemStack it : p.getInventory().getContents()) {
                    if (it != null && (it.getType() != null || it.getType() != Material.AIR)) {
                        p.sendMessage(org.bukkit.ChatColor.RED + "Esvazie o inventorio conseguir comprar o pacote!");
                        return false;
                    }
                }
                if (Mysqlcontrol.AddVencimento(p, NewVipSystem.ListaPacotes.get("cws_beapro"), 0)) {
                    VipManager.GivePackToPlayer(p, NewVipSystem.ListaPacotes.get("cws_beapro"));
                    p.sendMessage("§2Voce comprou o pacote com sucesso!");
                    return true;
                } else {
                    p.sendMessage("§cOcorreu algum erro ao comprar o pacote! contate um ADM!");
                    CardsDB.addAction(p.getUniqueId(), p.getName(), "ocorreu erro com beapro", 150);
                    return false;
                }

            }
        };

    }

    @Override
    public void closeInventory(Player p, Inventory s) {
        for (GemConfirmMenu gcm : GemConfirmMenu.lista) {
            if (gcm.inv == s) {
                gcm.close();
                Cooldown.addCoolDown(p, "gemshop", 5000);
            }
        }
        if (s.getTitle().equalsIgnoreCase(menudisplay)) {
            Cooldown.addCoolDown(p, "gemshop", 5000);
        }
    }

    @Override
    public void openInventory(final Player p) {
        if (Cooldown.isCooldown(p, "gemshop")) {
            p.sendMessage("§cAguarde um pouco para abrir o shop de gemas novamente!");
            return;
        }

        if (p == null) {
            return;
        }
        final int tem = GemManager.GetGems(p.getUniqueId());

        Inventory i = Bukkit.createInventory(p, 54, menudisplay);
        GemItem.encheInv(i, p, tem);
        p.openInventory(i);

    }

    @Override
    public void clickInventory(InventoryClickEvent e) {
        for (GemConfirmMenu gcm : GemConfirmMenu.lista) {
            if (gcm.inv.getTitle().equals(e.getInventory().getTitle())) {
                gcm.click(e);
            }
        }

        if (e.getInventory().getTitle().equals(menudisplay)) {
            e.setResult(Event.Result.DENY);
            final Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                String itemnome = MenuUtils.getName(e.getCurrentItem());
                if (itemnome != null) {
                    if (GemItem.gemitens.containsKey(itemnome)) {

                        GemItem s = GemItem.gemitens.get(itemnome);

                        new GemConfirmMenu(p, s);

                    }

                }
            }
        }

    }

    public static abstract class GemItem {

        public static HashMap<String, GemItem> gemitens = new HashMap();
        int preco;
        String nome;
        List<String> desc;
        int slot;
        Material m;
        int dur = -1;

        public abstract boolean click(Player p);

        public GemItem(int preco, String nome, List<String> desc, int slot, Material m) {
            this.preco = preco;
            this.nome = nome;
            this.desc = desc;
            this.slot = slot;
            this.m = m;
            gemitens.put(ChatColor.stripColor(nome), this);
        }

        public static void encheInv(Inventory v, Player p, int gems) {
            for (GemItem cs : gemitens.values()) {
                if (cs.slot != -1) {

                    v.setItem(cs.slot, cs.geraItem(p, gems));

                } else {

                    v.setItem(v.firstEmpty(), cs.geraItem(p, gems));
                }
            }
        }

        public void setDur(int dur) {
            if (dur < 0) {
                return;
            }
            this.dur = dur;
        }
        Color corfo = null;

        public static ItemStack setColor(ItemStack is, Color c) {

            ItemMeta im = is.getItemMeta();
            ((FireworkEffectMeta) im).setEffect(FireworkEffect.builder().withColor(c).build());
            is.setItemMeta(im);
            return is;
        }

        public void setCorfo(Color corfo) {
            this.corfo = corfo;
        }

        public ItemStack geraItem(Player p, int tem) {
            ChatColor cor = ChatColor.GREEN;
            if (tem < preco) {
                cor = ChatColor.RED;
            }
            String dn;
            if (nome.contains("§")) {
                dn = nome;
            } else {
                dn = cor + "§l" + nome;
            }
            List<String> temp = new ArrayList(desc);
            temp.add("§b§lPreço: " + cor + preco + " gemas !");
            ItemStack menuitem = MenuUtils.getMenuItem(m, dn, temp);

            if (dur != -1) {
                menuitem.setDurability((short) dur);
            }
            if (menuitem.getItemMeta() instanceof FireworkEffectMeta) {
                if (corfo != null) {
                    menuitem = setColor(menuitem, corfo);
                }
            }
            return MenuUtils.getItemIlusorio(menuitem);
        }

        public ItemStack geraItem(Player p) {
            ChatColor cor = ChatColor.GREEN;

            String dn;
            if (nome.contains("§")) {
                dn = nome;
            } else {
                dn = cor + "§l" + nome;
            }
            List<String> temp = new ArrayList(desc);

            ItemStack menuitem = MenuUtils.getMenuItem(m, dn, temp);

            if (dur != -1) {
                menuitem.setDurability((short) dur);
            }
            if (menuitem.getItemMeta() instanceof FireworkEffectMeta) {
                if (corfo != null) {
                    menuitem = setColor(menuitem, corfo);
                }
            }
            return MenuUtils.getItemIlusorio(menuitem);
        }

    }

}
