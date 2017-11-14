
package truco.plugin.cardlist.chain.comum;



import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;

public class PerolaNegra extends Carta {


    @Override
    public Raridade getRaridade() {
    return Carta.Raridade.COMUM;
    }


    @Override
    public String getNome() {
        return "Perola Negra";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 Ender Pearl"};
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.CHAIN;
    }
  
    public ItemStack [] getItems() {
        return new ItemStack[]{new ItemStack(Material.ENDER_PEARL, 1)};
    }

}
