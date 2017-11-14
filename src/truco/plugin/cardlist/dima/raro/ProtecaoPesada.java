/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.dima.raro;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDeathEvent;
import truco.plugin.utils.Utils;
import truco.plugin.utils.Utils.DamageType;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ProtecaoPesada extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Protecao Pesada";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Toma -40% de dano fisico", " + lentidao infinita"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.DIMA;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {

        if (Utils.getDamageType(ev.getCause()) == DamageType.FISICO) {
            ev.addDamageMult(0.60, "Protecao Pesada");
        }
    }

    @Override
    public void playerDeath(final CustomDeathEvent ev) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                if (ev.getPlayer() != null) {
                    ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1));
                }
            }
        }, 10);

    }

    @Override
    public void joinEvent(PlayerJoinEvent ev) {

        ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1));
        if (!eternos.contains(ev.getPlayer().getUniqueId())) {
            eternos.add(ev.getPlayer().getUniqueId());
        }

    }

    public static List<UUID> eternos = new ArrayList();

    @Override
    public void onEnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                for (UUID uid : eternos) {
                    Player p = Bukkit.getPlayer(uid);
                    if (p != null) {
                        if (!p.hasPotionEffect(PotionEffectType.SLOW)) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1));
                        }
                    }
                }
            }
        }, 20 * 10, 20 * 10);
    }

}
