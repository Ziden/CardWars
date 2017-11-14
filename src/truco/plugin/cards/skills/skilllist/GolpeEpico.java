package truco.plugin.cards.skills.skilllist;

import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cards.skills.skilltypes.TimedHit;

/**
 *
 * @author Carlos André Feldmann Júnior
 *
 */
public class GolpeEpico extends Skill {

    public GolpeEpico(Carta vinculada, int cd, int mana) {
        super(vinculada, cd, mana);
    }

    @Override
    public boolean onCast(Player p) {
        return TimedHit.hit.fazTimedHit(p, getName(), 20, 4);

    }

    @Override
    public String getName() {
        return "Golpe Epico";
    }

}
