package truco.plugin.cardlist.dima.comum;

import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PunhosDoMarreteiro extends Carta {

    @Override
    public Raridade getRaridade() {
        return Carta.Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Punhos do Marreteiro";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 pa de madeira", "+ 2 de dano com pas de madeira"};
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.DIMA;
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[]{new ItemStack(Material.WOOD_SPADE)};
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
        if (ev.getCause() != CustomDamageEvent.CausaDano.ATAQUE) {
            return;
        }
        if (donoDaCarta.getItemInHand().getType() == Material.WOOD_SPADE) {
            ev.addDamage(2);
        }
    }
}
