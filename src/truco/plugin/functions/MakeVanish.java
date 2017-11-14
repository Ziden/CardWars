package truco.plugin.functions;

import java.util.HashSet;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MakeVanish {

    private static MakeVanish vanish;

    public static MakeVanish getInstance() {
        if (vanish != null) {
            return vanish;
        }
        vanish = new MakeVanish();
        return vanish;
    }
    public static HashSet<String> vanished = new HashSet<String>();

    public static void makeVanished(Player p) {
        for (Player pEspec : Bukkit.getOnlinePlayers()) {
            p.showPlayer(pEspec);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equals(p.getName())) {
                continue;
            } else {
                player.hidePlayer(p);
            }
        }
        vanished.add(p.getName());
    }

    public static boolean isVanished(Player p) {
        return vanished.contains(p.getName());
    }

    public static void updateVanished() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (isVanished(p)) {
                makeVanished(p);
            } else {
                makeVisible(p);
            }
        }
    }

    public static void makeVisible(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        if (p != null) {
            makeVisible(p);
            return;
        }
        OfflinePlayer of = Bukkit.getOfflinePlayer(uuid);
        vanished.remove(of.getName());
    }

    public static void makeVisible(Player p) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showPlayer(p);
        }
        vanished.remove(p.getName());
    }
}
