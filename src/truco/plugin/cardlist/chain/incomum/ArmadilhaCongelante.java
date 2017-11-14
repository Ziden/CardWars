/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.chain.incomum;

import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.ChatUtils;
import truco.plugin.cards.StatusEffect;
import truco.plugin.cards.skills.skilltypes.Trap;

public class ArmadilhaCongelante extends Carta {

    Skill s = new Skill(this, 15, 35) {

        @Override
        public String getName() {
            return "Armadilha de Gelo";
        }

        @Override
        public boolean onCast(Player p) {
           Block colocando = p.getTargetBlock((Set<Material>)null, 10);
            if (colocando == null || colocando.getType() == Material.AIR) {
                p.sendMessage(ChatColor.RED + "Selecione um bloco válido para colocar a armadilha !");
                return false;
            }

            if (colocando.getRelative(BlockFace.UP).getType() == Material.AIR) {

            } else {
                p.sendMessage(ChatColor.RED + "O bloco em cima do bloco selecionado precisa estar vazio !");
                return false;
            }
            if (!Trap.canTrapBlock(colocando, p, this)) {
                return false;
            }
            Trap t = new Trap(CardWarsPlugin.mainarena.getTeam(p.getUniqueId()), colocando, p.getUniqueId(), this) {

                @Override
                public void activate(Player p) {
                    p.getWorld().playEffect(p.getLocation(), Effect.EXPLOSION_LARGE, 1);
                    StatusEffect.addStatusEffect(p, StatusEffect.StatusMod.CONGELADO, 6);
                    p.sendMessage(ChatColor.RED + "Voce foi pego numa armadilha congelante !");
                    //  Bukkit.getScheduler().scheduleSyncDelayedTask(Main._instance, r, 10*20);
                }

            };
            p.sendMessage(ChatColor.GREEN + "Voce colocou a armadilha !");
            ChatUtils.tellAction(p, "colocou uma armadilha");
            return true;
        }
    };

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Armadilha Gelida";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Coloca uma armadilha", "que congela inimigos"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.CHAIN;
    }

    @Override
    public Skill getSkill() {
        return s;
    }
}
