/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import truco.plugin.functions.Cooldown;
import truco.plugin.matchmaking.PlayerIngame;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CmdSairFila implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sairfila")) {
            if (sender instanceof Player) {
                
                final Player pl = (Player) sender;
                if (Cooldown.isCooldown(pl, "sairfilacd")) {
                    pl.sendMessage("§cEspere um pouco para executar a ação!");
                    return false;
                }
                Cooldown.addCoolDown(pl, "sairfilacd", 5000);
                MatchMaker.db.sai(pl, true);
            }
        }
        return false;
    }
}
