
package truco.plugin.cardlist.chain.raro;



import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;

public class AdagaDoLadino extends Carta {


    @Override
    public Raridade getRaridade() {
    return Carta.Raridade.RARO;
    }


    @Override
    public String getNome() {
        return "Adaga do Ladino";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 espada de madeira afiada II"};
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.CHAIN;
    }
  
    public ItemStack [] getItems() {
        ItemStack ss = new ItemStack(Material.WOOD_SWORD);
        ss.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        return new ItemStack[]{ss};
    }

}
