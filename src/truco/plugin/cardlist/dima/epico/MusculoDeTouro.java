/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */

package truco.plugin.cardlist.dima.epico;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MusculoDeTouro extends Carta{

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }


    @Override
    public String getNome() {
        return "Musculos De Touro";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"+ 10 de vida"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.DIMA;
    }

    @Override
    public double alteraVida(double vida) {
       return vida+=10;
    }


   




}
