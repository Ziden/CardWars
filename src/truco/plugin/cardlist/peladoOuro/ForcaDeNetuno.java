package truco.plugin.cardlist.peladoOuro;

import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 *
 */
public class ForcaDeNetuno extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Forca de Netuno";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Causa dano de agua", "e nao dano fisico"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO_OURO;
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
        if (ev.getCause() == CustomDamageEvent.CausaDano.ATAQUE) {
            ev.setCancelled("Forca de Netuno");
            DamageManager.damage(ev.getFinalDamage(), ev.getBateu(), ev.getTomou(), CustomDamageEvent.CausaDano.MAGIA_AGUA, "Forca de Netuno");
            ev.getTomou().getWorld().playEffect(ev.getTomou().getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
        }
    }

}
