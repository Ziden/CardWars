
package truco.plugin.cardlist.dimaOUouro.raro;



import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import truco.plugin.cards.Carta;
import truco.plugin.cards.StatusEffect;
import truco.plugin.events.CustomDamageEvent;

public class GolpeDeContusao extends Carta{


    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }

    @Override
    public String getNome() {
        return "Golpe de Contusão";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"Suas maçadas epicas causam silence"};
    }
    
    @Override
    public Armadura getArmadura() {
        return Armadura.OURO_DIMA;
    }
 

    @Override
    public void acertaGolpeEpico(CustomDamageEvent ev, Player bateu) {
        if(bateu.getItemInHand().getType()==Material.WOOD_SPADE ||bateu.getItemInHand().getType()==Material.STONE_SPADE ||bateu.getItemInHand().getType()==Material.GOLD_SPADE||bateu.getItemInHand().getType()==Material.DIAMOND_SPADE ) {
            if(ev.getPlayerTomou()!=null) {
                StatusEffect.addStatusEffect(ev.getPlayerTomou(), StatusEffect.StatusMod.SILENCE, 3);
            }
        }
    }

}
