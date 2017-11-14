/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.ferroOUdima.incomum;

import truco.plugin.cards.Carta;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CouroDuro extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Couro Duro";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 4 de vida"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.FERRO_DIMA;
    }

    @Override
    public double alteraVida(double vida) {
       return vida+=4;
    }



}
