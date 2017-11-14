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
import truco.plugin.utils.Utils;
import truco.plugin.cards.skills.skilltypes.Channeling;
import truco.plugin.damage.DamageManager;

import truco.plugin.utils.efeitos.CustomEntityFirework;
import truco.plugin.utils.efeitos.ParticleEffect;
import truco.plugin.utils.efeitos.ParticleManager;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class SugadorDeSangue extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }
    Skill s = new Skill(this, 11, 10) {

        @Override
        public String getName() {
            return "Drenar Vida";
        }

        @Override
        public int getChannelingTime() {
            return 4;
        }

        @Override
        public boolean onCast(final Player p) {
            if (p.hasMetadata("Channeling")) {
                p.sendMessage("§aVocê já está conjurando uma magia!");
                return false;
            }
            Entity target = LocUtils.getTarget(p,LocUtils.TargetType.INIMIGO);

            if (!(target instanceof Player)) {
                p.sendMessage("§9O alvo precisa ser um jogador!");
                return false;
            }
            final Player ptarget = (Player) target;
            if (!TeamUtils.canAttack(p, ptarget)) {
                p.sendMessage("§9Você não pode usar isso em um aliado!");
                return false;
            }
            ptarget.sendMessage("§4Está de olho em seu sangue!");
            final long morreuquando = Utils.getDeathMillis(ptarget);

            new Channeling(p, 4, this, new Runnable() {

                @Override
                public void run() {
                    if (!Channeling.checkAlvo(morreuquando, ptarget, p)) {
                        return;
                    }
                    int dano = 5;
                    DamageManager.cura(p, dano);
                    p.sendMessage("§4Você roubou um pouco de sangue do alvo!");
                    DamageManager.causaDanoBruto(p, ptarget, dano, "Sugador de Sangue");
                    ptarget.sendMessage("§4" + p.getName() + " lhe sugou um pouco de sangue!");
                    for (Location loc : ParticleManager.buildLine(p.getLocation().add(0, 1, 0), ptarget.getLocation().add(0, 1, 0))) {
                        ParticleEffect.REDSTONE.display(new ParticleEffect.OrdinaryColor(0, 0, 0), loc, 32);
                    }
                    CustomEntityFirework.spawn(ptarget.getLocation(), FireworkEffect.builder().withColor(Color.RED).with(FireworkEffect.Type.BURST).build());

                }
            });

            return true;
        }
    };

    @Override
    public String getNome() {
        return "Sangue Suga";
    }

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Rouba 5 de vida do alvo"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }
}
