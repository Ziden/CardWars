package truco.plugin.cardlist.chain.incomum;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ArcoAfiado extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Arco Afiado";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 3 dano ao bater com um arco"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.CHAIN;
    }

    @Override
    public void causaDano(Player causador, CustomDamageEvent ev) {
        if(ev.getCause()!=CustomDamageEvent.CausaDano.ATAQUE){
            return;
        }
        if (causador.getItemInHand().getType() == Material.BOW) {
            ev.addDamage(3);
        }
    }
}
