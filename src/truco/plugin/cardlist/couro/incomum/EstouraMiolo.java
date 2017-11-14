/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.incomum;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Snowball;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EfeitoProjetil;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cards.StatusEffect;
import truco.plugin.damage.DamageManager;
import truco.plugin.data.MetaShit;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class EstouraMiolo extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Estoura Miolo";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Chuva de gelo em volta de você", "causa dano de agua", "e congela por 2s"};
    }

    @Override
    public Skill getSkill() {
        return new Skill(this, 20, 10) {

            @Override
            public String getName() {
                return "Tempestade de Neve";
            }

            @Override
            public boolean onCast(Player p) {
                Tempestade task = new Tempestade(this, p);
                int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, task, 10, 10);

                task.taskId = id;
                return true;
            }
        };
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }

    private class Tempestade implements Runnable {

        private Player p;
        private Location l;
        private List<UUID> machucados = new ArrayList();
        private int taskId;
        private int vezes = 0;

        public Tempestade(Skill s, Player p) {
            this.p = p;
            this.l = p.getLocation();
        }

        @Override
        public void run() {
            vezes++;
           
            for (int x = 0; x < 9; x++) {
                int randomx = CardWarsPlugin.random.nextInt(6);
                if (CardWarsPlugin.random.nextBoolean()) {
                    randomx *= -1;
                }

                int randomz = CardWarsPlugin.random.nextInt(6);
                if (CardWarsPlugin.random.nextBoolean()) {
                    randomz *= -1;
                }
                Location loc = l.clone();
                loc.setX(loc.getX() + randomx);
                loc.setZ(loc.getZ() + randomz);
               

                Location spawn = loc.add(0, 16, 0);
                spawn.setDirection(new Vector(0, -0.1, 0));
                Silverfish zb = loc.getWorld().spawn(spawn, Silverfish.class);
                zb.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 2, 1));
                Snowball fb = zb.launchProjectile(Snowball.class);
                zb.remove();
                fb.setShooter(p);
                fb.setVelocity(new Vector(0, -0.1, 0));
                MetaShit.setMetaObject("magia", fb, true);
                EfeitoProjetil.addEfeito(fb, new EfeitoProjetil(p, fb) {

                    @Override
                    public void causaEfeito(Player gotHit, Player Shooter, Projectile projectile) {
                        StatusEffect.addStatusEffect(gotHit, StatusEffect.StatusMod.CONGELADO, 2);
                        DamageManager.damage(10, Shooter, gotHit, CustomDamageEvent.CausaDano.MAGIA_AGUA, "Estoura Miolo");
                   
                    }
                });

            }
            if (vezes == 16) {
                Bukkit.getScheduler().cancelTask(taskId);
            }
        }
    }

}
