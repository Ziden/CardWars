/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.comum;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent.CausaDano;
import truco.plugin.utils.TeamUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PoderAnciao extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public Skill getSkill() {
        return skill;
    }

    Skill skill = new Skill(this, 13, 10) {

        @Override
        public String getName() {
            return "Raios Saltantes";
        }

        @Override
        public boolean onCast(Player p) {
        new RaioLocao(p);
            return true;
        }
            
            
};
    @Override
    public String getNome() {
        return "Poder Anciao";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Cai raio em um inimigo proximo", "caso acerte pula para o proximo", "pode repetir até 5x"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }

    private class RaioLocao {

        Player player;

        List<UUID> atingidos;
        int taskId;

        public RaioLocao(final Player player) {
            List<UUID> atingidos = new ArrayList<UUID>();
            Player atual = player;
            for (int x = 1; x < 6; x++) {
                final int y = x;
                List<Entity> nearby = atual.getNearbyEntities(5, 5, 5);
                int tem = 0;
                ENTIDADES:
                for (Entity ent : nearby) {
                    if (ent instanceof Player) {
                        final Player atingido = (Player) ent;
                        if (TeamUtils.canAttack(player, atingido) && !atingidos.contains(atingido.getUniqueId()) && atingido != player) {
                            tem++;
                            Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                                @Override
                                public void run() {
                                    if (atingido != null) {
                                        atingido.sendMessage("§b* você foi atingido por um raio saltante de " + player.getName() + " *");
                                        atingido.getWorld().strikeLightningEffect(atingido.getLocation());
                                        DamageManager.damage(7-y,player,atingido, CausaDano.MAGIA_RAIO,"Raio Saltitante");
                                    }
                                }
                            }, 10 * x);
                            atingidos.add(atingido.getUniqueId());
                            atual = atingido;
                            break;
                        }
                    }
                }
                if (tem == 0) {
                    break;
                }
            }
        }

    }
}
