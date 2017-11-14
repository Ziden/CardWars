package truco.plugin.functions.fun;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.util.Vector;
import truco.plugin.CardWarsPlugin;
import truco.plugin.utils.kibes.EntityUtils;
import truco.plugin.utils.mobapi.mobs.MobsApi;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MobStack implements Listener {

    public MobStack() {
        Bukkit.getPluginManager().registerEvents(this, CardWarsPlugin._instance);
    }

    @EventHandler
    public static void clickentity(PlayerInteractEntityEvent ev) {
        if (MobsApi.bixos.containsKey(ev.getRightClicked())) {
            return;
        }
        if (!(ev.getRightClicked() instanceof LivingEntity)) {
            return;
        }
        LivingEntity clicado = (LivingEntity) ev.getRightClicked();

        if (ev.getRightClicked() instanceof LivingEntity) {
            if (clicado.getCustomName() != null) {
                return;
            }

        }
        if (clicado.getVehicle() != null) {
            return;
        }
        if (clicado.getPassenger() != null) {
            if (clicado instanceof Player) {
                derruba(clicado);
            }
            return;

        }
        if (ev.getRightClicked() instanceof Player) {
            return;
        }

        if (ev.getPlayer().getVehicle() != null) {
            return;
        }
        Entity ultimo;
        if (ev.getPlayer().getPassenger() != null) {
            Entity prox = ev.getPlayer().getPassenger();
            while (prox.getPassenger() != null) {
                prox = prox.getPassenger();
            }
            ultimo = prox;

        } else {
            ultimo = ev.getPlayer();
        }
        ev.getPlayer().sendMessage("§d[MobStacker] §cVocê pegou §e" + ev.getRightClicked().getType().getName() + " §c!");
        ultimo.setPassenger(clicado);

    }

    @EventHandler
    public void interact(PlayerInteractEvent ev) {
        if (ev.getAction() != Action.LEFT_CLICK_AIR) {
            return;
        }
        Player p = ev.getPlayer();

        if (p.getPassenger() == null) {
            return;
        }
        List<Entity> entis = new ArrayList();
        Entity t = p;
        while ((t = t.getPassenger()) != null) {
            entis.add(t);
        }
        Vector dir = p.getLocation().getDirection().normalize().multiply(2);
        for (Entity entida : entis) {
            if (entida.getVehicle() != null) {
                entida.getVehicle().eject();
            }
            entida.setVelocity(dir);
        }
        if (!entis.isEmpty()) {
            ev.getPlayer().sendMessage("§d[MobStacker] §cVocê arremecou seus animais!");
        }
    }

    @EventHandler
    public void toggleSneak(PlayerToggleSneakEvent ev) {
        if (ev.isSneaking() && ev.getPlayer().getPassenger() != null) {
            derruba(ev.getPlayer());
            ev.getPlayer().sendMessage("§d[MobStacker] §dVocê soltou seus animais!");
        }
    }

    @EventHandler
    public void exit(VehicleExitEvent e) {
        derruba(e.getExited());
    }

    public static void derruba(Entity e) {
        Entity saiu = e;
        if (saiu.getPassenger() == null) {
            return;
        }
        Entity s = saiu;
        List<Entity> toremove = new ArrayList();
        while ((s = s.getPassenger()) != null) {
            toremove.add(s);
        }
        for (Entity remo : toremove) {
            if (remo.getVehicle() != null) {
                remo.getVehicle().eject();
            }
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        derruba(e.getPlayer());
    }

    @EventHandler
    public void kick(PlayerKickEvent ev) {
        derruba(ev.getPlayer());
    }
}
