/*

 */
package truco.plugin.cmds;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.functions.Cooldown;
import truco.plugin.functions.LevelManager;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.signs.SignUtils;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CmdDarMoedas implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, final String[] strings) {

        if (cs instanceof Player) {
            if (strings.length != 2) {
                cs.sendMessage("§cUse §e/darmoedas {player} {valor} §c para dar moedas para os outros!!");
                return false;
            }
            final Player p = (Player) cs;
            if (!LevelManager.canUse(LevelManager.LevelBonus.GOLDLEVEL, p.getUniqueId())) {
                p.sendMessage("§cVocê ainda não pode dar moedas para jogadores!!");
                return false;
            }
            final Player palvo = Bukkit.getPlayer(strings[0]);
            if (palvo == null) {
                ChatUtils.sendMessage(cs, "§cJogador offline!");
                return false;
            }
            if (palvo == p) {
                ChatUtils.sendMessage(cs, "§cPara que você quer dar moedas para você mesmo?");
                return false;
            }
            if (!LevelManager.canUse(LevelManager.LevelBonus.GOLDLEVEL, palvo.getUniqueId())) {
                p.sendMessage("§cO alvo ainda não pode reber moedas!");
                return false;
            }
            if (!SignUtils.isInt(strings[1])) {
                ChatUtils.sendMessage(cs, "§cIsso ai é um numero no valor?");
                return false;
            }
            final UUID pmanda = p.getUniqueId();
            final UUID precebe = palvo.getUniqueId();
            final int vai = Integer.valueOf(strings[1]);

            if (vai < 1) {
                ChatUtils.sendMessage(cs, "§cValor negativo ou neutro não né amigo!");
                return false;
            }
            if (vai > 100) {
                ChatUtils.sendMessage(cs, "§cValor maximo para troca de moedas é 100!");
                return false;
            }
            if (!Cooldown.isCooldown(p, "dagold")) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        if (MatchMaker.db.hasGold(pmanda, vai)) {
                            MatchMaker.db.removeGold(pmanda, vai);
                            MatchMaker.db.addGold(precebe, vai);
                            Cooldown.addCoolDown(p, "dagold", 10000);

                            String s = Utils.getS(vai);
                            if (p != null) {
                                ChatUtils.sendMessage(p, "§6Você enviou " + vai + " moeda" + s + " para " + strings[0] + "!");
                            }

                            if (palvo != null) {
                                ChatUtils.sendMessage(palvo, "§6Você recebeu " + vai + " moeda" + s + " de " + Bukkit.getOfflinePlayer(pmanda).getName() + " !");
                            }
                            {
                            }
                        } else {
                            if (p != null) {
                                ChatUtils.sendMessage(p, "§cVocê não tem essas moedas!");
                            }
                        }

                    }
                }).start();
                return true;
            } else {
                ChatUtils.sendMessage(cs, "§eEspere um pouco para realizar este comando novamente!");
            }

            return true;
        }
        return false;
    }

}
