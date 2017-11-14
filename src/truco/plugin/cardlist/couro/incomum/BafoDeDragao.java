/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.incomum;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.LocUtils;
import truco.plugin.utils.TeamUtils;
import truco.plugin.cards.StatusEffect;
import truco.plugin.utils.efeitos.ParticleManager;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class BafoDeDragao extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Bafo de Dragao";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Atordoa inimigos a sua frente", "que estejam pegando fogo"};
    }

    @Override
    public Skill getSkill() {
        return new Skill(this, 16, 13) {

            @Override
            public String getName() {
                return "Queima Roupa";
            }

            @Override
            public boolean onCast(Player p) {
                BreathRunning task = new BreathRunning(this, p);
                int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, task, 4, 4);
                task.taskId = id;
                return true;
            }
        };
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }

    private class BreathRunning implements Runnable {

        private Player p;
        private int radius = 0;
        public int taskId = 0;
        private Skill s;
        private List<UUID> machucados= new ArrayList();
        private List<Location> locations;

        public BreathRunning(Skill s, Player p) {
            this.p = p;
            this.s = s;
        }

        @Override
        public void run() {
            if (locations == null) {
                locations = LocUtils.getRay(p, 24, 5);
            }
            Location l = locations.get(radius);
            double radiusFinal = radius;
            l.setY(l.getWorld().getHighestBlockAt(l).getY());
            Location start = new Location(p.getWorld(), l.getX() - radiusFinal / 2, l.getY(), l.getZ() - radiusFinal / 2);
            Location end = new Location(p.getWorld(), l.getX() + radiusFinal / 2, l.getY(), l.getZ() + radiusFinal / 2);

            // PlayEffect.play(VisualEffect.FLAME,"loc:" + Skill.toEffectLocation(l) + " draw:circle radius:"+radiusFinal);
             for(Location lf : ParticleManager.buildLine(start, end)){
                lf.getWorld().playEffect(lf, Effect.MOBSPAWNER_FLAMES, 1);
            }
            Arrow ar = (Arrow) l.getWorld().spawnEntity(l, EntityType.ARROW);
            for (Entity t : ar.getNearbyEntities(2, 2, 2)) {
                if (t instanceof Player) {
                    Player palvo = (Player) t;
                    if (TeamUtils.canAttack(p, palvo) && !machucados.contains(palvo.getUniqueId())) {
                        if (palvo.getFireTicks() > 0) {
                            StatusEffect.addStatusEffect(palvo, StatusEffect.StatusMod.STUN, 6);
                            machucados.add(palvo.getUniqueId());
                        }
                    }
                }
            }
            ar.remove();
            radius++;
            if (radius > 4) {
                Bukkit.getScheduler().cancelTask(taskId);
                return;
            }
        }
    }

}
