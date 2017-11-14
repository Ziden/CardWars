/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.pelado.comum;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.damage.DamageManager;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class AmigoDaNatureza extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Amigo da Natureza";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Usa uma bola de energia para curar", "5 coraçoes e remover veneno"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO;
    }
    Skill s = new Skill(this, 12, 23) {

        @Override
        public String getName() {
            return "Remover Impurezas";
        }

        @Override
        public boolean onCast(Player p) {
            if (p.hasPotionEffect(PotionEffectType.POISON)) {
                p.removePotionEffect(PotionEffectType.POISON);
            }
            DamageManager.cura(p, 5);
            return true;
        }
    };

    @Override
    public Skill getSkill() {
        return s;
    }

}
