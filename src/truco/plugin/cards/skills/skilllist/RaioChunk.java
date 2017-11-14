package truco.plugin.cards.skills.skilllist;

import truco.plugin.utils.TeamUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class RaioChunk extends Skill {

    public RaioChunk(Carta vinculada, int cud, int mana) {
        super(vinculada, cud, mana);
    }

    @Override
    public boolean onCast(Player p) {
        for (Entity t : p.getWorld().getEntities()) {
            if (t instanceof Player) {
                if (TeamUtils.canAttack(p, (Player) t)) {
                    DamageManager.damage(3, p, (Player)t, CustomDamageEvent.CausaDano.MAGIA_RAIO, "Raio Global");
                    t.getLocation().getWorld().strikeLightningEffect(t.getLocation());
                }
            }

        }
        return true;
    }

    @Override
    public String getName() {
        return "Raios Mortais";
    }

}
