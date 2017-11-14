
package truco.plugin.cards.skills.skilllist;



import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.ChatUtils;
import truco.plugin.data.MetaShit;


/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class FlechaComVeneno extends Skill{


    public FlechaComVeneno(Carta vinculada,int cd,int mana) {
	super(vinculada,cd, mana);
    }


    @Override
    public boolean onCast(Player p) {
    if(!p.hasMetadata("flechaveneno")){	
	ChatUtils.sendMessage(p, "§a§lSua proxima flecha vai conter um pouco de veneno!");
       MetaShit.setMetaObject("flechaveneno", p, true);
       return true;
    }else{
        ChatUtils.sendMessage(p,"§cVocê já ativou está habilidade, use primeiro para ativar denovo!");
       return false;
    }
    }


    @Override
    public String getName() {
    return "Flechada Venenosa";
    }

}
