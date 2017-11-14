
package truco.plugin.cardlist.chain.epico;



import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;

public class AdagaMestra extends Carta {


    @Override
    public Raridade getRaridade() {
    return Carta.Raridade.EPICO;
    }


    @Override
    public String getNome() {
        return "Adaga Mestra de Jabu";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 espada de madeira afiada III"};
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.CHAIN;
    }
  
    public ItemStack [] getItems() {
        ItemStack ss = new ItemStack(Material.WOOD_SWORD);
        ss.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        return new ItemStack[]{ss};
    }

}
