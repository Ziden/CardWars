/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.todos.epico;

import truco.plugin.cards.Carta;
import truco.plugin.cards.stats.MaisVidaTdm;
import truco.plugin.cards.stats.Stats;

/**
 *
 * @author Júnior
 */
public class LuzDeJabu extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Luz De Jabu";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+1 vida em tdm"};

    }

    @Override
    public Armadura getArmadura() {
        return Armadura.TODOS;
    }

    @Override
    public Stats[] getStats() {
        return new Stats[]{new MaisVidaTdm(this)};
    }
    
}
