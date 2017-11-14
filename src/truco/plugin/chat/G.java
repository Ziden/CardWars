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
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.Cooldown;
import truco.plugin.managers.PermManager;
import truco.plugin.socket.SocketManager;

/**
 *
 * @author usuario
 *
 */
public class G implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
//9934 6094  3789 1310 
            if (server == ServerType.TUTORIAL) {
                ChatUtils.sendMessage(p, "§bComplete o tutorial para se juntar aos jogadores!");
                return false;
            }
            if (Cooldown.isCooldown(p, "chatglobal")) {
                p.sendMessage("§aEspere um pouco para usar o chat global!");
                return false;
            }
            if (strings.length == 0) {
                p.sendMessage("§cUse §e/g mensagem §c!");
                return false;
            }
            if (!PermManager.GLOBAL.playerHas(cs) && server == ServerType.LOBBY) {
                ChatUtils.sendMessage(p, "§bSomente vips podem usar o chat global!");
                return false;
            }

            ChatMeta meta = ChatManager.getMeta(p);
            StringBuffer msg = new StringBuffer();
            PermissionUser user = PermissionsEx.getUser(p);
            String prefix = "";

            if (user != null && user.getPrefix() != null) {
                prefix = user.getPrefix();
            }
            if (!p.isOp()) {
                if (server == ServerType.LOBBY) {
                    Cooldown.addCoolDown(p, "chatglobal", 15000);
                } else if (server == ServerType.GAME) {
                    Cooldown.addCoolDown(p, "chatglobal", 5000);

                }
            }
            StringBuffer prefixo = new StringBuffer();
            if (prefix.length() > 0) {
                for (int x = 0; x < prefix.length(); x++) {
                    if (prefix.charAt(x) == '&') {
                        ChatColor cor = ChatColor.getByChar(prefix.charAt(x + 1));
                        x++;
                        prefixo.append(cor);
                    } else {
                        prefixo.append(prefix.charAt(x));
                    }
                }
            }
            prefix = prefixo.toString();
            ChatColor textColor = ChatColor.GRAY;
            ChatColor nickColor = ChatColor.WHITE;

            if (CardWarsPlugin.server == ServerType.LOBBY) {
                if (meta.nickColor != null) {
                    nickColor = meta.nickColor;
                }

                if (meta.chatColor != null) {
                    textColor = meta.chatColor;
                }
            } else if (server == ServerType.GAME) {
                if (CardWarsPlugin.getArena() != null) {
                    if (CardWarsPlugin.getArena().getTeam(p.getUniqueId()) != null) {
                        nickColor = CardWarsPlugin.getArena().getTeam(p.getUniqueId()).getCor();
                    }
                }
            }

            msg.append(ChatColor.GRAY);
            msg.append("[g] ");
            msg.append(prefix);
            msg.append("").append(ChatColor.WHITE).append(nickColor).append(p.getName()).append(ChatColor.GRAY).append(": ").append(textColor);

            for (String arg : strings) {
                msg.append(arg + " ");

            }

            String msgn = msg.toString();
            if (PermManager.CORESCHAT.playerHas(cs)) {
                msgn = ChatManager.translateColorCodes(msgn);
            }

            for (Player pl : Bukkit.getOnlinePlayers()) {
                ChatMeta metap = ChatManager.getMeta(pl);
                if (metap.recebeglobal == true) {
                    pl.sendMessage(msgn);
                }
            }
            if (server == ServerType.LOBBY) {
                SocketManager.bukkitBroadcastMessage(SocketManager.LOBBY_VARIABLE, msgn);
            }
        }
        return false;
    }


}
