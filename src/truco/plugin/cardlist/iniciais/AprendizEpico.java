package truco.plugin.cardlist.iniciais;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EventosCartas;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cards.skills.skilllist.GolpeEpico;
import truco.plugin.utils.ChatUtils;
import truco.plugin.cards.skills.skilltypes.TimedHit;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class AprendizEpico extends Carta {

    GolpeEpico g = new GolpeEpico(this, 5, 40);

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Aprendiz epico";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Golpe forte com espadas de madeira"};
    }

    @Override
    public Skill getSkill() {
        return g;
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.TODOS;
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
        if(ev.getCause()!=CausaDano.ATAQUE){
            return;
        }
        if (!TimedHit.hit.acertaTimed(donoDaCarta, getSkill().getName())) {
        } else {
            if (donoDaCarta.getItemInHand() != null && donoDaCarta.getItemInHand().getType() == Material.WOOD_SWORD) {
                donoDaCarta.sendMessage(ChatColor.GREEN + "Voce acertou um golpe epico !");
                if (ev.getTomou().getType() == EntityType.PLAYER) {
                    ((Player) ev.getTomou()).sendMessage(ChatColor.RED + "Tomou um golpe epico !");
                }
                EventosCartas.acertaGolpeEpico(ev, donoDaCarta);
                ev.addDamageMult(3, "Aprendiz Epico");
            } else {
                ChatUtils.sendMessage(donoDaCarta, "§6Você precisava estar com uma espada de madeira!");
            }
        }
    }
}
