/*

 */
package truco.plugin.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import truco.plugin.damage.DamageInfoPlayer;
import truco.plugin.damage.Dano;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CustomDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;

    private Dano dano;

    public CustomDeathEvent(Player player) {
        this.player = player;
        dano = DamageInfoPlayer.getInfo(player).getLastPlayerDamage();

    }

    public Dano getDano() {
        return dano;
    }

    public CustomDamageEvent.CausaDano getCausa() {
        if (dano == null) {
            return CustomDamageEvent.CausaDano.UNKNOWN;
        }
        return dano.getCausa();
    }

    public Player getMatador() {
        if (dano == null) {
            return null;
        }
        Player p = Bukkit.getPlayer(dano.getQuem());
        return p;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
