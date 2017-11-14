/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.ouroOUcouro.epico;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.functions.Cooldown;
import truco.plugin.utils.ChatUtils;

/**
 *
 * @author Júnior
 */
public class LuzDaGraca extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Luz da Graca";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"15% chance de reviver na hora", "Só pode acontecer uma vez por minuto"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.OURO_LEATHER;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        Player p = ev.getPlayerTomou();
        if (Cooldown.isCooldown(p, "luzdagraca")) {
            return;
        }
        if (p.getHealth() - ev.getFinalDamage() <= 0) {
            int x = CardWarsPlugin.random.nextInt(100);
            if (x < 14) {
                ev.setCancelled("Luz da Graca");
                ev.setDamage(0);
                Cooldown.addCoolDown(p, "luzdagraca", 60000);
                p.setHealth(p.getMaxHealth());
                ChatUtils.sendMessage(p, "§6§l[Luz Da Graca] §r§6Você reviveu!");
                for (Entity e : p.getNearbyEntities(7, 7, 7)) {
                    if (e instanceof Player) {
                        ChatUtils.sendMessage((Player) e, "§6§l[Luz Da Graca] §r§6" + p.getName() + " reviveu!");
                    }
                }
            }
        }

    }

}
