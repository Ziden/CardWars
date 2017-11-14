/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.functions.lobby;

import java.util.ArrayList;
import java.util.List;
import me.libraryaddict.scoreboard.ScoreboardManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import truco.plugin.cards.ControleCartas;
import truco.plugin.CardWarsPlugin;
import truco.plugin.functions.ScoreCWs;
import truco.plugin.utils.IconLib;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class LobbyMessages {

    ChatColor cc = ChatColor.GOLD;
    //"    CARDWARS    "
    String[] ordem = new String[]{
        "§6" + IconLib.STAFFICON + "  " + "§f§lCARDWARS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§c§lC§f§lARDWARS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lC§c§lA§f§lRDWARS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lCA§c§lR§f§lDWARS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lCAR§c§lD§f§lWARS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lCARD§c§lW§f§lARS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lCARDW§c§lA§f§lRS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lCARDWA§c§lR§f§lS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lCARDWAR§c§lS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lCARDWARS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lCARDWARS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lCARDWAR§c§lS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lCARDWA§c§lR§f§lS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lCARDW§c§lA§f§lRS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lCARD§c§lW§f§lARS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lCAR§c§lD§f§lWARS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lCA§c§lR§f§lDWARS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§4§lC§c§lA§f§lRDWARS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§c§lC§f§lARDWARS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§f§lCARDWARS" + "  §6" + IconLib.STAFFICON,
        "§6" + IconLib.STAFFICON + "  " + "§e§lCARDWARS" + "  §6" + IconLib.STAFFICON};

    public LobbyMessages() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                changeScoreboardName();

            }
        }, 5, 5);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new ActionBarRunnable(), 10, 10);
    }
    int lastscoreboardindex = 0;

    public void changeScoreboardName() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            ScoreboardManager.setDisplayName(p, DisplaySlot.SIDEBAR, ordem[lastscoreboardindex]);
            ScoreCWs.updatePing(p);
        }
        if (lastscoreboardindex == (ordem.length - 1)) {
            lastscoreboardindex = 0;
        } else {
            lastscoreboardindex++;

        }
    }

    public void sendActionBarMessages() {

    }

    public static class ActionBarRunnable implements Runnable {

        public ActionBarRunnable() {

            msgs.add("§5Servidor unico no mundo!");
            msgs.add("§a§lCompre gemas no site : www.instamc.com.br");
            msgs.add("§d§lCaso tenha alguma duvida, você pode voltar para o tutorial usando §c/cws tps");
            msgs.add("§e§lCartas épicas são quase impossiveis de ter!");
            msgs.add("§6§lVocê pode comprar pacotes aleatórios na loja por 150 moedas");
            msgs.add("§c§lComandos do servidor = §e/cws");
            msgs.add("§aCaso queira trocar de lobby use /cws tps e clique em Mudar Lobby!");
            msgs.add("§eVocê pode jogar com seus amigos através do §c/grupo!");
        }

        int vezes = 0;

        List<String> msgs = new ArrayList();
        int lastindex = 0;

        @Override
        public void run() {
            if (vezes == 7) {
                vezes = 0;
                if (lastindex == (msgs.size() - 1)) {
                    lastindex = 0;
                } else {
                    lastindex++;
                }
            }
            if (msgs.isEmpty()) {
                return;
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                Utils.sendActionBar(p, "§4§l[!]§r " + msgs.get(lastindex));

            }

            vezes++;

        }

    }
}
