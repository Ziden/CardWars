/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.pelado.incomum;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EfeitoProjetil;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cardlist.pelado.BolasDeEnergia;
import truco.plugin.data.MetaShit;
import truco.plugin.cards.skills.skilltypes.Channeling;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class BolaInquieta extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Bola Inquieta";
    }

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public String[] getDesc() {
        return new String[]{"arremeca todas suas bolas de energia", "causa dano bonus a cada bola"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO;
    }
    Skill s = new Skill(this, 12, 20) {

        @Override
        public String getName() {
            return "Juntamento de Energia";
        }

        @Override
        public int getChannelingTime() {
            return 3;
        }

        @Override
        public boolean onCast(final Player p) {
            final int tem = BolasDeEnergia.getBolas(p);
            if (tem < 1) {
                p.sendMessage("§eVocê precisa de pelo menos 1 bola de energia!");
                return false;
            }

            BolasDeEnergia.removeBolas(p);

            new Channeling(p, 3, this, new Runnable() {

                @Override
                public void run() {
                    Snowball s = p.launchProjectile(Snowball.class);
                    s.setVelocity(s.getVelocity().multiply(3));
                    MetaShit.setMetaObject("magia", s, true);
                    s.setShooter(p);
                    s.setFireTicks(Integer.MAX_VALUE);
                    EfeitoProjetil.addEfeito(s, new EfeitoProjetil(p, s) {

                        @Override
                        public void causaEfeito(Player gotHit, Player Shooter, Projectile projectile) {
                           
                            DamageManager.damage(6*tem, Shooter, gotHit, CustomDamageEvent.CausaDano.MAGIA, "Bola Inquieta");
                        }
                    });
                }
            });
            return true;
        }
    };

}
