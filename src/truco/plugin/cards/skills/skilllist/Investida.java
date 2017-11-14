package truco.plugin.cards.skills.skilllist;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.ChatUtils;


/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class Investida extends Skill {



    public Investida(Carta vinculada, int cd, int mana) {
	super(vinculada, cd, mana);
    }


    @Override
    public boolean onCast(final Player p) {

	Vector jumpDir = p.getLocation().getDirection().normalize().multiply(4);
	jumpDir.divide(new Vector(1, 5, 1));
	jumpDir.setY(0.6D);
	ChatUtils.sendMessage(p, ChatColor.DARK_AQUA+"Você saltou para frente!");
	p.setVelocity(jumpDir);
	int task = Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

	    @Override
	    public void run() {
		if (p != null) {
		    p.removeMetadata("investidadepedra", CardWarsPlugin._instance);
		}
	    }

	}, 3 * 20l);

	p.setMetadata("investidadepedra", new FixedMetadataValue(CardWarsPlugin._instance, task));
	return true;
    }


    @Override
    public String getName() {
	return "Doidao Voador";
    }




}
