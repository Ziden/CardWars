/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.ferro.epico;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.ChatUtils;
import truco.plugin.functions.game.Mana;
import truco.plugin.utils.efeitos.CustomEntityFirework;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MitouroEsquecido extends Carta {

    public static ArrayList<UUID> imortais = new ArrayList();

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }
    Skill s = new Skill(this, 60, 70) {

        @Override
        public String getName() {
            return "Monstro Imortal";
        }

        @Override
        public boolean onCast(Player p) {
            if (imortais.contains(p.getUniqueId())) {
                return false;
            }
            ChatUtils.tellAction(p, " está imortal por 5 segundos");
            p.sendMessage("§bVocê está imortal por 5 segundos!");
                CustomEntityFirework.spawn(p.getLocation(),FireworkEffect.builder().with(FireworkEffect.Type.CREEPER).withColor(Color.RED).build());
                
            imortais.add(p.getUniqueId());
            final UUID puuid = p.getUniqueId();
            Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    if (imortais.contains(puuid)) {
                        imortais.remove(puuid);
                        Player p = Bukkit.getPlayer(puuid);
                        if (p != null) {
                            ChatUtils.tellAction(p, " está mortal e fraco!");
                            p.sendMessage("§cSua imortalidade acabou!");
                            p.setHealth(1.0);
                            Mana.setMana(p, 1);
                             CustomEntityFirework.spawn(p.getLocation(),FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.PURPLE).build());
                
                        }
                    }
                }
            }, 5 * 20);
            return true;
        }
    };

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public String getNome() {
        return "Minotauro Esquecido";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Ao ativar você fica invuneravel por 5 segundos", "causa 2x dano", "ao ficar vuneravel fica com 1 de vida e 1 de mana"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.FERRO;
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
        if (imortais.contains(donoDaCarta.getUniqueId())) {
            ev.addDamageMult(2, "Minotauro Esquecido");
        }
    }

    @Override
    public void tomaDano(CustomDamageEvent ev) {
        if (imortais.contains(ev.getPlayerTomou().getUniqueId())) {
            ev.setCancelled("Minotauro Esquecido");
            if (ev.getPlayerBateu() != null) {
                ev.getPlayerBateu().sendMessage("§Seu alvo está invuneravel !");
            }

        }

    }
}
