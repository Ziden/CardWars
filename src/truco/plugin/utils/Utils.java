/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.utils;

import truco.plugin.data.MetaShit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import net.minecraft.server.v1_8_R2.*;
import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle.EnumTitleAction;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.potion.PotionEffect;
import truco.plugin.CardWarsPlugin;
import truco.plugin.events.CustomDamageEvent.CausaDano;

/**
 *
 * @author usuario
 *
 */
public class Utils {

    public static String getS(int s) {
        if (s > 1) {
            return "s";
        } else {
            return "";
        }
    }

    public static void deleteDatasFolders() {
        for (World r : Bukkit.getWorlds()) {
            r.setAutoSave(false);

            File pasta = new File(r.getName() + "/playerdata");
            if (pasta.exists()) {
                try {
                    FileUtils.deleteDirectory(pasta);
                    pasta.mkdirs();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }

    }

    public static void setHeaderAndFooter(Player player, String headers, String footers) {
        CraftPlayer craftplayer = (CraftPlayer) player;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent header = ChatSerializer.a("{text:\"" + headers + "\"}");
        IChatBaseComponent footer = ChatSerializer.a("{text:\"" + footers + "\"}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        try {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, header);
            headerField.setAccessible(!headerField.isAccessible());

            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, footer);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception e) {
            e.printStackTrace();
        }

        connection.sendPacket(packet);
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        CraftPlayer craftplayer = (CraftPlayer) player;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent titleJSON = ChatSerializer.a("{'text': '" + title + "'}");
        IChatBaseComponent subtitleJSON = ChatSerializer.a("{'text': '" + subtitle + "'}");
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleJSON, fadeIn, stay, fadeOut);
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleJSON);

        connection.sendPacket(titlePacket);
        connection.sendPacket(subtitlePacket);
    }

    public static void sendActionBar(Player player, String title) {
        CraftPlayer craftplayer = (CraftPlayer) player;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent titleJSON = ChatSerializer.a("{'text': '" + title + "'}");
        PacketPlayOutChat packet = new PacketPlayOutChat(titleJSON, (byte) 2);
        connection.sendPacket(packet);
    }

    public static FireworkEffect LaunchRandomFirework() {
        FireworkEffect.Builder builder = FireworkEffect.builder();
        if (CardWarsPlugin.random.nextInt(3) == 0) {
            builder.withTrail();
        } else if (CardWarsPlugin.random.nextInt(2) == 0) {
            builder.withFlicker();
        }
        builder.with(org.bukkit.FireworkEffect.Type.values()[CardWarsPlugin.random.nextInt(org.bukkit.FireworkEffect.Type.values().length)]);
        int colorCount = 17;
        builder.withColor(Color.fromRGB(CardWarsPlugin.random.nextInt(255), CardWarsPlugin.random.nextInt(255), CardWarsPlugin.random.nextInt(255)));
        while (CardWarsPlugin.random.nextInt(colorCount) != 0) {
            builder.withColor(Color.fromRGB(CardWarsPlugin.random.nextInt(255), CardWarsPlugin.random.nextInt(255), CardWarsPlugin.random.nextInt(255)));
            colorCount--;
        }
        return builder.build();
    }

    public static void spawnRandomFirework(Location loc) {
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        Random r = CardWarsPlugin.random;
        int rt = r.nextInt(4) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        if (rt == 1) {
            type = FireworkEffect.Type.BALL;
        }
        if (rt == 2) {
            type = FireworkEffect.Type.BALL_LARGE;
        }
        if (rt == 3) {
            type = FireworkEffect.Type.BURST;
        }
        if (rt == 4) {
            type = FireworkEffect.Type.CREEPER;
        }
        if (rt == 5) {
            type = FireworkEffect.Type.STAR;
        }
        Color c1 = Color.fromRGB(170, 0, 0);
        Color c2 = Color.WHITE;
        Color c3 = Color.GRAY;
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withColor(c2).withFade(c3).with(type).trail(r.nextBoolean()).build();
        fwm.addEffect(effect);
        int rp = r.nextInt(2) + 1;
        fwm.setPower(rp);
        fw.setFireworkMeta(fwm);
    }

    public static String getTimeToString(int t) {
        int tem = t;
        int min = (int) tem / 60;
        int sec = tem % 60;

        String mins = (min >= 10 ? "" : "0") + min + "";
        String secs = (sec >= 10 ? "" : "0") + sec + "";

        return mins + ":" + secs;
    }

    public static long getDeathMillis(Player ptarget) {
        if (ptarget.hasMetadata("morreuquando")) {
            return (long) MetaShit.getMetaObject("morreuquando", ptarget);
        }
        return -1;
    }

    public static void morreu(Player ptarget) {
        MetaShit.setMetaObject("morreuquando", ptarget, System.currentTimeMillis());
    }

    public static enum DamageType {

        FISICO, MAGICO, OUTRO;
    }
//poison, light, magic, fire, fire_ticks, drowning

    public static DamageType getDamageType(CausaDano causa) {

        if (causa == CausaDano.ATAQUE || causa == CausaDano.SKILL_ATAQUE || causa == CausaDano.FLECHA) {

            return DamageType.FISICO;

        } else if (causa == CausaDano.FOGO || causa == CausaDano.MAGIA || causa == CausaDano.MAGIA_AGUA || causa == CausaDano.MAGIA_FOGO || causa == CausaDano.MAGIA_RAIO) {
            return DamageType.MAGICO;
        }
        return DamageType.OUTRO;
    }

    public static void test(Player p) {
        try {
            String currentVersion;
            Object propertyManager;
            Object obj = Bukkit.getServer().getClass().getDeclaredMethod("getServer").invoke(Bukkit.getServer());
            propertyManager = obj.getClass().getDeclaredMethod("getPropertyManager").invoke(obj);
            currentVersion = propertyManager.getClass().getPackage().getName();
            Method handle = p.getClass().getMethod("getHandle");
            Class c = Class.forName(currentVersion + ".Entity");
            for (Field f : c.getDeclaredFields()) {
                System.out.println(f.getName() + "-" + f.toGenericString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setWidthHeight(Player p, float height, float width, float length) {
        try {
            String currentVersion;
            Object propertyManager;
            Object obj = Bukkit.getServer().getClass().getDeclaredMethod("getServer").invoke(Bukkit.getServer());
            propertyManager = obj.getClass().getDeclaredMethod("getPropertyManager").invoke(obj);
            currentVersion = propertyManager.getClass().getPackage().getName();
            Method handle = p.getClass().getMethod("getHandle");
            Class c = Class.forName(currentVersion + ".Entity");

            Field field2 = c.getDeclaredField("width");
            Field field3 = c.getDeclaredField("length");
            field2.setFloat(handle.invoke(p), (float) width);
            field3.setFloat(handle.invoke(p), (float) length);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean hasChangedBlockCoordinates(final Location fromLoc, final Location toLoc) {
        return !(fromLoc.getWorld().equals(toLoc.getWorld())
                && fromLoc.getBlockX() == toLoc.getBlockX()
                && fromLoc.getBlockY() == toLoc.getBlockY()
                && fromLoc.getBlockZ() == toLoc.getBlockZ());
    }

    public static Set<Block> getStructureBlocks(ProtectedRegion region, World mundo) {
        Set<Block> b = new HashSet();
        int x = region.getMinimumPoint().getBlockX();
        int y = region.getMinimumPoint().getBlockY();
        int z = region.getMinimumPoint().getBlockZ();
        int xf = region.getMaximumPoint().getBlockX();
        int yf = region.getMaximumPoint().getBlockY();
        int zf = region.getMaximumPoint().getBlockZ();
        for (int xt = x; xt <= xf; xt++) {
            for (int yt = y; yt <= yf; yt++) {
                for (int zt = z; zt <= zf; zt++) {
                    b.add(mundo.getBlockAt(xt, yt, zt));

                }

            }

        }
        return b;
    }

    public static void TeleportarTPBG(String server, CommandSender sender) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(CardWarsPlugin._instance, "BungeeCord");
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException localIOException) {
        }
        ((PluginMessageRecipient) sender).sendPluginMessage(CardWarsPlugin._instance, "BungeeCord", b.toByteArray());

    }

    public static void debug(String msg) {
        // if debug mode
        CardWarsPlugin.log.info("[DEBUG]" + msg);
    }

   
   

    public static void clearPlayerEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }


}
