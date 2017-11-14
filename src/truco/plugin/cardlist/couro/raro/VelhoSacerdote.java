/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.raro;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.LocUtils;
import truco.plugin.utils.TeamUtils;
import truco.plugin.cards.StatusEffect;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.efeitos.CustomEntityFirework;
import truco.plugin.utils.efeitos.ParticleEffect;
import truco.plugin.utils.efeitos.ParticleManager;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class VelhoSacerdote extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }
    Skill s = new Skill(this, 5, 5) {

        @Override
        public String getName() {
            return "Lanca de Gelo";
        }

        @Override
        public boolean onCast(Player p) {
            Entity target = LocUtils.getTarget(p,LocUtils.TargetType.INIMIGO);
            if (target == null) {
                p.sendMessage("§aSelecione um jogador!");
                return false;
            }
            if (!(target instanceof Player)) {
                p.sendMessage("§9O alvo precisa ser um jogador!");
                return false;
            }
            final Player ptarget = (Player) target;
            if (!TeamUtils.canAttack(p, ptarget)) {
                p.sendMessage("§9Você não pode usar isso em um aliado!");
                return false;
            }
            int dano = 4;
            if (StatusEffect.hasStatusEffect(ptarget, StatusEffect.StatusMod.CONGELADO)) {
                dano *= 3;
                StatusEffect.removeStatusEffect(ptarget, StatusEffect.StatusMod.CONGELADO);
                p.sendMessage("§4Você ganhou o dano bonus de congelamento!");
            }
            ptarget.sendMessage("§cVocê foi acertado por uma lança de gelo");
            DamageManager.damage(dano, p, ptarget, CustomDamageEvent.CausaDano.MAGIA_AGUA, "Lanca de Gelo");
            for (Location locs : ParticleManager.buildLine(p.getLocation().add(0, 1, 0), ptarget.getLocation().add(0, 1, 0))) {
                ParticleEffect.SNOW_SHOVEL.display(0, 0, 0, 0, 1, locs, 32);
            }

            CustomEntityFirework.spawn(ptarget.getLocation().add(0, 1, 0), FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.BLUE).build());
            return true;
        }
    };

    @Override
    public String getNome() {
        return "Velho Sacerdote";
    }

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Acerta um alvo com uma lança de gelo", "caso esteja congelado concede", "dano bonus e descongela o ", "alvo"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }

}
