/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.dima.comum;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.LocUtils;
import truco.plugin.data.MetaShit;
import truco.plugin.utils.TeamUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ProtetorDeRhodes extends Carta {

    public static void paraDeProteger(Player protetor, Player protegido) {
        protetor.removeMetadata("Protegendo", CardWarsPlugin._instance);
        protegido.removeMetadata("ProtegidoPor", CardWarsPlugin._instance);
        protegido.sendMessage(ChatColor.RED+protetor.getName()+" parou de te proteger.");
        protetor.sendMessage(ChatColor.RED+"Voce parou de proteger "+protegido.getName());
    }                
    
    Skill s = new Skill(this, 20, 30) {

        @Override
        public String getName() {
           return "Proteger Aliado";
        }

        @Override
        public boolean onCast(final Player p) {
            Entity alvo = LocUtils.getTarget(p,LocUtils.TargetType.ALIADO);
            if(alvo==null || alvo.getType()!=EntityType.PLAYER || alvo.getLocation().distance(p.getLocation())>6) {
                p.sendMessage(ChatColor.RED+"Voce precisa de um alvo !");
                return false;
            }
            final Player aliado = (Player)alvo;
            if(TeamUtils.canAttack(p, (Player)alvo)) {
                p.sendMessage(ChatColor.RED+"Voce so pode fazer isto em aliados !");
                return false;
            }
            p.teleport(alvo.getLocation());
            
            p.sendMessage("§aVoce esta protegendo "+aliado.getName()+", fique bem perto dele !");
            aliado.sendMessage(p.getName()+"Esta te protegendo, fique perto dele !");
            
            MetaShit.setMetaObject("ProtegidoPor", aliado, p.getUniqueId());
            MetaShit.setMetaObject("Protegendo", p, aliado.getUniqueId());
            
            Runnable r = new Runnable() {
                public void run() {
                    if(aliado.hasMetadata("ProtegidoPor")){
                        p.removeMetadata("Protegendo", CardWarsPlugin._instance);
                        aliado.removeMetadata("ProtegidoPor", CardWarsPlugin._instance);
                        aliado.sendMessage(ChatColor.RED+p.getName()+" parou de te proteger.");
                        p.sendMessage(ChatColor.RED+"Voce parou de proteger "+aliado.getName());
                    }
                    
                }
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, r, 20*10);
            return true;
        }
            
    };
    
    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Protetor de Rhodes";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Protege um aliado por 10 segundos"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.DIMA;
    }
   
    @Override
    public Skill getSkill() {
        return s;
    }
    
}
