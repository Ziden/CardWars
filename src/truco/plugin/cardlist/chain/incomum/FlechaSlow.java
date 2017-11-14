package truco.plugin.cardlist.chain.incomum;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
import truco.plugin.cards.CC;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EfeitoProjetil;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.ChatUtils;
import truco.plugin.data.MetaShit;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class FlechaSlow extends Carta {

 Skill s = new Skill(this, 15, 5) {

        @Override
        public boolean onCast(Player p) {
            if (!p.hasMetadata("flechagelo")) {
                ChatUtils.sendMessage(p, "§a§lSua proxima flecha vai ser de gelo!");
                MetaShit.setMetaObject("flechagelo", p, true);
                return true;
            } else {
                ChatUtils.sendMessage(p, "§cVocê já ativou está habilidade, use primeiro para ativar denovo!");
                return false;
            }
        }

        @Override
        public String getName() {
            return "Flechada De Gelo";
        }
    };
 
    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Mira Gelida";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Sua proxima flecha causa slow e deixa o alvo gelado"};
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
        if (p.hasMetadata("flechaslow")) {
            p.removeMetadata("flechaslow", CardWarsPlugin._instance);
            EfeitoProjetil.addEfeito((Projectile)ev.getProjectile(), new EfeitoProjetil(p, (Projectile) ev.getProjectile()) {

                @Override
                public void causaEfeito(Player gotHit, Player Shooter, Projectile projectile) {
                    CC.tacaSlow(gotHit, 2, 20*4);
                   // gotHit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*4, 2));
                    gotHit.sendMessage(ChatColor.DARK_GREEN + "Você foi acertado por uma flecha de gelo!");
                }
            });
        }
    }
}
