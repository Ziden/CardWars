/*

 */
package truco.plugin.listeners;

import br.pj.newlibrarysystem.eventos.EventoListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import truco.plugin.functions.ScoreCWs;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class LibraryListener implements EventoListener {

    @Override
    public void GemAlteradaEvent(br.pj.newlibrarysystem.eventos.GemAlteradaEvent ev) {
        Player p = Bukkit.getPlayer(ev.getUUID());
        if (p != null) {
            ScoreCWs.updateGems(ev.getNewGemCount(), p);
        }
    }

    @Override
    public void RecebeuSocketEvent(br.pj.newlibrarysystem.eventos.RecebeuSocketEvent rse) {
    }

}
