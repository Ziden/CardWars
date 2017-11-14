
package truco.plugin.cardlist.ouro.comum;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;

/**
 *
 * @author Carlos André Feldmann Júnior
 * 
 */

public class JardimDoEden extends Carta{

    @Override
    public Raridade getRaridade() {
        return Carta.Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Jardim do Eden";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 maça dourada"};
    }
    
    @Override
    public ItemStack [] getItems() {
        
        return new ItemStack[]{
            new ItemStack(Material.GOLDEN_APPLE)
        };
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.OURO;
    }

}
