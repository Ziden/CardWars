/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.raro;

import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.stats.Stats;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CoracaoFlamejante extends Carta {

    @Override
    public void onEnable() {
        st = new Stats("ignitemaior", this, 1);
    }

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Coracao Flamejante";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Seus ignites duram 3x mais"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }
    public static Stats st;

    @Override
    public Stats[] getStats() {
        return new Stats[]{st};
    }
}
