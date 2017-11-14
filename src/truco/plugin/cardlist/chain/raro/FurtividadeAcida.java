package truco.plugin.cardlist.chain.raro;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;
import truco.plugin.itens.Items;
import truco.plugin.utils.LocUtils;
import truco.plugin.functions.MakeVanish;

public class FurtividadeAcida extends Carta {

    @Override
    public Raridade getRaridade() {
        return Carta.Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Furtividade Acida";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 1 Bomba de Fumaca", "+3 dano com espada de madeira ao sair do invisivel"};
    }

    @Override
    public Armadura getArmadura() {
        return Carta.Armadura.CHAIN;
    }

    public ItemStack[] getItems() {
        return new ItemStack[]{Items.bomba.geraItem(1)};
    }

    @Override
    public void causaDano(Player donoDaCarta, CustomDamageEvent ev) {
        if (ev.getCause() != CausaDano.ATAQUE) {
            return;
        }
        if (donoDaCarta.getItemInHand().getType() == Material.WOOD_SWORD) {
            if (MakeVanish.isVanished(donoDaCarta) && donoDaCarta.hasMetadata("bombadefumaca")) {
                double angle = LocUtils.getAngle(donoDaCarta.getLocation().getDirection(), ev.getTomou().getLocation().getDirection());
                if (angle < 70) {
                    MakeVanish.makeVisible(donoDaCarta);
                    donoDaCarta.sendMessage(ChatColor.RED + "Voce deu um golpe ninja !");
                    ev.addDamageMult(2, "Golpe Ninja");
                    ev.getTomou().getWorld().playEffect(ev.getTomou().getLocation(), Effect.CRIT, 10);

                    ev.getTomou().getWorld().playEffect(ev.getBateu().getLocation(), Effect.SMOKE, 1);
                }
            }
        }
    }
}
