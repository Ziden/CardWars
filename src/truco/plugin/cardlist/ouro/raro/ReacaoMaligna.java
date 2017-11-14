/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.ouro.raro;

import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.cards.StatusEffect;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;

/**
 *
 * @author Júnior
 */
public class ReacaoMaligna extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Reacao Malignas";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Causar dano de agua deixa o alvo molhado", "Causar dano de raio em molhados stuna ele", "Causar dano de raio em molhados regenera 1 coração seu"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.OURO;
    }

    @Override
    public void causaDano(Player causador, CustomDamageEvent ev) {
        if (ev.getPlayerTomou()!=null) {
            Player palvo =  ev.getPlayerTomou();
            if (ev.getCause() == CausaDano.MAGIA_AGUA) {

                StatusEffect.addStatusEffect(palvo, StatusEffect.StatusMod.MOLHADO, 3);

            }
            if (ev.getCause() == CausaDano.MAGIA_AGUA) {
                if (StatusEffect.hasStatusEffect(palvo, StatusEffect.StatusMod.MOLHADO)) {
                    palvo.getWorld().strikeLightningEffect(palvo.getLocation());
                    StatusEffect.addStatusEffect(palvo, StatusEffect.StatusMod.STUN, 2);
                    DamageManager.cura(causador, 1);
                    StatusEffect.removeStatusEffect(palvo, StatusEffect.StatusMod.MOLHADO);

                }
            }
        }
    }

}
