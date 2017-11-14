/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.arena.Dominion;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import truco.plugin.arena.Arena.Team;
import truco.plugin.arena.GameState;
import truco.plugin.functions.Cooldown;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PlayerDominando {

    UUID uuid;
    Base b;
    int task = -1;
    int tempo = 0;
    public static int tempoPraConq = 8;
    public Dominion d;

    public static HashMap<UUID, PlayerDominando> dominando = new HashMap<UUID, PlayerDominando>();

    public PlayerDominando(Player p, Base b, Dominion d) {
        this.uuid = p.getUniqueId();
        this.b = b;
        this.d = d;
        b.setTadominando(uuid);
        dominando.put(uuid, this);
        start();
    }

    public void cancel() {
        if (task != -1) {
            dominando.remove(uuid);
            Bukkit.getScheduler().cancelTask(task);
            b.setTadominando(null);
        }
    }

    public static void trocaCor(Team t, ProtectedRegion pr, World m) {
        short d;
        if (t == Team.RED) {
            d = 14;
        } else if (t == Team.BLUE) {
            d = 11;
        } else {
            d = 0;
        }
        for (Block b : truco.plugin.utils.Utils.getStructureBlocks(pr, m)) {
            if (b.getType() == Material.STAINED_CLAY || b.getType() == Material.STAINED_GLASS || b.getType() == Material.STAINED_GLASS_PANE || b.getType() == Material.WOOL) {
                b.setData((byte) d);
            }
        }
    }

    public final void start() {

        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                if (b.d.getState() == GameState.INGAME) {
                    tempo++;
                    Player p = Bukkit.getPlayer(uuid);
                    if (p != null) {
                        Location l = p.getLocation();
                        if (b.getRegiao().contains(l.getBlockX(), l.getBlockY(), l.getBlockZ())) {
                            if (tempo >= tempoPraConq) {
                                Team t = d.getTeam(uuid);
                                Team tc = d.getTeam(uuid);
                                if (!Cooldown.isCooldown(p, "ganhamoedadmn")) {
                                    MatchMaker.db.addGoldWithThread(p.getUniqueId(), 1, true,true);
                                    Cooldown.addCoolDown(p, "ganhamoedadmn", 30000);
                                }
                                if (b.getDona() == null) {
                                    b.setDona(d.getTeam(uuid));
                                    ChatUtils.broadcastMessage("§eA equipe " + t.getColoredName(false) + " §edominou " + b.sufixo + " " + b.getRegiao().getId());
                                    for (Player pl : Bukkit.getOnlinePlayers()) {
                                        pl.playSound(pl.getLocation(), Sound.ZOMBIE_UNFECT, 1.5F, 100.0F);
                                        Utils.sendTitle(pl, "§3§l" + b.getRegiao().getId().toUpperCase(), "§7agora é da equipe " + t.getColoredName(false), 5, 10, 5);
                                    }
                                } else {
                                    b.setDona(null);
                                    tc = null;
                                    ChatUtils.broadcastMessage("§b" + b.sufixo.toUpperCase() + " " + b.getRegiao().getId() + " se tornou neutra!");
                                    for (Player pl : Bukkit.getOnlinePlayers()) {
                                        Utils.sendTitle(pl, "§3§l" + b.getRegiao().getId().toUpperCase(), "§7agora é §f§lNeutro(a) !", 5, 10, 5);
                                    }

                                }
                                trocaCor(tc, b.getRegiao(), p.getWorld());
                                cancel();
                            } else {

                                int t = tempoPraConq - tempo;
                                String v = t > 1 ? "s" : "";
                                String s = t > 1 ? "m" : "";
                                ChatUtils.sendMessage(p, "§eFalta" + s + " §c" + (tempoPraConq - tempo) + " §esegundo" + v + " para dominar a base!");
                                p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F + 0.1F * (tempo));
                            }
                        } else {
                            ChatUtils.broadcastMessage("§c" + b.sufixo.toUpperCase() + " " + b.getRegiao().getId() + " foi abandonada!");
                            ChatUtils.sendMessage(p, "§eVocê saiu da região da base!");
                            cancel();
                        }
                    } else {
                        ChatUtils.broadcastMessage("§c" + b.sufixo.toUpperCase() + " " + b.getRegiao().getId() + " foi abandonada!");
                        cancel();
                    }
                }
            }
        },
                20, 20);

    }

}
