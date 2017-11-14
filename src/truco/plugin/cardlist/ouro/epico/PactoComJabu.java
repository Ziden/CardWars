/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.ouro.epico;

import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Júnior
 */
public class PactoComJabu extends Carta {
    
    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }
    
    @Override
    public String getNome() {
        return "Pacto com Jabu";
    }
    
    @Override
    public String[] getDesc() {
        return new String[]{"- 10 de vida", "toma -50% de dano"};
    }
    
    @Override
    public Armadura getArmadura() {
        return Armadura.OURO;
    }
    
    @Override
    public double alteraVida(double vida) {
        return vida - 10;
    }
    
    @Override
    public void tomaDano(CustomDamageEvent ev) {
        ev.addDamageMult(0.5, "Pacto com Jabu");
    }
    
}
