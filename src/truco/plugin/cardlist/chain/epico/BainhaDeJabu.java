
package truco.plugin.cardlist.chain.epico;



import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EfeitoProjetil;
import truco.plugin.cards.EventosCartas;


/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class BainhaDeJabu extends Carta{


    @Override
    public Raridade getRaridade() {
    return Raridade.EPICO;
    }


    @Override
    public String getNome() {
    return "Bainha Abencoada de Jabu";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 dano com flechas","+ 2 dano com flecha a distancia","+ 20 flechas"};
    }
    
    public ItemStack [] getItems() {
        return new ItemStack[] {
           new ItemStack(Material.ARROW, 20)
        };
    }


    @Override
    public Armadura getArmadura() {
    return Armadura.CHAIN;
    }


   @Override
    public void playerTocaFlecha(EntityShootBowEvent ev) {
       EventosCartas.modificaDanoProjetil((Projectile)(ev.getProjectile()), 2);
        final Location ini = ev.getEntity().getLocation();
        EfeitoProjetil.addEfeito((Projectile) ev.getProjectile(), new EfeitoProjetil((Player) ev.getEntity(), (Projectile) ev.getProjectile()) {

            @Override
            public void causaEfeito(Player gotHit, Player Shooter, Projectile projectile) {
                if (ini.distance(gotHit.getLocation()) > 10) {
                    EventosCartas.modificaDanoProjetil(projectile, 2);
                }
            }
        });
    }

   

}
