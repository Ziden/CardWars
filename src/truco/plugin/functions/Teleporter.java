/*

 */
package truco.plugin.functions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;
import truco.plugin.CardWarsPlugin;
import truco.plugin.managers.lobbys.MultipleLobbysManager;
import truco.plugin.utils.BookUtil;
import truco.plugin.utils.LocUtils;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class Teleporter implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void EntityPortalEnterEvent(EntityPortalEnterEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player p = (Player) event.getEntity();
        if (Cooldown.isCooldown(p, "portalcool")) {
            return;

        }
        Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
        Block blocoabaixo = b.getRelative(BlockFace.DOWN);
        if (blocoabaixo.getType() != Material.CHEST && blocoabaixo.getType() != Material.TRAPPED_CHEST) {
            Cooldown.addCoolDown(p, "portalcool", 1000);
            return;
        }
        Cooldown.addCoolDown(p, "portalcool", 5000);
        Chest ch = (Chest) blocoabaixo.getState();
        if (ch.getInventory().getItem(0) == null || ch.getInventory().getItem(0).getType() != Material.WRITTEN_BOOK) {
            return;
        }
        final ItemStack livro = ch.getInventory().getItem(0).clone();
        if (BookUtil.getTitle(livro).equalsIgnoreCase("server")) {
            String sv = BookUtil.getPages(livro).get(0);
            if (sv.equalsIgnoreCase("Lobby")) {
                if (MultipleLobbysManager.getPerfectLobby(p) != null) {
                    sv = MultipleLobbysManager.getPerfectLobby(p).getNome();
                }
                Utils.TeleportarTPBG(sv, p);
            }
            
            p.teleport(p.getWorld().getSpawnLocation());
        } else if (BookUtil.getTitle(livro).equalsIgnoreCase("tp")) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    p.teleport(LocUtils.stringToLocation(BookUtil.getPages(livro).get(0)));
                }
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, r, 10);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void EntityPortalEvent(EntityPortalEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void PlayerPortalEvent(PlayerPortalEvent event) {
        event.setCancelled(true);
    }

}
