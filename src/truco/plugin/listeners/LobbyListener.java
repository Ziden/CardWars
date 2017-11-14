package truco.plugin.listeners;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import truco.plugin.cards.ControleCartas;
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.cards.Carta;
import truco.plugin.functions.SomeNegada;
import truco.plugin.cmds.CmdSpawn;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.functions.groups.EventosGrupos;
import truco.plugin.itens.CustomItem;
import truco.plugin.matchmaking.DBHandler.PlayerStatus;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.functions.Cooldown;
import truco.plugin.utils.ChatUtils;
import truco.plugin.managers.PermManager;
import truco.plugin.matchmaking.Threads.PlayerManagerThread;

import truco.plugin.utils.PlayerUtils;
import truco.plugin.utils.mobapi.mobs.MobsApi;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class LobbyListener implements Listener {

    @EventHandler
    public void morreJogador(final PlayerDeathEvent e) {
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void join(final PlayerJoinEvent ev) {
        Cooldown.addCoolDown(ev.getPlayer(), "trocahub", 10000);
        ev.getPlayer().teleport(CmdSpawn.getSpawnLocation(ev.getPlayer()));
        if (server == ServerType.LOBBY) {
            PlayerUtils.startaConta(ev.getPlayer());
            CardWarsPlugin.lobbymanager.mandaInfo();
    
        }

        if (server == ServerType.TUTORIAL) {
            ev.getPlayer().sendMessage("§a§lVocê está no tutorial! Termine ele para sair daqui!");
            PlayerUtils.someNegadaTuto(ev.getPlayer());
            PlayerUtils.startaCoisasVisuais(ev.getPlayer());

        } else if (server == ServerType.LOBBY) {
            PlayerUtils.sendWelcomeMessage(ev.getPlayer());

            SomeNegada.Join(ev);
        }
    }

    @EventHandler
    public static void login(PlayerLoginEvent ev) {
        if (ev.getResult() == Result.KICK_FULL) {
            if (PermManager.LOGAFULL.playerHas(ev.getPlayer()) || ev.getPlayer().isOp()) {
                ev.allow();
            } else {
                ev.disallow(Result.KICK_OTHER, "§cServidor lotado! §6Garanta seu slot obtendo vip!");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void clica(PlayerInteractEntityEvent ev) {
        if (ev.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            ev.setCancelled(true);
            return;
        }
        if (MobsApi.bixos.containsKey(ev.getRightClicked().getUniqueId())) {
            MobsApi.bixos.get(ev.getRightClicked().getUniqueId()).click(ev.getPlayer(), ev.getRightClicked());
            ev.setCancelled(true);
        }
    }

    public void quit(Player pc) {

        if (server == ServerType.TUTORIAL) {
            pc.getInventory().setItem(0, null);
            pc.getInventory().clear();
            return;
        }
        if (PlayerManagerThread.avisados.contains(pc.getUniqueId())) {
            PlayerManagerThread.avisados.remove(pc.getUniqueId());
        }
        CardWarsPlugin.lobbymanager.mandaInfo(Bukkit.getOnlinePlayers().size() - 1);

        //
        EventosGrupos.Quit(pc);

        MatchMaker.db.sai(pc, false);
    }

    @EventHandler
    public void negoKita(final PlayerQuitEvent ev) {
        quit(ev.getPlayer());
    }

    @EventHandler
    public void kick(PlayerKickEvent ev) {
        quit(ev.getPlayer());
    }

    @EventHandler
    public void move(PlayerMoveEvent ev) {
        if (ev.getTo().getY() <= -50) {
            ev.getPlayer().setFallDistance(0);
            ev.getPlayer().teleport(ev.getPlayer().getWorld().getSpawnLocation());

        }
    }

    @EventHandler
    public void tomadano(EntityDamageEvent ev) {
        if (ev instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent eev = (EntityDamageByEntityEvent) ev;
            if (eev.getDamager() instanceof Player) {
                Player p = (Player) eev.getDamager();

                if (p.isOp() && ev.getEntity().getType() != EntityType.PLAYER) {
                    return;
                }

            }
        }
        ev.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void inventario(final InventoryClickEvent ev) {
        if (ev.getCurrentItem() != null) {
            if (ev.getSlotType() == InventoryType.SlotType.ARMOR) {
                ev.setCancelled(true);
                Bukkit.getScheduler().runTaskLater(CardWarsPlugin._instance, new Runnable() {

                    public void run() {
                        ((Player) ev.getWhoClicked()).updateInventory();
                    }
                }, 2L);

            }

        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL) {
            if (e.getClickedBlock().getType() == Material.STONE_PLATE || e.getClickedBlock().getType() == Material.WOOD_PLATE || e.getClickedBlock().getType() == Material.IRON_PLATE) {
                if (e.getClickedBlock().getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_BLOCK) {

                    Vector v = e.getPlayer().getLocation().getDirection().normalize().multiply(2);
                    if (v.getY() > 3) {
                        v.setY(3);
                    }
                    if (v.getY() < 1) {
                        v.setY(1);
                    }

                    e.getPlayer().setVelocity(v);
                    e.setCancelled(true);

                }
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            String citem = CustomItem.getItem(e.getPlayer().getItemInHand());
            if (ControleCartas.getCarta(e.getPlayer().getItemInHand()) != null) {
                e.setCancelled(true);
                return;
            }
            if (citem != null) {
                CustomItem.getCustomItem(citem).interactLobby(e.getPlayer());
            }

        }

    }
}
