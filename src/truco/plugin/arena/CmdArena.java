package truco.plugin.arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import truco.plugin.arena.Arena.Team;
import truco.plugin.arena.DeathMatch.TeamDeathMatch;
import truco.plugin.arena.Dominion.Dominion;
import truco.plugin.arena.KomQuista.KomQuista;
import truco.plugin.matchmaking.Threads.MatchMaker;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CmdArena implements CommandExecutor {

    public static ArrayList<Player> etapas = new ArrayList();

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (cmnd.getName().equalsIgnoreCase("arena")) {
            if (cs instanceof Player) {
                Player p = (Player) cs;
                if (!p.isOp()) {
                    return false;
                }
                if (strings.length == 1) {
                    if (strings[0].equalsIgnoreCase("listar")) {
                        int x = 0;
                        for (Arena r : Arena.todasarenas) {
                            p.sendMessage("§c§lArena : " + r.getName());
                            x++;
                        }
                        if (x == 0) {
                            p.sendMessage("§cNenhuma arena!");
                        }
                        return true;
                    } else if (strings[0].equalsIgnoreCase("meuinfo")) {

                        for (Arena r : Arena.todasarenas) {
                            if (r instanceof TeamDeathMatch) {
                                TeamDeathMatch tdm = (TeamDeathMatch) r;
                                p.sendMessage("EQUIPE : " + tdm.getTeam(p.getUniqueId()).getName());
                                p.sendMessage("VIDAS : " + tdm.getVidas(p));
                                p.sendMessage("SPECTATOR : " + tdm.specs.contains(p));
                            }
                        }

                    } else if (strings[0].equalsIgnoreCase("gambetaspawn")) {

                        p.getWorld().setSpawnLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
                        p.sendMessage("spawn gambetado pra ca !");
                        return true;
                    } else if (strings[0].equalsIgnoreCase("ranking")) {
                        MatchMaker.db.showRanking(p);
                        return true;
                    } else if (strings[0].equalsIgnoreCase("gold")) {
                        int gold = MatchMaker.db.getGold(p.getUniqueId());
                        p.sendMessage("Seu gold: " + gold);
                    } else if (strings[0].equalsIgnoreCase("gerarRanking") && p.isOp()) {
                        /*
                         Thread t = new Thread() {
                         public void run() {
                         Main.log.info("Gerando rankings de ELO");
                         int maxTop = 20;

                         List<EloPlayer> players = MatchMaker.db.getElos();
                         Main.log.info("Obtive ELOS, gerando ranking !");

                         ArrayList<EloPlayer> elos = new ArrayList<EloPlayer>();
                         for (EloPlayer u : players) {
                         int elo = players.get(u);
                         if (elos.size() < 10) {
                                   
                                        
                         elos.add(u);
                         } else {
                         for (int x = 0; x < 10; x++) {
                         EloPlayer ep = elos.get(x);
                         if (ep.elo < elo) {
                         ep.u = u;
                         ep.elo = elo;
                         break;
                         }
                         }
                         }
                         }
                         Collections.sort(elos, new Comparator<EloPlayer>() {
                         public int compare(EloPlayer one, EloPlayer other) {
                         return other.elo - one.elo;
                         }
                         });
                         Main.log.info("Gerei elos !");
                         MatchMaker.db.updateEloRanking(elos);
                         Main.log.info("Elos gravados no banco !");
                         }
                         };
                         t.start();
                         */
                    }
                } else if (strings.length == 2) {

                    if (strings[0].equalsIgnoreCase("criartdm") && p.isOp()) {
                        if (Arena.getArenaByName(strings[1]) != null) {
                            p.sendMessage("Já existe uma arena com esse nome!");
                            return false;
                        }
                        if (Arena.todasarenas.size() > 0) {
                            return false;
                        }
                        TeamDeathMatch tdm = new TeamDeathMatch(strings[1]);
                        ArenaSave.saveArena(tdm);
                        p.sendMessage("Você criou uma arena com nome de " + strings[1] + " agora sete os spawns /arena spawn " + strings[1] + " red||blue");
                        return true;
                    } else if (strings[0].equalsIgnoreCase("criardominio") && p.isOp()) {
                        if (Arena.getArenaByName(strings[1]) != null) {
                            p.sendMessage("Já existe uma arena com esse nome!");
                            return false;
                        }
                        if (Arena.todasarenas.size() > 0) {
                            return false;
                        }
                        Dominion tdm = new Dominion(strings[1]);
                        ArenaSave.saveArena(tdm);
                        p.sendMessage("Você criou uma arena com nome de " + strings[1] + " agora sete os spawns /arena spawn " + strings[1] + " red||blue");
                        return true;
                    } else if (strings[0].equalsIgnoreCase("efeito") && p.isOp()) {
                        String efeito = strings[1];
                        String aceitos = "";
                        boolean foi = false;
                        for (Effect e : Effect.values()) {
                            aceitos += " " + e.getName();
                            if (e.getName().equalsIgnoreCase(efeito)) {
                                Block b = p.getTargetBlock((Set<Material>)null, 5);
                                b.getWorld().playEffect(b.getLocation(), e, 1);
                                foi = true;
                            }
                        }
                        if (!foi) {
                            p.sendMessage(ChatColor.GREEN + "efeitos: " + ChatColor.WHITE + aceitos);
                        }
                    } else if (strings[0].equalsIgnoreCase("criararena") && p.isOp()) {
                        if (Arena.getArenaByName(strings[1]) != null) {
                            p.sendMessage("Já existe uma arena com esse nome!");
                            return false;
                        }
                        if (Arena.todasarenas.size() > 0) {
                            p.sendMessage("Já tem uma arena nesse servidor!");
                            return false;
                        }

                        Arena a = null;

                        for (ArenaType t : ArenaType.values()) {
                            if (strings[1].equalsIgnoreCase(t.DEATHMATCH.name)) {
                                a = new TeamDeathMatch(strings[1]);
                            } else if (strings[1].equalsIgnoreCase(t.DOMINIO.name)) {
                                a = new Dominion(strings[1]);
                            } else if (strings[1].equalsIgnoreCase(t.KOMQUISTA.name)) {
                                a = new KomQuista(strings[1]);
                            }
                        }

                        ArenaSave.saveArena(a);
                        p.sendMessage("Você criou uma arena de " + strings[1] + " agora sete os spawns /arena spawn " + strings[1] + " red||blue");
                        return true;
                    } else if (strings[0].equalsIgnoreCase("excluir")) {
                        Arena r = Arena.getArenaByName(strings[1]);
                        if (r == null) {
                            p.sendMessage("Essa arena não existe!");
                            return false;
                        }
                        ArenaSave.removeArena(r);
                        p.sendMessage("Arena deletada com sucesso!");
                        return true;
                    } else if (strings[0].equalsIgnoreCase("fstart")) {
                        Arena r = Arena.getArenaByName(strings[1]);
                        if (r == null) {
                            p.sendMessage("Essa arena não existe!");
                            return false;
                        }

                        r.startGame();
                        return true;
                    } else if (strings[0].equalsIgnoreCase("info")) {
                        Arena r = Arena.getArenaByName(strings[1]);
                        if (r == null) {
                            p.sendMessage("Essa arena não existe!");
                            return false;
                        }

                        p.sendMessage("DBNAME: " + r.getDbName());
                        p.sendMessage("NAME: " + r.getName());
                        p.sendMessage("RED:" + r.getRedspawn().toString());
                        p.sendMessage("BLUE:" + r.getRedspawn().toString());
                        String plst = "";
                        for (UUID pr : r.getPlayers()) {
                            Player prt = Bukkit.getPlayer(pr);
                            if (pr != null) {
                                plst += prt + ":" + r.getTeam(pr) + ",";
                            }
                        }
                        p.sendMessage("PLAYERS:" + plst);
                        p.sendMessage("GAME STATE:" + r.getState().name());
                        p.sendMessage("TIME:" + r.getTime());

                        return true;
                    } else if (strings[0].equalsIgnoreCase("tp")) {
                        Arena r = Arena.getArenaByName(strings[1]);
                        if (r == null) {
                            p.sendMessage("Essa arena não existe!");
                            return false;
                        }
                        if (CardWarsPlugin.random.nextBoolean()) {
                            p.teleport(r.getBluespawn());
                        } else {
                            p.teleport(r.getRedspawn());
                        }
                        p.sendMessage("Teleportado!");
                        return true;
                    }

                } else if (strings.length == 3) {
                    if (strings[0].equalsIgnoreCase("spawn")) {
                        Arena r = Arena.getArenaByName(strings[1]);
                        if (r == null) {
                            p.sendMessage("§cNome invalido");
                            return false;
                        }
                        if (strings[2].equalsIgnoreCase("red")) {
                            r.setRedspawn(p.getLocation());

                        } else if (strings[2].equalsIgnoreCase("blue")) {
                            r.setBluespawn(p.getLocation());

                        }
                        p.sendMessage("Spawn " + strings[2] + " setado com sucesso!");
                        ArenaSave.saveArena(r);
                        return true;
                    } else if (strings[0].equalsIgnoreCase("setteam")) {
                        Arena r = Arena.getArenaByName(strings[1]);
                        Team t = null;
                        if (strings[2].equalsIgnoreCase("red")) {
                            t = Team.RED;

                        } else if (strings[2].equalsIgnoreCase("blue")) {
                            t = Team.BLUE;

                        }
                        if (t != null) {
                            r.setPlayerTeam(p, t);
                        }
                        return true;
                    }

                }

                sendHelp(p);
            }
        }
        return false;
    }

    public static void sendHelp(Player p) {
        p.sendMessage("§c/arena criartdm NOME");
        p.sendMessage("§c/arena excluir NOME");
        p.sendMessage("§c/arena spawn NOME red||blue");
        p.sendMessage("§c/arena listar");
        p.sendMessage("§c/arena info NOME");

    }

}
