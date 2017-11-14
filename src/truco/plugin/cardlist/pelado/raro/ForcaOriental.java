/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.pelado.raro;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.ItemUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ForcaOriental extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Forca Oriental";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+3 de dano com garras ou punhos", "recebe +1 de dano em tudo"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO;
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
        if (ev.getCause() != CustomDamageEvent.CausaDano.ATAQUE) {
            return;
        }
        if (ItemUtils.isClaw(donoDaCarta.getItemInHand()) || ItemUtils.isArm(donoDaCarta.getItemInHand())) {
            ev.addDamage(3);
        }
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        ev.addDamage(1);
    }

}
