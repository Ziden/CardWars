/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.chain.comum;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import truco.plugin.cards.Carta;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ArmaduraReforcada extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Armadura Reforcada";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 20% resist. flechas"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.CHAIN_FERRO;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        if (ev.getProjetil() != null && ev.getProjetil() instanceof Arrow) {
            ev.addDamageMult(0.80, "Armadura Reforcada");
        }
    }

}
