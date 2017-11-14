/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.dima.raro;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.ItemUtils;
import truco.plugin.utils.Utils;
import truco.plugin.utils.Utils.DamageType;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PasMisticas extends Carta {

    @Override
    public Carta.Raridade getRaridade() {
        return Carta.Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Pas Misticas";
    }

    @Override
    public String[] getDesc() {
        return new String[]{("Bloqueia 25% do dano estando abaixado e com uma pá")};
    }

    @Override
    public Carta.Armadura getArmadura() {
        return Carta.Armadura.DIMA;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        Player p = ev.getPlayerTomou();
        if (p.isSneaking() && ItemUtils.isSpade(p.getItemInHand().getType())) {
            if (Utils.getDamageType(ev.getCause()) == DamageType.FISICO) {
                ev.addDamageMult(0.75, "Abaixado com Pá");

            }

        }
    }

}
