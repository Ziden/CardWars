/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.signs;

import org.bukkit.block.Sign;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.cards.ControleCartas;

/**
 *
 * @author Carlos
 */
public class Equip {

    public static void blockclick(final PlayerInteractEvent ev, Sign s) {
        
        if (s.getLine(1).equalsIgnoreCase("Equipar") && !ev.getPlayer().hasPotionEffect(PotionEffectType.WATER_BREATHING)) {
            ControleCartas.updateInventoryCards(ev.getPlayer(),false);
            
        }
    }
}
