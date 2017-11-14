
package truco.plugin.cardlist.chain.comum;



import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import truco.plugin.itens.Items;

public class EsconderijoDeBraton extends Carta{


    @Override
    public Raridade getRaridade() {
    return Carta.Raridade.COMUM;
    }


    @Override
    public String getNome() {
    return "Fumaca de Braton";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 Bomba de Fumaca"};
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.CHAIN;
    }
  
    public ItemStack [] getItems() {
        return new ItemStack[]{Items.bomba.geraItem(1)};
    }
    

}
