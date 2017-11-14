/*

 */
package truco.plugin.functions.lobby.hologramas;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import truco.plugin.CardWarsPlugin;
import truco.plugin.arena.EloPlayer;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.IconLib;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ControleHologramas {

    public static Hologram rank = null;

    public ControleHologramas() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {

                final List<EloPlayer> list = MatchMaker.db.getRanking();
                if (rank != null) {
                    rank.delete();
                }
                rank = HologramsAPI.createHologram(CardWarsPlugin._instance, new Location(Bukkit.getWorld("world"), -83.7, 4, 83.5));

                rank.appendTextLine("§4" + IconLib.STAFFICON + " §6⇩ §cTop 5 de Elo §6⇩ §4" + IconLib.STAFFICON);

                for (int x = 0; x < 5; x++) {

                    if (list.size() > x) {

                        rank.appendTextLine("§a" + ChatUtils.getNumber(x + 1, true) + " §b⇝§c " + list.get(x).nome + " §b ⇝ §e" + list.get(x).elo);

                    } else {
                        rank.appendTextLine("§a" + ChatUtils.getNumber(x + 1, true) + " §b⇝ §c Ninguem §b⇝ §e 0");

                    }
                }

            }
        }, 5, 20 * 60);

    }

}
