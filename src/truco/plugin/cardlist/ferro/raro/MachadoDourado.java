/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.ferro.raro;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.cards.EventosCartas;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.ChatUtils;
import truco.plugin.cards.skills.skilltypes.TimedHit;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.efeitos.CustomEntityFirework;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MachadoDourado extends Carta {

    Skill skill = new Skill(this, 10, 40) {
        @Override
        public String getName() {
            return "Machadada Suprema";
        }

        @Override
        public boolean onCast(Player p) {
            return TimedHit.hit.fazTimedHit(p, getName(), 20, 4);

        }
    };

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Lenhador de Rhodes";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 machado de ouro", "Golpe forte com machados de ouro"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.FERRO;
    }

    @Override
    public Skill getSkill() {
        return skill;
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[]{new ItemStack(Material.GOLD_AXE)};
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
        if (ev.getTomou() == null) {
            return;
        }
        if (ev.getCause() != CustomDamageEvent.CausaDano.ATAQUE) {
            return;
        }
        if (TimedHit.hit.acertaTimed(donoDaCarta, getSkill().getName())) {
            if (donoDaCarta.getItemInHand() != null && donoDaCarta.getItemInHand().getType() == Material.GOLD_AXE) {
                donoDaCarta.sendMessage(ChatColor.GREEN + "Voce acertou uma machadada suprema!");
                if (ev.getTomou().getType() == EntityType.PLAYER) {
                    ((Player) ev.getTomou()).sendMessage(ChatColor.RED + "Tomou uma machadada suprema");
                }
                EventosCartas.acertaGolpeEpico(ev, donoDaCarta);
                ev.addDamage(16 + ControleCartas.rnd.nextInt(6));
                CustomEntityFirework.spawn(ev.getTomou().getLocation(), FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.YELLOW).build());

            } else {
                ChatUtils.sendMessage(donoDaCarta, "§6Você precisava estar com um machado de ouro!");
            }
        }
    }

}
