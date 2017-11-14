/*

 */
package truco.plugin.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.functions.Cooldown;
import truco.plugin.functions.LevelManager;
import truco.plugin.managers.PermManager;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.signs.SignUtils;
import truco.plugin.utils.ChatUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CmdVender implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {

        if (cs instanceof Player) {
            if (strings.length != 1) {
                ChatUtils.sendMessage(cs, "§cUse §e/vender {preco} §c para vender uma carta!");
                return false;
            }
            final Player p = (Player) cs;
            if (Cooldown.isCooldown(p, "vendercarta")) {
                ChatUtils.sendMessage(p, "§aEspere um pouco para vender outra carta!");
                return false;
            }
            if (!LevelManager.canUse(LevelManager.LevelBonus.SHOPLEVEL, p.getUniqueId())) {
                p.sendMessage("§cVocê ainda não pode vender cartas!");
                return false;
            }
            Carta c = ControleCartas.getCarta(p.getItemInHand());
            if (c != null) {
                if (SignUtils.isInt(strings[0])) {
                    if (Integer.valueOf(strings[0]) <= 0) {
                        ChatUtils.sendMessage(cs, "§cForneca um valor positivo!");
                        return false;
                    }
                    Cooldown.addCoolDown(p, "vendercarta", 7000);
                    int tem = CardsDB.getCardsVendor(p.getUniqueId());
                    boolean vip = PermManager.NAOPAGASHOP.playerHas(p);
                    int limite = getLimit(p);
                    if (limite > tem) {
                        if (vip || MatchMaker.db.hasGold(p.getUniqueId(), 2)) {
                            if (!vip) {
                                MatchMaker.db.removeGold(p.getUniqueId(), 2);
                            }
                            p.setItemInHand(null);
                            CardsDB.addCartToShop(p.getUniqueId(), p.getName(), c, Integer.valueOf(strings[0]));
                            ChatUtils.sendMessage(p, "§c§lVocê botou para vender a carta " + c.getNome() + " por " + strings[0] + " !");

                        } else {
                            ChatUtils.sendMessage(p, "§6§lVocê precisa de 2 moedas para vender uma carta!");

                        }
                    } else {

                        if (limite > 1) {
                            ChatUtils.sendMessage(p, "§c§lVocê atingiu o limite de " + limite + " cartas para vender !");
                        } else {
                            ChatUtils.sendMessage(p, "§c§lVocê atingiu o limite de 1 carta para vender !");
                        }
                    }

                } else {
                    ChatUtils.sendMessage(p, "§c§lUse um numero no valor que você quer vender! !");
                }
            } else {
                ChatUtils.sendMessage(p, "§9§lColoque uma carta em sua mão para vender!");
            }
            return true;

        }
        return false;
    }

    public static int getLimit(Player p) {

        for (int x = 54; x > 0; x--) {
            if (p.hasPermission("cardwars.vender." + x)) {
                return x;
            }
        }
        return 1;
    }
}
