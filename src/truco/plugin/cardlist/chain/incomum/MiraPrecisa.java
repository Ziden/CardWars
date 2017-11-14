package truco.plugin.cardlist.chain.incomum;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EfeitoProjetil;
import truco.plugin.cards.EventosCartas;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MiraPrecisa extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Mira de Rhodes";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 4 dano flechas a distancia", "+3 flechas"};
    }

    public ItemStack[] getItems() {
        return new ItemStack[]{
            new ItemStack(Material.ARROW, 3)
        };
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.CHAIN;
    }

    @Override
    public void playerTocaFlecha(EntityShootBowEvent ev) {
        final Location ini = ev.getEntity().getLocation();
        EfeitoProjetil.addEfeito((Projectile) ev.getProjectile(), new EfeitoProjetil((Player) ev.getEntity(), (Projectile) ev.getProjectile()) {

            @Override
            public void causaEfeito(Player gotHit, Player Shooter, Projectile projectile) {
                if (ini.distance(gotHit.getLocation()) > 35) {
                    EventosCartas.modificaDanoProjetil(projectile, 4);
                    gotHit.sendMessage(ChatColor.DARK_GREEN + "Você foi acertado por uma flecha a longa distancia!");
                    Shooter.sendMessage(ChatColor.GREEN + "Voce acertou uma flecha de longe !");
                }
                // gotHit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*4, 2));

            }
        });
    }

}
