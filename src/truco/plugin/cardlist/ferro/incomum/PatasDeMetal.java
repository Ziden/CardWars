/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.ferro.incomum;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.damage.DamageManager;
import truco.plugin.utils.ChatUtils;
import truco.plugin.data.MetaShit;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;
import truco.plugin.events.CustomDeathEvent;
import truco.plugin.utils.TeamUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PatasDeMetal extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }
    Skill s = new Skill(this, 15, 30) {

        @Override
        public String getName() {
            return "Esmagador de cabeças";
        }

        @Override
        public boolean onCast(final Player p) {
            if (p.hasMetadata("pulandopatas")) {
                return false;
            }
            ChatUtils.tellAction(p, "pulou para o céu");
            p.sendMessage("§aVocê pulou!");
            p.getWorld().playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 4, 1);

            int task = Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    if (p != null) {
                        ativa(p, false);
                    }
                }
            }, 6 * 20);
            Vector pulo = new Vector(0, 3, 0);
            p.setVelocity(pulo);
            MetaShit.setMetaObject("pulandopatas", p, task);
            return true;
        }
    };

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public String getNome() {
        return "Patas de Metal";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Ao ativar pula com força", "ao se chocar no chao causa dano", "em inimigos proximos"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.FERRO;
    }

    @Override
    public void playerDeath(CustomDeathEvent ev) {
        Player p = ev.getPlayer();
        if (p.hasMetadata("pulandopatas")) {
            Bukkit.getScheduler().cancelTask((int) MetaShit.getMetaObject("pulandopatas", p));

            p.removeMetadata("pulandopatas", CardWarsPlugin._instance);
        }
    }

    public void ativa(Player p, boolean cancela) {
        if (cancela) {
            Bukkit.getScheduler().cancelTask((int) MetaShit.getMetaObject("pulandopatas", p));
        }
        p.removeMetadata("pulandopatas", CardWarsPlugin._instance);
        p.getWorld().playEffect(p.getLocation().add(0, 1, 0), Effect.EXPLOSION_HUGE, 1);
        p.getWorld().playSound(p.getLocation(), Sound.EXPLODE, 5, 1);
        for (Entity t : p.getNearbyEntities(4, 4, 4)) {
            if (t instanceof Player) {
                if (TeamUtils.canAttack(p, (Player) t)) {
                    DamageManager.damage(((Player) t).getMaxHealth() / 3, p, (Player) t, CausaDano.SKILL_ATAQUE, "Patas de Metal");
                }
            }
        }
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        Player p = (Player) ev.getPlayerTomou();
        if (p.hasMetadata("pulandopatas")) {
            if (ev.getCause() == CausaDano.FALL) {
                ev.setCancelled("Patas de Metal");
                ativa(p, true);
            }
        }
    }
}
