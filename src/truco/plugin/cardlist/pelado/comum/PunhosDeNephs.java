/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.pelado.comum;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PunhosDeNephs extends Carta {
    
    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }
    
    @Override
    public String getNome() {
        return "Punhos de Nephs";
    }
    
    @Override
    public String[] getDesc() {
        return new String[]{"+2 de dano com punhos", "recebe + 1 de dano em tudo"};
    }
    
    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO;
    }
    
    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
        if (ev.getCause() != CustomDamageEvent.CausaDano.ATAQUE) {
            return;
        }
        if (donoDaCarta.getItemInHand() == null || donoDaCarta.getItemInHand().getType() == Material.AIR) {
            ev.addDamage(2);
        }
    }
    
    @Override
    public void tomaDano(CustomDamageEvent ev) {
        ev.addDamage(1);
    }
    
}
