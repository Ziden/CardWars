
package truco.plugin.cardlist.iniciais;



import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;


/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ArcoTosco extends Carta{


    @Override
    public Raridade getRaridade() {
    return Raridade.COMUM;
    }


    @Override
    public String getNome() {
    return "Arco Tosco";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 arco","+ 4 flechas"};
    }
    
    public ItemStack [] getItems() {
        return new ItemStack[] {
            new ItemStack(Material.BOW, 1),
           new ItemStack(Material.ARROW, 4)
        };
    }


    @Override
    public Armadura getArmadura() {
        return Armadura.TODOS;
    }

}
