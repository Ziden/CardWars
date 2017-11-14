/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.arena;

import truco.plugin.data.MetaShit;
import truco.plugin.functions.game.Mana;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.CardWarsPlugin;
import truco.plugin.functions.game.MultipleKills;
import truco.plugin.arena.Arena.Team;
import truco.plugin.data.mysql.MatchHistoryDB;
import truco.plugin.utils.*;
import truco.plugin.cards.StatusEffect;
import truco.plugin.cards.StatusEffect.StatusMod;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDeathEvent;
import truco.plugin.functions.Cooldown;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ArenaListener implements Listener {

    Arena ar;

    public ArenaListener(Arena ar) {
        this.ar = ar;
    }

    @EventHandler
    public void move(PlayerMoveEvent ev) {

        if (ar.getTime() < 10) {
            if (ar.getTeam(ev.getPlayer().getUniqueId()) != Team.SPEC) {
                if (Utils.hasChangedBlockCoordinates(ev.getFrom(), ev.getTo())) {
                    Location from = ev.getFrom();
                    double x = Math.floor(from.getX());
                    double z = Math.floor(from.getZ());

                    x += .5;
                    z += .5;
                    ev.getPlayer().teleport(new Location(from.getWorld(), x, from.getY(), z, from.getYaw(), from.getPitch()));

                }
            }
        }
    }

    @EventHandler
    public void atiraflecha(EntityShootBowEvent ev) {
        if (ev.getEntity() instanceof Player) {
            Player p = (Player) ev.getEntity();
            if (ar.specs.contains(p.getUniqueId())) {
                ev.setCancelled(true);
            } else {
            }

        }

    }

    @EventHandler
    public void join(PlayerJoinEvent ev) {
        Player p = ev.getPlayer();
      
        if (ar.toremove.contains(ev.getPlayer().getUniqueId())) {
            ar.removeSpec(ev.getPlayer().getUniqueId());
        }
        ev.getPlayer().sendMessage("§b[Arena] §c" + ar.getDesc());
    }

    public void quit(Player p) {
        Team t = ar.getTeam(p.getUniqueId());
        if (t == null) {
            return;
        }
        if (t == Team.SPEC) {
            ar.removePlayer(p.getUniqueId());
            return;
        }
        if (ar.getState() == GameState.INGAME) {
            ChatUtils.broadcastMessage("§7O Jogador §l" + p.getName() + "§r§7 saiu do jogo!");
        }
        Team oposta = Team.getTeamOposta(t);
        int onlinet = 0;
        int onlineoposta = 0;
        for (UUID uuid : ar.getPlayers()) {
            OfflinePlayer of = Bukkit.getOfflinePlayer(uuid);
            if (of.isOnline()) {
                if (ar.getTeam(uuid) == t) {
                    onlinet++;
                } else if (ar.getTeam(uuid) == oposta) {
                    onlineoposta++;
                }
            }
        }
        if (onlinet <= 1 && onlineoposta > 0) {
            ar.win(oposta);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void quit(PlayerQuitEvent ev) {
        quit(ev.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void kick(PlayerKickEvent ev) {
        quit(ev.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void edbev(CustomDamageEvent ev) {

        if (ar.getState() == GameState.POSGAME || ar.getState() == GameState.CARREGANDO) {
            ev.setCancelled("Carregamento");
            return;
        }
        if (ev.getPlayerTomou() != null) {
            if (Cooldown.isCooldown(ev.getPlayerTomou(), "nodamage")) {
                if (ev.getPlayerBateu() != null) {
                    ev.getPlayerBateu().sendMessage("§6§lAlvo invuneravel!");
                }
                ev.setCancelled("Invuneravel");
                return;
            }
        }
        if (ev.getPlayerBateu() != null && ev.getProjetil() != null) {

            Player shooter = ev.getPlayerBateu();
            if (ev.getPlayerTomou() != null) {
                Player p = ev.getPlayerTomou();
                if (!TeamUtils.canAttack(shooter, p)) {
                    ev.setCancelled("Equipe");
                }

            }

        }

        if (ev.getPlayerBateu() != null) {

            if (ev.getPlayerTomou() != null) {
                Player t1 = (Player) ev.getPlayerTomou();
                Player t2 = (Player) ev.getPlayerBateu();
                if (!TeamUtils.canAttack(t1, t2)) {
                    ev.setCancelled("Equipe");
                }
            }

            if (ar.specs.contains(ev.getPlayerBateu().getUniqueId())) {
                ev.setCancelled("Espectador");
            }
        }
        if (ev.getPlayerTomou() != null) {
            Player p = ev.getPlayerTomou();

            if (ar.specs.contains(p.getUniqueId())) {
                ev.setCancelled("Espectador");
            }
        }

    }

    @EventHandler
    public void death(CustomDeathEvent ev) {
        if (MultipleKills.mkills.containsKey(ev.getPlayer().getUniqueId())) {
            MultipleKills.mkills.remove(ev.getPlayer().getUniqueId());
        }
        final Player p = ev.getPlayer();
        if (ev.getMatador() != null && ev.getMatador() instanceof Player) {
            final Player killer = (Player) ev.getMatador();
            if (ar.getTeam(killer.getUniqueId()) != null && ar.getTeam(p.getUniqueId()) != null) {
                String nomedano = "";
                if (ev.getDano() != null) {
                    nomedano = "§d[" + ev.getDano().getNome() + "]";
                    Utils.sendTitle(p, "§4VOCÊ MORREU!", ar.getTeam(killer.getUniqueId()).getCor() + killer.getName() + "§7 matou voce com §r" + ev.getDano().getCausa().getCor() + ev.getDano().getNome(), 0, 30, 0);

                } else {
                    Utils.sendTitle(p, "§4VOCÊ MORREU!", "", 0, 30, 0);

                }
                for (Player dofor : Bukkit.getOnlinePlayers()) {
                    if (dofor == p) {
                        Utils.sendActionBar(dofor, ar.getTeam(killer.getUniqueId()).getCor() + killer.getName() + "§7 " + IconLib.CAVEIRA + " §a" + p.getName() + " " + nomedano);
                    } else if (dofor.getUniqueId() == killer.getUniqueId()) {
                        Utils.sendActionBar(dofor, "§a" + killer.getName() + "§7 " + IconLib.CAVEIRA + " " + ar.getTeam(p.getUniqueId()).getCor() + p.getName() + " " + nomedano);

                    } else {
                        Utils.sendActionBar(dofor, ar.getTeam(killer.getUniqueId()).getCor() + killer.getName() + "§7 " + IconLib.CAVEIRA + " " + ar.getTeam(p.getUniqueId()).getCor() + p.getName() + " " + nomedano);

                    }
                }

            }

            new Thread(new Runnable() {

                @Override
                public void run() {
                    if (killer != null) {
                        MatchHistoryDB.addKill(killer.getUniqueId());
                        MatchHistoryDB.addMorte(p.getUniqueId());
                        if (ar.getGameId() != -1) {

                            if (p != null) {
                                if (ar.getTeam(killer.getUniqueId()) != Team.SPEC && ar.getTeam(p.getUniqueId()) != Team.SPEC) {

                                    MultipleKills.mata(Bukkit.getPlayer(killer.getUniqueId()), p);
                                }
                            }
                        }
                    }
                }
            }
            ).start();

        }

    }

}
