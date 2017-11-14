/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.socket;

import truco.plugin.events.SocketReceivedEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class SocketManager {

    public static String charset = "UTF-16";

    public static String svname;
    public static String separador = "✲";

    public static List<String> tosend = Collections.synchronizedList(new LinkedList());

    //
    public static SocketConnectionThread t;
    //


    public static void start() {

        try {
            svname = CardWarsPlugin.serverName;
            t = new SocketConnectionThread(svname);
            t.start();
         
           

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendMessagetoPlayer(String uid, String msg) {
        Player p = Bukkit.getPlayer(UUID.fromString(uid));
        if (p != null) {
            p.sendMessage(msg);
            return;
        }
        sendMessage("mensagemuuid", uid + "¨¨" + msg);
    }

    public static void sendMessage(final String praquem, final String channel, String msg) {
        msg = msg.replace(separador, "");
        t.sendMessage(praquem, msg, channel);

    }
    
    

    public static String LOBBY_VARIABLE = "Lobbys";
    public static String ALL_VARIABLE = "all";

    public static void sendMessage(final String channel, final String msg) {
        sendMessage("all", channel, msg);
    }

    public static void TentaReconnect() {

        t = new SocketConnectionThread(svname);
        t.start();
    }

    

    public static void bukkitBroadcastMessage(String to, String msg) {
        sendMessage(to, "broadcast", msg);
    }

    public static void recebeMsg(String msg) {

        String channel = msg.split(separador)[0];

        String info = msg.split(separador)[1];
        SocketReceivedEvent ev = new SocketReceivedEvent(info, channel);

        Bukkit.getPluginManager().callEvent(ev);
    }

}
