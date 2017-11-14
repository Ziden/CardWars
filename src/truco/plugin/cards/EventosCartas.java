/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cardlist.couro.incomum.FinalInesperado;
import static truco.plugin.cardlist.couro.incomum.FinalInesperado.praesplodir;
import truco.plugin.cardlist.dima.comum.ProtetorDeRhodes;
import truco.plugin.cardlist.pelado.GolpeMultiplo;
import truco.plugin.cards.stats.ConjuracaoSafe;
import truco.plugin.functions.Cooldown;
import truco.plugin.functions.MakeVanish;
import truco.plugin.data.MetaShit;
import truco.plugin.utils.TeamUtils;
import truco.plugin.utils.Utils;
import truco.plugin.utils.Utils.DamageType;
import truco.plugin.cards.skills.skilltypes.BandageType.Bands;
import truco.plugin.cards.skills.skilltypes.Channeling;
import truco.plugin.cards.skills.skilltypes.CustomPotion;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDeathEvent;

/**
 *
 * @author usuario
 */
public class EventosCartas implements Listener {

    @EventHandler
    public void projetilbate(ProjectileHitEvent ev) {
        if (ev.getEntity().getShooter() instanceof Player) {
            Player p = (Player) ev.getEntity().getShooter();
            List<Carta> cartas = ControleCartas.getCartas(p);
            for (Carta c : cartas) {
                if (c != null) {
                    c.projetilBateEmAlgo(ev);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void tomadno(CustomDamageEvent ev) {

        if (ev.getTomou() instanceof Pig) {
            Pig porco = (Pig) ev.getTomou();
            if (porco.hasMetadata("emcima") && porco.getPassenger() != null) {

                Player passageiro = (Player) porco.getPassenger();
                LivingEntity damager = ev.getBateu();
                if (damager == passageiro) {
                    ev.setCancelled("Polimorfia");
                    return;
                }
                if (!(damager instanceof Player)) {
                    ev.setCancelled("Polimorfia");
                    return;
                }

                int task = (int) MetaShit.getMetaObject("emcima", porco);
                Bukkit.getScheduler().cancelTask(task);
                passageiro.getWorld().playEffect(passageiro.getLocation(), Effect.SMALL_SMOKE, 1);
                MakeVanish.makeVisible(passageiro);
                passageiro.sendMessage("§dVocê voltou a sua forma padrão!");
                passageiro.removeMetadata("polimorfia", CardWarsPlugin._instance);
                Utils.setWidthHeight(passageiro, 0F, 0.6F, 1.8F);
                porco.remove();
            }
        }

        if (ev.getPlayerTomou() != null) {
            Player p = ev.getPlayerTomou();
            if (p.getVehicle() != null && p.getVehicle().getType() == EntityType.PIG) {
                ev.setCancelled("Polimorfia");
                return;
            }

            // bigger then 4 damage will stop casting channeled spells
            if (ev.isCancelled()) {
                return;
            }
            if (ev.getFinalDamage() > 4) {
                if (p.hasMetadata("Channeling") && !ControleCartas.hasStat(p, ConjuracaoSafe.nome)) {
                    Channeling.disrupt(p);
                }
            }
        }
    }

    @EventHandler
    public void move(PlayerMoveEvent e) {
        for (Carta c : ControleCartas.getCartas(e.getPlayer())) {
            if (c != null) {
                c.move(e);
            }
        }
    }

    @EventHandler
    public void saidoporco(VehicleExitEvent ev) {
        if (ev.getVehicle().hasMetadata("emcima")) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void desloga(PlayerQuitEvent ev) {
        if (ev.getPlayer().hasMetadata("Channeling")) {
            Channeling.disrupt(ev.getPlayer());
        }
        if (ev.getPlayer().getVehicle() instanceof Pig && ev.getPlayer().hasMetadata("polimorfia")) {
            Pig porco = (Pig) ev.getPlayer().getVehicle();
            if (porco.hasMetadata("emcima") && porco.getPassenger() != null) {
                Player passageiro = ev.getPlayer();
                int task = (int) MetaShit.getMetaObject("emcima", porco);
                Bukkit.getScheduler().cancelTask(task);
                passageiro.getWorld().playEffect(passageiro.getLocation(), Effect.SMALL_SMOKE, 1);
                MakeVanish.makeVisible(passageiro);
                passageiro.sendMessage("§dVocê voltou a sua forma padrão!");
                passageiro.removeMetadata("polimorfia", CardWarsPlugin._instance);
                Utils.setWidthHeight(passageiro, 0F, 0.6F, 1.8F);
                porco.remove();
            }
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        for (Carta c : ControleCartas.getCartas(e.getPlayer())) {
            if (c != null) {
                c.interact(e);
            }
        }
    }

    @EventHandler
    public void pocao(PotionSplashEvent ev) {
        if (ev.getPotion().getShooter() instanceof Player) {
            Player shooter = (Player) ev.getPotion().getShooter();

            CustomPotion customPotion = CustomPotion.isCustomPotion(ev.getEntity().getItem());
            if (customPotion != null) {
                ev.getEntity().getEffects().clear();
                List<LivingEntity> toRemove = new ArrayList<LivingEntity>();
                for (LivingEntity e : ev.getAffectedEntities()) {

                    boolean naoToma = false;
                    if (e.getType() == EntityType.PLAYER) {
                        Player tomouPocao = (Player) e;
                        if (customPotion.isBeneficial) {
                            if (TeamUtils.canAttack(shooter, tomouPocao)) {
                                naoToma = true;
                            }
                        } else {
                            if (!TeamUtils.canAttack(shooter, tomouPocao)) {
                                naoToma = true;
                            }
                        }
                        if (!naoToma) {

                            customPotion.applySplashEffect(tomouPocao, shooter);
                        }
                    }
                }
                ev.setCancelled(true);
                ev.getAffectedEntities().clear();
            } else {
                // natural potion effects wont affect allied members
                // and beneficial potions wont affect enemy
                List<LivingEntity> toRemove = new ArrayList<LivingEntity>();
                for (LivingEntity e : ev.getAffectedEntities()) {
                    if (e.getType() == EntityType.PLAYER) {
                        Player tomouPocao = (Player) e;
                        for (PotionEffect ef : ev.getEntity().getEffects()) {
                            if (ef.getType() == PotionEffectType.DAMAGE_RESISTANCE || ef.getType() == PotionEffectType.JUMP || ef.getType() == PotionEffectType.INVISIBILITY || ef.getType() == PotionEffectType.INCREASE_DAMAGE || ef.getType() == PotionEffectType.HEALTH_BOOST || ef.getType() == PotionEffectType.HEAL || ef.getType() == PotionEffectType.REGENERATION || ef.getType() == PotionEffectType.SPEED || ef.getType() == PotionEffectType.FIRE_RESISTANCE || ef.getType() == PotionEffectType.SPEED) {
                                if (TeamUtils.canAttack(shooter, tomouPocao)) {
                                    toRemove.add(e);
                                }
                            } else {
                                if (!TeamUtils.canAttack(shooter, tomouPocao)) {
                                    toRemove.add(e);
                                }
                            }
                        }
                    }

                }
                ev.getAffectedEntities().removeAll(toRemove);
            }
        }

    }

    @EventHandler
    public void respawn(CustomDeathEvent ev) {
        if (praesplodir.containsKey(ev.getPlayer().getUniqueId())) {
            Bukkit.getScheduler().cancelTask(praesplodir.get(ev.getPlayer().getUniqueId()));
            FinalInesperado.explode(ev.getPlayer().getUniqueId(), (Player) MetaShit.getMetaObject("tacobomba", ev.getPlayer()));
        }
        if (ev.getPlayer().hasMetadata("bandagem")) {
            Bands b = (Bands) MetaShit.getMetaObject("bandagem", ev.getPlayer());
            b.stop();

        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (GolpeMultiplo.getGolpeMultiplo(p).alvo != null) {
                if (GolpeMultiplo.getGolpeMultiplo(p).alvo == ev.getPlayer().getUniqueId()) {
                    GolpeMultiplo.clear(p);
                }
            }

        }
        for (Carta c : ControleCartas.getCartas(ev.getPlayer())) {
            if (c != null) {
                c.playerDeath(ev);
            }
        }
        Channeling.disrupt(ev.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void dano(CustomDamageEvent ev) {
        if (ev.isCancelled()) {
            return;
        }
        if (ev.getBateu() != null) {
            if (ev.getBateu().getType() == EntityType.PLAYER) {
                for (Carta c : ControleCartas.getCartas(ev.getPlayerBateu())) {
                    if (c != null) {
                        c.causaDano(ev.getPlayerBateu(), ev);
                        if (ev.isCancelled()) {
                            return;
                        }
                    }
                }

            }
        }
        if (ev.getPlayerTomou() != null) {
            for (Carta c : ControleCartas.getCartas((Player) ev.getPlayerTomou())) {
                if (c != null) {
                    c.tomaDano(ev);
                    if (ev.isCancelled()) {
                        return;
                    }
                }
            }
        }
        // se alguma carta cancelou...
        if (ev.isCancelled()) {
            return;
        }

        LivingEntity tomou = ev.getTomou();
        if (tomou.hasMetadata("bandagem")) {
            Bands b = (Bands) MetaShit.getMetaObject("bandagem", tomou);
            b.stop();
            if (tomou.getType() == EntityType.PLAYER) {
                ((Player) tomou).sendMessage(ChatColor.RED + "Voce parou de aplicar as bandagems");
            }
        }

        if (ev.getProjetil() != null) {
            if (ev.getPlayerBateu() != null) {

                Projectile proj = ev.getProjetil();

                if (proj.hasMetadata("efeito")) {
                    List<EfeitoProjetil> efeitos = (List<EfeitoProjetil>) MetaShit.getMetaObject("efeito", proj);
                    for (EfeitoProjetil ep : efeitos) {
                        ep.causaEfeito(ev.getPlayerTomou(), ep.getShooter(), proj);
                    }
                }
                if (proj.hasMetadata("modDano")) {
                    double dano = (double) MetaShit.getMetaObject("modDano", proj);
                    ev.addDamage(dano);
                }

            }
        }
        if (ev.getPlayerBateu() != null && ev.getPlayerTomou() != null) {
            Player p = ev.getPlayerBateu();
            if (Cooldown.isCooldown(p, "escudodivino")) {
                if (ev.getCause() == CustomDamageEvent.CausaDano.ATAQUE) {
                    p.sendMessage("§aVocê ignorou o dano físico pelo motivo de ter um Escudo Divino");
                    ev.getPlayerBateu().sendMessage("§cSeu alvo tem um escudo divino e ignorou!");
                    ev.getPlayerBateu().getWorld().playSound(p.getLocation(), Sound.ZOMBIE_METAL, 1, 1);
                    ev.getPlayerBateu().getWorld().playEffect(ev.getTomou().getLocation(), Effect.FIREWORKS_SPARK, 10);
                    ev.setCancelled("Escudo Divino");
                    return;
                }
            }
            if (p.hasMetadata("ProtegidoPor")) {
                Player protegendo = Bukkit.getPlayer((UUID) MetaShit.getMetaObject("ProtegidoPor", p));
                if (protegendo == null || (p.getLocation().distance(protegendo.getLocation()) > 4)) {
                    ProtetorDeRhodes.paraDeProteger(protegendo, p);
                } else {
                    DamageManager.damage(ev.getFinalDamage(), ev.getPlayerBateu(), protegendo, CustomDamageEvent.CausaDano.MAGIA, "Protegeu");

                    ev.addDamageMult(0.5, "Protegido");
                    ev.getPlayerBateu().sendMessage(ChatColor.RED + protegendo.getName() + " protegeu seu ataque");

                }
            }
        }

    }

    @EventHandler
    public void entiShoot(EntityShootBowEvent ev) {
        if (ev.getEntity().getVehicle() instanceof Pig) {
            ev.setCancelled(true);
            return;
        }
        if (ev.getEntity() instanceof Player) {
            for (Carta c : ControleCartas.getCartas((Player) ev.getEntity())) {
                if (c != null) {
                    c.playerTocaFlecha(ev);
                }
            }
        }
    }

    public static void modificaDanoProjetil(Projectile p, double modDano) {
        double dano = 0;
        if (p.hasMetadata("modDano")) {
            dano = (double) MetaShit.getMetaObject("modDano", p);
        }
        dano += modDano;
        MetaShit.setMetaObject("modDano", p, dano);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void join(PlayerJoinEvent ev) {
        Player p = ev.getPlayer();
        for (Carta c : ControleCartas.getCartas(p)) {
            if (c != null) {
                c.joinEvent(ev);
            }
        }
    }

    public static void acertaGolpeEpico(CustomDamageEvent ev, Player p) {
        for (Carta c : ControleCartas.getCartas(p)) {
            if (c != null) {
                c.acertaGolpeEpico(ev, p);
            }
        }
    }
}
