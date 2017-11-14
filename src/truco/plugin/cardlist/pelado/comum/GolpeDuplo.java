/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.pelado.comum;

import truco.plugin.cardlist.pelado.GolpeMultiplo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import truco.plugin.cards.CC;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.cardlist.pelado.BolasDeEnergia;
import truco.plugin.damage.DamageManager;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.ItemUtils;
import truco.plugin.data.MetaShit;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDeathEvent;
import truco.plugin.utils.efeitos.CustomEntityFirework;

public class GolpeDuplo extends Carta {

    Skill s = new Skill(this, 3, 10) {

        @Override
        public String getName() {
            return "Golpe Duplo";
        }

        @Override
        public boolean onCast(Player p) {
            if (BolasDeEnergia.getBolas(p) == 0) {
                p.sendMessage(ChatColor.RED + "Voce precisa de 1 Bola de Energia !");
                return false;
            }
            if (GolpeMultiplo.getGolpeMultiplo(p).passo == GolpeMultiplo.GPasso.NULL) {
                ChatUtils.sendMessage(p, "§a§lSeu proximo soco sera um golpe duplo I!");
                GolpeMultiplo.getGolpeMultiplo(p).setPasso(GolpeMultiplo.GPasso.ATIVEIDUPLO);
                BolasDeEnergia.removeBola(p);
                return true;
            } else {
                ChatUtils.sendMessage(p, "§cVocê já ativou está habilidade, use primeiro para ativar denovo!");
                return false;
            }
        }
    };

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Meteoro Duplo";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Causa 2 socos no inimigo", "e joga ele para longe", "Usa 1 bola de energia"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.PELADO;
    }

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public void causaDano(final Player donoDaCarta, CustomDamageEvent ev) {
        if (ev.getCause() != CustomDamageEvent.CausaDano.ATAQUE) {
            return;
        }
        if (ItemUtils.isArm(donoDaCarta.getItemInHand()) || ItemUtils.isClaw(donoDaCarta.getItemInHand())) {
            if (ev.getPlayerTomou() == null) {
                return;
            }
            if (GolpeMultiplo.getGolpeMultiplo(donoDaCarta).passo == GolpeMultiplo.GPasso.ATIVEIDUPLO) {
                ev.addDamageMult(1.5, "Golpe Duplo");
                ev.addKnockBack("Golpe Duplo", 2.3);
                donoDaCarta.sendMessage(ChatColor.GREEN + "Voce deu 2 socos fortes em seu inimigo");

                CustomEntityFirework.spawn(ev.getTomou().getLocation(), FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.YELLOW).build());

                GolpeMultiplo.getGolpeMultiplo(donoDaCarta).setPasso(GolpeMultiplo.GPasso.ACERTEIDUPLO);
                GolpeMultiplo.getGolpeMultiplo(donoDaCarta).setAlvo(ev.getTomou().getUniqueId());
                ChatUtils.tellAction(donoDaCarta, "deu um golpe duplo");
                Runnable r = new Runnable() {
                    public void run() {
                        if (donoDaCarta != null) {
                            if (GolpeMultiplo.getGolpeMultiplo(donoDaCarta).passo == GolpeMultiplo.GPasso.ACERTEIDUPLO) {
                                GolpeMultiplo.clear(donoDaCarta);
                            }
                        }
                    }
                };
                Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, r, 20 * 4);
            }
        }
    }

    @Override
    public void playerDeath(CustomDeathEvent ev) {
        if (GolpeMultiplo.getGolpeMultiplo(ev.getPlayer()).passo != GolpeMultiplo.GPasso.NULL) {
            GolpeMultiplo.clear(ev.getPlayer());
        }
    }

}
