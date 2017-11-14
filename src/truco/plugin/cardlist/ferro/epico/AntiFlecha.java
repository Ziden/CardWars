package truco.plugin.cardlist.ferro.epico;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Michael Markus Ackermann
 */
public class AntiFlecha extends Carta {

    @Override
    public Raridade getRaridade() {
        return Carta.Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Casco de Zeratul";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 20% esquiva dano de flechas"};
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.FERRO;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        if (ev.getProjetil()!=null && ev.getProjetil().getType()==EntityType.ARROW) {
             
                int c = CardWarsPlugin.random.nextInt(99);
                if (c <= 19) {
                    ev.getPlayerTomou().sendMessage("§b* você se esquivou da flecha");
                    ev.setCancelled("Anti Flecha");

                }
            }
        }

    

}
