/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.epico;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.damage.DamageManager;
import truco.plugin.utils.LocUtils;
import truco.plugin.functions.MakeVanish;
import truco.plugin.data.MetaShit;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.TeamUtils;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class DruidaDeStendor extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }
    Skill s = new Skill(this, 34, 20) {

        @Override
        public String getName() {
            return "Polimorfia";
        }

        @Override
        public boolean onCast(Player p) {
            Entity target = LocUtils.getTarget(p,LocUtils.TargetType.INIMIGO);
            if (!(target instanceof Player)) {
                p.sendMessage("§dO alvo precisa ser um jogador!");
                return false;
            }
            final Player ptarget = (Player) target;
            if (!TeamUtils.canAttack(p, ptarget)) {
                p.sendMessage("§dVocê não pode usar isso em um aliado!");
                return false;
            }
            if (ptarget.getVehicle() != null) {
                p.sendMessage("§dEste alvo já foi transformado!");
                return false;
            }
            DamageManager.damage(1, p, ptarget, CustomDamageEvent.CausaDano.MAGIA, "Druida de Stendor");
            ptarget.getWorld().playEffect(ptarget.getLocation(), Effect.LARGE_SMOKE, 1);
            
            ptarget.sendMessage("§dVocê foi transformado em um porco!");
            p.sendMessage("§dVocê transformou seu alvo em um porco!");
            final Pig porco = p.getWorld().spawn(ptarget.getLocation(), Pig.class);

            porco.setAdult();
            porco.setPassenger(ptarget);
            porco.setCustomName(CardWarsPlugin.getArena().getTeam(ptarget.getUniqueId()).getCor() + ptarget.getName());

            porco.setCustomNameVisible(true);

            Utils.setWidthHeight(ptarget, -1, -1, -1);
            MakeVanish.makeVanished(ptarget);
            int task = Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    if (porco != null && !porco.isDead()) {
                        porco.remove();

                        ptarget.getWorld().playEffect(ptarget.getLocation(), Effect.LARGE_SMOKE, 1);
                        MakeVanish.makeVisible(ptarget);
                        ptarget.sendMessage("§dVocê voltou a sua forma padrão!");
                        ptarget.removeMetadata("polimorfia", CardWarsPlugin._instance);
                        Utils.setWidthHeight(ptarget, 0F, 0.6F, 1.8F);
                    }
                }
            }, 20 * 5);
            MetaShit.setMetaObject("polimorfia", ptarget, true);
            MetaShit.setMetaObject("emcima", porco, task);

            return true;
        }
    };

    @Override
    public String getNome() {
        return "Druida De Stendor";
    }

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Transforma o alvo em um porco"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }
}
