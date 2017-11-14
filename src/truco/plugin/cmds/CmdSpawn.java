/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cmds;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import static truco.plugin.CardWarsPlugin.server;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CmdSpawn implements CommandExecutor {

    public static Location getSpawnLocation(Player p) {
        Location spawn = p.getWorld().getSpawnLocation();
        spawn.setX(spawn.getX() + 0.5);
        spawn.setZ(spawn.getZ() + 0.5);
        spawn.setYaw(90);
        return spawn;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;

            if (args.length == 1 && args[0].equalsIgnoreCase("set") && p.isOp()) {
                Location l = p.getLocation();
                p.getWorld().setSpawnLocation(l.getBlockX(), l.getBlockY(), l.getBlockZ());

                p.getWorld().getSpawnLocation().setPitch(l.getPitch());
                p.getWorld().getSpawnLocation().setYaw(l.getYaw());
                p.sendMessage("§aSpawn setado para ca!");
            } else {
                if (CardWarsPlugin.server == ServerType.LOBBY) {
                  
                    p.teleport(getSpawnLocation(p));

                } else if(server==ServerType.GAME){
                    if (!p.isOp()) {
                        p.sendMessage(ChatColor.RED + "Voce não pode dar /spawn em jogo! !");

                    } else {
                        p.teleport(p.getWorld().getSpawnLocation());
                    }
                }
            }

        }
        return false;
    }
}
