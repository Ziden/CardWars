package truco.plugin.cardlist.ferro.incomum;

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
public class PeleRochosa extends Carta {

    @Override
    public Raridade getRaridade() {
        return Carta.Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Pernas de Zeratul";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 5% esquiva dano"};
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.CHAIN_FERRO;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        Player p = (Player) ev.getPlayerTomou();
        int c = CardWarsPlugin.random.nextInt(99);
        if (c <= 4) {
            p.sendMessage("§b* você se esquivou do ataque *");
            ev.setCancelled("Pele Rochosa");

        }
    }
}
