/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */

package truco.plugin.cardlist.ferroOUdima.raro;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class OssoInquebravel extends Carta{

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }


    @Override
    public String getNome() {
        return "Osso Inquebravel";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"+ 6 de vida"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.FERRO_DIMA;
    }

    @Override
    public double alteraVida(double vida) {
        return vida+=6;
    }


   




}
