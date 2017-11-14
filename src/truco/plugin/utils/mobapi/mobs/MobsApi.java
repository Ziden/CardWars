/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.utils.mobapi.mobs;


import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.craftbukkit.v1_8_R2.CraftServer;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import truco.plugin.managers.ClickAction;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MobsApi {
    public static HashMap<UUID, ClickAction> bixos = new HashMap();

    public static Entity summonCustomEntity(net.minecraft.server.v1_8_R2.Entity en, Location l) {

        EntityTypes.spawnEntity(en, l);
        Entity t = CraftEntity.getEntity((CraftServer) Bukkit.getServer(), en);

        return t;
    }
}
