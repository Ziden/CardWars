/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.ferro.raro;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PuloEstendido extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    Skill s = new Skill(this, 12, 10) {

        @Override
        public String getName() {
            return "Pulo Maior";
        }

        @Override
        public boolean onCast(Player p) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 3));
            return true;
        }
    };

    @Override
    public String getNome() {
        return "Pulo Estendido";
    }

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Pulo mais alto por 5 segundos"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.FERRO;
    }

}
