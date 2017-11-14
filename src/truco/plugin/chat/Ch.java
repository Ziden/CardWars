/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import truco.plugin.managers.PermManager;

/**
 *
 * @author usuario
 *
 */
public class Ch implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (cs instanceof Player) {
            if (args.length != 1) {
                Player p = (Player) cs;
                p.sendMessage(ChatColor.AQUA + "_____oO CardWars Chat Oo______");

                if (CardWarsPlugin.server == ServerType.LOBBY) {
                    p.sendMessage(ChatColor.AQUA + "|" + ChatColor.GREEN + " /tell <quem> <msg> - manda uma msg");
                    p.sendMessage(ChatColor.AQUA + "|" + ChatColor.GREEN + " /r <msg> responde a msg");
                    p.sendMessage(ChatColor.AQUA + "|" + ChatColor.GREEN + " /l - fala no canal local");
                    if (PermManager.GLOBAL.playerHas(cs)) {
                        p.sendMessage(ChatColor.AQUA + "|" + ChatColor.GREEN + " /g <msg> fala no global");

                    }

                } else {
                    p.sendMessage(ChatColor.AQUA + "|" + ChatColor.GREEN + " /g <msg> fala no global");
                    p.sendMessage(ChatColor.AQUA + "|" + ChatColor.GREEN + " /l - fala no canal de sua equipe");
                }

                p.sendMessage(ChatColor.AQUA + "______________________________");
            }

        }
        return true;
    }
}
