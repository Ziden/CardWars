/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.comum;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.TeamUtils;
import truco.plugin.cards.StatusEffect;
import truco.plugin.utils.efeitos.ParticleEffect;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PernasCongeladas extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Pernas Congeladas";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Congela todos inimigos proximos"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }

    Skill s = new Skill(this, 17, 15) {

        @Override
        public String getName() {
            return "Esfria Pernas";
        }

        @Override
        public boolean onCast(Player p) {

            for (Entity e : p.getNearbyEntities(5, 3, 5)) {
                if (e instanceof Player) {
                    Player perto = (Player) e;
                    if (TeamUtils.canAttack(p, perto)) {
                        ParticleEffect.CLOUD.display((float) 0.3, (float) 0.3, (float) 0.3, 0, 6, perto.getLocation().clone().add(0, 1.6, 0), 16);
                        StatusEffect.addStatusEffect(perto, StatusEffect.StatusMod.CONGELADO, 2);
                        perto.sendMessage("§b" + p.getName() + " lhe congelou !");
                    }
                }
            }
            return true;
        }
    };

    @Override
    public Skill getSkill() {
        return s;
    }

}
