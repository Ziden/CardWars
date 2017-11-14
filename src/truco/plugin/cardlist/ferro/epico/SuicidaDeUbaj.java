/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.ferro.epico;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDeathEvent;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.TeamUtils;
import truco.plugin.utils.efeitos.CustomEntityFirework;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class SuicidaDeUbaj extends Carta {

    public static ArrayList<UUID> suicidas = new ArrayList();

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Suicida de Ubaj";
    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                if (suicidas.isEmpty()) {
                    return;
                }
                for (UUID uid : suicidas) {
                    Player p = Bukkit.getPlayer(uid);
                    if (p != null) {

                        CustomEntityFirework.spawn(p.getLocation(), FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.RED).build());

                    }
                }
            }
        }, 300, 20);
    }
    Skill s = new Skill(this, 40, 100) {

        @Override
        public String getName() {
            return "Suicidio";
        }

        @Override
        public boolean onCast(final Player p) {
            if (suicidas.contains(p.getUniqueId())) {
                return false;
            }
            p.getInventory().setHelmet(new ItemStack(Material.TNT));
            p.updateInventory();
            suicidas.add(p.getUniqueId());
            ChatUtils.tellAction(p, "vai se explodir");
            p.sendMessage("§eEm 5 segundos você vai virar um passarinho!");
            final UUID puid = p.getUniqueId();
            Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    if (!suicidas.contains(puid)) {
                        return;
                    }
                    suicidas.remove(puid);
                    if (p != null) {
                        p.getInventory().setHelmet(null);
                        p.getWorld().playEffect(p.getLocation().add(0, 2, 0), Effect.EXPLOSION_HUGE, 1);
                        p.getWorld().playSound(p.getLocation(), Sound.EXPLODE, 4, 1);
                        for (Entity e : p.getNearbyEntities(7, 7, 7)) {
                            if (e instanceof Player) {
                                if (TeamUtils.canAttack(p, (Player) e)) {
                                    DamageManager.damage(27, p, (Player) e, CustomDamageEvent.CausaDano.SKILL_ATAQUE, "Suicida de Ubaj");
                                }
                            }
                        }

                        p.setVelocity(new Vector(0, 6, 0));
                        DamageManager.causaDanoBruto(null, p, p.getMaxHealth(), "Suicida de Ubaj");
                    }
                }
            }, 20 * 5);
            return true;
        }
    };

    @Override
    public String[] getDesc() {
        return new String[]{"Coloca uma bomba em sua cabeça", "que explode em 5 segundos"};
    }

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.FERRO;
    }

    @Override
    public void playerDeath(CustomDeathEvent ev) {
        if (suicidas.contains(ev.getPlayer().getUniqueId())) {
            suicidas.remove(ev.getPlayer().getUniqueId());
        }
    }
}
