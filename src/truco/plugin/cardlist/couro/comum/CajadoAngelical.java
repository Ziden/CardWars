/*

 */
package truco.plugin.cardlist.couro.comum;

import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.itens.Items;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CajadoAngelical extends Carta {
    
    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }
    
    @Override
    public String getNome() {
        return "Cajado Angelical";
    }
    
    @Override
    public ItemStack[] getItems() {
        return new ItemStack[]{Items.cajadomagico.geraItem(1)};
    }
    
    @Override
    public String[] getDesc() {
        return new String[]{"Ganha um cajado que gasta 7 de mana", "e causa 2 de dano"};
    }
    
    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }
    
}
