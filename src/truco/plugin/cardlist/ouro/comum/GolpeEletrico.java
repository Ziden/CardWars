package truco.plugin.cardlist.ouro.comum;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;

public class GolpeEletrico extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Golpe Eletrico";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Seus golpes epicos com pas", "causam dano de raio"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.OURO;
    }

    @Override
    public void acertaGolpeEpico(CustomDamageEvent ev, Player bateu) {
        if (bateu.getItemInHand().getType() == Material.WOOD_SPADE || bateu.getItemInHand().getType() == Material.STONE_SPADE || bateu.getItemInHand().getType() == Material.GOLD_SPADE || bateu.getItemInHand().getType() == Material.DIAMOND_SPADE) {
            if (ev.getTomou().getType() == EntityType.PLAYER) {
                ev.setCancelled("Golpe Eletrico");
                DamageManager.damage(ev.getFinalDamage()*1.10, bateu, ev.getTomou(), CustomDamageEvent.CausaDano.MAGIA_RAIO, "Golpe Eletrico");
            }
        }
    }

}
