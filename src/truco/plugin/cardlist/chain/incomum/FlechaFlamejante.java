package truco.plugin.cardlist.chain.incomum;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EfeitoProjetil;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.damage.DamageManager;
import truco.plugin.utils.ChatUtils;
import truco.plugin.data.MetaShit;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.TeamUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class FlechaFlamejante extends Carta {

    Skill s = new Skill(this, 10, 30) {

        @Override
        public boolean onCast(Player p) {
            if (!p.hasMetadata("flechafogo")) {
                ChatUtils.sendMessage(p, "§a§lSua proxima flecha vai ser de fogo!");
                MetaShit.setMetaObject("flechafogo", p, true);
                return true;
            } else {
                ChatUtils.sendMessage(p, "§cVocê já ativou está habilidade, use primeiro para ativar denovo!");
                return false;
            }
        }

        @Override
        public String getName() {
            return "Flechada De Fogo";
        }
    };

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Flecha Flamejante";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Sua proxima flecha vai causar dano de fogo",
            "e taca fogo em seu inimigo"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.CHAIN;
    }

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public void playerTocaFlecha(EntityShootBowEvent ev) {

        Player p = (Player) ev.getEntity();
        if (p.hasMetadata("flechafogo")) {
            p.removeMetadata("flechafogo", CardWarsPlugin._instance);
            MetaShit.addMetaObject("magia", ev.getProjectile(), true); // setDamage 0 no final
            if (ev.getProjectile() instanceof Arrow) {
                ((Arrow) ev.getProjectile()).setFireTicks(Integer.MAX_VALUE);
            }
            final Carta car = this;
            EfeitoProjetil.addEfeito((Projectile) ev.getProjectile(), new EfeitoProjetil(p, (Projectile) ev.getProjectile()) {

                @Override
                public void causaEfeito(Player gotHit, Player Shooter, Projectile projectile) {
                    if (TeamUtils.canAttack(Shooter, gotHit)) {
                        DamageManager.damage(6, Shooter, gotHit, null, CustomDamageEvent.CausaDano.MAGIA_FOGO, false, car);
                        DamageManager.addFireTicks(gotHit, 20 * 5);
                        gotHit.sendMessage(ChatColor.DARK_GREEN + "Você foi acertado por uma flecha flamejante!");
                    }
                }
            });
        }
    }
}
