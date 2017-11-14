
package truco.plugin.cardlist.dima.comum;



import truco.plugin.cards.Carta;


/**
 *
 * @author Carlos André Feldmann Júnior
 * 
 */

public class BensaoMirim extends Carta{

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Bençao do Deus Mirim";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"+ 2 vida"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.DIMA;
    }

    @Override
    public double alteraVida(double vida) {
	return vida+=2;
    }
   

}
