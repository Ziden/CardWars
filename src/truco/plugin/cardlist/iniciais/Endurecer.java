
package truco.plugin.cardlist.iniciais;



import truco.plugin.cards.Carta;


/**
 *
 * @author Carlos André Feldmann Júnior
 * 
 */

public class Endurecer extends Carta{

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }


    @Override
    public String getNome() {
        return "Endurecer";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"+ 2 vida"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.TODOS;
    }


    @Override
    public double alteraVida(double vida) {
	return vida+=2;
    }
   

}
