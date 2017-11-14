/*

 */
package truco.plugin.damage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftLivingEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import truco.plugin.CardWarsPlugin;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.cardlist.couro.raro.CoracaoFlamejante;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.cards.EfeitoProjetil;
import truco.plugin.cards.StatusEffect;
import truco.plugin.data.MetaShit;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;
import truco.plugin.events.CustomDeathEvent;
import truco.plugin.functions.Cooldown;
import truco.plugin.functions.ScoreCWs;
import truco.plugin.functions.game.Mana;
import truco.plugin.utils.efeitos.ParticleEffect;
import truco.plugin.utils.kibes.EntityUtils;
import truco.plugin.utils.kibes.UtilAction;
import truco.plugin.utils.kibes.UtilAlg;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class DamageManager implements Listener {

    public static DamageIndicatorManager dim = null;

    public DamageManager() {
        dim = new DamageIndicatorManager(CardWarsPlugin._instance);
        Bukkit.getPluginManager().registerEvents(this, CardWarsPlugin._instance);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void nativedamage(EntityDamageEvent ev) {
        if (server != CardWarsPlugin.ServerType.GAME) {
            return;
        }
        if (ev.isCancelled()) {
            return;
        }
        if (ev.getDamage() < 1) {
            return;
        }
        ev.setCancelled(true);
        LivingEntity damager = GetDamagerEntity(ev, true);
        LivingEntity damagee = GetDamageeEntity(ev);
        Projectile proj = GetProjectile(ev);

        if (damagee == null) {
            ev.setCancelled(true);
            return;
        }
        damagee.setNoDamageTicks(0);
        damagee.setLastDamage(0D);
        boolean kn = true;
        CausaDano cd = CausaDano.ATAQUE;
        String causa = "Ataque Corpo a Corpo";
        if (ev.getCause() == DamageCause.FALL) {
            cd = CausaDano.FALL;
            kn = false;
            causa = "Queda";
        } else if (ev.getCause() == DamageCause.POISON) {
            if (Cooldown.isCooldown(damagee, "tomadanoveneno")) {
                ev.setCancelled(true);
                return;
            } else {
                Cooldown.addCoolDown(damagee, "tomadanoveneno", 800);
                kn = false;
                causa = "Veneno";
                cd = CausaDano.VENENO;
            }
        } else if (ev.getCause() == DamageCause.PROJECTILE && proj != null) {

            if (proj.hasMetadata("magia")) {
                if (damagee instanceof Player) {
                    if (proj.hasMetadata("efeito")) {
                        List<EfeitoProjetil> efeitos = (List<EfeitoProjetil>) MetaShit.getMetaObject("efeito", proj);
                        for (EfeitoProjetil ep : efeitos) {
                            ep.causaEfeito((Player) damagee, ep.getShooter(), proj);
                        }
                    }

                }
                return;

            } else {

                causa = "Projetil";

                ev.setDamage(4);
                cd = CausaDano.FLECHA;
            }

        } else if (ev.getCause() == DamageCause.ENTITY_ATTACK) {
            if (Cooldown.isCooldown(damager, "dardano")) {
                return;
            } else {
                //MEIO SEGUNDO PARA BATER NOVAMENTE
                ev.setDamage(ev.getDamage() * 0.6);
                Cooldown.addCoolDown(damager, "dardano", 500);
            }
        } else if (ev.getCause() == DamageCause.LAVA || ev.getCause() == DamageCause.FIRE) {
            if (Cooldown.isCooldown(damagee, "lavadano")) {

            } else {
                causaDanoBruto(null, damagee, 4, "Lava");
                Cooldown.addCoolDown(damagee, "lavadano", 500);

            }
            return;

        } else if (ev.getCause() == DamageCause.SUFFOCATION) {
            return;
        }
        damage(ev.getDamage(), damager, damagee, proj, cd, kn, causa);
    }

    public static double trim(int degree, double d) {
        String format = "0.0";

        for (int i = 1; i < degree; i++) {
            format = format + "0";
        }
        DecimalFormat twoDForm = new DecimalFormat(format);

        return Double.valueOf(twoDForm.format(d).replace(",", "."));
    }

    public void morre(CustomDamageEvent ev) {

    }

    public static void causaDanoBruto(Player quem, LivingEntity emquem, double qt, String nome) {
        if (server != CardWarsPlugin.ServerType.GAME) {
            return;
        }
        double vida = emquem.getHealth() - qt;
        Dano dano;
        if (quem != null) {
            dano = new Dano(qt, quem.getName(), CausaDano.REAL, System.currentTimeMillis(), nome);
        } else {
            dano = new Dano(qt, "Sem Causador", CausaDano.REAL, System.currentTimeMillis(), nome);

        }
        if (emquem instanceof Player) {
            DamageInfoPlayer.getInfo((Player) emquem).addDano(dano);
        }

        EntityUtils.PlayDamageSound(emquem);
        emquem.getWorld().playEffect(emquem.getEyeLocation(), Effect.STEP_SOUND, 55);
        //     ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.STAINED_CLAY, (byte) 14), new Vector(0.2, 0.2, 0.2), 1, emquem.getLocation().add(0, 1.3, 0), 32);
        dim.showDamageIndicator(emquem, CausaDano.REAL.getCor() + "" + trim(1, qt));
        if (vida > 0) {
            emquem.setHealth(vida);
            emquem.playEffect(EntityEffect.HURT);

        } else {
            if (emquem instanceof Player) {

                Bukkit.getPluginManager().callEvent(new CustomDeathEvent((Player) emquem));
                DamageInfoPlayer.getInfo((Player) emquem).reset();
                reset((Player) emquem);
            } else {
                ((CraftLivingEntity) emquem).getHandle().die();
            }

        }

    }

    public void applyKnockBack(CustomDamageEvent ev) {
        double knockback = ev.getFinalDamage();
        if (knockback > 1.0) {
            knockback = 1.0;
        }
        for (double mod : ev.getKnockbackMod().values()) {
            knockback *= mod;
        }
        Vector trajectory = UtilAlg.getTrajectory2d(ev.GetDamagerEntity(true), ev.getTomou());
        trajectory.multiply(0.6D * knockback);
        trajectory.setY(Math.abs(trajectory.getY()));

        UtilAction.velocity(ev.getTomou(), trajectory, 0.2D + trajectory.length() * 0.8D, false, 0.0D, Math.abs(0.2D * knockback), 0.4D + 0.04D * knockback, true);

    }

    public static void applyKnockBack(LivingEntity emquem, LivingEntity quem, int mp) {
        double knockback = mp;

        Vector trajectory = UtilAlg.getTrajectory2d(quem, emquem);
        trajectory.multiply(0.6D * knockback);
        trajectory.setY(Math.abs(trajectory.getY()));

        UtilAction.velocity(emquem, trajectory, 0.2D + trajectory.length() * 0.8D, false, 0.0D, Math.abs(0.2D * knockback), 0.4D + 0.04D * knockback, true);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void damageFinal(CustomDamageEvent ev) {
        if (ev.isCancelled()) {
            return;
        }

        double vida = ev.getTomou().getHealth() - ev.getFinalDamage();
        if (ev.getPlayerTomou() != null) {
            String nome = ev.getPlayerBateu() != null ? ev.getPlayerBateu().getName() : "Sem causador";

            Dano dano = new Dano(ev.getFinalDamage(), nome, ev.getCause(), System.currentTimeMillis(), ev.getNome());
            DamageInfoPlayer.getInfo(ev.getPlayerTomou()).addDano(dano);
        }
        if (ev.getPlayerBateu() != null && ev.getProjetil() != null) {
            ev.getPlayerBateu().playSound(ev.getPlayerBateu().getLocation(), Sound.SUCCESSFUL_HIT, (float) 0.5, (float) 0.5);
        }
        if (ev.getProjetil() != null) {
            ev.getProjetil().remove();
        }
        dim.showDamageIndicator(ev.getTomou(), "" + ev.getCause().getCor() + "§l" + trim(1, ev.getFinalDamage()));
        EntityUtils.PlayDamageSound(ev.getTomou());
        ev.getTomou().getWorld().playEffect(ev.getTomou().getEyeLocation(), Effect.STEP_SOUND, 55);
        if (vida > 0) {
            ev.getTomou().setHealth(vida);
            ev.getTomou().playEffect(EntityEffect.HURT);
            if (ev.isKnockback() && ev.GetDamagerEntity(true) != null) {
                applyKnockBack(ev);
            }

        } else {
            if (ev.getPlayerTomou() != null) {

                ParticleEffect.EXPLOSION_NORMAL.display((float) 0.9, (float) 0.1, (float) 0.9, 0, 30, ev.getPlayerTomou().getLocation().add(0, 1, 0), 32);
                Bukkit.getPluginManager().callEvent(new CustomDeathEvent(ev.getPlayerTomou()));
                reset(ev.getPlayerTomou());
                DamageInfoPlayer.getInfo((Player) ev.getPlayerTomou()).reset();
            } else {
                ((CraftLivingEntity) ev.getTomou()).getHandle().die();
            }
        }

    }

    public static void reset(final Player p) {
        p.setHealth(p.getMaxHealth());
        Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {

                if (p != null) {
                    p.setFireTicks(0);
                }

            }
        }, 10);
        p.getInventory().clear();
        for (PotionEffect eff : p.getActivePotionEffects()) {
            p.removePotionEffect(eff.getType());
        }
        DamageManager.removeDebuffs(p);
        Mana.setMana(p, Mana.getMana(p).maxMana);
    }

    public static void addFireTicks(LivingEntity t, int fireticks) {
        if (t.getFireTicks() < 0) {
            t.setFireTicks(fireticks);
        } else {
            t.setFireTicks(t.getFireTicks() + fireticks);

        }
    }

    public static void addMagicFireTicks(LivingEntity t, int fireticks, Player p) {
        if (ControleCartas.hasStat(p, CoracaoFlamejante.st.getNome())) {
            fireticks *= 3;
        }
        addFireTicks(t, fireticks);
    }

    public static void removeDebuffsPotions(LivingEntity t) {
        PotionEffectType[] efeitosruins = new PotionEffectType[]{PotionEffectType.HUNGER, PotionEffectType.POISON, PotionEffectType.BLINDNESS, PotionEffectType.SLOW};
        for (PotionEffectType debuff : efeitosruins) {
            if (t.hasPotionEffect(debuff)) {
                t.removePotionEffect(debuff);
            }
        }
    }

    public static void removeDebuffs(Player p) {

        StatusEffect.StatusMod[] ruins = new StatusEffect.StatusMod[]{StatusEffect.StatusMod.STUN, StatusEffect.StatusMod.SNARE, StatusEffect.StatusMod.SILENCE, StatusEffect.StatusMod.CONGELADO};
        for (StatusEffect.StatusMod st : ruins) {
            if (StatusEffect.hasStatusEffect(p, st)) {
                StatusEffect.removeStatusEffect(p, st);
            }
        }

    }

    public static void cura(LivingEntity curado, double quanto) {
        if (curado.hasPotionEffect(PotionEffectType.POISON)) {
            return;
        }
        dim.showDamageIndicator(curado, "§a§l" + quanto);
        if (curado.getHealth() + quanto > curado.getMaxHealth()) {

            curado.setHealth(curado.getMaxHealth());
        } else {
            curado.setHealth(curado.getHealth() + quanto);
        }

    }

    public static void damage(double dano, LivingEntity bateu, LivingEntity tomo, Projectile proj, CausaDano causa, boolean kn, String nome) {
        if (server != CardWarsPlugin.ServerType.GAME) {
            return;
        }
        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(dano, tomo, bateu, kn, causa, proj, nome));
    }

    public static void damage(double dano, LivingEntity bateu, LivingEntity tomo, Projectile proj, CausaDano causa, boolean kn, Carta carta) {
        if (server != CardWarsPlugin.ServerType.GAME) {
            return;
        }
        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(dano, tomo, bateu, kn, causa, proj, carta.getNome()));
    }

    public static void damage(double dano, LivingEntity bateu, LivingEntity tomo, CausaDano causa, String nome) {
        if (server != CardWarsPlugin.ServerType.GAME) {
            return;
        }
        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(dano, tomo, bateu, false, causa, null, nome));
    }

    private LivingEntity GetDamageeEntity(EntityDamageEvent event) {
        if ((event.getEntity() instanceof LivingEntity)) {
            return (LivingEntity) event.getEntity();
        }
        return null;
    }

    private Projectile GetProjectile(EntityDamageEvent event) {
        if (!(event instanceof EntityDamageByEntityEvent)) {
            return null;
        }
        EntityDamageByEntityEvent eventEE = (EntityDamageByEntityEvent) event;

        if ((eventEE.getDamager() instanceof Projectile)) {
            return (Projectile) eventEE.getDamager();
        }
        return null;
    }

    @EventHandler
    public void regen(EntityRegainHealthEvent ev) {
        if (ev.getEntity() instanceof LivingEntity) {
            dim.showDamageIndicator((LivingEntity) ev.getEntity(), "§a§l" + ev.getAmount());
            if (ev.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC_REGEN || ev.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || ev.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN) {
                //EM BREVE ADICIONAR + REGENERAÇÃO!
            }
        }

    }

    public static LivingEntity GetDamagerEntity(EntityDamageEvent event, boolean ranged) {
        if (!(event instanceof EntityDamageByEntityEvent)) {
            return null;
        }
        EntityDamageByEntityEvent eventEE = (EntityDamageByEntityEvent) event;

        if ((eventEE.getDamager() instanceof LivingEntity)) {
            return (LivingEntity) eventEE.getDamager();
        }
        if (!ranged) {
            return null;
        }
        if (!(eventEE.getDamager() instanceof Projectile)) {
            return null;
        }
        Projectile projectile = (Projectile) eventEE.getDamager();

        if (projectile.getShooter() == null) {
            return null;
        }
        if (!(projectile.getShooter() instanceof LivingEntity)) {
            return null;
        }
        return (LivingEntity) projectile.getShooter();
    }

}
