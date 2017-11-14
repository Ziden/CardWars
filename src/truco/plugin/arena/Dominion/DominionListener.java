/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.arena.Dominion;

import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.arena.Arena.Team;
import truco.plugin.arena.GameState;
import truco.plugin.cards.ControleCartas;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;
import truco.plugin.events.CustomDeathEvent;
import truco.plugin.functions.Cooldown;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.MakeVanish;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class DominionListener implements Listener {

    Dominion domi;

    public DominionListener(Dominion d) {
        domi = d;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void damage(CustomDamageEvent ev) {
        if (ev.getPlayerTomou() != null) {
            Player p = ev.getPlayerTomou();
            if (ev.getCause() != CausaDano.FALL && ev.getFinalDamage() > 0 && !ev.isCancelled()) {
                if (PlayerDominando.dominando.containsKey(p.getUniqueId())) {
                    PlayerDominando pd = PlayerDominando.dominando.get(p.getUniqueId());
                    ChatUtils.broadcastMessage("§c§l" + pd.b.sufixo.toUpperCase() + " " + pd.b.getRegiao().getId() + " parou de ser dominada!");
                    pd.cancel();

                }
            }
        }
    }

    @EventHandler
    public void respawn(final CustomDeathEvent ev) {

        final Dominion dm = domi;
        final UUID uuid = ev.getPlayer().getUniqueId();
        if (!dm.getSpecs().contains(uuid)) {
            dm.addSpec(ev.getPlayer());

            Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p != null) {

                        Location essa = null;
                        double dis = 9999999;
                        for (Base b : dm.bases) {
                            if (b.getDona() == dm.getTeam(ev.getPlayer().getUniqueId())) {
                                Location s;
                                com.sk89q.worldedit.Vector v = (b.getRegiao().getMaximumPoint().subtract(b.getRegiao().getMinimumPoint()).divide(2)).add(b.getRegiao().getMinimumPoint());
                                if (b.getRegiao().getFlag(DefaultFlag.TELE_LOC) != null) {
                                    s = BukkitUtil.toLocation(b.getRegiao().getFlag(DefaultFlag.TELE_LOC));
                                } else {

                                    s = new Location(ev.getPlayer().getWorld(), v.getBlockX(), v.getBlockY(), v.getBlockZ());
                                }
                                if (s.distance(ev.getPlayer().getLocation()) < dis) {
                                    essa = s;
                                    dis = s.distance(ev.getPlayer().getLocation());
                                }
                            }
                        }
                        Location base = dm.getTpLocation(ev.getPlayer());
                        if (essa != null) {
                            if (base.distance(ev.getPlayer().getLocation()) < essa.distance(ev.getPlayer().getLocation())) {
                                essa = base;
                            }
                        }
                        if (essa != null) {

                            ev.getPlayer().teleport(essa);
                        } else {
                            dm.tp(ev.getPlayer());
                        }

                        ChatUtils.sendMessage(p, "§aVocê renasceu!");
                        ControleCartas.updateInventoryCards(ev.getPlayer(), false);
                        Cooldown.addCoolDown(p, "nodamage", 2000);
                    }
                    dm.removeSpec(uuid);

                }
            }, 20 * 8);

        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        if (domi.getState() != GameState.INGAME) {
            return;
        }
        if (domi.getTeam(e.getPlayer().getUniqueId()) != null && domi.getTeam(e.getPlayer().getUniqueId()) != Team.SPEC && !domi.getSpecs().contains(e.getPlayer().getUniqueId())) {
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (e.getClickedBlock().getType() != Material.SPONGE) {
                    return;
                }

                Iterator<ProtectedRegion> aprs = CardWarsPlugin.worldGuard.getRegionManager(e.getPlayer().getWorld()).getApplicableRegions(e.getClickedBlock().getLocation()).iterator();
                if (aprs.hasNext()) {
                    ProtectedRegion regiao = aprs.next();
                    Base b = Base.getBase(regiao, domi);
                    if (b != null) {
                        if (MakeVanish.isVanished(e.getPlayer()) && e.getPlayer().hasMetadata("bombadefumaca")) {
                            e.getPlayer().sendMessage("§7Você não pode dominar invisivel!");
                            return;
                        }
                        Location ploc = e.getPlayer().getLocation();

                        if (regiao.contains(ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ())) {
                            Team t = domi.getTeam(e.getPlayer().getUniqueId());
                            if (t != b.getDona()) {
                                if (Skill.checkCD(e.getPlayer(), "dominou", 5)) {
                                    if (b.tadominando == null) {
                                        ChatUtils.sendMessage(e.getPlayer(), "§eVocê está dominando uma região!");
                                        ChatUtils.broadcastMessage("§eO Jogador " + t.getCor() + e.getPlayer().getName() + "§e está dominando " + b.sufixo + " " + regiao.getId() + " !");
                                        if (b.getDona() != null) {
                                            for (UUID ut : domi.getPlayers(b.getDona())) {
                                                Player pt = Bukkit.getPlayer(ut);
                                                if (pt != null) {
                                                    ChatColor cor = b.getDona().getCor();
                                                    Utils.sendTitle(pt, "§b§lCuidado !!!!", t.getCor() + e.getPlayer().getName() + "§e está dominando " + cor + b.sufixo + " " + regiao.getId() + "§e!", 5, 10, 5);
                                                    pt.playSound(pt.getLocation(), Sound.CLICK, 1, 1);
                                                    pt.playSound(pt.getLocation(), Sound.CLICK, 1, 1);
                                                }
                                            }

                                        }
                                        new PlayerDominando(e.getPlayer(), b, domi);
                                    } else {
                                        OfflinePlayer of = Bukkit.getOfflinePlayer(b.tadominando);
                                        ChatUtils.sendMessage(e.getPlayer(), "§eO Jogador " + domi.getTeam(of.getUniqueId()).getCor() + "" + of.getName() + "§e já está dominado está base!");
                                    }

                                    Skill.addCd(e.getPlayer(), "dominou");
                                }
                            } else {
                                ChatUtils.sendMessage(e.getPlayer(), "§eEstá base já é sua!");
                            }
                        } else {
                            ChatUtils.sendMessage(e.getPlayer(), "§bVocê precisa estar dentro da base para conquistar ela!");
                        }

                    }
                }
            }
        }
    }
}
