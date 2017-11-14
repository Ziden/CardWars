/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */

package truco.plugin.cardlist.dimaOUouro.incomum;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import truco.plugin.cards.Carta;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.ItemUtils;
/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PaAbencoada extends Carta{

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }


    @Override
    public String getNome() {
        return "Pa Abençoada";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"+1 de dano com pás"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.OURO_DIMA;
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
         if(ev.getCause()!=CustomDamageEvent.CausaDano.ATAQUE){
            return;
        }
        if(ItemUtils.isSpade(donoDaCarta.getItemInHand().getType())){
            ev.addDamage(1);
        }
    }
  

   




}
