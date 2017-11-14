package truco.plugin.cardlist.peladoOuro;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.functions.game.Mana;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class KiAumentado extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Ki Aumentado";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+2 dano fisico, precisa de mana para atacar"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO_OURO;
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
        if (ev.getCause() != CustomDamageEvent.CausaDano.ATAQUE) {
            return;
        }
        if (Mana.spendMana(donoDaCarta, 5)) {
            ev.addDamage(2);
        } else {
            ev.setCancelled("Ki Aumentado Sem Mana");
        }

    }

}
