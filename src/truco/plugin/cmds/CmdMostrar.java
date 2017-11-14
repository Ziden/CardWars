/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.CardWarsPlugin;
import truco.plugin.signs.Mostrar;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.Cooldown;
import truco.plugin.managers.PermManager;

/**
 *
 * @author Júnior
 */
public class CmdMostrar implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {

        if (cs instanceof Player && cmnd.getName().equalsIgnoreCase("mostrar")) {
            Player p = (Player) cs;
            if (Cooldown.isCooldown(p, "cartachat")) {
                p.sendMessage("§cEspere um pouco para mostrar outra carta no chat!");
                return false;
            }
            if (!PermManager.MOSTRAR.playerHas(p)) {
                ChatUtils.sendMessage(cs, "§bSomente vips podem mostrar cartas no chat!");
                return false;
            }
            if (p.getItemInHand() == null) {
                p.sendMessage("§aColoque uma carta na mão!");
                return false;
            }
            Carta c = ControleCartas.getCarta(p.getItemInHand());
            if (c == null) {
                p.sendMessage("§aColoque uma carta na mão!");
                return false;
            }
            Cooldown.addCoolDown(p, "cartachat", 15000);
            ChatUtils.mostracartaChat(p, p.getItemInHand());

        }
        return true;

    }
}
