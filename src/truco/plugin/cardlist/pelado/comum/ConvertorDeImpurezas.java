/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.pelado.comum;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cardlist.pelado.BolasDeEnergia;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.functions.game.Mana;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ConvertorDeImpurezas extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Convertor de Impuresas";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"25% de chance de ganhar uma", "bola de energia quando bate", "pode usar uma bola de energia", "para regenerar mana"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO;
    }

    @Override
    public void causaDano(Player causador, CustomDamageEvent ev) {
        if (ev.getCause() != CustomDamageEvent.CausaDano.ATAQUE) {
            return;
        }
        int chance = CardWarsPlugin.random.nextInt(100);
        if (chance < 25) {
            BolasDeEnergia.addBola(causador);
            causador.playSound(causador.getLocation(), Sound.DRINK, 99, 1);
        }

    }

    @Override
    public Skill getSkill() {
        return s;
    }

    Skill s = new Skill(this, 6, 0) {

        @Override
        public String getName() {
            return "Gerador de Mana";
        }

        @Override
        public boolean onCast(Player p) {
            if (BolasDeEnergia.getBolas(p) < 1) {
                p.sendMessage("§eVocê precisa de pelo menos 1 bola de energia!");
                return false;
            }
            p.sendMessage("§eVocê regenerou 20 de mana!");
            BolasDeEnergia.removeBola(p);
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 99, 1);
            Mana.changeMana(p, 20);
            return true;
        }
    };
}
