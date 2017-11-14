package truco.plugin.cardlist.peladoOuro;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.Potion.Tier;
import org.bukkit.potion.PotionType;
import truco.plugin.cards.Carta;

/**
 *
 * @author Carlos André Feldmann Júnior
 *
 */
public class AntigoAlquimista extends Carta {

    @Override
    public Raridade getRaridade() {
        return Carta.Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Antigo Alquimista";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+2 Pocoes de Vida", "+2 Pocao de Vida Splash"};
    }

    @Override
    public ItemStack[] getItems() {

        return new ItemStack[]{
            new Potion(PotionType.INSTANT_HEAL, Tier.TWO, false).toItemStack(1),
            new Potion(PotionType.INSTANT_HEAL, Tier.ONE, true).toItemStack(1),
            new Potion(PotionType.INSTANT_HEAL, Tier.TWO, false).toItemStack(1),
            new Potion(PotionType.INSTANT_HEAL, Tier.ONE, true).toItemStack(1)
        };
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.PELADO_OURO;
    }

}
