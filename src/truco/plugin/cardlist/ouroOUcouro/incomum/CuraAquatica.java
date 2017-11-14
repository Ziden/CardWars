/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.ouroOUcouro.incomum;

import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;


/**
 *
 * @author Júnior
 */
public class CuraAquatica extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Cura Aquatica";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Danos de agua lhe curam 1 coração"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.OURO_LEATHER;
    }

    @Override
    public void causaDano(Player causador,CustomDamageEvent ev) {
        if (ev.getCause() == CausaDano.MAGIA_AGUA) {
            DamageManager.cura(causador, 1);
         
        }
    }

}
