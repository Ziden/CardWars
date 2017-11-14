/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin.ServerType;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.functions.groups.ControleGrupos;
import truco.plugin.functions.groups.Grupo;
import truco.plugin.matchmaking.DBHandler;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.functions.LevelManager;
import truco.plugin.utils.SoundUtils;

/**
 *
 * @author Carlos
 */
public class CmdGrupo implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (cmd.getName().equalsIgnoreCase("grupo")) {
            if (server != ServerType.LOBBY) {
                return false;
            }
            if (sender instanceof Player) {
                final Player p = (Player) sender;
                if (!LevelManager.canUse(LevelManager.LevelBonus.GROUPLEVEL, p.getUniqueId())) {
                    p.sendMessage("§5§l[GRUPO]§r §cVocê ainda não pode usar o sistema de grupos!");
                    return false;
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("convidar")) {

                        final Player pl = Bukkit.getPlayer(args[1]);
                        if (pl == null) {
                            p.sendMessage("§5§l[GRUPO]§r §c§lEste jogador está offline! ");
                            return false;
                        }
                        if (pl == p) {
                            p.sendMessage("§5§l[GRUPO]§r §cQual o motivo de convidar você mesmo?! ");

                            return false;
                        }
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                if (MatchMaker.db.isPlayerInQueue(p.getUniqueId()) != DBHandler.PlayerStatus.NONE) {
                                    p.sendMessage("§5§l[GRUPO]§r §a§lVocê não pode mandar convites na fila!");
                                    return;
                                }
                                if( MatchMaker.db.isPlayerInQueue(pl.getUniqueId()) != DBHandler.PlayerStatus.NONE){
                                    p.sendMessage("§5§l[GRUPO]§r §a§lQuem você está convidando já está na fila!!");
                                    return;
                                    
                                }
                                ControleGrupos.MandaConviteGrupo(p, pl);

                            }
                        }).start();

                        return true;
                    } else if (args[0].equalsIgnoreCase("kick")) {

                        final Player pl = Bukkit.getPlayer(args[1]);
                        Grupo g = ControleGrupos.getGrupo(p);
                        if (g == null) {
                            p.sendMessage("§5§l[GRUPO]§r §cVocê não tem grupo! ");
                            return false;
                        }
                        if (g.getLider() != p.getUniqueId()) {
                            p.sendMessage("§5§l[GRUPO]§r §eSomente o lider do grupo pode kickar! ");

                            return false;

                        }

                        if (pl == null) {
                            p.sendMessage("§5§l[GRUPO]§r §cEste jogador está offline! ");
                            return false;
                        }
                        if (pl == p) {
                            p.sendMessage("§5§l[GRUPO]§r §eCaso queira sair do grupo use §c/grupo sair §e! ");

                            return false;
                        }
                        if (g.kickMember(pl)) {
                            p.sendMessage("§5§l[GRUPO] §eVocê expulsou " + pl.getName() + " do grupo!");

                        } else {
                            p.sendMessage("§5§l[GRUPO] §eO jogador " + pl.getName() + " não é membro do grupo!");

                        }

                        return true;
                    }
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("aceitar")) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                if (MatchMaker.db.isPlayerInQueue(p.getUniqueId()) != DBHandler.PlayerStatus.NONE) {
                                    p.sendMessage("§5§l[GRUPO]§r §a§lVocê não pode aceitar convites na fila!");
                                    return;
                                }
                                ControleGrupos.aceitaConvite(p);

                            }
                        }).start();
                        return true;
                    } else if (args[0].equalsIgnoreCase("meugrupo")) {
                        Grupo meu = ControleGrupos.getGrupo(p);
                        if (meu != null) {
                            sender.sendMessage("  ");
                            sender.sendMessage("§6§l§m==============§5§l GRUPO §6§l§m==============");
                            p.sendMessage("§6§l§m- §b§lLider §f- §e" + Bukkit.getOfflinePlayer(meu.getLider()).getName());
                            for (int x = 1; x < meu.getPlayers().size(); x++) {
                                p.sendMessage("§6§l§m- §a§lMembro " + x + " §f- §7" + Bukkit.getOfflinePlayer(meu.getPlayers().get(x)).getName());
                            }

                            sender.sendMessage("§6§l§m==============§5§l GRUPO §6§l§m==============");
                            sender.sendMessage("  ");
                            return true;

                        } else {

                            sender.sendMessage("§5§l[GRUPO]§r §4§lVocê não tem grupo!");
                            return false;
                        }
                    } else if (args[0].equalsIgnoreCase("sair")) {
                        Grupo meu = ControleGrupos.getGrupo(p);
                        if (meu != null) {
                            if (meu.getLider() == p.getUniqueId()) {
                                meu.disband();
                            } else {
                                meu.removeMember(p);
                                SoundUtils.playSound(SoundUtils.Som.SAIGRUPO, Integer.MAX_VALUE, p);
                                sender.sendMessage("§5§l[GRUPO]§r §a§lVocê saiu do grupo!");
                            }

                        } else {
                            sender.sendMessage("§5§l[GRUPO]§r §4§lVocê não tem grupo!");
                        }
                        return true;
                    }
                }

            }
            sender.sendMessage("  ");
            sender.sendMessage("§6§l§m==============§5§l GRUPO §6§l§m==============");
            sender.sendMessage("§6§l-§a§l /grupo convidar PLAYER");
            sender.sendMessage("§6§l-§9§l /grupo aceitar");
            sender.sendMessage("§6§l-§c§l /grupo meugrupo");
            sender.sendMessage("§6§l-§d§l /grupo kick PLAYER");
            sender.sendMessage("§6§l-§e§l /grupo sair");
            sender.sendMessage("§6§l§m==============§5§l GRUPO §6§l§m==============");
            sender.sendMessage("  ");
        }
        return false;
    }
}
