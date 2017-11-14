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

/**
 *
 * @author usuario
 * 
 */

public class L implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if(cs instanceof Player) {
            Player p = (Player)cs;
            ChatMeta meta = ChatManager.getMeta(p);
            if(meta.talkingTo==null) {
                p.sendMessage(ChatColor.YELLOW+"Voce ja está falando normalmente !");
            } else {
                meta.talkingTo = null;
                p.sendMessage(ChatColor.GREEN+"Agora você está falando no local !");
            }
        }
        
        return true;
    }
    
}
