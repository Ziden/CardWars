package truco.plugin.cardlist.iniciais;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MachadoBatalha extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Machado de Batalha";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 machado de ferro"};
    }

    public ItemStack[] getItems() {
        return new ItemStack[]{
                    new ItemStack(Material.IRON_AXE, 1)
                };
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.FERRO;
    }

   
}
