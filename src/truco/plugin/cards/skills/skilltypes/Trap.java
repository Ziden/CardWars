/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cards.skills.skilltypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.arena.Arena.Team;
import truco.plugin.data.MetaShit;
import truco.plugin.utils.efeitos.ParticleEffect;

public abstract class Trap {

    public static HashSet<Trap> redTraps = new HashSet<Trap>();
    public static HashSet<Trap> blueTraps = new HashSet<Trap>();

    public Block b;
    public Team team;
    public UUID dono;
    public Skill skill;

    public abstract void activate(Player p);

    public static void tickEffect() {

        if (CardWarsPlugin.mainarena == null) {
            return;
        }

        List<Player> timeazul = new ArrayList();
        List<Player> timever = new ArrayList();
        for (Player p : Bukkit.getOnlinePlayers()) {
            Team t = CardWarsPlugin.mainarena.getTeam(p.getUniqueId());
            if (t == Team.RED) {
                timever.add(p);
            } else if (t == Team.BLUE) {
                timeazul.add(p);
            } else {
                timever.add(p);
                timeazul.add(p);
            }
        }

        for (Trap trap : redTraps) {
            Location toplay = trap.b.getRelative(BlockFace.UP).getLocation().add(0.5, 0.5, 0.5);
            ParticleEffect.SPELL_MOB.display(new ParticleEffect.OrdinaryColor(255, 0, 0), toplay, timever);
        }
        for (Trap trap : blueTraps) {
            Location toplay = trap.b.getRelative(BlockFace.UP).getLocation().add(0.5, 0.5, 0.5);
            ParticleEffect.SPELL_MOB.display(new ParticleEffect.OrdinaryColor(0, 0, 255), toplay, timeazul);
        }
    }

    public void trigger(Block b, Player p) {
        activate(p);
        List<Trap> traps = (List<Trap>) MetaShit.getMetaObject("trap", b);
        traps.remove(this);
        if (traps.isEmpty()) {
            b.removeMetadata("trap", CardWarsPlugin._instance);
        }
        if (team == Team.RED) {
            redTraps.remove(this);
        } else if (team == Team.BLUE) {
            blueTraps.remove(this);
        }
        p.sendMessage(ChatColor.RED + "Voce ativou uma armadilha !");
    }

    public static boolean canTrapBlock(Block b, Player p, Skill s) {
        if (!b.hasMetadata("trap")) {
            return true;
        }
        List<Trap> traps = (List<Trap>) MetaShit.getMetaObject("trap", b);
        for (Trap trp : traps) {
            if (trp.dono == p.getUniqueId()) {
                p.sendMessage("§aVocê já tem uma armadilha nesse bloco!");
                return false;
            }
            if (trp.skill == s && trp.team == CardWarsPlugin.getArena().getTeam(p.getUniqueId())) {
                p.sendMessage("§aAlguem ja colocou uma armadilha desse tipo nesse bloco!");
                return false;
            }
        }
        return false;
    }

    public Trap(Team trapper, Block target, UUID quemcoloco, Skill s) {
        b = target;
        skill = s;
        dono = quemcoloco;
        this.team = trapper;
        // show the trap to theyr team
        List<Trap> traps;
        if (target.hasMetadata("trap")) {
            traps = (List<Trap>) MetaShit.getMetaObject("trap", target);
        } else {
            traps = new ArrayList();
        }
        traps.add(this);
        MetaShit.setMetaObject("trap", target, traps);
        if (team == Team.RED) {
            redTraps.add(this);
        } else if (team == Team.BLUE) {
            blueTraps.add(this);
        }
        /*
         for (UUID u : Main.mainarena.getPlayers(trapper)) {
         Player p = Bukkit.getPlayer(u);
         if (p != null) {
         p.sendBlockChange(target.getRelative(BlockFace.UP).getLocation(), Material.RED_MUSHROOM, (byte) 0);
         }
         }
         */
    }

    public static void move(PlayerMoveEvent ev) {
        Block bd = ev.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
        Block bl = ev.getPlayer().getLocation().getBlock();
        if (ev.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        for (Block step : Arrays.asList(bl, bd)) {
            if (step.hasMetadata("trap")) {
                List<Trap> traps = (List<Trap>) MetaShit.getMetaObject("trap", step);
                for (Trap trap : new ArrayList<Trap>(traps)) {
                    if (CardWarsPlugin.mainarena.getTeam(ev.getPlayer().getUniqueId()) != trap.team) {
                        trap.trigger(step, ev.getPlayer());
                    }
                }
                break;

            }
        }

    }
}
