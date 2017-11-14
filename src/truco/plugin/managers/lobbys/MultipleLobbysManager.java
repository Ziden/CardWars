/*

 */
package truco.plugin.managers.lobbys;

import java.util.Collection;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import truco.plugin.socket.SocketManager;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MultipleLobbysManager {

    private static HashMap<String, LobbyObject> lobbys = new HashMap();

    private int lastplayers = -1;

    public Collection<LobbyObject> getLobbys() {
        return lobbys.values();
    }

    public boolean lobbyExiste(String lobbyname) {
        return lobbys.containsKey(lobbyname);
    }

    public void recebiInfo(String svname, int max, int tem) {
        if (lobbys.containsKey(svname)) {
            LobbyObject lo = lobbys.get(svname);
            lo.setMax(max);
            lo.setPlayers(tem);
        } else {
            LobbyObject lo = new LobbyObject(svname, max, tem);
            lobbys.put(svname, lo);

        }
        ChangeLobbyMenu.update();
    }

    public void tomadc(String sv) {
        if (lobbys.containsKey(sv)) {
            lobbys.remove(sv);
            ChangeLobbyMenu.update();
        }
    }

    public static LobbyObject getPerfectLobby(Player p) {
        int menor = 99999;
        LobbyObject lob = null;
        for (LobbyObject lobby : CardWarsPlugin.lobbymanager.getLobbys()) {
            if (p.hasPermission("cardwars.logafull")) {
                lob = lobby;
                break;
            }
            if (lobby.getPlayers() >= lobby.getMax()) {
                continue;
            }
            if (lobby.getPlayers() < menor) {
                menor = lobby.getPlayers();
                lob = lobby;
            }
        }
        return lob;
    }

    public static LobbyObject getPerfectLobby() {
        int menor = 99999;
        LobbyObject lob = null;
        for (LobbyObject lobby : CardWarsPlugin.lobbymanager.getLobbys()) {
            if (lobby.getPlayers() >= lobby.getMax()) {
                continue;
            }
            if (lobby.getPlayers() < menor) {
                menor = lobby.getPlayers();
                lob = lobby;
            }
        }
        return lob;
    }

    public MultipleLobbysManager() {
        if (CardWarsPlugin.server == CardWarsPlugin.ServerType.LOBBY) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    mandaInfo();

                }
            }, 100, 20 * 30);
        }
    }

    public void mandaInfo() {
        mandaInfo(Bukkit.getOnlinePlayers().size());
    }

    public void mandaInfo(int temon) {
        if (Bukkit.hasWhitelist()) {
            temon = Bukkit.getMaxPlayers();
        }
        SocketManager.sendMessage(SocketManager.ALL_VARIABLE, "lobbyinfo", CardWarsPlugin.serverName + "¬" + temon + "¬"
                + Bukkit.getMaxPlayers());
        recebiInfo(CardWarsPlugin.serverName, Bukkit.getMaxPlayers(), temon);

    }

}
