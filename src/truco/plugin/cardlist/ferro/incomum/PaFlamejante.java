/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.ferro.incomum;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PaFlamejante extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Pá Flamejante";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 pá ferro que taca fogo"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.FERRO;
    }

    @Override
    public ItemStack[] getItems() {
        ItemStack pa = new ItemStack(Material.IRON_SPADE);
        pa.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
        return new ItemStack[]{pa};

    }

}
