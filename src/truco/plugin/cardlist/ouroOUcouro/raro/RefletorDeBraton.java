/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truco.plugin.cardlist.ouroOUcouro.raro;


import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cards.skills.skilllist.Refletor;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.Utils;
import truco.plugin.utils.Utils.DamageType;

/**
 *
 * @author Júnior
 */
public class RefletorDeBraton extends Carta {
    
    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Refletor de Braton";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Bloqueia e reflete o dano mágico"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.OURO_LEATHER;
    }

    Refletor r = new Refletor(this, 20, 30,"Dano Magico");

    @Override
    public Skill getSkill() {
        return r;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        if (ev.getPlayerTomou().hasMetadata("Refletor2xDano Magico")) {
            if (Utils.getDamageType(ev.getCause())==DamageType.MAGICO) {
               
                if (ev.getBateu()!=null) {
                    LivingEntity damager = ev.getBateu();
                    DamageManager.damage(ev.getFinalDamage()*2, ev.getPlayerTomou(), damager, CustomDamageEvent.CausaDano.UNKNOWN, "Refletor de Braton");
                    ev.getPlayerTomou().removeMetadata("Refletor2xDano Magico", CardWarsPlugin._instance);
                    ev.getPlayerTomou().sendMessage("§aVocê refletiu 200% do dano!");
                    ev.setCancelled("Refletor de Braton");
                    Location l = damager.getLocation();
          
                    if (damager instanceof Player) {
                        
                        ((Player) damager).sendMessage("§cSeu inimigo lhe retornou 200% do dano magico!");
                    }
                }

            }
        }
    }
}
