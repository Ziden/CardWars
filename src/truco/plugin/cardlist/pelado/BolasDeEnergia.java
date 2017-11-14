/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.pelado;

import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import truco.plugin.data.MetaShit;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class BolasDeEnergia {

    public static void addBola(Player p) {

        int tem = 0;
        if (p.hasMetadata("bolasdeenergia")) {
            tem = (int) MetaShit.getMetaObject("bolasdeenergia", p);

        }
        if (tem >= 5) {
            return;
        }
        tem++;
        MetaShit.setMetaObject("bolasdeenergia", p, tem);
        p.sendMessage("§eBolas de Energia " + (tem) + "/5");

    }
    public static void addBolas(Player p,int qtd) {

        int tem = 0;
        if (p.hasMetadata("bolasdeenergia")) {
            tem = (int) MetaShit.getMetaObject("bolasdeenergia", p);

        }
        if (tem >= 5) {
            return;
        }
        if(qtd+tem > 5){
            tem = 5;
        }else{
            tem+=qtd;
        }
        
        MetaShit.setMetaObject("bolasdeenergia", p, tem);
        p.sendMessage("§eBolas de Energia " + (tem) + "/5");

    }

    public static void removeBolas(Player p) {
        if (p.hasMetadata("bolasdeenergia")) {
            p.removeMetadata("bolasdeenergia", CardWarsPlugin._instance);

        }
        p.sendMessage("§eBolas de Energia 0/5");
    }

    public static void removeBola(Player p) {
        int tem = 0;
        if (p.hasMetadata("bolasdeenergia")) {
            tem = (int) MetaShit.getMetaObject("bolasdeenergia", p);

        }
        if (tem == 0) {
            return;
        }
        MetaShit.setMetaObject("bolasdeenergia", p, tem - 1);
        p.sendMessage("§eBolas de Energia " + (tem - 1) + "/5");

    }

    public static int getBolas(Player p) {
        if (p.hasMetadata("bolasdeenergia")) {
            return (int) MetaShit.getMetaObject("bolasdeenergia", p);

        }
        return 0;
    }

}
