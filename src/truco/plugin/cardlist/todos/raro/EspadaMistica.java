/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truco.plugin.cardlist.todos.raro;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;

/**
 *
 * @author usuario
 */
public class EspadaMistica extends Carta {

    @Override
    public Raridade getRaridade() {
       return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Espada Mistica";
    }

    @Override
    public String[] getDesc() {
        return new String [] {
            "+2 dano espada de ferro"
        };
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.TODOS;
    }
    
    public void causaDanoFisico(Player dono, EntityDamageEvent ev) {
       
	if(dono.getItemInHand().getType()==Material.IRON_SWORD) {
          if(ControleCartas.canUseCard(dono, this)){
	    ev.setDamage(ev.getDamage()+2);
	  }
        }
    }
    
}
