/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.ouroOUcouro.raro;

import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.damage.DamageManager;
import truco.plugin.utils.ChatUtils;


/**
 *
 * @author Júnior
 */
public class ForcaDaMente extends Carta {

    Skill skill = new Skill(this, 14, 30) {

        @Override
        public String getName() {
            return "Limpador de Debuffs Expert";
        }

        @Override
        public boolean onCast(Player p) {
            Player alv;
            alv = p;
            ChatUtils.sendMessage(p, "§aVocê se livrou de todos debuffs");
            DamageManager.removeDebuffs(alv);
            alv.setFireTicks(0);
            DamageManager.removeDebuffsPotions(alv);
         
            return true;
        }
    };

    @Override
    public Skill getSkill() {
        return skill;
    }

    @Override
    public Carta.Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Forca da Mente";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Remove todos os seus debuffs"};
    }

    @Override
    public Carta.Armadura getArmadura() {
        return Carta.Armadura.OURO_LEATHER;
    }
}
