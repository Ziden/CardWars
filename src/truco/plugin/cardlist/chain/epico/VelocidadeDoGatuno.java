package truco.plugin.cardlist.chain.epico;

import org.bukkit.event.entity.EntityShootBowEvent;
import truco.plugin.cards.Carta;

/**
 *
 * @author Carlos André Feldmann Júnior
 *
 */
public class VelocidadeDoGatuno extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Dedos Ligeiros";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Atira flechas com força", "sem precisar carregar"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.CHAIN;
    }

    @Override
    public void playerTocaFlecha(EntityShootBowEvent ev) {

        ev.getProjectile().setVelocity(ev.getProjectile().getVelocity().normalize().multiply(5));
    }

}
