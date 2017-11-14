/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cmds;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

/**
 *
 * @author Gabriel
 */
public class CmdMV implements CommandExecutor {

    public CmdMV() {
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (!cs.isOp()) {
            return true;
        }
        Player p = (Player) cs;
        if (args.length == 0) {
            cs.sendMessage("§cUtilize §f/mv <load> | tp | list");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("load")) {
                cs.sendMessage("§cUtilize §f/mv load <mundo>");
                return true;
            } else if (args[0].equalsIgnoreCase("tp")) {
                cs.sendMessage("§cUtilize §f/mv tp <mundo>");
                return true;
            } else if (args[0].equalsIgnoreCase("list")) {
                for (World w : Bukkit.getWorlds()) {
                    cs.sendMessage(w.getName());
                }
                return true;
            } else if (args[0].equalsIgnoreCase("limpar")) {
                for (Entity e : p.getWorld().getEntities()) {
                    if (e.getType() == EntityType.DROPPED_ITEM
                            || e instanceof Creature
                            || e instanceof Monster) {
                        e.remove();
                    }
                }
                cs.sendMessage("§cLimpo!");
                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("load")) {
                WorldCreator w = new WorldCreator(args[1]);
                w.createWorld();
                cs.sendMessage("Mundo criado: " + args[1]);
                return true;
            } else if (args[0].equalsIgnoreCase("tp")) {
                World w = Bukkit.getWorld(args[1]);
                if (w == null) {
                    cs.sendMessage("§cMundo nao existe!");
                    return true;
                }
                p.teleport(w.getSpawnLocation());
                p.sendMessage("§cTeleportado ao mundo " + args[1]);
                return true;
            }
        }
        return true;
    }
}
