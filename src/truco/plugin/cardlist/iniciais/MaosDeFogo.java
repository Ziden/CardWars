/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.iniciais;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cards.skills.skilllist.Firebola;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;

/**
 *
 * @author usuario
 */
public class MaosDeFogo extends Carta {

    Firebola f = new Firebola(this, 3, 5);

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Maos de Fogo";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Atira fireballs", "Dano com soco causa fogo", "+ 2 pocoes de vida"};
    }

    public ItemStack[] getItems() {
        return new ItemStack[]{
                    new Potion(PotionType.INSTANT_HEAL).toItemStack(2)
                };
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER_PELADO;
    }

    public Skill getSkill() {
        return f;
    }

    @Override
    public void causaDano(Player donoDaCarta,CustomDamageEvent ev) {
        if(ev.getCause()!=CausaDano.ATAQUE)return;
        if (donoDaCarta.getItemInHand() == null || donoDaCarta.getItemInHand().getType() == Material.AIR) {
            if (ev.getTomou() instanceof LivingEntity) {
                DamageManager.addMagicFireTicks((LivingEntity)ev.getTomou(), 20, donoDaCarta);
            }
        }
    }
}
