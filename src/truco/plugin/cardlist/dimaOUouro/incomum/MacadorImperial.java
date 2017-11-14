package truco.plugin.cardlist.dimaOUouro.incomum;

import truco.plugin.cards.skills.skilllist.MacadaEpica;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EventosCartas;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.ItemUtils;
import truco.plugin.cards.skills.skilltypes.TimedHit;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MacadorImperial extends Carta {

    MacadaEpica g = new MacadaEpica(this, 5, 40);

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Mestre das Maças";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Golpe epico com pás"};
    }

    @Override
    public Skill getSkill() {
        return g;
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.OURO_DIMA;
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
         if(ev.getCause()!=CustomDamageEvent.CausaDano.ATAQUE){
            return;
        }
        if (!TimedHit.hit.acertaTimed(donoDaCarta, getSkill().getName())) {

        } else {
            if (donoDaCarta.getItemInHand() != null && ItemUtils.isSpade(donoDaCarta.getItemInHand().getType())) {
                donoDaCarta.sendMessage(ChatColor.GREEN + "Voce acertou uma maçada epica !");
                if (ev.getTomou().getType() == EntityType.PLAYER) {
                    ((Player) ev.getTomou()).sendMessage(ChatColor.RED + "Tomou uma maçada epica !");
                }
                EventosCartas.acertaGolpeEpico(ev, donoDaCarta);
                ev.addDamageMult(2.0, "Macada Epica");
            } else {
                donoDaCarta.sendMessage(ChatColor.GREEN + "Voce precisava de uma pá para acertar! !");
            }
        }
    }
}
