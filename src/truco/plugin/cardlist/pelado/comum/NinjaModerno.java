/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.pelado.comum;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cardlist.pelado.BolasDeEnergia;
import truco.plugin.cards.skills.skilltypes.Channeling;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class NinjaModerno extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Ninja Moderno";
    }

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public String[] getDesc() {
        return new String[]{"gera uma bola de energia"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO;
    }

    Skill s = new Skill(this, 11, 20) {

        @Override
        public String getName() {
            return "Gerar Bola de Energia";
        }

        @Override
        public int getChannelingTime() {
            return 5;
        }

        @Override
        public boolean onCast(final Player p) {
            new Channeling(p, 5, this, new Runnable() {

                @Override
                public void run() {
                    BolasDeEnergia.addBola(p);
                    p.sendMessage("§aVocê gerou uma bola de energia!");
                    p.playSound(p.getLocation(), Sound.DRINK, 99, 1);
                }
            });
            return true;
        }

    };

}
