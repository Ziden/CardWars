package truco.plugin.cards;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.bukkit.Bukkit;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.CardWarsPlugin;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CardLoader {

    public static void load() {
        File f = new File(CardWarsPlugin.class.getProtectionDomain().getCodeSource().getLocation().getFile().replaceAll("%20", " ")).getAbsoluteFile();
        JarFile jf;
        try {
            jf = new JarFile(f);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        Enumeration en = jf.entries();
        List<Carta> addons = new ArrayList<Carta>();
        int carrego = 0;
        while (en.hasMoreElements()) {

            Object entry = en.nextElement();
            Object addon = getCarta(entry);

            if (addon != null) {
                if (addon instanceof Carta) {
                    Carta h = (Carta) addon;
                    ControleCartas.addCart(h);
                    carrego++;
                }
            }
        }
        CardWarsPlugin.log.info("CARREGOU "+carrego+" CARTAS! ---- !");
    }

    private static Object getCarta(Object ne) {
        JarEntry entry = (JarEntry) ne;
        String name = entry.getName();
        name = name.replaceAll("/", ".");
        if (!name.endsWith(".class")) {
            return null;
        }
        name = name.replace(".class", "");
        if (!name.contains("cardlist")) {
            return null;
        }
        Class c;
        try {
            c = Class.forName(name);
        } catch (ClassNotFoundException ex) {
            CardWarsPlugin.log.info("ERRO NA CLASSE :" + name);
            ex.printStackTrace();
            return null;
        }
        if (Carta.class.isAssignableFrom(c)) {
            Carta w = null;
            try {

                w = (Carta) c.newInstance();
                return w;

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        return null;
    }
}
