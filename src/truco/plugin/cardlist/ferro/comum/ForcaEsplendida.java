/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.ferro.comum;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.ItemUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ForcaEsplendida extends Carta {
    
    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }
    
    @Override
    public String getNome() {
        return "Força Esplendida";
    }
    
    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 de dano com espadas/machados/pás"};
    }
    
    @Override
    public Armadura getArmadura() {
        return Armadura.FERRO;
    }
    
    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
         if(ev.getCause()!=CustomDamageEvent.CausaDano.ATAQUE){
            return;
        }
        if (donoDaCarta.getItemInHand() != null) {
            Material namao = donoDaCarta.getItemInHand().getType();
            if (ItemUtils.isSpade(namao) || ItemUtils.isAxe(namao) || ItemUtils.isSword(namao)) {
                ev.addDamage(1);
            }            
        }
    }
    
}
