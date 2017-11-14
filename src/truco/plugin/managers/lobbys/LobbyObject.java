/*

 */
package truco.plugin.managers.lobbys;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import truco.plugin.CardWarsPlugin;
import truco.plugin.menus.MenuUtils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class LobbyObject {

    private String nome;
    private int max;
    private int players;

    public LobbyObject(String nome, int max, int players) {
        this.nome = nome;
        this.max = max;
        this.players = players;
    }

    public int getMax() {
        return max;
    }

    public String getNome() {
        return nome;
    }

    public int getPlayers() {
        return players;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public ItemStack getLobbyItem() {
        Material m;
        if (nome.equals(CardWarsPlugin.serverName)) {
            m = Material.LAPIS_BLOCK;
        } else if (max == players) {
            m = Material.REDSTONE_BLOCK;
        } else {
            m = Material.EMERALD_BLOCK;
        }
        List<String> lore = new ArrayList();
        lore.add("");
        lore.add("§cPlayers: §e" + players + "/" + max);
        lore.add("");
        lore.add("§bClique aqui para entrar no lobby");
        ItemStack is = MenuUtils.getMenuItem(m, "§a" + nome.split("_")[1], lore);
        return MenuUtils.getItemIlusorio(is);
    }

}
