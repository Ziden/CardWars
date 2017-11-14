/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.arena.KomQuista;

/**
 *
 * @author usuario
 */
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import truco.plugin.CardWarsPlugin;
import truco.plugin.arena.Arena.Team;
import truco.plugin.cards.ControleCartas;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.functions.MakeVanish;
import truco.plugin.data.MetaShit;
import truco.plugin.events.CustomDeathEvent;
import truco.plugin.functions.Cooldown;
import truco.plugin.functions.ScoreCWs;
import truco.plugin.utils.ChatUtils;

public class KomQuistaListener implements Listener {

    public KomQuista komquista = null;
    public static int vidared = 100;
    public static int vidablue = 100;

    public KomQuistaListener(KomQuista k) {
        komquista = k;
    }

    @EventHandler
    public void interage(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (komquista.getTeam(p.getUniqueId()) == Team.SPEC || komquista.getSpecs().contains(p.getUniqueId())) {
            return;
        }
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {

                Sign placa = (Sign) e.getClickedBlock().getState();
                if (placa.getLine(0).equalsIgnoreCase("[CardWars]")) {
                    if (placa.getLine(1).equalsIgnoreCase("[KomQuista]")) {
                        e.setCancelled(true);
                        if (MakeVanish.isVanished(p) && p.hasMetadata("bombadefumaca")) {
                            p.sendMessage("§7Você está invisivel não pode bater na placa!");
                            return;
                        }
                        if (placa.getLine(3).isEmpty()) {
                            placa.setLine(3, "§4♥§0§r:100");
                        }
                        placa.update();
                        // qual time que eh dono da placa (+ eh vermelho - eh azul)
                        Team timeDonoDaPlaca = placa.getLine(2).equalsIgnoreCase("+") ? Team.RED : Team.BLUE;

                        Team timeDoPlayer = komquista.getTeam(p.getUniqueId());

                        if (timeDoPlayer == timeDonoDaPlaca) {
                            p.sendMessage(ChatColor.RED + "Voce nao pode bater no seu proprio castelo...");
                            e.setCancelled(true);
                            return;
                        }

                        //if (KomQuista.isOpen()) {
                        if (p.hasMetadata("bateunokomquista")) {
                            return;
                        }
                        //if (cp.getClan() != KomQuista.lastClan) {
                        MetaShit.setMetaString("bateunokomquista", p, null);
                        Runnable r = new Runnable() {

                            final Player pr = p;

                            @Override
                            public void run() {
                                pr.removeMetadata("bateunokomquista", CardWarsPlugin._instance);
                            }
                        };
                        Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, r, 15);
                        int vida = Integer.parseInt(placa.getLine(3).split(":")[1]);
                        int dano = 1;
                        Material m = p.getItemInHand().getType();

                        if (m == Material.GOLD_SWORD || m == Material.GOLD_AXE || m == Material.GOLD_HOE || m == Material.GOLD_PICKAXE) {
                            dano += 2;
                        }
                        if (p.getItemInHand().getType() == Material.BONE && p.isOp()) {
                            dano = vida - 1;
                        }
                        placa.getWorld().playEffect(placa.getLocation(), Effect.MAGIC_CRIT, 4);

                        placa.getWorld().playSound(placa.getLocation(), Sound.BLAZE_HIT, 1, 1);
                        if (vida - dano <= 0) {
                            // KomQuista.dominou(cp.getClan());
                            MatchMaker.db.addGoldWithThread(p.getUniqueId(), 3, true, true);
                            komquista.domina(timeDoPlayer);
                            placa.getWorld().getHighestBlockAt(placa.getLocation()).getLocation();
                            double x = placa.getWorld().getHighestBlockAt(placa.getLocation()).getLocation().getX();
                            double y = placa.getWorld().getHighestBlockAt(placa.getLocation()).getLocation().getY();
                            double z = placa.getWorld().getHighestBlockAt(placa.getLocation()).getLocation().getZ();
                            placa.getWorld().strikeLightningEffect(placa.getWorld().getHighestBlockAt(placa.getLocation()).getLocation());
                            placa.getWorld().strikeLightningEffect(new Location(placa.getWorld(), x, y, z));
                            placa.getWorld().strikeLightningEffect(new Location(placa.getWorld(), x + 2, y, z));
                            placa.getWorld().strikeLightningEffect(new Location(placa.getWorld(), x - 2, y, z));
                            placa.getWorld().strikeLightningEffect(new Location(placa.getWorld(), x, y, z - 2));
                            placa.getWorld().strikeLightningEffect(new Location(placa.getWorld(), x, y, z + 2));
                            placa.getWorld().playEffect(placa.getLocation(), Effect.EXPLOSION_LARGE, 7);

                            placa.setLine(3, "§4♥§0§r:100");
                            placa.update();

                        } else {
                            placa.setLine(3, "§4♥§0§r:" + (vida - dano));

                            placa.update();
                            if (timeDonoDaPlaca == Team.RED) {
                                for (Player pl : Bukkit.getOnlinePlayers()) {
                                    ScoreCWs.setScoreLine(pl, 2, "§c§lVida: " + (vida - dano));

                                }
                                vidared = vida - dano;
                            } else {
                                for (Player pl : Bukkit.getOnlinePlayers()) {
                                    ScoreCWs.setScoreLine(pl, 1, "§9§lVida: " + (vida - dano));
                                }
                                vidablue = vida - dano;
                            }
                        }

                        //}
                    }
                }
            }
        }

    }

    @EventHandler
    public void respawn(CustomDeathEvent ev) {

        final UUID uuid = ev.getPlayer().getUniqueId();
        if (komquista.getTeam(uuid) == Team.SPEC) {
            ev.getPlayer().teleport(komquista.getTpLocation(ev.getPlayer()));
            return;
        }
        if (!komquista.getSpecs().contains(uuid)) {
            komquista.addSpec(ev.getPlayer());
            Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p != null) {
                        p.teleport(komquista.getTpLocation(p));

                        ControleCartas.updateInventoryCards(p, false);
                        ChatUtils.sendMessage(p, "§aVocê renasceu!");
                        Cooldown.addCoolDown(p, "nodamage", 5000);

                    }

                    komquista.removeSpec(uuid);

                }

            }, 20 * 5);
            ev.getPlayer().teleport(komquista.getTpLocation(ev.getPlayer()));
            ev.getPlayer().sendMessage("Voce morreu, e renascera em alguns segundos !");
        }
    }
}
