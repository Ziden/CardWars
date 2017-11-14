/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.iniciais;

import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.itens.Items;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class GarraDeBhefos extends Carta {
    
    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }
    
    @Override
    public String getNome() {
        return "Garra De Bhefos";
    }
    
    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 garra de madeira"};
    }
    
    @Override
    public ItemStack[] getItems() {
        return new ItemStack[]{Items.garramadeira.geraItem(1)};
    }
    
    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO;
    }
    
}
