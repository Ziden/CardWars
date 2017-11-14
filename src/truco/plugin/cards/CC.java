package truco.plugin.cards;

import java.util.Random;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import truco.plugin.CardWarsPlugin;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CC {

    public static Random r = CardWarsPlugin.random;

    public static void tocaPracima(LivingEntity e, int quanto) {
        e.setVelocity(new Vector(0, quanto, 0));
    }

    public static void tacaPosion(LivingEntity e, int quanto, int tempo) {
        e.addPotionEffect(new PotionEffect(PotionEffectType.POISON, tempo, quanto));
    }

    public static void tacaSlow(LivingEntity e, int quanto, int tempo) {
        e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, tempo, quanto));
    }

    public static void tacaCegueira(LivingEntity e, int tempo) {
        e.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, tempo, 5));
    }

    public static void randomKnockBackWeak(LivingEntity e, int mt) {
        double x = 0.2 * mt;
        if (r.nextBoolean()) {
            x *= -1;
        }
        double y = 0.1;

        double z = 0.2 * mt;
        if (r.nextBoolean()) {
            z *= -1;
        }
        e.setVelocity(e.getVelocity().add(new Vector(x, y, z)));
    }

    public static void mineknock(LivingEntity p, LivingEntity t, double knocklvl) {

        t.setVelocity(t.getVelocity().add(t.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(knocklvl)).setY(0.3));
    }
      public static void mineknockFlecha(Entity p, LivingEntity t, double knocklvl) {

        t.setVelocity(t.getVelocity().add(t.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(knocklvl)).setY(0.4));
    }

    public static void randomKnockBackStrong(LivingEntity e) {
        e.setVelocity(new Vector(1 + r.nextDouble(), 1 + r.nextDouble(), 1 + r.nextDouble()));
    }
}
