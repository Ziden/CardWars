/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.utils;

import org.bukkit.entity.Player;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class SoundUtils {

    public static enum Som {

        DOUBLEKILL("cardwars.game.doublekill"),
        TRIPLEKILL("cardwars.game.triplekill"),
        QUADRAKILL("cardwars.game.quadrakill"),
        PENTAKILL("cardwars.game.pentakill"),
        FIRSTBLOOD("cardwars.game.firstblood"),
        KILLINGSPREE("cardwars.game.killingspree"),
        DOMINATING("cardwars.game.dominating"),
        MEGAKILL("cardwars.game.megakill"),
        UNSTOPPABLE("cardwars.game.unstoppable"),
        WICKEDSICK("cardwars.game.whickedsick"),
        MONSTERKILL("cardwars.game.monsterkill"),
        GODLIKE("cardwars.game.godlike"),
        HOLYSHIT("cardwars.game.holyshit"),
        CONVITE("cardwars.lobby.convite"),
        SAIGRUPO("cardwars.lobby.saigrupo");

        private String caminho;

        public String getCaminho() {
            return caminho;
        }

        private Som(String caminho) {
            this.caminho = caminho;
        }
    }

    public static void playSound(Som s, int volume, Player p) {
        p.playSound(p.getLocation(), s.getCaminho(), volume, 1);
    }
}
