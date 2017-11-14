
package truco.plugin.cardlist.chain.raro;



import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EfeitoProjetil;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cards.skills.skilllist.FlechaComVeneno;
import truco.plugin.utils.ChatUtils;
import truco.plugin.data.MetaShit;
import truco.plugin.cards.StatusEffect;


/**
 *
 * @author Carlos André Feldmann Júnior
 * 
 */

public class FlechaSilence extends Carta{

 Skill s = new Skill(this, 15, 5) {

        @Override
        public boolean onCast(Player p) {
            if (!p.hasMetadata("flechasilence")) {
                ChatUtils.sendMessage(p, "§a§lSua proxima flecha vai causar silence!");
                MetaShit.setMetaObject("flechasilence", p, true);
                return true;
            } else {
                ChatUtils.sendMessage(p, "§cVocê já ativou está habilidade, use primeiro para ativar denovo!");
                return false;
            }
        }

        @Override
        public String getName() {
            return "Flechada Silenciosa";
        }
    };
    
    
    @Override
    public Raridade getRaridade() {
    return Raridade.RARO;
    }
    
    @Override
    public String getNome() {
        return "Arqueiro Silencioso";
    }


    @Override
    public String[] getDesc() {
    return new String[]{"Sua proxima flecha causara silence"};
    }


    @Override
    public Armadura getArmadura() {
    return Armadura.CHAIN;
    }


    @Override
    public Skill getSkill() {
	return s;
    }


    @Override
    public void playerTocaFlecha(EntityShootBowEvent ev) {
	Player p = (Player)ev.getEntity();
	if(p.hasMetadata("flechasilence")){
	    p.removeMetadata("flechasilence", CardWarsPlugin._instance);
	    EfeitoProjetil.addEfeito((Projectile)ev.getProjectile(),new EfeitoProjetil(p, (Projectile)ev.getProjectile()) {
		
		@Override
		public void causaEfeito(Player gotHit, Player Shooter, Projectile projectile) {
                    StatusEffect.addStatusEffect(gotHit, StatusEffect.StatusMod.SILENCE, 4);
		    gotHit.sendMessage(ChatColor.DARK_GREEN+"Você foi acertado por uma flecha silenciosa!");
		}

	    });
	}
    }
    
    
   
}
