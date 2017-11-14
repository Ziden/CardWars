/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.pelado.raro;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import truco.plugin.cards.CC;
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

public class GolpeQuadruplo extends Carta {

    Skill s = new Skill(this, 3, 40) {

        @Override
        public String getName() {
            return "Golpe Quadruplo";
        }

        @Override
        public boolean onCast(Player p) {
            if (BolasDeEnergia.getBolas(p) == 0) {
                p.sendMessage(ChatColor.RED + "Voce precisa de 1 Bola de Energia !");
                return false;
            }
            if (GolpeMultiplo.getGolpeMultiplo(p).passo != GolpeMultiplo.GPasso.ACERTEITRIPLO) {
                p.sendMessage(ChatColor.RED + "Voce so pode usar isto logo depois de um golpe triplo !");
                return false;
            }
            ChatUtils.tellAction(p, "deu um golpe quadruplo");
            Player alvo = Bukkit.getPlayer(GolpeMultiplo.getGolpeMultiplo(p).alvo);

            if (alvo != null && !MakeVanish.isVanished(alvo)) {
                p.teleport(alvo);
                CC.mineknock(p, alvo, 4);
                DamageManager.damage(12, p, alvo, CustomDamageEvent.CausaDano.SKILL_ATAQUE, "Golpe Quadruplo");

            } else {

                p.sendMessage(ChatColor.RED + "Seu alvo se foi.");
            }

            GolpeMultiplo.clear(p);
            return false;
        }
    };

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Punhos do Dragao";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Causa 4 socos no inimigo", "So pode ser usado dps do Golpe Triplo", "Usa 1 bola de energia"};
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
