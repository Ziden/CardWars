/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.dimaOUouro.comum;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.Utils;
import truco.plugin.utils.Utils.DamageType;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class BloqueioDeMercurio extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Bloqueio de Mercurio";
    }

    @Override
    public String[] getDesc() {
        return new String[]{("Bloqueia 25% do dano estando abaixado e com umá pa de pedra")};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.OURO_DIMA;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        Player player = ev.getPlayerTomou();
        if (player.isSneaking()&&player.getItemInHand().getType() == Material.STONE_SPADE) {
        ev.addDamageMult(0.75, "Bloqueio de Mercurio");
        }

    }


}
