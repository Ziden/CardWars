/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.signs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import truco.plugin.CardWarsPlugin;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.functions.Cooldown;
import truco.plugin.managers.lobbys.LobbyObject;
import truco.plugin.managers.lobbys.MultipleLobbysManager;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos
 */
public class SignUtils {

    public static String line1 = "§c§l[CardWars]";

    public static boolean isInt(String g) {
        try {
            int s = Integer.valueOf(g);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static void clicaplaca(final PlayerInteractEvent ev) {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (ev.getClickedBlock().getType() == Material.WALL_SIGN || ev.getClickedBlock().getType() == Material.SIGN || ev.getClickedBlock().getType() == Material.SIGN_POST) {

                final Sign s = (Sign) ev.getClickedBlock().getState();
                if (s.getLine(0).equalsIgnoreCase(SignUtils.line1)) {
                    Mostrar.blockclick(ev, s);
                    SignTeleporter.blockclick(ev, s);
                    Equip.blockclick(ev, s);
                    if (s.getLine(1).equalsIgnoreCase("Tutorial")) {
                        if (Cooldown.isCooldown(ev.getPlayer(), "terminatuto")) {
                            return;
                        }
                        Cooldown.addCoolDown(ev.getPlayer(), "terminatuto", 60000);
                        if (!MatchMaker.db.fezTutorial(ev.getPlayer().getUniqueId())) {
                            MatchMaker.db.terminaTutorial(ev.getPlayer().getUniqueId());
                            ev.getPlayer().sendMessage("§a§lVocê terminou o tutorial, agora pode jogar normalmente!");
                            ev.getPlayer().sendMessage("§bAo chegar no Lobby procure a sala de equipamentos!");
                            ev.getPlayer().sendMessage("§bSuas cartas do tutorial iram estar no BAU!!");
                            ev.getPlayer().getEnderChest().clear();
                            List<ItemStack> ints = new ArrayList();
                            for (int x = 0; x < 9; x++) {
                                ints.add(null);
                            }
                            CardsDB.saveEnderchest(ints, ev.getPlayer().getUniqueId());
                        } else {
                            ev.getPlayer().sendMessage("§a§lVocê será teleportado para o Lobby!");
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                            @Override
                            public void run() {
                                if (ev.getPlayer() == null) {
                                    return;
                                }

                                LobbyObject lo = MultipleLobbysManager.getPerfectLobby(ev.getPlayer());

                                if (lo == null) {
                                    ev.getPlayer().kickPlayer("§aTodos os lobbys estão lotados tente entrar novamente!");
                                } else {
                                    Utils.TeleportarTPBG(lo.getNome(), ev.getPlayer());
                                }
                            }
                        }, 20 * 10);
                    }
                }
            }
        }
    }

    public static void colocaplaca(SignChangeEvent ev) {
        if (ev.getLine(0).equalsIgnoreCase("Server")) {
            if (ev.getPlayer().isOp()) {
                ev.setLine(0, line1);
            }

        }
    }

}
