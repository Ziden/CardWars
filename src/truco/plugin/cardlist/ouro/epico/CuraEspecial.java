/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.ouro.epico;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;

import truco.plugin.utils.LocUtils;
import truco.plugin.utils.TeamUtils;
import truco.plugin.utils.Utils;
import truco.plugin.cards.skills.skilltypes.Channeling;
import truco.plugin.damage.DamageManager;

/**
 *
 * @author Júnior
 */
public class CuraEspecial extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Cura especial";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Cura um aliado com metade de sua vida"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.OURO;
    }
    Skill s = new Skill(this, 10, 10) {

        @Override
        public String getName() {
            return "Cura Forte";
        }

        @Override
        public int getChannelingTime() {
            return 3;
        }

        @Override
        public boolean onCast(final Player p) {
            if (p.hasMetadata("Channeling")) {
                p.sendMessage("§aVocê já está conjurando uma magia!");
                return false;
            }
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
            final long morreuquando = Utils.getDeathMillis(aliado);
            new Channeling(p, 3, this, new Runnable() {

                @Override
                public void run() {
                    if (!Channeling.checkAlvo(morreuquando, aliado, p)) {
                        return;
                    }

                    DamageManager.cura(aliado, aliado.getMaxHealth() / 2);
                    aliado.sendMessage("§aVocê foi curado por " + p.getName() + " !");
                    p.sendMessage("§aVocê curou " + aliado.getName() + "!");
                 

                }
            });

            return false;

        }
    };

    @Override
    public Skill getSkill() {
        return s;
    }

}
