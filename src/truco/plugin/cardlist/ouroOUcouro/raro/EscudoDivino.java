/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truco.plugin.cardlist.ouroOUcouro.raro;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.Cooldown;
import truco.plugin.utils.LocUtils;
import truco.plugin.utils.TeamUtils;

/**
 *
 * @author Júnior
 */
public class EscudoDivino extends Carta{
     Skill skill = new Skill(this, 15, 50) {

        @Override
        public String getName() {
            return "Escudo fisico";
        }

        @Override
        public boolean onCast(Player p) {
            Player alv;
            if (p.isSneaking()) {
                alv = p;
                ChatUtils.sendMessage(p, "§aVocê está protegido por 3 segundos de dano fisico.");
            } else {
                Entity alvo = LocUtils.getTarget(p,LocUtils.TargetType.ALIADO);
                if (alvo == null || alvo.getType() != EntityType.PLAYER || alvo.getLocation().distance(p.getLocation()) > 10) {
                    p.sendMessage(ChatColor.RED + "Voce precisa de um alvo !");
                    return false;
                }
                final Player aliado = (Player) alvo;
                if (TeamUtils.canAttack(p, (Player) alvo)) {
                    p.sendMessage(ChatColor.RED + "Voce so pode fazer isto em aliados !");
                    return false;
                }
                ChatUtils.sendMessage(aliado, "§a" + p.getName() + " lhe protegeu por 3 segundos de dano fisico");
                alv = aliado;

            }
            Cooldown.addCoolDown(alv, "escudodivino", 3*1000);                      
            return true;
        }
    };

    @Override
    public Skill getSkill() {
        return skill;
    }

    @Override
    public Carta.Raridade getRaridade() {
        return Carta.Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Escudo Divino";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Da a um aliado um escudo que ignora dano fisico por 3s"};
    }

    @Override
    public Carta.Armadura getArmadura() {
        return Carta.Armadura.OURO_LEATHER;
    }
}
