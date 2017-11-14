/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.pelado.raro;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDeathEvent;
import truco.plugin.utils.efeitos.ParticleEffect;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CorpoFechado extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Corpo Fechado";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"50% de resistencia a dano", "porem ganha lentidao e silence",};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO;
    }

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        if (afetados.contains(ev.getPlayerTomou().getUniqueId())) {
            ev.addDamageMult(0.5, "Corpo Fechado");
        }
    }
    Skill s = new Skill(this, 120, 50) {

        @Override
        public String getName() {
            return "Fechar Corpo";
        }

        @Override
        public boolean onCast(Player p) {
            if (afetados.contains(p.getUniqueId())) {
                p.sendMessage("§aVocê já está blindado!");
                return false;
            }
            p.sendMessage("§7§l            Você se blindou!  Você não pode usar skills até morrer!");
            p.getWorld().playSound(p.getLocation(), Sound.ANVIL_USE, 4, 1);
            p.getWorld().playEffect(p.getLocation(), Effect.EXPLOSION_LARGE, 1);
            afetados.add(p.getUniqueId());
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1));
            return true;
        }

    };

    @Override
    public void playerDeath(final CustomDeathEvent ev) {
        if (afetados.contains(ev.getPlayer().getUniqueId())) {
            afetados.remove(ev.getPlayer().getUniqueId());
            if (ev.getPlayer().hasPotionEffect(PotionEffectType.SLOW)) {
                ev.getPlayer().removePotionEffect(PotionEffectType.SLOW);
            }
        }
    }

    public static List<UUID> afetados = new ArrayList();

    @Override
    public void onEnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                for (UUID uid : afetados) {
                    Player p = Bukkit.getPlayer(uid);
                    if (p != null) {
                        if (!p.hasPotionEffect(PotionEffectType.SLOW)) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1));
                        }
                        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.IRON_INGOT,(byte) 0), 0, 0, 0, 0, 1, p.getLocation().add(0, 0.5, 0), 32);

                    }
                }
            }
        }, 5, 5);
    }

}
