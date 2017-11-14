/*

 */
package truco.plugin.managers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public abstract class ClickAction {

    public abstract void click(Player p, Entity t);
}
