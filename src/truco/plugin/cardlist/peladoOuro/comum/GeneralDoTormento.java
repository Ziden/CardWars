/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.peladoOuro.comum;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import static truco.plugin.cards.Carta.noPermToUse;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.ItemUtils;
import truco.plugin.cards.StatusEffect;
import truco.plugin.cards.skills.skilltypes.TimedHit;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.efeitos.CustomEntityFirework;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class GeneralDoTormento extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "General do Tormento";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"golpe forte com punhos", "que causa stun de 3s", "e dano divino"};
    }

    Skill s = new Skill(this, 16, 10) {

        @Override
        public String getName() {
            return "Punhos Fortes";
        }

        @Override
        public boolean onCast(Player p) {
            return TimedHit.hit.fazTimedHit(p, getName(), 20, 5);
        }

    };

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO_OURO;
    }

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
        if (ev.getCause() != CustomDamageEvent.CausaDano.ATAQUE) {
            return;
        }
        if (TimedHit.hit.acertaTimed(donoDaCarta, s.getName())) {
            if (ItemUtils.isArm(donoDaCarta.getItemInHand())) {
                DamageManager.causaDanoBruto(ev.getPlayerBateu(), ev.getTomou(), 6, getNome());

                if (ev.getTomou() instanceof Player) {
                    StatusEffect.addStatusEffect((Player) ev.getTomou(), StatusEffect.StatusMod.STUN, 3);
                    ((Player) ev.getTomou()).sendMessage("§a" + donoDaCarta.getName() + " lhe acertou punhos fortes!");
                    CustomEntityFirework.spawn(ev.getTomou().getLocation(), FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.AQUA).build());
                }
                donoDaCarta.sendMessage("§aVocê acertou punhos fortes!");
            } else {
                donoDaCarta.sendMessage("§eVocê precisava usar o golpe com os punhos!");
            }
        }
    }

}
