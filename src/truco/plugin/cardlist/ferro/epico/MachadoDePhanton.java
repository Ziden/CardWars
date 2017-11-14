/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.ferro.epico;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MachadoDePhanton extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Machado de Phanton";
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[]{new ItemStack(Material.DIAMOND_AXE)};
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 machado de dima"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.FERRO;
    }

}
