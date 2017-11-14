/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.pelado.epico;

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
public class CapacidadeIntelectual extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Capacidade Intelectual Aprimorada";
    }

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public String[] getDesc() {
        return new String[]{"gera 5 bolas de energia"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO;
    }
    Skill s = new Skill(this, 35, 60) {

        @Override
        public String getName() {
            return "Gerar Energia Avancada";
        }

        @Override
        public int getChannelingTime() {
            return 10;
        }

        @Override
        public boolean onCast(final Player p) {
            new Channeling(p, 10, this, new Runnable() {

                @Override
                public void run() {
                    BolasDeEnergia.addBolas(p, 5);
                    p.sendMessage("§eVocê coneguiu regenerar 5 bolas de energia!");
                }

            });

            return true;
        }
    };

}
