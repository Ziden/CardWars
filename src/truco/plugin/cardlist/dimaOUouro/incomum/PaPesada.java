/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.dimaOUouro.incomum;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PaPesada extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Pá Pesada";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Pá de ouro causa lentidão","+ 1 pá de ouro"};
    }

    @Override
    public ItemStack[] getItems() {
    return new ItemStack[]{new ItemStack(Material.GOLD_SPADE)};
    }

    
    @Override
    public Armadura getArmadura() {
        return Armadura.OURO_DIMA;
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
         if(ev.getCause()!=CustomDamageEvent.CausaDano.ATAQUE){
            return;
        }
        if (donoDaCarta.getItemInHand().getType() == Material.GOLD_SPADE) {
           ev.getTomou().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*3, 1));
       
        }
    }

}
