/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.epico;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.TeamUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class TeleporteEletrico extends Carta {
    
    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }
    Skill s = new Skill(this, 15, 15) {
        
        @Override
        public String getName() {
            return "Teleporte Eletrico";
        }
        
        @Override
        public boolean onCast(Player p) {
            
            return blink(p);
        }
    };
    
    public static void blinka(Player p, Location onde) {
        onde.setPitch(p.getLocation().getPitch());
        onde.setYaw(p.getLocation().getYaw());
        List<Entity> atacados = new ArrayList();
        for (Entity ent : p.getNearbyEntities(5, 5, 5)) {
            if (ent instanceof Player) {
                Player alvo = (Player) ent;
                
                if (TeamUtils.canAttack(p, alvo)) {
                    atacados.add(ent);
                    DamageManager.damage(7, p, alvo, CustomDamageEvent.CausaDano.MAGIA_RAIO, "Teleporte Eletrico");
                }
            }
        }
        p.getWorld().strikeLightningEffect(p.getLocation());
        p.teleport(onde);
        
        p.getWorld().strikeLightningEffect(p.getLocation());
        for (Entity ent : p.getNearbyEntities(5, 5, 5)) {
            if (ent instanceof Player) {
                Player alvo = (Player) ent;
                if (TeamUtils.canAttack(p, alvo) && !atacados.contains(ent)) {
                    DamageManager.damage(7, p, alvo, CustomDamageEvent.CausaDano.MAGIA_RAIO, "Teleporte Eletrico");
                    
                }
            }
        }
        
    }
    
    @Override
    public Skill getSkill() {
        return s;
    }
    
    public static boolean blink(Player p) {
        
        Block b = p.getTargetBlock((Set<Material>) null, 13);
        if (b == null) {
            return false;
        }
        Block up = b.getRelative(BlockFace.UP);
        if (up.getType() == Material.AIR && up.getRelative(BlockFace.UP).getType() == Material.AIR) {
            blinka(p, up.getLocation());
            return true;
        } else if (b.getType() == Material.AIR && b.getRelative(BlockFace.UP).getType() == Material.AIR) {
            blinka(p, b.getLocation());
            return true;
        }
        return false;
    }
    
    @Override
    public String getNome() {
        return "Sabedoria de Nustoph";
    }
    
    @Override
    public String[] getDesc() {
        return new String[]{"Teleporta em direcao do mouse", "causa dano eletrico a inimigos proximos", "na ida e na chegada"};
    }
    
    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }
}
