package truco.plugin.itens;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import truco.plugin.CardWarsPlugin;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.ItemUtils;
import truco.plugin.functions.MakeVanish;
import truco.plugin.data.MetaShit;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.functions.Cooldown;

public class BombaFumaca extends CustomItem {

    public BombaFumaca() {
        super("Bomba de Fumaça", Material.SLIME_BALL, "Fica invisivel por alguns segundos", ChatColor.AQUA, '☯', CustomItem.ItemRaridade.INCOMUM);
    }

    @Override
    public void interactGame(final Player p) {
        if (Cooldown.isCooldown(p, "bombadefumacacd")) {
            p.sendMessage("Espere um pouco para usar outra bomba de fumaça!");
            return;
        }
        Cooldown.addCoolDown(p, "bombadefumacacd", 9000);
        MakeVanish.makeVanished(p);
        p.getWorld().playEffect(p.getLocation(), Effect.EXTINGUISH, 10);
        p.sendMessage(ChatColor.GREEN + "Voce sumiu !");
        ChatUtils.tellAction(p, " se escondeu ");
        ItemUtils.consumeItemInHand(p);
        final UUID playeruuuid = p.getUniqueId();
        final String nome = p.getName();
        int task = Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                if (MakeVanish.vanished.contains(nome)) {
                    MakeVanish.makeVisible(playeruuuid);
                    if (p != null) {
                        p.removeMetadata("bombadefumaca", CardWarsPlugin._instance);
                        p.sendMessage(ChatColor.RED + "Voce se revelou !");
                    }
                }
            }
        }, 20 * 7);
        MetaShit.setMetaObject("bombadefumaca", p, task);
    }

    public static void revela(Player p) {
        if (MakeVanish.isVanished(p) && p.hasMetadata("bombadefumaca")) {
            MakeVanish.makeVisible(p);
            Bukkit.getScheduler().cancelTask((int) MetaShit.getMetaObject("bombadefumaca", p));
            p.removeMetadata("bombadefumaca", CardWarsPlugin._instance);

            p.sendMessage(ChatColor.RED + "Voce se revelou !");

        }
    }

    public static void atiraFlecha(EntityShootBowEvent ev) {
        if (!(ev.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) ev.getEntity();
        revela(p);

    }

    public static void tomaDano(CustomDamageEvent ev) {
        if (ev.getTomou().getType() == EntityType.PLAYER) {
            Player p = (Player) ev.getTomou();
            revela(p);

        }
    }

    public static void bate(CustomDamageEvent ev) {
        if (ev.getTomou().getType() == EntityType.PLAYER) {
            if (ev.getBateu().getType() == EntityType.PLAYER) {
                Player p = (Player) ev.getBateu();
                revela(p);
            }
        }
    }
}
