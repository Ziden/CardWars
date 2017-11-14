/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.epico;

import java.util.Set;
import org.bukkit.Effect;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.data.MetaShit;
import truco.plugin.utils.TeamUtils;
import truco.plugin.cards.StatusEffect;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class AliadodoUniverso extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }
    Skill s = new Skill(this, 20, 20) {

        @Override
        public String getName() {
            return "Bola Mostruosa";
        }

        @Override
        public boolean onCast(final Player p) {
            Block target = p.getTargetBlock((Set<Material>) null, 20);
            if (target == null || target.getType() == Material.AIR) {
                p.sendMessage("§aSelecione um bloco valido!");
                return false;
            }


            final Block b = target.getWorld().getHighestBlockAt(target.getLocation());
            Location spawn = b.getLocation().clone().add(0, 12, 0);
            spawn.setDirection(new Vector(0, -0.1, 0));
            Zombie zb = target.getWorld().spawn(spawn, Zombie.class);
            zb.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 2, 1));
            Fireball fb = zb.launchProjectile(Fireball.class);
            fb.setVelocity(new Vector(0, -1.4, 0));
            fb.setShooter(p);
            zb.remove();

            MetaShit.setMetaObject("bolamostruosa", fb, true);
            MetaShit.setMetaObject("magia", fb, true);

            p.sendMessage("§dO grande universo lhe atendeu seu pedido e enviou uma bola de fogo!");
            return true;
        }
    };

    @Override
    public String getNome() {
        return "Aliado do Universo";
    }

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public void projetilBateEmAlgo(ProjectileHitEvent ev) {
        if (ev.getEntity().hasMetadata("bolamostruosa")) {
            Player shooter = (Player) ev.getEntity().getShooter();
            
            ev.getEntity().getWorld().playEffect(ev.getEntity().getLocation(), Effect.EXPLOSION_LARGE, 1);
            for (Entity ent : ev.getEntity().getNearbyEntities(5, 5, 5)) {
                if (ent instanceof Player) {
                    Player alvo = (Player) ent;
                    if (TeamUtils.canAttack(shooter, alvo)) {
                        alvo.sendMessage("§cUma bola gigante lhe acertou!");
                        StatusEffect.addStatusEffect(alvo, StatusEffect.StatusMod.STUN, 4);
                        DamageManager.damage(13, shooter, alvo, CustomDamageEvent.CausaDano.MAGIA_RAIO, "Bola Monstruosa");

                    }
                }
            }
        }
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Invoca uma bola de fogo", "que cai do céu causando dano ", "de fogo em area e dando stun"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }
}
