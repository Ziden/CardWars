package truco.plugin.cardlist.dima.comum;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import truco.plugin.cards.Carta;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.Utils;
import truco.plugin.utils.Utils.DamageType;

/**
 *
 * @author Carlos André Feldmann Júnior
 *
 */
public class PeleCauterizada extends Carta {

    @Override
    public Raridade getRaridade() {
        return Carta.Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Pele Cauterizada";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Recebe -25% dano fisico", "Recebe +25% dano magico"};
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.DIMA;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        if (Utils.getDamageType(ev.getCause())==DamageType.FISICO) {
            ev.addDamageMult(0.75, "Pele Cauterizada");
        } else if (Utils.getDamageType(ev.getCause())==DamageType.MAGICO) {
            ev.addDamageMult(1.25, "Pele Cauterizada");
        }
    }

}
