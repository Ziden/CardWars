/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cards.skills.skilllist;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EfeitoProjetil;
import truco.plugin.cards.skills.Skill;
import truco.plugin.damage.DamageManager;
import truco.plugin.utils.ChatUtils;
import truco.plugin.data.MetaShit;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author usuario
 */
public class Firebola extends Skill {

    public Firebola(Carta c, int cd, int mana) {
        super(c, 3, mana);
    }

    @Override
    public boolean onCast(Player p) {

        Projectile fb = p.launchProjectile(SmallFireball.class);
        EfeitoProjetil efeito = new EfeitoProjetil(p, fb) {

            @Override
            public void causaEfeito(Player gotHit, Player Shooter, Projectile projectile) {
                ChatUtils.sendMessage(gotHit, ChatColor.AQUA + "* tomou uma fireball na testa *");
                DamageManager.damage(7, Shooter, gotHit, CustomDamageEvent.CausaDano.MAGIA_FOGO, "Bola de Fogo");
            }
        };
        fb.setFireTicks(0);
        EfeitoProjetil.addEfeito(fb, efeito);
        MetaShit.setMetaObject("magia", fb, true);

        MetaShit.setMetaObject("cartaOrigem", fb, this.vinculada.getNome());
        return true;
    }

    @Override
    public String getName() {
        return "Bola De Fogo";
    }
}
