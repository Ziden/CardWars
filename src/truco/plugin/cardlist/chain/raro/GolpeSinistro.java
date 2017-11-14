package truco.plugin.cardlist.chain.raro;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;
import truco.plugin.utils.LocUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class GolpeSinistro extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Golpes Sinistros de Ubaj";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 dano com espadas de madeira ", "+ 3 pelas costas"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.CHAIN;
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
        if (ev.getCause() != CausaDano.ATAQUE) {
            return;
        }
        if (donoDaCarta.getItemInHand().getType() == Material.WOOD_SWORD) {

            int danobonus = 1;
            double angle = LocUtils.getAngle(donoDaCarta.getLocation().getDirection(), ev.getTomou().getLocation().getDirection());
            if (angle < 70) {
                danobonus += 2;

                ev.getTomou().getWorld().playEffect(ev.getTomou().getLocation(), Effect.CRIT, 1);
            }
            ev.addDamage(danobonus);
        }

    }
}
