package truco.plugin.cmds;

import java.util.UUID;
//import org.apache.commons.mail.EmailException;
//import org.apache.commons.mail.SimpleEmail;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.menus.Menu;
import truco.plugin.menus.OpenMenu;
import truco.plugin.menus.TeleporterMenu;
import truco.plugin.signs.SignUtils;
import truco.plugin.data.mysql.MatchHistoryDB;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.LevelManager;
import truco.plugin.managers.lobbys.ChangeLobbyMenu;
import truco.plugin.managers.PermManager;

/**
 *
 * @author Carlos André Feldmann Júnior
 *
 */
public class CmdPrincipal implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, final String[] strings) {
        if (cmnd.getName().equalsIgnoreCase("cws")) {
            
            if (strings.length == 1) {
                if (strings[0].equalsIgnoreCase("tps") && cs instanceof Player) {
                    if (CardWarsPlugin.server == ServerType.LOBBY) {
                        Menu.menus.get(TeleporterMenu.menuname).openInventory((Player) cs);
                        return true;
                    }
                } else if (strings[0].equalsIgnoreCase("hubs") && cs instanceof Player) {
                    if (CardWarsPlugin.server == ServerType.LOBBY) {
                        Menu.menus.get(ChangeLobbyMenu.name).openInventory((Player) cs);
                        return true;
                    }
                } else if (strings[0].equalsIgnoreCase("jogos") && cs instanceof Player) {
                    if (CardWarsPlugin.server == ServerType.LOBBY && (((Player)cs).hasPermission("cardwars.staff"))) {
                        Menu.menus.get("GamesMenu").openInventory((Player) cs);
                        return true;
                    }
                }
                
            } else if (strings.length == 2) {
                
            } else if (strings.length == 3) {
                
            }
            
        }
        cs.sendMessage("   ");
        cs.sendMessage("§c-=-=-=-=-=-=-=-=§eCardWars§c-=-=-=-=-=-=-=-=-=-=-=-");
        if (cs instanceof Player) {
            int qtdForProxLevel = 100 * (MatchMaker.db.getLevel(((Player) cs).getUniqueId()) / 2);
            cs.sendMessage("§dPara você upar para o level §c" + (MatchMaker.db.getLevel(((Player) cs).getUniqueId()) + 1) + "§d você precisa de §c" + qtdForProxLevel + "§d xp!");
        }
        
        cs.sendMessage("§a/cws tps §7- Abre o menu de teleportes!");
        cs.sendMessage("§a/darmoedas {player} {quantidade} §7- da moedas para outro jogador!");
        cs.sendMessage("§a/vender {preco} §7- bota a venda sua carta!");
        cs.sendMessage("§a/grupo §7- ve os comandos de grupo!");
        cs.sendMessage("§a/vercartas §7- ve suas cartas!!");
        cs.sendMessage("§a/sairfila §7- sai da fila de jogo!!");
        if (PermManager.CARDMASTER.playerHas(cs)) {
            cs.sendMessage("§b/vercartas NOME §7- ve as cartas de alguem!!");
            cs.sendMessage("§b/mostrar §7- mostra sua carta da mao no chat!!");
        }
        if (cs.isOp()) {
            
            cs.sendMessage("§c/cadm - COMANDOS DE OP!");
        }
        cs.sendMessage("§c-=-=-=-=-=-=-=-=§eCardWars§c-=-=-=-=-=-=-=-=-=-=-=-");
        cs.sendMessage("   ");
        
        return false;
    }
}
