package truco.plugin.cardlist.ouro.comum;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.LocUtils;
import truco.plugin.utils.TeamUtils;
import truco.plugin.utils.Utils;
import truco.plugin.cards.skills.skilltypes.Channeling;
import truco.plugin.damage.DamageManager;

/**
 *
 * @author Carlos André Feldmann Júnior
 *
 */
public class CuraLeve extends Carta {

    public Skill regen = new Skill(this, 6, 15) {

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
            Player aliado = null;

            if (!p.isSneaking()) {
                Entity alvo = LocUtils.getTarget(p,LocUtils.TargetType.ALIADO);
                if (alvo == null || alvo.getType() != EntityType.PLAYER || alvo.getLocation().distance(p.getLocation()) > 10) {
                    p.sendMessage(ChatColor.RED + "Voce precisa de um alvo !");
                    return false;
                }

                if (TeamUtils.canAttack(p, (Player) alvo)) {
                    p.sendMessage(ChatColor.RED + "Voce so pode fazer isto em aliados !");
                    return false;
                }
                aliado = (Player) alvo;
            } else {
                aliado = p;
            }
            final Player runnablealvo = aliado;
            final long morreuquando = Utils.getDeathMillis(runnablealvo);
            Runnable cura = new Runnable() {
                public void run() {
                    if (!Channeling.checkAlvo(morreuquando, runnablealvo, p)) {
                        return;
                    }

                    DamageManager.cura(runnablealvo, 7);

                    if (p != runnablealvo) {
                        runnablealvo.sendMessage(ChatColor.GREEN + p.getName() + " curou voce ");
                    } else {
                        p.sendMessage("§aVocê se curou!");
                    }
                    p.removeMetadata("Channeling", CardWarsPlugin._instance);
                }
            };
            new Channeling(p, 4, this, cura);
            return true;
        }

        @Override
        public String getName() {
            return "Toque de Jabu";
        }

    };

    @Override
    public Carta.Raridade getRaridade() {
        return Carta.Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Maos de Jabu";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Cura um aliado levemente",
            "se abaixando cura você mesmo"};
    }

    @Override
    public Carta.Armadura getArmadura() {
        return Carta.Armadura.OURO;
    }

    @Override
    public Skill getSkill() {
        return regen;
    }

}
