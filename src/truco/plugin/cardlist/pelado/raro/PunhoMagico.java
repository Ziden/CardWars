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
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PunhoMagico extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Punho Magico";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"cada ataque regenera 1 de vida"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO;
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
        if (ev.getCause() == CustomDamageEvent.CausaDano.ATAQUE) {
            DamageManager.cura(donoDaCarta, 1);
        }
    }

}
