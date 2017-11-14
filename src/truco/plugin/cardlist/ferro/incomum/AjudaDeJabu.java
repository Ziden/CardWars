/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.ferro.incomum;

import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.itens.Items;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class AjudaDeJabu extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Ajuda de Jabu";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"+ 2 bandagens"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.FERRO;
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[]{Items.bandagem.geraItem(2)};
    }

}
