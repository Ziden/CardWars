/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.pelado.incomum;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cardlist.pelado.BolasDeEnergia;
import truco.plugin.cardlist.pelado.GolpeMultiplo;
import truco.plugin.damage.DamageManager;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.MakeVanish;
import truco.plugin.data.MetaShit;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.TeamUtils;
import truco.plugin.utils.Utils;
import truco.plugin.utils.efeitos.CustomEntityFirework;

public class GolpeTriplo extends Carta {

    Skill s = new Skill(this, 3, 20) {

        @Override
        public String getName() {
            return "Golpe Triplo";
        }

        @Override
        public boolean onCast(Player p) {
            if (BolasDeEnergia.getBolas(p) == 0) {
                p.sendMessage(ChatColor.RED + "Voce precisa de 1 Bola de Energia !");
                return false;
            }
            if (GolpeMultiplo.getGolpeMultiplo(p).passo != GolpeMultiplo.GPasso.ACERTEIDUPLO) {
                p.sendMessage(ChatColor.RED + "Voce so pode usar isto logo depois de um golpe duplo !");
                return false;
            }

            ChatUtils.tellAction(p, "deu um golpe triplo");
            Player alvo = Bukkit.getPlayer(GolpeMultiplo.getGolpeMultiplo(p).alvo);

            if (alvo != null && !MakeVanish.isVanished(alvo) && !TeamUtils.isSpec(alvo)) {
                p.teleport(alvo);
                alvo.setVelocity(new Vector(0, 0.7, 0));
                GolpeMultiplo.getGolpeMultiplo(p).setPasso(GolpeMultiplo.GPasso.ACERTEITRIPLO);
                DamageManager.damage(6, p, alvo, CustomDamageEvent.CausaDano.SKILL_ATAQUE, "Golpe Triplo");
                CustomEntityFirework.spawn(alvo.getLocation(), FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.ORANGE).build());

            } else {
                GolpeMultiplo.clear(p);
                p.sendMessage(ChatColor.RED + "Seu alvo se foi.");
            }
            return false;
        }
    };

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Canhao de Socos Triplo";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Causa 3 socos no inimigo", "So pode ser usado dps do Golpe Duplo", "Usa 1 bola de energia"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO;
    }

    @Override
    public Skill getSkill() {
        return s;
    }
}
