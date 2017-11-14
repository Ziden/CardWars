
package truco.plugin.cardlist.dima.comum;

import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import truco.plugin.itens.Items;

/**
 *
 * @author Carlos André Feldmann Júnior
 * 
 */

public class BandagensDeJabu extends Carta{

    @Override
    public Raridade getRaridade() {
        return Carta.Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Bandagens de Jabu";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 bandagem"};
    }
    
    @Override
    public ItemStack [] getItems() {
        
        return new ItemStack[]{Items.bandagem.geraItem(1)};
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.DIMA;
    }

}
