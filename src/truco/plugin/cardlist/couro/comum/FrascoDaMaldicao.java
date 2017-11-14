/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.comum;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class FrascoDaMaldicao extends Carta {
    
    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }
    
    @Override
    public String getNome() {
        return "Frasco Da Maldicao";
    }
    
    @Override
    public String[] getDesc() {
        return new String[]{"+ 2 poçoes de dano instantaneo"};
    }

    @Override
    public ItemStack[] getItems() {
        Potion s = new Potion(PotionType.INSTANT_DAMAGE);
        s.setSplash(true);
        
        return new ItemStack[]{s.toItemStack(2)};
    }
    
    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }
    
}
