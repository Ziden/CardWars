/*

 */
package truco.plugin.functions.fun;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import truco.plugin.CardWarsPlugin;
import truco.plugin.utils.kibes.UtilAction;
import truco.plugin.utils.kibes.UtilBlock;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class DoubleJump implements Listener {

    public DoubleJump() {
        Bukkit.getPluginManager().registerEvents(this, CardWarsPlugin._instance);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (isGrounded(p) && p.getGameMode() != GameMode.CREATIVE) {
                        p.setAllowFlight(true);

                    }
                }
            }
        }, 20, 20);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void DoubleJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        event.setCancelled(true);
        player.setFlying(false);
        player.setAllowFlight(false);

        UtilAction.velocity(player, 1.4D, 0.2D, 1.0D, true);
        player.playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);

    }

   
    public static boolean isGrounded(org.bukkit.entity.Entity ent) {
        return UtilBlock.solid(ent.getLocation().getBlock().getRelative(BlockFace.DOWN));
    }

}
