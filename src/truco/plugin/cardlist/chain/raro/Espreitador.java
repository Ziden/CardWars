package truco.plugin.cardlist.chain.raro;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.CardWarsPlugin;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class Espreitador extends Carta {

    @Override
    public Raridade getRaridade() {
        return Carta.Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Manobras do Mestre Chii";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 15% Esquiva Dano"};
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.CHAIN;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        Player p = (Player) ev.getPlayerTomou();
        int c = CardWarsPlugin.random.nextInt(99);
        if (c <= 14) {
           
                p.sendMessage("§b* você se esquivou do ataque *");
                ev.setCancelled("Esquivou");
            
        }
    }
}
