
package truco.plugin.cardlist.chain.incomum;



import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.cards.EventosCartas;


/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class FlechaPontiaguda extends Carta{


    @Override
    public Raridade getRaridade() {
    return Raridade.INCOMUM;
    }


    @Override
    public String getNome() {
    return "Flecha Pontiaguda";
    }


    @Override
    public String[] getDesc() {
    return new String[]{"+ 2 dano com flechas"};
    }


    @Override
    public Armadura getArmadura() {
    return Armadura.CHAIN;
    }


    @Override
    public void playerTocaFlecha(EntityShootBowEvent ev) {
	if(ControleCartas.canUseCard((Player)ev.getEntity(), this)){
           EventosCartas.modificaDanoProjetil((Projectile)ev.getProjectile(),2);
	}
    }
   

}
