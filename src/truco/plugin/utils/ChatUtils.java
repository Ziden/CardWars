package truco.plugin.utils;


import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChatUtils {

    private static String pre = "§4Card§fWars§e>§r ";

        public static void sendOpMessage(String msg) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(msg);
            }
        }
    }

    public static void sendMessage(CommandSender p, String msg) {
        p.sendMessage(pre + msg);
    }

    public static void broadcastMessage(String msg) {
        Bukkit.broadcastMessage(pre + msg);
    }

    public static void tellAction(Player p, String action) {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (p != null && pl != p) {
                if (p.getLocation().distance(pl.getLocation()) < 10) {
                    pl.sendMessage(ChatColor.AQUA + "* " + p.getName() + " " + action + " *");
                }
            }
        }
    }

    public static String getNumber(int s, boolean bold) {
        //❶❷❸❹❺❻❼❽❾❿
        if (bold) {
            switch (s) {
                case 1:
                    return "❶";
                case 2:
                    return "❷";
                case 3:
                    return "❸";
                case 4:
                    return "❹";
                case 5:
                    return "❺";
                case 6:
                    return "❻";
                case 7:
                    return "❼";
                case 8:
                    return "❽";
                case 9:
                    return "❾";
                case 10:
                    return "❿";
                default:
                    return String.valueOf(s);
            }
        } else {
            switch (s) {
                case 1:
                    return "①";
                case 2:
                    return "②";
                case 3:
                    return "③";
                case 4:
                    return "④";
                case 5:
                    return "⑤";
                case 6:
                    return "⑥";
                case 7:
                    return "⑦";
                case 8:
                    return "⑧";
                case 9:
                    return "⑨";
                case 10:
                    return "⑩";
                default:
                    return String.valueOf(s);
            }
        }
        //①②③④⑤⑥⑦⑧⑨⑩

    }

     public static void ganhou(String quem, ItemStack qual) {
        IChatBaseComponent ic = ChatSerializer.a("{text:\"" + quem + " conseguiu a carta \",color:yellow}");
        ic.addSibling(bukkitStackToChatComponent(qual));
        ic.addSibling(ChatSerializer.a("{text:\" !\",color:red}"));
        for (Player p : Bukkit.getOnlinePlayers()) {
            send(p, ic);
        }
    }
    public static void ganhou(Player quem, ItemStack qual) {
        ganhou(quem.getName(), qual);
    }

    public static void mostracartaChat(Player quem, ItemStack qual) {
        IChatBaseComponent ic = ChatSerializer.a("{text:\"O jogador " + quem.getName() + " tem a carta \",color:aqua}");
        ic.addSibling(bukkitStackToChatComponent(qual));
        ic.addSibling(ChatSerializer.a("{text:\" !\",color:red}"));
        for (Player p : Bukkit.getOnlinePlayers()) {
            send(p, ic);
        }
    }

    public static void send(Player player, IChatBaseComponent chat) {

        PacketPlayOutChat packet = new PacketPlayOutChat(chat);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static IChatBaseComponent bukkitStackToChatComponent(ItemStack stack) {
        net.minecraft.server.v1_8_R2.ItemStack nms = CraftItemStack.asNMSCopy(stack);

        return nms.C();
    }
}
