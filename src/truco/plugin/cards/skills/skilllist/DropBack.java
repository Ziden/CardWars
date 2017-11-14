package truco.plugin.cards.skills.skilllist;



import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.efeitos.ParticleEffect;


/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class DropBack extends Skill {



    public DropBack(Carta vinculada, int cd,int mana) {
	super(vinculada, cd, mana);
    }


    @Override
    public boolean onCast(Player p) {

	Vector jumpDir = p.getLocation().getDirection().normalize().multiply(4).multiply(-1);
	jumpDir.divide(new Vector(1, 5, 1));
	jumpDir.setY(0.6);
	p.setVelocity(jumpDir);
	ChatUtils.sendMessage(p, "§l§7Você saltou para trás!");
	ParticleEffect.REDSTONE.display(new ParticleEffect.OrdinaryColor(25, 25, 25),p.getLocation(), 32);
	p.getWorld().playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 10, 1);
	return true;

    }


    @Override
    public String getName() {
    return "Pulo Para Tras!";
    }




}
