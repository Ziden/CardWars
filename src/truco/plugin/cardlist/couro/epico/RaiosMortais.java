
package truco.plugin.cardlist.couro.epico;



import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cards.skills.skilllist.RaioChunk;


/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class RaiosMortais extends Carta{

    RaioChunk r = new RaioChunk(this, 30, 20);

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }


    @Override
    public String getNome() {
        return "Raios Mortais";
    }


    @Override
    public String[] getDesc() {
    return new String[]{"Cai um raio em todos inimigos no mapa"};
    }


    @Override
    public Armadura getArmadura() {
    return Armadura.LEATHER;
    }


    @Override
    public Skill getSkill() {
	return r;
    }


   

}
