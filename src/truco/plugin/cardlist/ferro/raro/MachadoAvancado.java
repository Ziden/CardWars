/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.ferro.raro;

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
public class MachadoAvancado extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Machado Avancado";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 machado de ferro afiado"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.FERRO;
    }

    @Override
    public ItemStack[] getItems() {
        ItemStack pa = new ItemStack(Material.IRON_AXE);
        pa.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        return new ItemStack[]{pa};

    }

}
