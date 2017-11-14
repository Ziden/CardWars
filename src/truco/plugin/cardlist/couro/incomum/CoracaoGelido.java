/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.couro.incomum;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.data.MetaShit;
import truco.plugin.cards.StatusEffect;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;

/**
 *
 * @author Júnior
 */
public class CoracaoGelido extends Carta {

    Skill skill = new Skill(this, 9, 5) {

        @Override
        public String getName() {
            return "Cristal Puro";
        }

        @Override
        public boolean onCast(Player p) {
            if (p.hasMetadata("cristalpuromais")) {
                p.sendMessage("§bVocê já ativou essa skill, primeiro use ela!");
                return false;
            }
            MetaShit.setMetaObject("cristalpuromais", p, true, CardWarsPlugin._instance);
            p.sendMessage("§9Seu proximo dano de agua atordoara o alvo!");
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
        return "Coracao Gelido";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Sua proximo dano de agua", "atordoa o alvo"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }

    @Override
    public void causaDano(Player causador,CustomDamageEvent ev) {
        if (ev.getCause()==CausaDano.MAGIA_AGUA && causador.hasMetadata("cristalpuromais")) {
            if (ev.getPlayerTomou()!=null) {
                causador.removeMetadata("cristalpuromais", CardWarsPlugin._instance);
                causador.sendMessage("§9Você atoordou seu alvo por conta do seu Coracao Gelido!");
                StatusEffect.addStatusEffect((Player) ev.getPlayerTomou(), StatusEffect.StatusMod.STUN, 6);
                ev.getPlayerTomou().sendMessage("§b*seu inimigo lhe atordoou*");
            }
        }
    }

}
