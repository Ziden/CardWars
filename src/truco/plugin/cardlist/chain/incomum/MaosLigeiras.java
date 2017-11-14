/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.chain.incomum;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.CardWarsPlugin;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MaosLigeiras extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Maos Ligeiras";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 25% chance pegar uma flecha no ar"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.CHAIN;
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        Projectile proj = ev.getProjetil();
        if (proj != null) {
            Player p = ev.getPlayerTomou();
            if (proj instanceof Arrow) {
                if (CardWarsPlugin.random.nextInt(100) < 25) {
                    p.sendMessage(ChatColor.GREEN + "Voce pegou uma flecha !");
                    ev.setCancelled("Pegou Flecha no Ar");
                    p.getInventory().addItem(new ItemStack(Material.ARROW, 1));
                }
            }
        }
    }
}
