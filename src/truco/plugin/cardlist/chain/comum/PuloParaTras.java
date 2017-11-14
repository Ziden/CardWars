
package truco.plugin.cardlist.chain.comum;



import org.bukkit.ChatColor;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cards.skills.skilllist.DropBack;


/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PuloParaTras extends Carta{

    DropBack d = new DropBack(this,10,1);

    @Override
    public Raridade getRaridade() {
    return Raridade.COMUM;
    }


    @Override
    public String getNome() {
    return "Dibre do Ladino";
    }


    @Override
    public String[] getDesc() {
    return new String[]{"Pula para tras"};
    }


    @Override
    public Armadura getArmadura() {
    return Armadura.CHAIN;
    }
    
    @Override
    public Skill getSkill(){
	return d;
    }
}
