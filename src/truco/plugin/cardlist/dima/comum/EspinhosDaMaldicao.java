/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.dima.comum;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;
import truco.plugin.utils.Utils;
import truco.plugin.utils.Utils.DamageType;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class EspinhosDaMaldicao extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Espinhos da Maldição";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Retorna 25% do dano fisico recebido"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.DIMA;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        if (ev.getBateu() == null) {
            return;
        }
        if (ev.getCause() == CausaDano.ATAQUE || ev.getCause() == CausaDano.SKILL_ATAQUE) {

            {
                DamageManager.damage(ev.getInitialDamage() / 4, ev.getTomou(), ev.getBateu(), CausaDano.UNKNOWN, "Espinhos da Maldicao");

            }

        }
    }
}
