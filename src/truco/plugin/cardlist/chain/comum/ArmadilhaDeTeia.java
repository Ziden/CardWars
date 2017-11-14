/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.chain.comum;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
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
import truco.plugin.cards.skills.skilltypes.Trap;

public class ArmadilhaDeTeia extends Carta {

    Skill s = new Skill(this, 15, 35) {

        @Override
        public String getName() {
            return "Armadilha Pegajosa";
        }

        @Override
        public boolean onCast(Player p) {
            Block colocando = p.getTargetBlock((Set<Material>)null, 10);
            if (colocando == null || colocando.getType() == Material.AIR) {
                p.sendMessage(ChatColor.RED + "Selecione um bloco válido para colocar a bomba !");
                return false;
            }

            if (colocando.getRelative(BlockFace.UP).getType() == Material.AIR) {
                
            } else {
                p.sendMessage(ChatColor.RED + "O bloco em cima do bloco selecionado precisa estar vazio !");
                return false;
            
            }
            if(!Trap.canTrapBlock(colocando, p,this)){
                return false;
            }
            Trap t = new Trap(CardWarsPlugin.mainarena.getTeam(p.getUniqueId()), colocando,p.getUniqueId(),this) {

                @Override
                public void activate(Player p) {
                    p.getWorld().playEffect(p.getLocation(), Effect.EXPLOSION_LARGE, 1);
                    final HashSet<Block> webs = new HashSet<Block>();
                    Block b = p.getLocation().getBlock();
                    if(b.getType()==Material.AIR) {
                        b.setType(Material.WEB);
                        webs.add(b);
                    }
                    Block b2 = b.getRelative(BlockFace.UP);
                    if(b2.getType()==Material.AIR) {
                        b2.setType(Material.WEB);
                        webs.add(b2);
                    }
                    b2 = b.getRelative(BlockFace.NORTH);
                    if(b2.getType()==Material.AIR) {
                        b2.setType(Material.WEB);
                        webs.add(b2);
                    }
                    b2 = b.getRelative(BlockFace.SOUTH);
                    if(b2.getType()==Material.AIR) {
                        b2.setType(Material.WEB);
                        webs.add(b2);
                    }
                    b2 = b.getRelative(BlockFace.EAST);
                    if(b2.getType()==Material.AIR) {
                        b2.setType(Material.WEB);
                        webs.add(b2);
                    }
                    b2 = b.getRelative(BlockFace.WEST);
                    if(b2.getType()==Material.AIR) {
                        b2.setType(Material.WEB);
                        webs.add(b2);
                    }
                    Runnable r = new Runnable() {
                        public void run() {
                            for(Block b:webs) {
                                b.setType(Material.AIR);
                            }
                        }
                    };
                    Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, r, 10*20);
                }
                
            };
            p.sendMessage(ChatColor.GREEN+"Voce colocou a armadilha !");
            ChatUtils.tellAction(p, "colocou uma armadilha");
            return true;
        }
    };

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Teias Do Farao";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Coloca uma armadilha", "que prende inimigos"};
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
