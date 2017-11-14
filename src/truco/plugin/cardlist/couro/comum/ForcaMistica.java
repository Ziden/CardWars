/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.comum;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cards.skills.skilllist.Refletor;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;
import truco.plugin.utils.Utils;
import truco.plugin.utils.Utils.DamageType;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ForcaMistica extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Forca Mistica";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Bloqueia o dano", "e arremaca o alvo para longe"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }

    Refletor r = new Refletor(this, 13, 10, "repulsao");

    @Override
    public Skill getSkill() {
        return r;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        Player player = ev.getPlayerTomou();
        LivingEntity damager = ev.getBateu();
        if (damager == null) {
            return;
        }
        if (player.hasMetadata("Refletor2xrepulsao")) {
            if (ev.getCause() == CausaDano.ATAQUE) {

                if (damager instanceof LivingEntity) {
                    damager.setVelocity(damager.getVelocity().add(damager.getLocation().toVector().subtract(player.getLocation().toVector()).add(new Vector(0, 1, 0)).normalize().multiply(3)));
                    player.removeMetadata("Refletor2xrepulsao", CardWarsPlugin._instance);
                    player.sendMessage("§aVocê arremecou o alvo para longe!");
                    ev.setCancelled("Forca Mistica");
                    Location l = damager.getLocation();

                    if (damager instanceof Player) {
                        ((Player) damager).sendMessage("§cSeu inimigo lhe arremecou para longe!");
                    }
                }

            }
        }
    }
}
