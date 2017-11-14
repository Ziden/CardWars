/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.ouroOUcouro.incomum;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta;

import truco.plugin.cards.StatusEffect;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;

/**
 *
 * @author Júnior
 *
 */
public class SilencioEletrico extends Carta {
    
    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }
    
    @Override
    public String getNome() {
        return "Silencio Eletrico";
    }
    
    @Override
    public String[] getDesc() {
        return new String[]{"Seus danos eletricos causam silence de 1s"};
    }
    
    @Override
    public Armadura getArmadura() {
        return Armadura.OURO_LEATHER;
    }
    
    @Override
    public void causaDano(Player causador, CustomDamageEvent ev) {
        if (ev.getCause() == CausaDano.MAGIA_RAIO && ev.getPlayerTomou() != null) {
            StatusEffect.addStatusEffect(ev.getPlayerTomou(), StatusEffect.StatusMod.SILENCE, 1);
          
        }
    }
}
