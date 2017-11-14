/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.ouroOUcouro.incomum;

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
 * @author Júnior
 */
public class MantoProtetor extends Carta {
    
    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }
    
    @Override
    public String getNome() {
        return "Manto Protetor";
    }
    
    @Override
    public String[] getDesc() {
        return new String[]{"Recebe -25% dano magico", "Recebe +50% de dano fisicos"};
    }
    
    @Override
    public Armadura getArmadura() {
        return Armadura.OURO_LEATHER;
    }
    
    @Override
    public void tomaDano(CustomDamageEvent ev) {
        DamageType tipo = Utils.getDamageType(ev.getCause());
        if (tipo == DamageType.MAGICO) {
            ev.addDamageMult(0.75, "Manto Protetor");
        } else if (tipo == DamageType.FISICO) {
            ev.addDamageMult(1.5,"Manto Protetor");
        }
        
    }
    
}
