package truco.plugin.arena.DeathMatch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import truco.plugin.CardWarsPlugin;
import truco.plugin.arena.Arena.Team;
import truco.plugin.arena.GameState;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.Cooldown;
import truco.plugin.events.CustomDeathEvent;
import truco.plugin.functions.ScoreCWs;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class TeamDeathMatchListener implements Listener {

    TeamDeathMatch tm;

    public TeamDeathMatchListener(TeamDeathMatch tm) {
        this.tm = tm;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void join(final PlayerJoinEvent ev) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (ev.getPlayer() == null) {
                        break;
                    }
                    if (p != ev.getPlayer()) {
                        tm.createScore(p);
                    }
                }
            }
        }, 10);

    }

    public void removeVida(Player removido, Player kiler) {
        if (!tm.getSpecs().contains(removido.getUniqueId())) {

            if (tm.getState() == GameState.INGAME) {
                final Player p = removido;
                tm.addMorte(p);
                int tem = tm.getVidas(p);
                final Player killer = kiler;
                tm.addKill(killer);

                if (tem != 0) {
                    if (killer != null) {
                        ChatUtils.broadcastMessage("§eO jogador §c" + killer.getName() + "§e matou §c§l" + p.getName() + "§e que acabou ficando com §c" + (tem) + "§e vidas!");
                    } else {
                        ChatUtils.broadcastMessage("§eO jogador §c" + p.getName() + "§e morreu e acabou ficando com §c§l" + (tem) + "§e vidas!");
                    }
                } else {

                    for (Player pOn : Bukkit.getOnlinePlayers()) {
                        ScoreCWs.setScoreLine(pOn, 2, ChatColor.BLUE + "Time Azul: " + tm.vivosblue);
                        ScoreCWs.setScoreLine(pOn, 1, ChatColor.RED + "Time Vermelho: " + tm.vivosred);

                    }
                    if (killer != null) {
                        ChatUtils.broadcastMessage("§eO jogador §c" + killer.getName() + "§e eliminou §c§l" + p.getName() + "§e do jogo!");
                    } else {
                        ChatUtils.broadcastMessage("§eO jogador §c" + p.getName() + "§e foi eliminado do jogo!");
                    }
                }

            }
        }
    }

    @EventHandler
    public void death(CustomDeathEvent ev) {

        if (tm.getTeam(ev.getPlayer().getUniqueId()) != Team.SPEC) {

            if (ev.getMatador() != null) {
                removeVida(ev.getPlayer(), (Player) ev.getMatador());
            } else if (ev.getMatador() == null) {
                removeVida(ev.getPlayer(), null);
            } else {
                removeVida(ev.getPlayer(), null);
            }
            tm.tp(ev.getPlayer());
            Cooldown.addCoolDown(ev.getPlayer(), "nodamage", 5000);

        }
    }

    public void saiu(Player p) {
        if (tm.getTeam(p.getUniqueId()) == Team.SPEC) {
            return;
        }
        if (tm.getState() == GameState.INGAME) {
            removeVida(p, null);
        }

        boolean ganhei = true;
        for (Player vivo : tm.getLivesPlayers()) {
            if (vivo == p) {
                continue;
            }
            if (tm.getTeam(vivo.getUniqueId()) == Team.SPEC) {
                continue;
            }
            if (tm.getTeam(vivo.getUniqueId()) == Team.getTeamOposta(tm.getTeam(p.getUniqueId()))) {
                if (vivo.isOnline()) {
                    ganhei = false;
                }
            }

        }
        if (ganhei && Team.getTeamOposta(tm.getTeam(p.getUniqueId())) != null) {
            tm.win(Team.getTeamOposta(tm.getTeam(p.getUniqueId())));
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent ev) {
        saiu(ev.getPlayer());
    }

    @EventHandler
    public void kick(PlayerKickEvent ev) {
        saiu(ev.getPlayer());
    }
}
