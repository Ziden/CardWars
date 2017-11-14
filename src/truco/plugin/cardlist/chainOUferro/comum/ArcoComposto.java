package truco.plugin.cardlist.chainOUferro.comum;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ArcoComposto extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Arco Composto";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 arco knockback I", "+6 flechas"};
    }

    public ItemStack[] getItems() {
        ItemStack arco = new ItemStack(Material.BOW, 1);
        arco.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
        return new ItemStack[]{
            arco,
            new ItemStack(Material.ARROW, 6)
        };
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.CHAIN_FERRO;
    }

}
