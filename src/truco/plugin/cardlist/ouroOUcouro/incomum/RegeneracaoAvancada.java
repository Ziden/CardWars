/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.ouroOUcouro.incomum;

import truco.plugin.cards.Carta;
import truco.plugin.cards.stats.MaisRegenMana;
import truco.plugin.cards.stats.Stats;

/**
 *
 * @author Júnior
 */
public class RegeneracaoAvancada extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Regeneracao Avancada";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 50% de regeneração de mana"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.OURO_LEATHER;
    }

    Stats[] stats = new Stats[]{new MaisRegenMana(this,50)};
    @Override
    public Stats[] getStats() {
        return stats;
    }

}
