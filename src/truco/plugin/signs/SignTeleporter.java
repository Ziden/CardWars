/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.signs;

import org.bukkit.block.Sign;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.managers.lobbys.MultipleLobbysManager;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos
 */
public class SignTeleporter {

    public static void blockclick(final PlayerInteractEvent ev, Sign s) {

        if (s.getLine(1).equalsIgnoreCase("Teleporte")) {
            if (!s.getLine(2).equalsIgnoreCase("")) {
                if (s.getLine(2) != null) {
                    ChatUtils.sendMessage(ev.getPlayer(), "§cSendo teleportado para o server " + s.getLine(2));
                    String sv = s.getLine(2);
                    if(s.getLine(2).equalsIgnoreCase("Lobby")){
                        sv = MultipleLobbysManager.getPerfectLobby().getNome();
                    }
                    Utils.TeleportarTPBG(sv, ev.getPlayer());
                }

            }

        }
    }
}
