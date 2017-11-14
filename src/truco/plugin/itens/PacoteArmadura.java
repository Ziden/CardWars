/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.itens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import truco.plugin.cards.Carta;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.ControleCartas;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.signs.SignUtils;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.Cooldown;
import truco.plugin.socket.SocketManager;
import truco.plugin.utils.IconLib;
import truco.plugin.utils.ItemUtils;

/**
 *
 * @author Júnior
 */
public class PacoteArmadura extends CustomItem {

    public PacoteArmadura() {
        super("Pacote Armadura", Material.FIREWORK_CHARGE, "Ganha uma carta conforme a armadura do pacote!", ChatColor.GOLD, IconLib.STAFFICON.getChar(), CustomItem.ItemRaridade.INCOMUM);
    }

    @Override
    public ItemStack geraItem(int x) {
        return new ItemStack(Material.INK_SACK);
    }

    public ItemStack geraItem(int x, Armadura r) {
        ItemStack i = new ItemStack(ma, 1, (short) data);
        Color cor = getCor(r);
        ItemMeta im = i.getItemMeta();
        if (ma == Material.FIREWORK_CHARGE && im instanceof FireworkEffectMeta) {

            ((FireworkEffectMeta) im).setEffect(FireworkEffect.builder().withColor(cor).build());

        }
        im.setDisplayName(raridade.getCor() + "" + simbolo + "§r §cPacote de " + r.desc + "");
        List<String> newlore = new ArrayList(lore);
        newlore.add("§0§kArmor:" + r);
        im.setLore(newlore);
        i.setItemMeta(im);
        return i;

    }

    @Override
    public void interactLobby(final Player p) {
        if (p.getInventory().firstEmpty() == -1) {
            p.sendMessage("§cInventário lotado esvazie ele para abrir um pacote!");
            return;
        }
        ItemStack item = p.getItemInHand();
        if (item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
            if (item.getItemMeta().getLore().size() == 3) {
                String gold = item.getItemMeta().getLore().get(2).split(":")[1];

                try {

                    Armadura r = Armadura.valueOf(gold);
                    ItemUtils.consumeItemInHand(p);

                    List<Carta> cartas = new ArrayList();
                    cartas.add(ControleCartas.abrePacote(r));
                    cartas.add(ControleCartas.abrePacote());
                    cartas.add(ControleCartas.abrePacote());
                    for (Carta c : cartas) {
                        ItemUtils.addCarta(p, c.toItemStack());

                        if (c.getRaridade() == Carta.Raridade.EPICO) {
                            SocketManager.sendMessage("all", "ganhacarta", p.getName() + ";" + c.getNome());
                            ChatUtils.ganhou(p, c.toItemStack());
                        } else {
                            p.sendMessage(ChatColor.GREEN + "Voce obteve a carta " + c.getDisplayName() + "§r§e !");

                        }
                    }
                    p.playSound(p.getLocation(), Sound.LEVEL_UP, 99, 1);
                    p.updateInventory();
                } catch (IllegalArgumentException e) {
                    p.sendMessage("§cContactar algum admin item bugado!");
                }
                p.updateInventory();

            }
        }

    }

    public static Color getCor(Armadura r) {
        Color cor;
        if (r.equals(Armadura.FERRO)) {
            cor = Color.fromRGB(245, 245, 245);
        } else if (r.equals(Armadura.CHAIN)) {
            cor = Color.fromRGB(212, 199, 204);
        } else if (r.equals(Armadura.DIMA)) {
            cor = Color.fromRGB(0, 255, 242);
        } else if (r.equals(Armadura.LEATHER)) {
            cor = Color.fromRGB(171, 101, 101);
        } else if (r.equals(Armadura.OURO)) {
            cor = Color.fromRGB(255, 208, 0);
        } else if (r.equals(Armadura.PELADO)) {
            cor = Color.fromRGB(214, 182, 194);
        } else if (r.equals(Armadura.TODOS)) {
            cor = Color.fromRGB(50, 99, 50);
        } else {
            cor = Color.WHITE;
        }
        return cor;
    }
}
