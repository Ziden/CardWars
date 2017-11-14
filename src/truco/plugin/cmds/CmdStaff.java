/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cmds;

import truco.plugin.functions.ScoreCWs;
import truco.plugin.functions.game.Mana;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.ControleCartas;
import truco.plugin.CardWarsPlugin.ServerType;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.itens.Items;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.signs.SignUtils;
import truco.plugin.data.mysql.MatchHistoryDB;
import truco.plugin.menus.CardStockMenu;
import truco.plugin.menus.Menu;
import truco.plugin.menus.OpenMenu;
import truco.plugin.socket.SocketManager;
import truco.plugin.utils.*;
import truco.plugin.utils.SoundUtils.Som;
import truco.plugin.utils.mobapi.mobs.MobsApi;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CmdStaff implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("staff")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("cardwars.staff")) {
                    p.sendMessage("§cVocê não tem permissão para isso!");
                    return false;
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("jogos")) {
                        if (CardWarsPlugin.server == ServerType.LOBBY && p.hasPermission("cardwars.staff")) {
                            Menu.menus.get("GamesMenu").openInventory(p);
                            return true;
                        }
                    }
                    if (args[0].equalsIgnoreCase("hub")) {
                        if (CardWarsPlugin.server != ServerType.LOBBY && p.hasPermission("cardwars.staff")) {
                            Utils.TeleportarTPBG("cws_Lobby-1", p);
                            return false;
                        }
                    }
                }
                sendHelp(sender, 1);
            }
        }
        return false;

    }

    public CmdStaff() {

        cmds.put("help {page}", "mostra os comandos");
        cmds.put("jogos", "ve os jogos");
        cmds.put("hub", "volta pro hub");

    }
    public static HashMap<String, String> cmds = new HashMap();

    public static void sendHelp(CommandSender cs, int page) {
        List<String> send = new ArrayList();
        int foi = 0;
        int comecoem;
        if (page == 1) {
            comecoem = 0;
        } else {
            comecoem = (page - 1) * 7;
        }
       
        if (cmds.size() < (comecoem + 1)) {
            cs.sendMessage("§aNenhuma ajuda para a pagina §c" + page + "§a !");
            return;
        }
        List<Entry<String, String>> list = new ArrayList(cmds.entrySet());
        for (int x = comecoem; x < list.size(); x++) {
            if (foi == 7) {
                break;
            }
            Entry entry = list.get(x);
            send.add("§b/staff " + entry.getKey() + " §9-§7 " + entry.getValue());
            foi++;
        }
        cs.sendMessage("  ");
        cs.sendMessage("§c*==-*==-*==-§4CardWars Staff Cmds §c*==-*==-*==-");
        cs.sendMessage("§cPAGINA:" + page);

        for (String mn : send) {
            cs.sendMessage(mn);
        }
        cs.sendMessage("§c*==-*==-*==-§4CardWars Staff Cmds §c*==-*==-*==-");
        cs.sendMessage("  ");
    }
}
