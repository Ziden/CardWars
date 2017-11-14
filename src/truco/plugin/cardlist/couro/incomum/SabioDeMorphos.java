/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.incomum;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.StatusEffect;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class SabioDeMorphos extends Carta {
    
    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }
    
    @Override
    public String getNome() {
        return "Sabio de Morphos";
    }
    
    @Override
    public String[] getDesc() {
        return new String[]{"Usar magias de fogo em alvos congelados", "tira o congelado e causa 2x dano"};
    }
    
    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }
    
    @Override
    public void causaDano(Player bateu,CustomDamageEvent ev) {
        if (ev.getCause() ==CausaDano.MAGIA_FOGO && ev.getPlayerTomou()!=null) {
            Player tomou = ev.getPlayerTomou();
            if (StatusEffect.hasStatusEffect(tomou, StatusEffect.StatusMod.CONGELADO)) {
                ev.addDamage(ev.getInitialDamage());
                tomou.getWorld().playEffect(tomou.getLocation(), Effect.WITCH_MAGIC, 1);
                StatusEffect.removeStatusEffect(tomou, StatusEffect.StatusMod.CONGELADO);
                tomou.getWorld().playSound(tomou.getLocation(), Sound.FIRE_IGNITE, 5, 1);
            }
        }
    }
    
}
