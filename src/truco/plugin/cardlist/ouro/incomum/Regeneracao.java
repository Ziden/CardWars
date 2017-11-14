package truco.plugin.cardlist.ouro.incomum;


import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.cards.Carta;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.TeamUtils;
import truco.plugin.utils.efeitos.CustomEntityFirework;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class Regeneracao extends Carta {

    public Skill regen = new Skill(this,20,10) {

            @Override
            public boolean onCast(Player p) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));
                CustomEntityFirework.spawn(p.getLocation(),FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.RED).build());
                
                for (Entity t : p.getNearbyEntities(5, 5, 5)) {
                    if (t instanceof Player) {
                        Player p2 = (Player) t;
                        if (!TeamUtils.canAttack(p, p2)) {
                            p2.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));
                            p2.sendMessage("§d§lO jogador " + p.getName() + " lhe deu um buff de regeneração!");
                            
                        }
                    }

                }

                return true;
            }


	    @Override
	    public String getName() {
	    return "Regeneração Massiva";
	    }

        };
    
    @Override
    public Carta.Raridade getRaridade() {
        return Carta.Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Regeneração Aliada";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Aliados perto de você ganham regeneracao"};
    }

    @Override
    public Carta.Armadura getArmadura() {
        return Carta.Armadura.OURO;
    }

    @Override
    public Skill getSkill() {
        return regen;
    }

}
