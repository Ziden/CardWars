package truco.plugin.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.arena.Arena.Team;
import truco.plugin.functions.Cooldown;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class TeamUtils {

    public static boolean canAttack(Player e1, Player e2) {
        if (server == ServerType.TUTORIAL) {
            return false;
        }
        if (Cooldown.isCooldown(e2, "nodamage")) {
            return false;
        }
        if (CardWarsPlugin.mainarena == null) {
            return true;
        }
        if (e2.getGameMode() == GameMode.CREATIVE) {
            return false;
        }
        if (e1 == e2) {
            return false;
        }
        if (e2.getVehicle() != null && e2.getVehicle() instanceof Pig) {
            return false;
        }
        if (CardWarsPlugin.getArena().getSpecs().contains(e1.getUniqueId())) {
            return false;
        }
        if (CardWarsPlugin.getArena().getSpecs().contains(e2.getUniqueId())) {
            return false;
        }
        if (CardWarsPlugin.getArena().getTeam(e2.getUniqueId()).equals(Team.SPEC)) {
            return true;
        }

        if (CardWarsPlugin.getArena().getTeam(e1.getUniqueId()).equals(Team.SPEC)) {
            return true;
        }
        return CardWarsPlugin.mainarena.getTeam(e1.getUniqueId()) != CardWarsPlugin.mainarena.getTeam(e2.getUniqueId());
    }

    public static boolean isSpec(Player alvo) {
        if (CardWarsPlugin.getArena() == null) {
            return false;
        }
        return CardWarsPlugin.getArena().getSpecs().contains(alvo.getUniqueId());
    }
}
