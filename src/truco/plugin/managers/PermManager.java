/*

 */
package truco.plugin.managers;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public enum PermManager {

    CARDMASTER("cardwars.vip"),
    CARDHERO("cardwars.hero"),
    BEAPRO("cardwars.beapro"),
    EXP2X("cardwars.xp2"),
    EXP3X("cardwars.xp3"),
    GOLD2X("cardwars.gold2"),
    GOLD3X("cardwars.gold3"),
    LOGAFULL("cardwars.logafull"),
    GRUPO("cardwars.grupo"),
    VISIVEL("cardwars.visivel"),
    MOSTRAR("cardwars.mostrar"),
    NAOPAGASHOP("cardwars.shopnaopaga"),
    VERCARTAS("cardwars.vercartas"),
    GLOBAL("cardwars.chatglobal"),
    CORESCHAT("cardwars.coreschat");
    private String permission;

    private PermManager(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public boolean playerHas(CommandSender p) {
        return p.hasPermission(getPermission());
    }

}
