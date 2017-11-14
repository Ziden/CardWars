package truco.plugin.cardlist.iniciais;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class EspadaDeTreino extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Espada de Treino";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 espada de madeira","+ 1 de dano com uma espada de madeira"};
    }

    public ItemStack[] getItems() {
        return new ItemStack[]{
            new ItemStack(Material.WOOD_SWORD, 1)
        };
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.TODOS;
    }

    @Override
    public void causaDano(Player causador, CustomDamageEvent ev) {
        if (ev.getCause() == CustomDamageEvent.CausaDano.ATAQUE && causador.getItemInHand() != null && causador.getItemInHand().getType() == Material.WOOD_SWORD) {
            ev.addDamage(1);
        }
    }

}
