/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.pelado.epico;

import truco.plugin.cards.Carta;
import truco.plugin.cards.stats.MaisRegenMana;
import truco.plugin.cards.stats.Stats;

/**
 *
 * @author Júnior
 */
public class MenteSerena extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Mente Serena";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+100% de regeneração de mana"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER_PELADO;
    }

    Stats[] stats = new Stats[]{new MaisRegenMana(this,100)};
    @Override
    public Stats[] getStats() {
        return stats;
    }

}
