/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cards.skills.skilllist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.ChatUtils;
import truco.plugin.data.MetaShit;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class Refletor extends Skill {

    String nome;

    public Refletor(Carta vinculada, int cud, int mana, String nome) {
        super(vinculada, cud, mana);
        this.nome = nome;
    }

    @Override
    public String getName() {
        return "Refletor De " + nome;
    }

    @Override
    public boolean onCast(final Player p) {
        MetaShit.setMetaObject("Refletor2x"+nome, p, true);
        ChatUtils.sendMessage(p, "§cVocê ativou o refletor !");
        Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                if (p != null) {
                    if (p.hasMetadata("Refletor2x"+nome)) {
                        p.removeMetadata("Refletor2x"+nome, CardWarsPlugin._instance);
                        ChatUtils.sendMessage(p, "§aO tempo do seu refletor acabou!");
                    }
                }
            }
        }, 20 * 3);
        return true;
    }

}
