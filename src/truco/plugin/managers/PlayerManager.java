/*

 */
package truco.plugin.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import truco.plugin.CardWarsPlugin;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.arena.Arena;
import truco.plugin.arena.Arena.Team;
import truco.plugin.arena.GameState;
import truco.plugin.cardlist.pelado.raro.CorpoFechado;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.cards.StatusEffect;
import truco.plugin.data.MetaShit;
import truco.plugin.functions.Cooldown;
import truco.plugin.itens.CustomItem;
import truco.plugin.itens.Items;

import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.menus.ArmorSelectMenu.Armadura;
import truco.plugin.menus.MenuUtils;

import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.ItemUtils;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PlayerManager {

    public static HashMap<Player, HashMap<Integer, ItemStack>> itensquickbar = new HashMap<Player, HashMap<Integer, ItemStack>>();
    public static HashMap<UUID, Integer> last = new HashMap();

    public static void setArmor(Player p, final Armadura arm) {
        if (MatchMaker.db.getArmor(p.getUniqueId()).equals(arm)) {
            p.sendMessage("§a§lVocê já está equipado com está armadura!");
            return;
        }
        p.getInventory().setArmorContents(arm.getArmorContents());
        final UUID uuid = p.getUniqueId();

        new Thread(new Runnable() {

            @Override
            public void run() {
                MatchMaker.db.setArmor(uuid, arm);
            }
        }).start();
        p.playSound(p.getLocation(), Sound.NOTE_PLING, Integer.MAX_VALUE, 2);
        p.updateInventory();
        p.closeInventory();
        String msg = "§aVocê equipou a §c" + arm.getName() + "§a !";
        if (arm == Armadura.PELADO) {
            msg = "§aVocê §cremoveu sua armadura §a!";
        }
        ChatUtils.sendMessage(p, msg);
        for (int x = 0; x < 9; x++) {
            ItemStack carta = p.getEnderChest().getItem(x);
            if (carta != null) {
                ItemUtils.addCarta(p, carta);
                p.getEnderChest().setItem(x, null);
            }
        }
    }

    public static boolean podeUsar(Player p) {
        if (server == CardWarsPlugin.ServerType.LOBBY) {
            return false;
        }
        if (p.getVehicle() != null) {
            return false;
        }

        if (CardWarsPlugin.getArena() != null) {
            if (CardWarsPlugin.getArena().getTeam(p.getUniqueId()) != null) {
                if (CardWarsPlugin.getArena().getTeam(p.getUniqueId()) != Team.SPEC) {
                    if (CardWarsPlugin.getArena().getState() != GameState.INGAME) {
                        return false;
                    }
                }
            }
            if (CardWarsPlugin.getArena().getSpecs().contains(p.getUniqueId())) {
                ChatUtils.sendMessage(p, "§cEspetadores não podem usar skills");
                return false;
            }
        }
        if (CorpoFechado.afetados.contains(p.getUniqueId())) {
            p.sendMessage("§aVocê ativou corpo blindado, você não pode usar skills!");
            return false;
        }
        if (StatusEffect.hasStatusEffect(p, StatusEffect.StatusMod.SILENCE)) {
            Utils.sendTitle(p, "§9§lSILENCE", "§7Você está silenciado!", 0, 20, 0);
            return false;

        }
        if (StatusEffect.hasStatusEffect(p, StatusEffect.StatusMod.STUN)) {

            Utils.sendTitle(p, "§1§lSTUN", "§7Você está atordoado!!", 0, 20, 0);
            return false;
        }
        if (p.getVehicle() != null) {
            ChatUtils.sendMessage(p, "§cVocê não pode usar magias em cima de uma montaria!");
            return false;

        }

        return true;
    }

    public static void preparaSkill(final Player p) {
        if (p.getInventory().getHeldItemSlot() == 0) {
            if (!podeUsar(p)) {
                return;
            }
            ArrayList<Carta> cartas = ControleCartas.getCartas(p);
            HashMap<Integer, ItemStack> hotbar = new HashMap();
            for (int x = 0; x < 9; x++) {
                hotbar.put(x, p.getInventory().getItem(x));
                Carta c = cartas.get(x);
                if (c != null && c.getSkill() != null) {
                    ItemStack i = c.toItemStack().clone();
                    ItemMeta im = i.getItemMeta();
                    im.setDisplayName("§a§l" + c.getSkill().getName() + "§9 M:" + c.getSkill().mana + " §5CD:" + c.getSkill().cd);
                    i.setItemMeta(im);
                    p.getInventory().setItem(x, MenuUtils.getItemIlusorio(i));
                } else {
                    ItemStack it = new ItemStack(Material.INK_SACK);
                    ItemMeta im = it.getItemMeta();
                    im.setDisplayName("§c§lNenhuma Carta Ativa!");
                    it.setItemMeta(im);
                    p.getInventory().setItem(x, MenuUtils.getItemIlusorio(it));

                }

            }

            if (last.containsKey(p.getUniqueId())) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                    @Override
                    public void run() {
                        p.getInventory().setHeldItemSlot(last.get(p.getUniqueId()));
                    }
                }, 1);
            }
            itensquickbar.put(p, hotbar);
            p.updateInventory();
            int task = Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    acabaConj(p);
                    p.sendMessage("§b§lSeu tempo de conjuração acabou!");
                }
            }, 20 * 10);
            MetaShit.setMetaObject("cspell", p, task);
            p.sendMessage("§aAgora você pode usar uma habilidade!");
            Cooldown.addCoolDown(p, "cspellcdd", 750);
        }
    }

    public static void acabaConj(Player p) {
        if (itensquickbar.containsKey(p) && p.hasMetadata("cspell")) {
            p.removeMetadata("cspell", CardWarsPlugin._instance);
            for (int x = 0; x < 9; x++) {
                ItemStack it = itensquickbar.get(p).get(x);
                p.getInventory().setItem(x, it);

            }
            last.put(p.getUniqueId(), p.getInventory().getHeldItemSlot());
            itensquickbar.remove(p);
            p.updateInventory();

        }
    }

    public static void ActiveSkill(final Player p, int skill) {
        if (Cooldown.isCooldown(p, "cspellcdd")) {
            return;
        }

        if (!podeUsar(p)) {
            return;
        }
        int task = (int) MetaShit.getMetaObject("cspell", p);
        Bukkit.getScheduler().cancelTask(task);

        Carta c = ControleCartas.getCartas(p).get(skill);
        last.put(p.getUniqueId(), skill);
        p.removeMetadata("cspell", CardWarsPlugin._instance);
        if (itensquickbar.containsKey(p)) {

            for (int x = 0; x < 9; x++) {
                ItemStack it = itensquickbar.get(p).get(x);
                p.getInventory().setItem(x, it);

            }
            itensquickbar.remove(p);
            p.updateInventory();
        }
        if (c != null) {
            if (c.getSkill() != null) {
                c.getSkill().castSkill(p);
            } else {
                p.sendMessage("§c§lEstá é uma carta passiva e não possui uma habilidade!");
            }

        } else {
            p.sendMessage("§c§lVocê não possui nenhuma carta neste slot!");
        }

    }

    public static void usaSkill(Player p) {

        if (!p.hasMetadata("cspell")) {

            String citem = CustomItem.getItem(p.getItemInHand());
            if (citem != null && citem.equalsIgnoreCase(Items.icaster.getName())) {
                boolean pode = podeUsar(p);
                if (pode) {
                    preparaSkill(p);
                }
            }
        } else {
            boolean pode = podeUsar(p);
            if (p.getInventory().getHeldItemSlot() < 9) {
                if (pode) {
                    ActiveSkill(p, p.getInventory().getHeldItemSlot());
                    Cooldown.addCoolDown(p, "CajadoMagicoCd", 3000);
                }
            }
        }

    }
}
