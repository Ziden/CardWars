/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.utils.mobapi.mobs;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.server.v1_8_R2.Entity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;

import org.bukkit.event.entity.CreatureSpawnEvent;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeBlaze;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeCaveSpider;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeCreeper;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeEnderman;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeMagmaCube;
import truco.plugin.utils.mobapi.mobs.monsters.FreezePigZombie;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeSilverfish;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeSkeleton;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeSlime;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeSpider;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeWitch;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeZombie;
import truco.plugin.utils.mobapi.mobs.passives.FreezeBat;
import truco.plugin.utils.mobapi.mobs.passives.FreezeChicken;
import truco.plugin.utils.mobapi.mobs.passives.FreezeCow;
import truco.plugin.utils.mobapi.mobs.passives.FreezeHorse;
import truco.plugin.utils.mobapi.mobs.passives.FreezeIronGolem;
import truco.plugin.utils.mobapi.mobs.passives.FreezeMushroomCow;
import truco.plugin.utils.mobapi.mobs.passives.FreezeOcelot;
import truco.plugin.utils.mobapi.mobs.passives.FreezePig;
import truco.plugin.utils.mobapi.mobs.passives.FreezeSheep;
import truco.plugin.utils.mobapi.mobs.passives.FreezeSnowman;
import truco.plugin.utils.mobapi.mobs.passives.FreezeVillager;
import truco.plugin.utils.mobapi.mobs.passives.FreezeWolf;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class EntityTypes {

    //NAME("Entity name", Entity ID, yourcustomclass.class);
//NAME("Entity name", Entity ID, yourcustomclass.class);
    //You can add as many as you want.
    public static enum Tipos {

        FREEZE_ZOMBIE("Zombie", 54, FreezeZombie.class),
        FREEZE_SKELETON("Skeleton", 51, FreezeSkeleton.class),
        FREEZE_WITCH("Witch", 66, FreezeWitch.class),
        FREEZE_PIGZOMBIE("PigZombie", 57, FreezePigZombie.class),
        FREEZE_SLIME("Slime", 55, FreezeSlime.class),
        FREEZE_SILVERFISH("Silverfish", 60, FreezeSilverfish.class),
        FREEZE_SPIDER("Spider", 52, FreezeSpider.class),
        FREEZE_CAVESPIDER("CaveSpider", 59, FreezeCaveSpider.class),
        FREEZE_CREEPER("Creeper", 50, FreezeCreeper.class),
        FREEZE_ENDERMAN("Enderman", 58, FreezeEnderman.class),
        FREEZE_MAGMA("MagmaCube", 62, FreezeMagmaCube.class),
        FREEZE_BLAZE("Blaze", 61, FreezeBlaze.class),
        //
        FREEZE_PIG("Pig", 90, FreezePig.class),
        FREEZE_COW("Cow", 92, FreezeCow.class),
        FREEZE_SHEEP("Sheep", 91, FreezeSheep.class),
        FREEZE_BAT("Bat", 65, FreezeBat.class),
        FREEZE_CHICKEN("Chicken", 93, FreezeChicken.class),
        FREEZE_HORSE("Horse", 100, FreezeHorse.class),
        FREEZE_IRONGOLEM("IronGolem", 99, FreezeIronGolem.class),
        FREEZE_MUSHROOMCOW("MushroomCow", 96, FreezeMushroomCow.class),
        FREEZE_OCELOT("Ocelot", 98, FreezeOcelot.class),
        FREEZE_SNOWMAN("Snowman", 97, FreezeSnowman.class),
        FREEZE_VILLAGER("Villager", 120, FreezeVillager.class),
        FREEZE_WOLF("Wolf", 95, FreezeWolf.class),;
        Class<? extends Entity> custom;
        String name;
        int id;

        private Tipos(String name, int id, Class<? extends Entity> custom) {
            this.name = name;
            this.custom = custom;
            this.id = id;
        }

    }

    public EntityTypes() {
        for(Tipos t : Tipos.values()){
            addToMaps(t.custom, t.name,t.id);
        }
    }

    public static void spawnEntity(Entity entity, Location loc) {
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);

        ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    public static Object getPrivateField(String fieldName, Class clazz, Object object) {
        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }

    private static void addToMaps(Class clazz, String name, int id) {
        //getPrivateField is the method from above.
        //Remove the lines with // in front of them if you want to override default entities (You'd have to remove the default entity from the map first though).
        //((Map)getPrivateField("c", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(name, clazz);
        ((Map) getPrivateField("d", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).put(clazz, name);
        //((Map)getPrivateField("e", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(Integer.valueOf(id), clazz);
        ((Map) getPrivateField("f", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).put(clazz, Integer.valueOf(id));
        //((Map)getPrivateField("g", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(name, Integer.valueOf(id));
    }
}
