/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.chain.raro;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EventosCartas;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cards.skills.skilltypes.TimedHit;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class TiroDePrecisao extends Carta {

    Skill skill = new Skill(this, 10, 40) {
        @Override
        public String getName() {
            return "Flechada Monstra";
        }

        @Override
        public boolean onCast(Player p) {
            return TimedHit.hit.fazTimedHit(p, getName(), 20, 4);

        }
    };

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Sniper Oculto de Braton";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Golpe epico para atirar flecha monstra"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.CHAIN;
    }

    @Override
    public Skill getSkill() {
        return skill;
    }

    @Override
    public void playerTocaFlecha(EntityShootBowEvent ev) {
        if (TimedHit.hit.acertaTimed(((Player) ev.getEntity()), getSkill().getName())) {
            ((Player) ev.getEntity()).sendMessage(ChatColor.GREEN + "Voce atirou uma flecha mostra!");
            EventosCartas.modificaDanoProjetil((Projectile) ev.getProjectile(), 10);
        }
    }
}
