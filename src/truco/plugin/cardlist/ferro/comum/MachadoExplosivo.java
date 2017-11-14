/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.ferro.comum;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.cards.EventosCartas;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.ItemUtils;
import truco.plugin.cards.skills.skilltypes.TimedHit;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MachadoExplosivo extends Carta {

    Skill skill = new Skill(this, 10, 40) {
        @Override
        public String getName() {
            return "Machadada Epica";
        }

        @Override
        public boolean onCast(Player p) {
            return TimedHit.hit.fazTimedHit(p, getName(), 20, 4);

        }
    };

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Machado Explosivo";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Golpe forte com machados"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.FERRO;
    }

    @Override
    public Skill getSkill() {
        return skill;
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
         if(ev.getCause()!=CustomDamageEvent.CausaDano.ATAQUE){
            return;
        }
        if (TimedHit.hit.acertaTimed(donoDaCarta, getSkill().getName())) {
            if (donoDaCarta.getItemInHand() != null && ItemUtils.isAxe(donoDaCarta.getItemInHand().getType())) {
                donoDaCarta.sendMessage(ChatColor.GREEN + "Voce acertou uma machadada epica!");
                if (ev.getTomou().getType() == EntityType.PLAYER) {
                    ((Player) ev.getTomou()).sendMessage(ChatColor.RED + "Tomou uma machadada epica");
                }
                EventosCartas.acertaGolpeEpico(ev, donoDaCarta);
                ev.addDamage( 5 + ControleCartas.rnd.nextInt(6));
            } else {
                ChatUtils.sendMessage(donoDaCarta, "§6Você precisava estar com um machado!");
            }
        }
    }

}
