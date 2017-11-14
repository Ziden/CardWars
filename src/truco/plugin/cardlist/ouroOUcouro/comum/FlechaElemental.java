package truco.plugin.cardlist.ouroOUcouro.comum;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EfeitoProjetil;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.damage.DamageManager;
import truco.plugin.utils.ChatUtils;

import truco.plugin.utils.ItemUtils;
import truco.plugin.data.MetaShit;
import truco.plugin.events.CustomDamageEvent.CausaDano;

/**
 *
 * @author Carlos André Feldmann Júnior
 *
 */
public class FlechaElemental extends Carta {

    public Skill regen = new Skill(this, 3, 10) {

        @Override
        public boolean onCast(final Player p) {

            if (!p.getInventory().contains(Material.ARROW)) {
                p.sendMessage(ChatColor.RED + "Voce precisa de uma flecha !");
                return false;
            }

            ItemUtils.consumeItem(p, Material.ARROW, 1);
            p.updateInventory();
            Projectile fb = p.launchProjectile(Arrow.class);
            fb.setVelocity(fb.getVelocity().multiply(3));
            EfeitoProjetil efeito = new EfeitoProjetil(p, fb) {

                @Override
                public void causaEfeito(Player gotHit, Player Shooter, Projectile projectile) {
                    ChatUtils.sendMessage(gotHit, ChatColor.AQUA + "* tomou uma flecha elemental *");
   
                    boolean dano = CardWarsPlugin.random.nextBoolean();
                    CausaDano causa =dano ? CausaDano.MAGIA_AGUA:CausaDano.MAGIA_RAIO;
                    DamageManager.damage(6, Shooter, gotHit, causa, "Flecha Elemental");
                    
                }
            };
            MetaShit.addMetaObject("magia", fb, "true"); // cancela o dano nativo
            EfeitoProjetil.addEfeito(fb, efeito);
            MetaShit.setMetaObject("cartaOrigem", fb, this.vinculada.getNome());
            return true;
        }

        @Override
        public String getName() {
            return "Flecha Elemental";
        }
    };

    @Override
    public Carta.Raridade getRaridade() {
        return Carta.Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Encantador de Flechas";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Encanta uma flecha", "com um elemento aleatorio", "e atira ela"};
    }

    @Override
    public Carta.Armadura getArmadura() {
        return Carta.Armadura.OURO_LEATHER;
    }

    @Override
    public Skill getSkill() {
        return regen;
    }
}
