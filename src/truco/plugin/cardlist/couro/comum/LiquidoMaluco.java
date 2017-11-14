/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.comum;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class LiquidoMaluco extends Carta {
    
    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }
    
    @Override
    public String getNome() {
        return "Liquido Maluco";
    }
    
    @Override
    public String[] getDesc() {
        return new String[]{"+ 2 poçoes de vida potentes"};
    }
    
    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }
    
    @Override
    public ItemStack[] getItems() {
        ItemStack is = new ItemStack(Material.POTION);
        is.setDurability((short) 8229);
        is.setAmount(2);
        return new ItemStack[]{is};
    }
    
}
