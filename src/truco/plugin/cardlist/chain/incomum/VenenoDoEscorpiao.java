package truco.plugin.cardlist.chain.incomum;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.cards.EfeitoProjetil;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class VenenoDoEscorpiao extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Veneno do Escorpiao";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 5 dano com flechas", "em alvos envenenados"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.CHAIN;
    }

    @Override
    public void playerTocaFlecha(EntityShootBowEvent ev) {
        Player p = (Player) ev.getEntity();
        final Carta car = this;

        EfeitoProjetil.addEfeito((Projectile) ev.getProjectile(), new EfeitoProjetil(p, (Projectile) ev.getProjectile()) {

            @Override
            public void causaEfeito(Player gotHit, Player Shooter, Projectile projectile) {
                if (gotHit.hasPotionEffect(PotionEffectType.POISON)) {
                    DamageManager.damage(5, Shooter, gotHit, null, CustomDamageEvent.CausaDano.SKILL_ATAQUE, false, car);
                }
            }
        });
    }
}
