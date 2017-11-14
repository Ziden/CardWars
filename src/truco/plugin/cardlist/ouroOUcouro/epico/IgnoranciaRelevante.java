/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.ouroOUcouro.epico;

import truco.plugin.cards.Carta;
import truco.plugin.cards.stats.ConjuracaoSafe;
import truco.plugin.cards.stats.Stats;

/**
 *
 * @author Júnior
 */
public class IgnoranciaRelevante extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Ignorancia Relevante";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Conjurações nao podem ser canceladas por dano"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.OURO_LEATHER;
    }

    ConjuracaoSafe sf = new ConjuracaoSafe(this);
    @Override
    public Stats[] getStats() {
        return new Stats[]{sf};
    }

}
