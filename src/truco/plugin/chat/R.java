/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.chat;

import org.bukkit.Bukkit;
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
public class R implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;

            ChatMeta meta = ChatManager.getMeta(p);
            if (meta.lastPlayerMessage == null) {
                p.sendMessage(ChatColor.RED + "Ninguem falou com voce para voce dar /r <mensagem>");
                return true;
            }
            Player target = Bukkit.getPlayer(meta.lastPlayerMessage);


            if (target == null) {
                p.sendMessage(ChatColor.RED + "Este jogador nao esta online !");
                return true;
            }
            ChatMeta tmeta = ChatManager.getMeta(target);
            if (tmeta.ignoreTell) {
                if (target.isOp()) {
                    p.sendMessage(ChatColor.RED + "Este jogador nao esta online !");
                } else {
                    p.sendMessage(ChatColor.YELLOW + "Jogador ocupado!");
                }

                meta.lastPlayerMessage = null;
                return true;
            }

            if (args.length == 0) {
                p.sendMessage(ChatColor.YELLOW + "Iniciando conversa privada com " + ChatColor.WHITE + target.getName() + ChatColor.YELLOW + ".");
                meta.talkingTo = target.getName();
                return true;
            }

            StringBuffer str = new StringBuffer();
            str.append(ChatColor.DARK_AQUA + "De " + p.getName() + ": " + ChatColor.AQUA);

            StringBuffer strFrom = new StringBuffer();
            strFrom.append(ChatColor.DARK_AQUA + "Para " + target.getName() + ": " + ChatColor.AQUA);

            for (int x = 0; x < args.length; x++) {
                str.append(args[x] + " ");
                strFrom.append(args[x] + " ");
            }
            p.sendMessage(strFrom.toString());

            target.sendMessage(str.toString());
            ChatManager.getMeta(target).lastPlayerMessage = p.getName();
        }
        return true;
    }
}
