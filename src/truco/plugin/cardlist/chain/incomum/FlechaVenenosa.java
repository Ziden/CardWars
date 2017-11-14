
package truco.plugin.cardlist.chain.incomum;



import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
import truco.plugin.cards.CC;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EfeitoProjetil;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cards.skills.skilllist.FlechaComVeneno;


/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class FlechaVenenosa extends Carta{


    @Override
    public Raridade getRaridade() {
    return Raridade.INCOMUM;
    }
    


    @Override
    public String getNome() {
        return "Flecha Venenosa";
    }


    @Override
    public String[] getDesc() {
    return new String[]{"Sua proxima flecha será venenosa"};
    }


    @Override
    public Armadura getArmadura() {
    return Armadura.CHAIN;
    }
Skill s = new FlechaComVeneno(this, 10,7);

    @Override
    public Skill getSkill() {
	return s;
    }


    @Override
    public void playerTocaFlecha(EntityShootBowEvent ev) {
	Player p = (Player)ev.getEntity();
	if(p.hasMetadata("flechaveneno")){
	    p.removeMetadata("flechaveneno", CardWarsPlugin._instance);
	    EfeitoProjetil.addEfeito((Projectile)ev.getProjectile(),new EfeitoProjetil(p, (Projectile)ev.getProjectile()) {
		
		@Override
		public void causaEfeito(Player gotHit, Player Shooter, Projectile projectile) {
		CC.tacaPosion(gotHit, 0, 20*6);
		gotHit.sendMessage(ChatColor.DARK_GREEN+"Você foi acertado por uma flecha venenosa!");
		}

	    });
	}
    }
    
    
   
}
