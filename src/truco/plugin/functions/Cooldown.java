/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.functions;

import org.bukkit.metadata.Metadatable;
import truco.plugin.CardWarsPlugin;
import truco.plugin.data.MetaShit;

/**
 *
 * @author usuario
 */
public class Cooldown {

    public static void addCoolDown(Metadatable p, String cd, long millis) {
        MetaShit.setMetaObject(cd, p, System.currentTimeMillis() + millis);
    }

    public static boolean isCooldown(Metadatable p, String cd) {
        if (!p.hasMetadata(cd)) {
            return false;
        }
        long ccd = (long) MetaShit.getMetaObject(cd, p);
        if (System.currentTimeMillis() < ccd) {
            return true;
        }
        p.removeMetadata(cd, CardWarsPlugin._instance);
        return false;
    }

}
