/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.pelado.epico;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.itens.Items;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class GarraDeZeus extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Garra de Zeus";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+1 garra dourada"};
    }

    @Override
    public ItemStack[] getItems() {
        ItemStack garra = Items.garraouro.geraItem(1);
        garra.addUnsafeEnchantment(Enchantment.LUCK, 1);
        return new ItemStack[]{garra};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO;
    }

}
