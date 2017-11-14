
/*
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 */

package truco.plugin.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import truco.plugin.chat.ChatManager;
import truco.plugin.chat.ChatMeta;

/**
 *
 * @author usuario
 * 
 */

public class Staff implements CommandExecutor {

   @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if(cs instanceof Player) {
            Player p = (Player)cs;
            if(!p.hasPermission("cardwars.chatbigboss")) {
                return true;
            } 
            ChatMeta meta = ChatManager.getMeta(p);
            if(meta.inChannel==null || !meta.inChannel.equalsIgnoreCase("staff")) {
                meta.inChannel = "staff"; 
                p.sendMessage(ChatColor.GOLD+"Voce esta falando dos bigboss (privado), pegue um whisky e sirva-se de um charuto, pode fazer as fofoca pros coleginha!");
            } else {
                meta.inChannel = null;
                p.sendMessage(ChatColor.GOLD+"Voce saiu do chat bigboss");
            }
        }
        return true;
    }
     
}
