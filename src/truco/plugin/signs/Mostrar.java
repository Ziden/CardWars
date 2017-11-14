/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.signs;

import org.bukkit.block.Sign;
import org.bukkit.event.player.PlayerInteractEvent;
import truco.plugin.functions.SomeNegada;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.Cooldown;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class Mostrar {

    public static void blockclick(final PlayerInteractEvent ev, Sign s) {

        if (s.getLine(1).equalsIgnoreCase("VerJogadores")) {
            if (!Cooldown.isCooldown(ev.getPlayer(), "clicoverjogadores")) {

                Cooldown.addCoolDown(ev.getPlayer(), "clicoverjogadores", 6000);
                SomeNegada.usaCmd(ev.getPlayer());
            }else{
                ev.getPlayer().sendMessage("§cEspere um pouco para fazer isso novamente!");
            }
        }

    }
}
