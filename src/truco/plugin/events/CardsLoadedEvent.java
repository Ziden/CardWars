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
public class CardsLoadedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;

    public CardsLoadedEvent(Player player) {
        this.player = player;

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
