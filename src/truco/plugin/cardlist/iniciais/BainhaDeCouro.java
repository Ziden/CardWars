
package truco.plugin.cardlist.iniciais;



import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.cards.EventosCartas;


/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class BainhaDeCouro extends Carta{


    @Override
    public Raridade getRaridade() {
    return Raridade.COMUM;
    }


    @Override
    public String getNome() {
    return "Bainha de Couro";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"+ 2 dano flechas","+ 2 flechas"};
    }
    
    public ItemStack [] getItems() {
        return new ItemStack[] {
           new ItemStack(Material.ARROW, 2)
        };
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
