/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.itens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.ControleCartas;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.ItemUtils;
import truco.plugin.cards.skills.skilltypes.BandageType;
import truco.plugin.socket.SocketManager;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class Items {

    public static CustomItem pacote = null;
    public static CustomItem pacoteini = null;
    public static CustomItem pacoteepico = null;
    public static CustomItem pacoteraro = null;
    public static CustomItem icaster = null;
    public static CustomItem bandagem = null;
    public static CustomItem garramadeira = null;
    public static CustomItem garrapedra = null;
    public static CustomItem garraferro = null;
    public static CustomItem garraouro = null;
    public static PacoteArmadura pacotearmadura = null;
    public static GoldItem golditem = null;
    public static Color corraro = Color.fromBGR(242, 182, 0);
    public static BombaFumaca bomba = null;
    public static CajadoMagico cajadomagico = null;

    public static void startPacotes() {
        pacote = new CustomItem("Pacote de Cartas", Material.FIREWORK_CHARGE, "Ao usar ganha-se de uma a três cartas aleatórias", ChatColor.RED, '♠', CustomItem.ItemRaridade.INCOMUM, Color.LIME) {

            @Override
            public void interactLobby(Player p) {
                if (p.getInventory().firstEmpty() == -1) {
                    p.sendMessage("§cSeu inventário está cheio!");
                    return;
                }
                List<Carta> c = new ArrayList<Carta>();

                int qt = 1 + new Random().nextInt(3);
                for (int x = 0; x < qt; x++) {
                    c.add(ControleCartas.abrePacote());
                }
                for (Carta sorteada : c) {
                    if (sorteada != null) {
                        ItemUtils.addCarta(p, sorteada.toItemStack());

                        if (sorteada.getRaridade() == Raridade.EPICO) {
                            SocketManager.sendMessage("all", "ganhacarta", p.getName() + ";" + sorteada.getNome());
                            ChatUtils.ganhou(p, sorteada.toItemStack());
                        } else {
                            p.sendMessage(ChatColor.GREEN + "Voce obteve a carta " + sorteada.getDisplayName() + "§r§e !");
                        }

                    }
                }

                ItemUtils.consumeItemInHand(p);
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 99, 1);
                p.getPlayer().updateInventory();

            }

        };
        pacoteini = new CustomItem("Pacote de Carta", Material.FIREWORK_CHARGE, "Ao usar ganha-se uma carta aleatória", ChatColor.RED, '♠', CustomItem.ItemRaridade.COMUM, Color.YELLOW) {

            @Override
            public void interactLobby(Player p) {
                if (p.getInventory().firstEmpty() == -1) {
                    p.sendMessage("§cSeu inventário está cheio!");
                    return;
                }
                Carta sorteada = ControleCartas.getCardByName("Aprendiz epico");
                p.sendMessage(ChatColor.GREEN + "Voce obteve a carta " + sorteada.getDisplayName());
                p.getInventory().addItem(sorteada.toItemStack());
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 99, 1);
                ItemUtils.consumeItemInHand(p);
                p.updateInventory();
            }
        };
        pacoteraro = new CustomItem("Pacote de Carta Raro", Material.FIREWORK_CHARGE, "Ao usar ganha-se pelo menos uma carta rara aleatória", ChatColor.RED, '♠', CustomItem.ItemRaridade.RARO, corraro) {

            @Override
            public void interactLobby(Player p) {
                if (p.getInventory().firstEmpty() == -1) {
                    p.sendMessage("§cSeu inventário está cheio!");
                    return;
                }
                List<Carta> c = new ArrayList<Carta>();

                c.add(ControleCartas.abrePacote(Raridade.RARO));
                int rnd = new Random().nextInt(10);
                if (rnd <= 3) {

                    c.add(ControleCartas.abrePacote(Raridade.COMUM));
                    c.add(ControleCartas.abrePacote(Raridade.INCOMUM));
                } else {
                    c.add(ControleCartas.abrePacote(Raridade.COMUM));
                    c.add(ControleCartas.abrePacote(Raridade.COMUM));

                }
                for (Carta sorteada : c) {
                    if (sorteada != null) {
                        ItemUtils.addCarta(p, sorteada.toItemStack());

                        if (sorteada.getRaridade() == Raridade.EPICO) {
                            SocketManager.sendMessage("all", "ganhacarta", p.getName() + ";" + sorteada.getNome());
                            ChatUtils.ganhou(p, sorteada.toItemStack());
                        } else {
                            p.sendMessage(ChatColor.GREEN + "Voce obteve a carta " + sorteada.getDisplayName() + "§r§e !");
                        }

                    }
                }
                ItemUtils.consumeItemInHand(p);
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 99, 1);
                p.getPlayer().updateInventory();

            }

        };
        pacoteepico = new CustomItem("Pacote de Carta Epico", Material.FIREWORK_CHARGE, "Ao usar ganha-se pelo menos uma carta epica aleatória", ChatColor.RED, '♠', CustomItem.ItemRaridade.EPICO, Color.PURPLE) {

            @Override
            public void interactLobby(Player p) {
                if (p.getInventory().firstEmpty() == -1) {
                    p.sendMessage("§cSeu inventário está cheio!");
                    return;
                }
                List<Carta> c = new ArrayList<Carta>();

                c.add(ControleCartas.abrePacote(Raridade.EPICO));
                int rnd = new Random().nextInt(10);
                if (rnd == 0) {

                    c.add(ControleCartas.abrePacote(Raridade.RARO));
                    c.add(ControleCartas.abrePacote(Raridade.INCOMUM));
                } else {
                    c.add(ControleCartas.abrePacote(Raridade.COMUM));
                    c.add(ControleCartas.abrePacote(Raridade.INCOMUM));

                }
                for (Carta sorteada : c) {
                    if (sorteada != null) {
                        ItemUtils.addCarta(p, sorteada.toItemStack());

                        if (sorteada.getRaridade() == Raridade.EPICO) {
                            SocketManager.sendMessage("all", "ganhacarta", p.getName() + ";" + sorteada.getNome());
                            ChatUtils.ganhou(p, sorteada.toItemStack());
                        } else {
                            p.sendMessage(ChatColor.GREEN + "Voce obteve a carta " + sorteada.getDisplayName() + "§r§e !");
                        }

                    }
                }
                ItemUtils.consumeItemInHand(p);
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 99, 1);
                p.getPlayer().updateInventory();

            }

        };
    }

    public int getEmptySlots(Inventory inventory) {
        int i = 0;
        for (ItemStack is : inventory.getContents()) {
            if (is == null) {
                continue;
            }
            i++;
        }
        return i;
    }

    public static void start() {
        startPacotes();
        cajadomagico = new CajadoMagico();
        bomba = new BombaFumaca();
        icaster = new CustomItem("Lançador de Habilidades", Material.BLAZE_ROD, "Usada para conjurar habilidades", ChatColor.AQUA, '☯', CustomItem.ItemRaridade.COMUM);
        golditem = new GoldItem();
        pacotearmadura = new PacoteArmadura();
        bandagem = new CustomItem("Bandagem", Material.COAL, "Ao usar e nao tomar dano, regenera boa parte da vida.", ChatColor.AQUA, '♠', CustomItem.ItemRaridade.INCOMUM) {

            @Override
            public void interactGame(Player p) {
                if (BandageType.applyBandage(p)) {
                    ItemUtils.consumeItemInHand(p);
                }
            }
        };
        garramadeira = new CustomItem("Garra de Madeira", Material.WOOD_HOE, "+3 de dano", ChatColor.RED, '◈', CustomItem.ItemRaridade.COMUM);
        garrapedra = new CustomItem("Garra de Pedra", Material.STONE_HOE, "+4 de dano", ChatColor.RED, '◈', CustomItem.ItemRaridade.INCOMUM);
        garraferro = new CustomItem("Garra de Ferro", Material.IRON_HOE, "+5 de dano", ChatColor.RED, '◈', CustomItem.ItemRaridade.RARO);
        garraouro = new CustomItem("Garra Dourada", Material.GOLD_HOE, "+6 de dano", ChatColor.RED, '◈', CustomItem.ItemRaridade.EPICO);

    }
}
