/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cmds;

import truco.plugin.functions.ScoreCWs;
import truco.plugin.functions.game.Mana;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.CardWarsPlugin.ServerType;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.itens.Items;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.signs.SignUtils;
import truco.plugin.data.mysql.MatchHistoryDB;
import truco.plugin.menus.CardStockMenu;
import truco.plugin.menus.Menu;
import truco.plugin.menus.OpenMenu;
import truco.plugin.socket.SocketManager;
import truco.plugin.utils.*;
import truco.plugin.utils.SoundUtils.Som;
import truco.plugin.utils.mobapi.mobs.MobsApi;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CmdAdmin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("cadm")) {
            if (sender instanceof Player) {

                final Player p = (Player) sender;
                if (!p.isOp()) {
                    return false;
                }
                if (args.length > 1) {
                    if (args[0].equalsIgnoreCase("iname")) {
                        if (p.getItemInHand() == null) {
                            p.sendMessage("§aColoca um item na mao!");
                            return false;
                        }
                        String name = "";
                        for (int x = 0; x < args.length; x++) {
                            if (x == 0) {
                                continue;
                            }
                            if (x != (args.length - 1)) {
                                name += args[x] + " ";
                            } else {
                                name += args[x];
                            }
                        }
                        name = name.replace("&", "§");
                        ItemStack is = p.getItemInHand().clone();
                        ItemMeta im = p.getItemInHand().getItemMeta();
                        im.setDisplayName(name);
                        is.setItemMeta(im);
                        p.setItemInHand(is);
                        p.updateInventory();
                        return true;
                    }

                    if (args[0].equalsIgnoreCase("addlore")) {
                        if (p.getItemInHand() == null) {
                            p.sendMessage("§aColoca um item na mao!");
                            return false;
                        }
                        String name = "";
                        for (int x = 0; x < args.length; x++) {
                            if (x == 0) {
                                continue;
                            }
                            if (x != (args.length - 1)) {
                                name += args[x] + " ";
                            } else {
                                name += args[x];
                            }
                        }
                        name = name.replace("&", "§");
                        ItemStack is = p.getItemInHand().clone();
                        ItemMeta im = p.getItemInHand().getItemMeta();
                        List<String> lore;
                        if (im.getLore() != null) {
                            lore = im.getLore();
                            lore.add(name);
                        } else {
                            lore = Arrays.asList(name);
                        }
                        im.setLore(lore);
                        is.setItemMeta(im);
                        p.setItemInHand(is);
                        p.updateInventory();
                        return true;
                    }

                }
                if (args.length > 2) {
                    if (args[0].equalsIgnoreCase("setlore")) {
                        if (p.getItemInHand() == null) {
                            p.sendMessage("§aColoca um item na mao!");
                            return false;
                        }

                        if (!SignUtils.isInt(args[1])) {
                            p.sendMessage("§aCara isso(" + args[1] + ") não é um numero!");
                            return false;
                        }

                        int qtd = Integer.valueOf(args[1]);

                        if (qtd <= 0) {
                            p.sendMessage("§cNão trabalho com numeros negativos ou neutros!");
                            return false;
                        }
                        if (p.getItemInHand().getItemMeta() == null
                                || p.getItemInHand().getItemMeta().getLore() == null
                                || p.getItemInHand().getItemMeta().getLore().size() < qtd) {
                            p.sendMessage("§cEsse item não tem uma lore acho que voce procura /cadm addlore !");
                            return false;
                        }
                        String name = "";
                        for (int x = 0; x < args.length; x++) {
                            if (x == 0 || x == 1) {
                                continue;
                            }
                            if (x != (args.length - 1)) {
                                name += args[x] + " ";
                            } else {
                                name += args[x];
                            }
                        }
                        name = name.replace("&", "§");
                        ItemStack is = p.getItemInHand().clone();
                        ItemMeta im = p.getItemInHand().getItemMeta();
                        List<String> lore = im.getLore();
                        lore.set(qtd - 1, name);
                        im.setLore(lore);
                        is.setItemMeta(im);
                        p.setItemInHand(is);
                        p.updateInventory();
                        return true;
                    }

                }

                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("tpbook")) {
                        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                        List<String> PaginasAux = new ArrayList();
                        PaginasAux.add(LocUtils.locationToString(p.getLocation()));
                        BookUtil.setPages(book, PaginasAux);
                        BookUtil.setTitle(book, "tp");
                        p.setItemInHand(book);
                        p.sendMessage("§cLivro pra tp criado!");
                        return true;
                    } else if (args[0].equalsIgnoreCase("cartasdb")) {

                        int qt = ControleCartas.getCards().size();
                        p.sendMessage("§bSão " + qt + " cartas para colocar no database!");
                        long ti = System.currentTimeMillis();

                        MatchHistoryDB.reloadCards();
                        p.sendMessage("§bCartas Repassadas Para o DataBase!");
                        long tempo = (System.currentTimeMillis() - ti);
                        p.sendMessage("§bDemorou " + tempo + " milisegundos com média de " + tempo / qt + " por carta!");

                        return true;

                    } else if (args[0].equalsIgnoreCase("devolver")) {

                        CardsDB.devolve();
                        return true;

                    }
                    if (args[0].equalsIgnoreCase("limpamobs")) {
                        int i = 0;
                        for (Entity e : p.getWorld().getEntities()) {
                            if (e instanceof LivingEntity && e.getType() != EntityType.PLAYER && e.getType() != EntityType.ARMOR_STAND) {
                                if (!MobsApi.bixos.containsKey(e)) {
                                    e.remove();
                                    i++;
                                }
                            }
                        }
                        p.sendMessage("§cVocê limpou " + i + " mobs!");
                        return true;

                    }
                    if (args[0].equalsIgnoreCase("addgeral")) {

                        if (p.getItemInHand() != null) {
                            CardsDB.addGeral(p.getItemInHand());
                            p.sendMessage("Adicionado para todos!");
                        }
                        return true;

                    }

                    if (args[0].equalsIgnoreCase("som")) {

                        String lista = "";
                        for (Som s : Som.values()) {
                            lista += s.name() + ", ";
                        }
                        p.sendMessage("§aUse /som {som}");
                        p.sendMessage("§cSons disponiveis §a: " + lista + " !");
                        return true;

                    }
                    if (args[0].equalsIgnoreCase("menu")) {
                        Menu.menus.get(OpenMenu.menuname).openInventory(p);
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("icons")) {
                        for (IconLib ic : IconLib.values()) {
                            p.sendMessage(ic.name() + " - §a" + ic);
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("fillmana")) {
                        if (server == ServerType.GAME) {

                            Mana.setMana(p, 100);
                            p.sendMessage("§aMana cheia!");
                            return true;

                        }
                    } else if (args[0].equalsIgnoreCase("name")) {
                        if (server == ServerType.LOBBY) {
                            ScoreCWs.setLobbyName(-1, p);
                            p.sendMessage("§aNome atualizado!");
                            return true;
                        }
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("ver")) {
                        Player alvo = Bukkit.getPlayer(args[1]);
                        if (alvo != null) {
                            EntityPlayer ep = ((CraftPlayer) alvo).getHandle();
                            p.sendMessage("Complete ? " + ep.getProfile().isComplete());
                            p.sendMessage("Legacy ? " + ep.getProfile().isLegacy());
                            p.sendMessage("UUID : " + ep.getProfile().getId().toString());
                        } else {
                            p.sendMessage("§aJogador Offline!");
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("golditem")) {
                        if (SignUtils.isInt(args[1])) {
                            int quanto = Integer.valueOf(args[1]);
                            p.getInventory().addItem(Items.golditem.geraItem(quanto));
                            p.updateInventory();
                            ChatUtils.sendMessage(p, "§6Ta no teu inventario!");
                        } else {
                            ChatUtils.sendMessage(p, "§cSó trabalho com numeros meu amigo!");
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("head")) {
                        ItemStack s = ItemUtils.getHead(args[1]);
                        p.getInventory().addItem(s);
                        p.updateInventory();
                        return true;
                    } else if (args[0].equalsIgnoreCase("estoque")) {
                        String name = args[1];
                        CardStockMenu.openInv(p, 1, name);
                        return true;
                    } else if (args[0].equalsIgnoreCase("som")) {
                        Som s = Som.valueOf(args[1].toUpperCase());
                        if (s != null) {
                            SoundUtils.playSound(s, Integer.MAX_VALUE, p);
                            p.sendMessage("§cSom " + s.name() + " foi tocado!");

                        } else {
                            p.sendMessage("§aUse /som para ver a lista de sons!");
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("addgoldglobal")) {

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                for (Player pc : Bukkit.getOnlinePlayers()) {
                                    if (pc != null) {
                                        if (SignUtils.isInt(args[1])) {
                                            MatchMaker.db.addGold(pc.getUniqueId(), Integer.parseInt(args[1]));
                                            ChatUtils.sendMessage(pc, "§cToma ae " + args[1] + " de gold!");
                                        }
                                    }
                                }
                            }
                        }).start();
                        return true;

                    } else if (args[0].equalsIgnoreCase("setelo")) {

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                if (p != null) {
                                    if (SignUtils.isInt(args[1])) {
                                        MatchMaker.db.setElo(p.getUniqueId(), Integer.parseInt(args[1]));
                                        ChatUtils.sendMessage(p, "§6§lSeu novo elo : " + args[1]);
                                    }
                                }
                            }
                        }).start();
                        return true;

                    } else if (args[0].equalsIgnoreCase("help")) {
                        if (SignUtils.isInt(args[1]) && Integer.valueOf(args[1]) > 0) {
                            sendHelp(p, Integer.parseInt(args[1]));

                        } else {
                            p.sendMessage("§aSó trabalho com numeros positivos");
                        }
                        return true;

                    } else if (args[0].equalsIgnoreCase("setlevel")) {

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                if (p != null) {
                                    if (SignUtils.isInt(args[1])) {
                                        MatchMaker.db.setLevel(p.getUniqueId(), Integer.parseInt(args[1]));
                                        ChatUtils.sendMessage(p, "§6§lSeu novo nível: " + args[1]);
                                    }
                                }
                            }
                        }).start();
                        return true;

                    } else if (args[0].equalsIgnoreCase("setgold")) {

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                if (p != null) {
                                    if (SignUtils.isInt(args[1])) {
                                        MatchMaker.db.setGold(p.getUniqueId(), Integer.parseInt(args[1]));
                                        ChatUtils.sendMessage(p, "§6§lSeu novo dinheiro : " + MatchMaker.db.getGold(p.getUniqueId()));
                                    }
                                }
                            }
                        }).start();
                        return true;

                    }
                }

            }
            sendHelp(sender, 1);
        }
        return false;
    }

    public CmdAdmin() {

        cmds.put("help {page}", "mostra os comandos");
        cmds.put("name", "atualiza seu nome no scoreboard");
        cmds.put("cartasdb", "Passa todas cartas pro database(trava o server por algum tempo)!");
        cmds.put("fillmana", "Enche a mana!");
        cmds.put("ver {PLAYER}", "ve algumas infos do player");
        cmds.put("head {NOME}", "Pega a cabeça do jogador!");
        cmds.put("setelo {valor}", "Seta seu elo para o valor desejado!");
        cmds.put("setgold {valor}", "Seta seu gold para o valor desejado!");
        cmds.put("golditem {valor}", "Pega um item que se clicar com ele pega gold!");
        cmds.put("iname {NOME}", "Seta o nome de um item!");
        cmds.put("setlore {LINHA} {LORE}", "Seta uma linha da lore!");
        cmds.put("addlore {LORE}", "Adiciona uma linha de lore!");
        cmds.put("addgoldglobal {valor}", "Da gold para todo mundo!");
        cmds.put("icons", "ve os icones!");
        cmds.put("som {som}", "toca um som");
        cmds.put("limpamobs", "limpa todos os mobs");
        cmds.put("tpbook", "cria livro de teleporte");
        cmds.put("estoque", "ve o estoque de um jogador");
        cmds.put("menu", "abre o menu do jogo");
    }
    public static HashMap<String, String> cmds = new HashMap();

    public static void sendHelp(CommandSender cs, int page) {
        List<String> send = new ArrayList();
        int foi = 0;
        int comecoem;
        if (page == 1) {
            comecoem = 0;
        } else {
            comecoem = (page - 1) * 7;
        }
        cs.sendMessage("Lenght = " + SocketManager.tosend.size());
        if (cmds.size() < (comecoem + 1)) {
            cs.sendMessage("§aNenhuma ajuda para a pagina §c" + page + "§a !");
            return;
        }
        List<Entry<String, String>> list = new ArrayList(cmds.entrySet());
        for (int x = comecoem; x < list.size(); x++) {
            if (foi == 7) {
                break;
            }
            Entry entry = list.get(x);
            send.add("§b/cadm " + entry.getKey() + " §9-§7 " + entry.getValue());
            foi++;
        }
        cs.sendMessage("  ");
        cs.sendMessage("§c*==-*==-*==-§4CardWars Admin Cmds §c*==-*==-*==-");
        cs.sendMessage("§cPAGINA:" + page);

        for (String mn : send) {
            cs.sendMessage(mn);
        }
        cs.sendMessage("§c*==-*==-*==-§4CardWars Admin Cmds §c*==-*==-*==-");
        cs.sendMessage("  ");
    }
}
