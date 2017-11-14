package truco.plugin.cardlist.dima.raro;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import truco.plugin.cards.CC;
import truco.plugin.cards.Carta;

import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cards.skills.skilllist.Investida;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.TeamUtils;
import truco.plugin.utils.efeitos.ParticleEffect;

/**
 *
 * @author Carlos André Feldmann Júnior
 *
 */
public class InvestidaDePedra extends Carta {

    Investida i = new Investida(this, 20, 14);

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Investida de Pedra";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Se joga para frente até atingir um inimigo"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.DIMA;
    }

    public Skill getSkill() {
        return i;
    }

    @Override
    public void move(PlayerMoveEvent ev) {

        if (ev.getPlayer().hasMetadata("investidadepedra")) {
            if (ev.getFrom().getX() == ev.getTo().getX() && ev.getFrom().getY() == ev.getTo().getY() && ev.getFrom().getZ() == ev.getTo().getZ()) {
                return;
            }
            Block from = ev.getFrom().getBlock();
            Block to = ev.getTo().getBlock();
            if (to.getLocation() != from.getLocation()) {
                ParticleEffect.FLAME.display((float) 0.5, (float) 0.5, (float) 0.5, 0, 5, to.getLocation(), 32);
            }
            int x = 0;
            HashSet<LivingEntity> ents = new HashSet<LivingEntity>();
            for (Entity t : ev.getPlayer().getNearbyEntities(2, 2, 2)) {
                if (t instanceof LivingEntity) {
                    if (t instanceof Player) {
                        if (!TeamUtils.canAttack(ev.getPlayer(), (Player) t)) {
                            continue;
                        }
                    }
                    x++;
                    ents.add((LivingEntity) t);
                }
            }
            if (x > 0) {
                Bukkit.getScheduler().cancelTask((int) ev.getPlayer().getMetadata("investidadepedra").get(0).value());
                ev.getPlayer().removeMetadata("investidadepedra", CardWarsPlugin._instance);
                ev.getPlayer().getWorld().playSound(ev.getPlayer().getLocation(), Sound.EXPLODE, 5, 5);
                ev.getPlayer().getWorld().playEffect(ev.getPlayer().getLocation(), Effect.EXPLOSION_LARGE, 1);

                ev.getPlayer().setVelocity(new Vector(0, 0, 0));
                ev.getPlayer().setFallDistance(0);
                ev.getPlayer().teleport(ev.getPlayer().getLocation());

                for (LivingEntity t : ents) {
                    CC.tocaPracima(t, 1);
                    DamageManager.damage(2, ev.getPlayer(), t, CustomDamageEvent.CausaDano.SKILL_ATAQUE, "Investida de Pedra");
                    CC.tacaSlow(t, 1, 5 * 20);
                    CC.tacaCegueira(t, 20 * 2);
                    if (t instanceof Player) {
                        ((Player) t).sendMessage(ChatColor.AQUA + "* um louco se jogou em você *");
                    }
                }
            }

        }
    }
}
