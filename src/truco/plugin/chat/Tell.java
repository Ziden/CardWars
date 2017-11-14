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
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import static truco.plugin.CardWarsPlugin.server;

/**
 *
 * @author usuario
 *
 */
public class Tell implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        // if(cs instanceof Player) {
        //Player p = (Player)cs;
        //if(args.length<1) {
        //    p.sendMessage(ChatColor.LIGHT_PURPLE+"Digite /msg <quem> <mensagem> ou /msg <quem>");
        //    return true;
        //}
        if (server == ServerType.TUTORIAL) {
            return false;
        }
        if (cs instanceof Player) {
            Player p = (Player) cs;
            ChatMeta meta = ChatManager.getMeta(p);
            if (args.length < 1) {
                if (meta.talkingTo == null) {
                    p.sendMessage(ChatColor.YELLOW + "Digite " + ChatColor.GREEN + "/tell" + ChatColor.RED + " <quem> <mensagem> " + ChatColor.YELLOW + "Para iniciar o chat privado !");
                    return true;
                } else {
                    meta.talkingTo = null;
                    p.sendMessage(ChatColor.YELLOW + "Agora voce esta falando no local !");
                    return true;
                }
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("on")) {
                    p.sendMessage(ChatColor.GREEN + "Tell ligado !");
                    meta.ignoreTell = false;
                    return true;
                } else if (args[0].equalsIgnoreCase("off")) {
                    p.sendMessage(ChatColor.GREEN + "Tell desligado !");
                    meta.ignoreTell = true;
                    return true;
                }
            }

            String player = args[0];
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                p.sendMessage(ChatColor.YELLOW + "Nick nao encontrado.");
                return true;
            }
            if (target == p) {
                p.sendMessage("§eCara não precisa ser solitario, tente se comunicar com outras pessoas!");
                return true;
            }
            if (ChatManager.getMeta(target).ignoreTell && target.isOp()) {
                p.sendMessage(ChatColor.YELLOW + "Nick nao encontrado.");
                return false;
            }
            if (ChatManager.getMeta(target).ignoreTell && !cs.isOp()) {
                p.sendMessage(ChatColor.RED + "Este jogador esta ocupado.");
                return false;
            }
            if (args.length == 1) {
                p.sendMessage(ChatColor.YELLOW + "Iniciando conversa privada com " + ChatColor.WHITE + target.getName() + ChatColor.YELLOW + ".");
                ChatManager.getMeta(p).talkingTo = target.getName();
            } else {
                StringBuffer str = new StringBuffer();
                StringBuffer strFrom = new StringBuffer();

                strFrom.append(ChatColor.DARK_AQUA + "Para " + target.getName() + ": " + ChatColor.AQUA);
                str.append(ChatColor.DARK_AQUA + "De " + p.getName() + ": " + ChatColor.AQUA);
                for (int x = 1; x < args.length; x++) {
                    str.append(args[x] + " ");
                    strFrom.append(args[x] + " ");
                }
                target.sendMessage(str.toString());
                p.sendMessage(strFrom.toString());
                CardWarsPlugin.log.info(ChatColor.AQUA + strFrom.toString());
                ChatManager.getMeta(target).lastPlayerMessage = p.getName();
            }
        }
        return true;
    }
}
