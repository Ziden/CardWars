/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.couro.incomum;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.LocUtils;
import truco.plugin.utils.TeamUtils;
import truco.plugin.cards.skills.skilltypes.Channeling;
import truco.plugin.cards.StatusEffect;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.efeitos.CustomEntityFirework;


/**
 *
 * @author Júnior
 */
public class PerturbadorDoSilencio extends Carta {

    Skill skill = new Skill(this, 9, 15) {

        @Override
        public String getName() {
            return "Cancelamento Magico";
        }

        @Override
        public boolean onCast(Player p) {

            Entity alvo = LocUtils.getTarget(p,LocUtils.TargetType.INIMIGO);
            if (alvo == null || alvo.getType() != EntityType.PLAYER || alvo.getLocation().distance(p.getLocation()) > 10) {
                p.sendMessage(ChatColor.RED + "Voce precisa de um alvo !");
                return false;
            }
            final Player aliado = (Player) alvo;
            if (!TeamUtils.canAttack(p, (Player) alvo)) {
                p.sendMessage(ChatColor.RED + "Voce so pode fazer isto em inimigos !");
                return false;
            }

            if (!aliado.hasMetadata("Channeling")) {
                p.sendMessage("§aEsse jogador não estava conjurando uma magia!");
                return true;
            }
            ChatUtils.sendMessage(aliado, "§a" + p.getName() + " lhe cancelou a conjuração!");
            p.sendMessage("§cVocê cancelou a conjuração dele!");
            DamageManager.damage(6, p, aliado, CustomDamageEvent.CausaDano.MAGIA, "Perturbador do Silencio");
            StatusEffect.addStatusEffect(p, StatusEffect.StatusMod.SILENCE, 6);
            CustomEntityFirework.spawn(p.getLocation(), FireworkEffect.builder().withColor(Color.BLUE).with(FireworkEffect.Type.BURST).build());

            Channeling.disrupt(aliado);
            return true;
        }
    };

    @Override
    public Skill getSkill() {
        return skill;
    }

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Perturbador do Silencio";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Tenta cancelar o conjuramento", "do seu alvo, caso consiga ", "cause 6 de dano e 6s de silence"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }

}
