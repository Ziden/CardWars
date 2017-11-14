/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.chat;

import java.util.UUID;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.arena.Arena.Team;
import truco.plugin.managers.PermManager;
import truco.plugin.socket.SocketManager;
import truco.plugin.utils.ChatUtils;

/**
 *
 * @author usuario
 */
public class ChatManager implements Listener {

    public static String translateColorCodes(String string) {
        if (string == null) {
            return "";
        }
        String newstring = string;
        newstring = chatColorPattern.matcher(newstring).replaceAll("\u00A7$1");
        //newstring = chatMagicPattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatBoldPattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatStrikethroughPattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatUnderlinePattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatItalicPattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatResetPattern.matcher(newstring).replaceAll("\u00A7$1");
        return newstring;
    }
    protected static Pattern chatColorPattern = Pattern.compile("(?i)&([0-9A-F])");
    protected static Pattern chatMagicPattern = Pattern.compile("(?i)&([K])");
    protected static Pattern chatBoldPattern = Pattern.compile("(?i)&([L])");
    protected static Pattern chatStrikethroughPattern = Pattern.compile("(?i)&([M])");
    protected static Pattern chatUnderlinePattern = Pattern.compile("(?i)&([N])");
    protected static Pattern chatItalicPattern = Pattern.compile("(?i)&([O])");
    protected static Pattern chatResetPattern = Pattern.compile("(?i)&([R])");

    public static ChatMeta getMeta(Player p) {
        if (p == null) {
            return new ChatMeta();
        }
        if (p.hasMetadata("chat")) {
            ChatMeta meta = (ChatMeta) p.getMetadata("chat").get(0);
            return meta;
        } else {
            ChatMeta cMeta = new ChatMeta();
            p.setMetadata("chat", cMeta);
            return cMeta;
        }
    }

    public ChatManager() {
        Bukkit.getPluginCommand("l").setExecutor(new L());
        Bukkit.getPluginCommand("g").setExecutor(new G());

        Bukkit.getPluginCommand("r").setExecutor(new R());

        Bukkit.getPluginCommand("tell").setExecutor(new Tell());
        Bukkit.getPluginCommand("ch").setExecutor(new Ch());

        Bukkit.getPluginCommand("chatbb").setExecutor(new Staff());

        Bukkit.getServer().getPluginManager().registerEvents(this, CardWarsPlugin._instance);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void fala(AsyncPlayerChatEvent ev) {
        if (ev.isCancelled()) {
            return;
        }
        // evitar memory leak  , o " string " + " string " do java aloca mta memoria atoa
        StringBuilder cache = new StringBuilder();
        ChatMeta meta = ChatManager.getMeta(ev.getPlayer());
        if (meta.talkingTo != null) {
            Player p = Bukkit.getPlayer(meta.talkingTo);
            ChatMeta targetmeta = getMeta(p);
            if ((p == null) || (p.isOp() && targetmeta.ignoreTell)) {
                meta.talkingTo = null;
            } else {
                cache.append(ChatColor.DARK_AQUA).append("De ").append(ev.getPlayer().getName()).append(": ").append(ChatColor.AQUA).append(ev.getMessage());
                p.sendMessage(cache.toString());
                cache.delete(0, cache.length());
                cache.append(ChatColor.DARK_AQUA).append("Para ").append(p.getName()).append(": ").append(ChatColor.AQUA).append(ev.getMessage());
                ev.getPlayer().sendMessage(cache.toString());
                ChatManager.getMeta(p).lastPlayerMessage = ev.getPlayer().getName();
                ev.setCancelled(true);
                return;
            }
        }
        ev.getRecipients().clear();
        ev.setFormat("%2$s");

        String prefix = "";

        PermissionUser pu = PermissionsEx.getUser(ev.getPlayer());
        if (pu != null && pu.getPrefix() != null) {
            prefix = pu.getPrefix();
        }

        ChatColor textColor = ChatColor.YELLOW;
        ChatColor nickColor = ChatColor.WHITE;

        if (meta.nickColor != null) {
            nickColor = meta.nickColor;
        }

        if (meta.chatColor != null) {
            textColor = meta.chatColor;
        }

        String channel = "[l]";
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
        if (meta.inChannel == null) {
            if (server == ServerType.LOBBY) {
                for (Entity e : ev.getPlayer().getNearbyEntities(50, 50, 50)) {
                    if (e instanceof Player) {
                        ev.getRecipients().add((Player) e);

                    }
                }
                if (ev.getRecipients().isEmpty()) {
                    ev.getPlayer().sendMessage(ChatColor.AQUA + "* ninguem perto para te ouvir *");
                    ev.setCancelled(true);
                }else{
                   for(Player st : Bukkit.getOnlinePlayers()){
                       if(st.hasPermission("cardwars.staff")){
                           ev.getRecipients().add(st);
                       }
                   }
                }

                ev.getRecipients().add(ev.getPlayer());
            } else if (server == ServerType.GAME) {
                if (CardWarsPlugin.getArena() != null) {
                    Team t = CardWarsPlugin.getArena().getTeam(ev.getPlayer().getUniqueId());
                    if (t != null) {
                        channel = t.getCor() + "[Equipe]";
                        textColor = ChatColor.YELLOW;
                        for (UUID uuid : CardWarsPlugin.getArena().getPlayers(t)) {
                            Player puid = Bukkit.getPlayer(uuid);
                            if (puid != null) {
                                ev.getRecipients().add(puid);
                            }
                        }
                    } else {
                        ev.setCancelled(true);
                        return;
                    }
                } else {

                    ev.setCancelled(true);
                    return;
                }
            } else {
                ChatUtils.sendMessage(ev.getPlayer(), "§bComplete o tutorial para se juntar aos jogadores!");

                ev.setCancelled(true);
                return;
            }
        } else if (meta.inChannel.equalsIgnoreCase("staff")) {
            channel = ChatColor.BLUE + "[BigBoss]";
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("cardwars.chatbigboss")) {
                    ev.getRecipients().add(p);
                }
            }

        }

        cache.delete(0, cache.length());
        cache.append(ChatColor.YELLOW).append(channel).append(" ").append(prefix).append(ChatColor.WHITE).append(nickColor).append(ev.getPlayer().getName()).append(ChatColor.GRAY).append(": ").append(textColor).append(ev.getMessage());
        String msg = cache.toString();
        if (PermManager.CORESCHAT.playerHas(ev.getPlayer())) {
            msg = translateColorCodes(msg);
        }
        ev.setMessage(msg);
        // ja limpando tudo pra ter menos coisa pro GC coletar ja que chat vai toda hora
        if (channel.equalsIgnoreCase(ChatColor.BLUE + "[BigBoss]")) {
            SocketManager.sendMessage("chatbb", msg);
        }
        cache = null;
        channel = null;
        prefix = null;
        nickColor = null;
        textColor = null;
    }
}
