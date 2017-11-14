package truco.plugin.damage;

import java.lang.reflect.Field;
import net.minecraft.server.v1_8_R2.DataWatcher;
import net.minecraft.server.v1_8_R2.Packet;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R2.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R2.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import truco.plugin.CardWarsPlugin;

public class DamageIndicatorManager {

    private Plugin plugin;

    public DamageIndicatorManager(Plugin plugin) {
        this.plugin = plugin;
    }

    boolean damageindi = false;

    public void showDamageIndicator(LivingEntity entity, String tosend) {
        if (!damageindi) {
            return;
        }
        double x = CardWarsPlugin.random.nextDouble();
        double z = CardWarsPlugin.random.nextDouble();
        if (x < 0.5) {
            x = 0.5;
        }
        if (z < 0.5) {
            z = 0.5;
        }
        if (CardWarsPlugin.random.nextBoolean()) {
            x *= -1;
        }
        if (CardWarsPlugin.random.nextBoolean()) {
            x *= -z;
        }

        sendDamageIndicator(entity, entity.getLocation().add(x, 3.5, z), tosend, true, 20);
    }

    public void sendDamageIndicator(Entity nearTo, Location loc, String name, boolean move, int hideAfterTicks) {
        final int passengerID = next();
        final int vehicleID = next();
        try {
            Packet armorStandPacket = spawnArmorStandPacket(name, loc, passengerID);

            final Location center = nearTo.getLocation();
            sendPackets(center, new Packet[]{armorStandPacket});

            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                public void run() {
                    Packet destroyPacket = DamageIndicatorManager.destoryPacket(new int[]{vehicleID, passengerID});
                    DamageIndicatorManager.sendPackets(center, new Packet[]{destroyPacket});
                }
            }, hideAfterTicks);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.plugin.getLogger().severe("Unable to send damage indicators, please report this error!");
        }
    }

    private static Packet destoryPacket(int... entityIDs) {
        Packet destroyPacket = new PacketPlayOutEntityDestroy(entityIDs);

        return destroyPacket;
    }

    private static Packet spawnArmorStandPacket(String customName, Location loc, int entityID)
            throws Exception {
        PacketPlayOutSpawnEntityLiving armorStandPacket = new PacketPlayOutSpawnEntityLiving();

        setPrivateField(armorStandPacket, "a", Integer.valueOf(entityID));

        setPrivateField(armorStandPacket, "b", Integer.valueOf(30));

        setPrivateField(armorStandPacket, "c", Integer.valueOf((int) Math.floor(loc.getX() * 32.0D)));
        setPrivateField(armorStandPacket, "d", Integer.valueOf((int) Math.floor((loc.getY() + -2.1D) * 32.0D)));
        setPrivateField(armorStandPacket, "e", Integer.valueOf((int) Math.floor(loc.getZ() * 32.0D)));

        DataWatcher armorStandDataWatcher = new DataWatcher(null);
        armorStandDataWatcher.a(0, Byte.valueOf((byte) 32));
        armorStandDataWatcher.a(2, customName);
        armorStandDataWatcher.a(3, Byte.valueOf((byte) 1));
        armorStandDataWatcher.a(10, Byte.valueOf((byte) 15));
        setPrivateField(armorStandPacket, "l", armorStandDataWatcher);

        return armorStandPacket;
    }

    public static void setPrivateField(Object obj, String fieldName, Object newValue)
            throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, newValue);
    }

    public static Object getPrivateField(Object obj, String fieldName)
            throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }
    private static int entityID = 1000000000;

    public static int next() {
        if (entityID == 2147483647) {
            entityID = 1000000000;
        }
        return entityID++;
    }

    private static void sendPackets(Location nearTo, Packet... packets) {
        for (Player player : nearTo.getWorld().getPlayers()) {

            PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
            if (conn != null) {
                for (Packet packet : packets) {
                    if (packet != null) {
                        conn.sendPacket(packet);
                    }
                }
            }

        }
    }
}
