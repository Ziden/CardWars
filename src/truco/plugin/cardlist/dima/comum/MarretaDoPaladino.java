
package truco.plugin.cardlist.dima.comum;



import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;


/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MarretaDoPaladino extends Carta{


    @Override
    public Raridade getRaridade() {
    return Carta.Raridade.COMUM;
    }


    @Override
    public String getNome() {
    return "Marreta Paladinica";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"+1 pa de pedra"};
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.DIMA;
    }
  
    public ItemStack [] getItems() {
        return new ItemStack[]{new ItemStack(Material.STONE_SPADE)};
    }
    

}
