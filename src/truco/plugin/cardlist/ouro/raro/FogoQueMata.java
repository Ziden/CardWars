/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.ouro.raro;

import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.StatusEffect;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;

/**
 *
 * @author Júnior
 */
public class FogoQueMata extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Fogo que mata";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Causar dano de raio deixa o alvo eletrizado", "Causar dano de fogo em alvos eletrizados causa dano em area"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.OURO;
    }

    @Override
    public void causaDano(Player causador, CustomDamageEvent ev) {

        LivingEntity alvo = ev.getTomou();
        if (alvo instanceof Player) {
            Player palvo = (Player) alvo;
            if (ev.getCause() == CausaDano.MAGIA_RAIO) {

                StatusEffect.addStatusEffect(palvo, StatusEffect.StatusMod.ELETRIZADO, 3);

            }
            if (ev.getCause() == CausaDano.MAGIA_FOGO) {
                if (StatusEffect.hasStatusEffect(palvo, StatusEffect.StatusMod.ELETRIZADO)) {

                    DamageManager.damage(6, causador, alvo, CausaDano.MAGIA_FOGO, "Fogo que Mata");
                    alvo.getWorld().playEffect(palvo.getLocation(), Effect.EXPLOSION_LARGE, 1);
                    for (Entity li : palvo.getNearbyEntities(4, 4, 4)) {
                        if (li instanceof Player) {

                            DamageManager.damage(4, causador, alvo, CausaDano.MAGIA_FOGO, "Fogo que Mata");
                        }
                    }
                    DamageManager.cura(causador, 1);
                    StatusEffect.removeStatusEffect(palvo, StatusEffect.StatusMod.ELETRIZADO);

                }
            }
        }
    }
}
