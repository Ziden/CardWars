/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.pelado.comum;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cardlist.pelado.BolasDeEnergia;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CorpoEnergizado extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Corpo Energizado";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"10% de chance de receber", "uma bola de energia ao tomar dano"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        if (ev.getBateu() == null) {
            return;
        }
        int rnd = CardWarsPlugin.random.nextInt(100);
        if (rnd < 10) {
            BolasDeEnergia.addBola(ev.getPlayerTomou());
        }

    }

}
