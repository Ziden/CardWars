
package truco.plugin.cardlist.ouroOUcouro.incomum;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.Potion.Tier;
import org.bukkit.potion.PotionType;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.skilltypes.CustomPotion;

public class QuimicoCruel extends Carta{

    @Override
    public Raridade getRaridade() {
        return Carta.Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Quimico Cruel";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 pocao de slow splash","+ 1 pocao de silence"};
    }
    
    @Override
    public ItemStack [] getItems() {
        
        return new ItemStack[]{
            CustomPotion.silence.toItemStack(),
            new Potion(PotionType.SLOWNESS,Tier.ONE,true).toItemStack(1)
        };
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.OURO_LEATHER;
    }

}
