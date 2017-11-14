/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.dima.epico;


import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cards.skills.skilllist.Refletor;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.Utils;
import truco.plugin.utils.Utils.DamageType;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class BarreiraVingativa extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Barreira Vingativa";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Bloqueia e reflete o dano físico"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.DIMA;
    }
    Refletor r = new Refletor(this, 13, 30, "dano fisico");

    @Override
    public Skill getSkill() {
        return r;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        if(ev.getBateu()==null)return;
        Player player = ev.getPlayerTomou();
        if (player.hasMetadata("Refletor2xdano fisico")) {
            if (Utils.getDamageType(ev.getCause()) == DamageType.FISICO) {

                LivingEntity damager = ev.getBateu();
             
                DamageManager.damage(ev.getFinalDamage()*2, player, damager, CustomDamageEvent.CausaDano.UNKNOWN, "Barreira Vingativa");
                player.removeMetadata("Refletor2xdano fisico", CardWarsPlugin._instance);
                player.sendMessage("§aVocê refletiu 200% do dano fisico!");
                ev.setCancelled("Barreira Vingativa");
                Location l = damager.getLocation();
              

                if (damager instanceof Player) {
                    ((Player) damager).sendMessage("§cSeu inimigo lhe retornou 200% do dano fisico!");
                }


            }
        }
    }
}
