package truco.plugin.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.libraryaddict.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import truco.plugin.cards.ControleCartas;
import truco.plugin.CardWarsPlugin;
import truco.plugin.arena.Arena;
import truco.plugin.arena.Arena.Team;
import truco.plugin.arena.GameState;
import truco.plugin.itens.BombaFumaca;
import truco.plugin.data.mysql.MatchHistoryDB;
import truco.plugin.itens.CustomItem;
import truco.plugin.itens.Items;
import truco.plugin.matchmaking.PlayerIngame;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.utils.*;
import truco.plugin.cards.StatusEffect;
import truco.plugin.cards.skills.skilltypes.Trap;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CardsLoadedEvent;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;
import truco.plugin.functions.MakeVanish;
import truco.plugin.functions.ScoreCWs;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class GameListener implements Listener {

    public static HashMap<Player, PlayerIngame> infs = new HashMap();

    @EventHandler
    public static void login(PlayerLoginEvent ev) {
        if (ev.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            ev.allow();

        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void fixEnchants(final CustomDamageEvent ev) {
        if (ev.isCancelled()) {
            return;
        }
        if (ev.getPlayerBateu() != null) {
            if (ev.getPlayerBateu().getItemInHand() != null) {
                if (ev.getPlayerBateu().getItemInHand().containsEnchantment(Enchantment.FIRE_ASPECT)) {
                    final int ticks = ev.getPlayerBateu().getItemInHand().getEnchantmentLevel(Enchantment.FIRE_ASPECT) * 80;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                        @Override
                        public void run() {
                            DamageManager.addFireTicks(ev.getTomou(), ticks);
                        }
                    }, 2);
                }
                if (ev.getPlayerBateu().getItemInHand().containsEnchantment(Enchantment.KNOCKBACK)) {
                    double lvl = ev.getPlayerBateu().getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK);
                    lvl = 1 + (lvl * 0.7);
                    ev.addKnockBack("KnockBack Enchantment", lvl);
                }

            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void status(CustomDamageEvent ev) {
        if (ev.getPlayerBateu() != null) {
            if (StatusEffect.hasStatusEffect(ev.getPlayerBateu(), StatusEffect.StatusMod.STUN)) {
                ev.setCancelled("Stun");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void changeItensDamage(CustomDamageEvent ev) {
        if (ev.getPlayerBateu() == null) {
            return;
        }
        Player p = ev.getPlayerBateu();

        String s = CustomItem.getItem(p.getItemInHand());
        if (p.getItemInHand() != null) {
            if (p.getItemInHand().getType() == Material.IRON_SWORD) {
                double add = p.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) * 0.75;
                ev.setDamage(3 + add);
            } else if (p.getItemInHand().getType() == Material.WOOD_SWORD) {
                double add = p.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) * 0.75;
                ev.setDamage(2 + add);
            } else if (p.getItemInHand().getType() == Material.STONE_SWORD) {
                double add = p.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) * 0.75;
                ev.setDamage(2.5 + add);
            } else if (p.getItemInHand().getType() == Material.IRON_AXE) {
                double add = p.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) * 0.75;
                ev.setDamage(3 + add);
            } else if (p.getItemInHand().getType() == Material.DIAMOND_AXE) {
                double add = p.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) * 0.75;
                ev.setDamage(5 + add);
            }
        }
        if (s != null) {
            if (s.equalsIgnoreCase(Items.garramadeira.getName())) {
                ev.setDamage(3);
            } else if (s.equalsIgnoreCase(Items.garrapedra.getName())) {
                ev.setDamage(4);
            } else if (s.equalsIgnoreCase(Items.garraferro.getName())) {
                ev.setDamage(5);

            } else if (s.equalsIgnoreCase(Items.garraouro.getName())) {
                ev.setDamage(6);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void interactLow(PlayerInteractEvent ev) {
        if (StatusEffect.hasStatusEffect(ev.getPlayer(), StatusEffect.StatusMod.STUN)) {
            Utils.sendTitle(ev.getPlayer(), "§1§lSTUN", "§7Você está atordoado!!", 0, 20, 0);
            if (ev.getPlayer().getItemInHand().getType() == Material.POTION) {
                ev.setCancelled(true);
            }
            return;
        }
        if (ev.getPlayer().getVehicle() != null) {
            if (ev.getPlayer().getItemInHand().getType() == Material.POTION) {
                ev.setCancelled(true);
            }
            return;
        }
        if (CardWarsPlugin.getArena() != null && CardWarsPlugin.getArena().getState() != GameState.INGAME) {
            if (ev.getPlayer().getItemInHand().getType() == Material.POTION) {
                ev.setCancelled(true);
            }
            return;
        }

    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            String citem = CustomItem.getItem(e.getPlayer().getItemInHand());

            if (citem != null) {

                if (CardWarsPlugin.getArena() != null) {

                    if (CardWarsPlugin.getArena().getState() == GameState.INGAME) {
                        CustomItem.getCustomItem(citem).interactGame(e.getPlayer());
                    }

                }
            }

        }
    }

    // BLOOOOOOOOD !
    @EventHandler(priority = EventPriority.HIGHEST)
    void dano(CustomDamageEvent ev) {
//MIGRANDOOOOOOOOoo
        if (ev.getFinalDamage() > 0 && !ev.isCancelled()) {

            Player bateu = ev.getPlayerBateu();
            Player tomou = ev.getPlayerTomou();
            if (bateu == null || tomou == null) {
                return;
            }

            BombaFumaca.bate(ev);
            BombaFumaca.tomaDano(ev);

            if ((ev.getCause() == CausaDano.ATAQUE)) {

                if (ItemUtils.isSword(bateu.getItemInHand().getType()) && tomou.isBlocking()) {
                    double angle = LocUtils.getAngle(bateu.getLocation().getDirection(), tomou.getLocation().getDirection());
                    if (angle > 100) {
                        ev.addDamageMult(0.85, "Bloqueando");
                        bateu.sendMessage("§aAtaque Bloqueado");
                    }
                }
            }

            bateu.updateInventory();

            if (StatusEffect.hasStatusEffect(tomou, StatusEffect.StatusMod.CONGELADO)) {
                int numero = CardWarsPlugin.random.nextInt(100);
                if (numero < 25) {
                    StatusEffect.removeStatusEffect(tomou, StatusEffect.StatusMod.CONGELADO);
                }
            }

        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void flechabate(ProjectileHitEvent ev) {
        if (ev.getEntity().hasMetadata("magia")) {
            ev.getEntity().remove();
        }
    }

    @EventHandler
    public void move(PlayerMoveEvent ev) {
        // changed block Z X coords
        if (ev.getFrom().getBlockX() != ev.getTo().getBlockX() || ev.getFrom().getBlockZ() != ev.getTo().getBlockZ()) {
            Trap.move(ev);
        }
        if (StatusEffect.hasStatusEffect(ev.getPlayer(), StatusEffect.StatusMod.STUN) || StatusEffect.hasStatusEffect(ev.getPlayer(), StatusEffect.StatusMod.SNARE) || StatusEffect.hasStatusEffect(ev.getPlayer(), StatusEffect.StatusMod.CONGELADO)) {
            if (Utils.hasChangedBlockCoordinates(ev.getFrom(), ev.getTo())) {
                Location from = ev.getFrom();
                Location to = ev.getTo();
                double x = Math.floor(from.getX());
                double z = Math.floor(from.getZ());
                x += .5;
                z += .5;
                ev.getPlayer().teleport(new Location(from.getWorld(), x, from.getY(), z, from.getYaw(), from.getPitch()));

            }
        }
        if (ev.getTo().getY() <= -10) {
            DamageManager.causaDanoBruto(null, ev.getPlayer(), ev.getPlayer().getHealth(), "Void");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void inventario(final InventoryClickEvent ev) {

        String citem = CustomItem.getItem(ev.getCurrentItem());
        if (citem != null && citem.equals(Items.icaster.getName())) {
            ev.setCancelled(true);
            ev.getWhoClicked().closeInventory();
            return;
        }

    }

    @EventHandler
    public void carregou(CardsLoadedEvent ev) {

        final Player pl = ev.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                if (pl == null) {
                    return;
                }
                if (!japegou.contains(pl.getUniqueId())) {
                    if (CardWarsPlugin.getArena() != null) {
                        ControleCartas.updateInventoryCards(pl, false);
                        Utils.clearPlayerEffects(pl);
                        pl.setHealth(pl.getMaxHealth());
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                final int gameid = CardWarsPlugin.getArena().getGameId();
                                MatchHistoryDB.addPlayerHistory(pl, CardWarsPlugin.getArena().getTeam(pl.getUniqueId()), gameid, MatchMaker.db.getArmor(pl.getUniqueId()));
                            }
                        }).start();
                    }
                    japegou.add(pl.getUniqueId());
                }
            }
        }, 5);

    }

    public void logoucomequipe(final Player pl, final Team t) {
        final Arena ar = CardWarsPlugin.getArena();

        if (ar != null) {
            ar.startGame();

            if (ar.getTeam(pl.getUniqueId()) == null) {

                ar.addPlayer(pl, t);
                ar.tp(pl);

                ChatUtils.sendMessage(pl, "§b§lVocê está na equipe " + t.getCor() + "§l" + t.getName().toLowerCase() + "§r!");
                ar.startPlayer(pl);

            } else {
                pl.getInventory().setHelmet(null);
            }
            if (t.equals(Arena.Team.RED)) {
                Utils.sendTitle(pl, "§cVocê está na equipe Vermelha", "§aBom Jogo", 0, 20 * 3, 10);
            } else {

                Utils.sendTitle(pl, "§9Você está na equipe Azul", "§aBom Jogo", 0, 20 * 3, 10);
            }
            ChatUtils.broadcastMessage("§aO Jogador §l" + pl.getName() + "§r§a entrou no jogo!");
            ar.createScore(pl);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    final int elo = MatchMaker.db.getElo(pl.getUniqueId());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                        @Override
                        public void run() {
                            if (pl != null) {
                                ScoreCWs.setIngameName(elo, pl, t);
                            }
                        }
                    });
                }
            }).start();

        }
    }

    List<UUID> japegou = new ArrayList();

    @EventHandler(priority = EventPriority.NORMAL)
    public void join(final PlayerJoinEvent ev) {

        if (ev.getPlayer() == null) {
            return;
        }

        //DB.startPlayer(ev.getPlayer());
        final PlayerIngame ingame = getPlayerInGameBr(ev.getPlayer());

        if (ingame != null) {

            Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance,
                    new Runnable() {

                        @Override
                        public void run() {

                            final Arena.Team t;
                            if (ingame.team == 0) {
                                t = Arena.Team.RED;
                            } else {
                                t = Arena.Team.BLUE;
                            }
                            ScoreboardManager.registerScoreboard(ev.getPlayer());
                            logoucomequipe(ev.getPlayer(), t);

                        }
                    });
        } else {
            if (ev.getPlayer().hasPermission("cardwars.staff")) {

                Arena ar = CardWarsPlugin.getArena();
                if (ar != null) {
                    if (ar.getTeam(ev.getPlayer().getUniqueId()) == null) {
                        ar.addPlayer(ev.getPlayer(), Arena.Team.SPEC);
                        ev.getPlayer().sendMessage("§cTu ta de spec!");
                        ev.getPlayer().teleport(ev.getPlayer().getWorld().getSpawnLocation());
                        ar.addSpec(ev.getPlayer());
                        MakeVanish.makeVanished(ev.getPlayer());
                        Utils.sendTitle(ev.getPlayer(), "§7Tu ta de espectador", "§7cuidado o que você faz", 0, 20 * 3, 10);

                    }
                    ScoreboardManager.registerScoreboard(ev.getPlayer());
                    ar.createScore(ev.getPlayer());
                }

            } else {
                Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                    @Override
                    public void run() {
                        ev.getPlayer().kickPlayer("§cVocê não deveria estar aqui!");
                    }
                }, 20 * 3);

            }

        }

    }

    public PlayerIngame getPlayerInGameBr(Player p) {

        if (infs.containsKey(p)) {
            return infs.get(p);
        } else {

            try {

                PlayerIngame pi = MatchMaker.db.getIngamePlayer(p);
                if (pi != null) {
                    infs.put(p, pi);
                }
                return pi;
            } catch (NullPointerException e) {
                //e.printStackTrace();
                return null;
            }
        }

    }

    @EventHandler
    public void leave(PlayerQuitEvent e) {
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void atira(EntityShootBowEvent ev) {
        if (!ev.isCancelled()) {
            BombaFumaca.atiraFlecha(ev);

        }
    }
}
