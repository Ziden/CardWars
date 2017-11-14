/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.dima.incomum;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.cards.stats.MenosManaEscudo;
import truco.plugin.cards.stats.Stats;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.functions.game.Mana;
import truco.plugin.utils.Utils;
import truco.plugin.utils.Utils.DamageType;
import truco.plugin.utils.efeitos.ParticleEffect;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class EscudoDefensor extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Escudo Defensor";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 escudo", "Ignora dano fisico com escudo na mao"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.DIMA;
    }

    @Override
    public ItemStack[] getItems() {
        ItemStack is = new ItemStack(Material.BONE);
        ItemMeta itm = is.getItemMeta();
        itm.setDisplayName("§7§lEscudo");
        is.setItemMeta(itm);
        return new ItemStack[]{is};
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        if (ev.getBateu() == null) {
            return;
        }
        Player player = ev.getPlayerTomou();
        if (player.getItemInHand().getType() == Material.BONE) {
            if (Utils.getDamageType(ev.getCause()) == DamageType.FISICO) {

                if (ev.getFinalDamage() < 1) {
                    return;
                }
                int quanto = 20;
                for (Stats s : ControleCartas.getStats(player)) {
                    if (s instanceof MenosManaEscudo) {
                        quanto -= s.getX();
                    }
                }
                if (quanto < 0) {
                    quanto = 0;
                }
                LivingEntity damager = ev.getBateu();
                if (Mana.spendMana(player, quanto)) {
                    player.sendMessage(ChatColor.GOLD + "Voce bloqueou o ataque");
                   
                    ParticleEffect.FIREWORKS_SPARK.display((float)0.5,(float)0.5, (float)0.5, 0,3, player.getLocation().add(0,1.2,0), 32);
                    player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_METAL,1, 1);
                    if (damager instanceof Player) {
                        ((Player) damager).sendMessage("§cSeu inimigo bloqueu seu ataque com o escudo!");
                    }
                    ev.setCancelled("Escudo");
                }
            }
        }
    }

}
