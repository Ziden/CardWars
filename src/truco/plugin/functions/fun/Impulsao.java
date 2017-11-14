/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.functions.fun;

import java.util.HashMap;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class Impulsao {

    public static double vboost = 1.2;
    public static double hboost = 1.5;
    public static Material mat = Material.BEDROCK;
    private static HashMap<String, String> jogadorpulando = new HashMap();
    private static HashMap<String, Object[]> jogadorpulando2 = new HashMap();

 
    public static void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getEntity();
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        if (jogadorpulando.containsKey(p.getName())) {
            jogadorpulando.remove(p.getName());
            event.setDamage(0);
            event.setCancelled(true);
        }
        if (jogadorpulando2.containsKey(p.getName())) {
            jogadorpulando2.remove(p.getName());
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    public static boolean nofalldamage(Material m) {
        if ((m == Material.AIR) || (m == Material.WATER) || (m == Material.SOUL_SAND)) {
            return false;
        }
        return true;
    }

    public static void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (jogadorpulando2.containsKey(p.getName())) {
            if (p.getLocation().distance((Location) ((Object[]) jogadorpulando2.get(p.getName()))[0]) < 1.0D) {
                p.setVelocity(new Vector(0, 0, 0));
                p.setFallDistance(0.0F);
                event.setCancelled(true);
                //p.setSprinting(false);
                if (p.getGameMode() != GameMode.CREATIVE) {
                    p.setAllowFlight(false);
                }
                jogadorpulando2.remove(p.getName());
                if ((!nofalldamage(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType())) && (!nofalldamage(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getType())) && (!nofalldamage(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getType()))) {
                    jogadorpulando.put(p.getName(), "---");
                }
                return;
            }
            Double speed = (Double) ((Object[]) jogadorpulando2.get(p.getName()))[1];
            if (p.getLocation().distance((Location) ((Object[]) jogadorpulando2.get(p.getName()))[0]) < 10.0D) {
                if (speed.doubleValue() > 1.0D) {
                    speed = Double.valueOf(speed.doubleValue() * 0.5D);
                    if (speed.doubleValue() < 1.0D) {
                        speed = Double.valueOf(1.0D);
                    }
                }
            }
            Vector dir = ((Location) ((Object[]) jogadorpulando2.get(p.getName()))[0]).toVector().subtract(p.getLocation().toVector()).normalize();
            p.setVelocity(dir.multiply(speed.doubleValue()));
            p.setFallDistance(0.0F);
        }
    }

    public static void onPlayerMoveonBlock(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if ((p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == mat) && (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getType() == Material.SIGN_POST)) {
            shotsign(p);
        } else if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == mat) {
            shot(p);
        }
        onPlayerMove(event);
    }

    private static void shotsign(Player p) {
        Location loc = new Location(p.getWorld(), 0.0D, 0.0D, 0.0D);
        Sign s = (Sign) p.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getState();
        Double speed = Double.valueOf(1.0D);
        if (s.getLine(0).contains("+")) {
            if (!s.getLine(0).trim().split("\\+")[0].equalsIgnoreCase("CW_Booster")) {
                return;
            }
            try {
                speed = Double.valueOf(Double.parseDouble(s.getLine(0).trim().split("\\+")[1].trim()));
            } catch (NumberFormatException e) {
                return;
            }
        } else if (!s.getLine(0).trim().equalsIgnoreCase("CW_Booster")) {
            return;
        }
        try {
            loc.setX(Float.parseFloat(s.getLine(1).trim()));
            loc.setY(Float.parseFloat(s.getLine(2).trim()));
            loc.setZ(Float.parseFloat(s.getLine(3).trim()));
        } catch (NumberFormatException e) {
            return;
        }
        Object[] o
                = {
                    loc, speed
                };
        jogadorpulando2.put(p.getName(), o);
        if (speed.doubleValue() < 1.0D) {
            p.setAllowFlight(true);
        }
        Vector dir = ((Location) ((Object[]) jogadorpulando2.get(p.getName()))[0]).toVector().subtract(p.getLocation().toVector()).normalize();
        p.setVelocity(dir.multiply(speed.doubleValue()));
    }

    private static void shot(Player p) {
        p.playEffect(p.getLocation(), Effect.BLAZE_SHOOT, 0);
        Block sb = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
        Block ub = sb.getRelative(BlockFace.DOWN);
        double h = 0.0D;
        double v = 0.0D;
        int max_p = 25;
        int max = 0;
        String dir = "up";
        Block db = ub;
        if ((sb.getRelative(BlockFace.NORTH).getType() == mat) && (ub.getRelative(BlockFace.NORTH).getType() == mat)) {
            dir = "up";
        } else if ((sb.getRelative(BlockFace.NORTH_EAST).getType() == mat) && (ub.getRelative(BlockFace.NORTH_EAST).getType() == mat)) {
            dir = "up";
        } else if ((sb.getRelative(BlockFace.EAST).getType() == mat) && (ub.getRelative(BlockFace.EAST).getType() == mat)) {
            dir = "up";
        } else if ((sb.getRelative(BlockFace.SOUTH_EAST).getType() == mat) && (ub.getRelative(BlockFace.SOUTH_EAST).getType() == mat)) {
            dir = "up";
        } else if ((sb.getRelative(BlockFace.SOUTH).getType() == mat) && (ub.getRelative(BlockFace.SOUTH).getType() == mat)) {
            dir = "up";
        } else if ((sb.getRelative(BlockFace.SOUTH_WEST).getType() == mat) && (ub.getRelative(BlockFace.SOUTH_WEST).getType() == mat)) {
            dir = "up";
        } else if ((sb.getRelative(BlockFace.WEST).getType() == mat) && (ub.getRelative(BlockFace.WEST).getType() == mat)) {
            dir = "up";
        } else if ((sb.getRelative(BlockFace.NORTH_WEST).getType() == mat) && (ub.getRelative(BlockFace.NORTH_WEST).getType() == mat)) {
            dir = "up";
        } else if (ub.getRelative(BlockFace.NORTH).getType() == mat) {
            dir = "NORTH";
            db = ub.getRelative(BlockFace.NORTH);
        } else if (ub.getRelative(BlockFace.EAST).getType() == mat) {
            dir = "EAST";
            db = ub.getRelative(BlockFace.EAST);
        } else if (ub.getRelative(BlockFace.SOUTH).getType() == mat) {
            dir = "SOUTH";
            db = ub.getRelative(BlockFace.SOUTH);
        } else if (ub.getRelative(BlockFace.WEST).getType() == mat) {
            dir = "WEST";
            db = ub.getRelative(BlockFace.WEST);
        } else if (ub.getRelative(BlockFace.NORTH_EAST).getType() == mat) {
            dir = "NORTH_EAST";
            db = ub.getRelative(BlockFace.NORTH_EAST);
        } else if (ub.getRelative(BlockFace.SOUTH_EAST).getType() == mat) {
            dir = "SOUTH_EAST";
            db = ub.getRelative(BlockFace.SOUTH_EAST);
        } else if (ub.getRelative(BlockFace.SOUTH_WEST).getType() == mat) {
            dir = "SOUTH_WEST";
            db = ub.getRelative(BlockFace.SOUTH_WEST);
        } else if (ub.getRelative(BlockFace.NORTH_WEST).getType() == mat) {
            dir = "NORTH_WEST";
            db = ub.getRelative(BlockFace.NORTH_WEST);
        }
        if (dir.equalsIgnoreCase("up")) {
            Block tmp = ub;
            max = max_p;
            while (max > 0) {
                tmp = tmp.getRelative(BlockFace.DOWN);
                if (tmp.getType() == mat) {
                    v += vboost;
                    max--;
                } else if (max == max_p) {
                    v = vboost;
                    max = -9;
                } else {
                    max = -99;
                }
            }
            p.setVelocity(new Vector(0.0D, v, 0.0D));
            jogadorpulando.put(p.getName(), "up");
        } else {
            Block tmp = db;
            max = max_p;
            while (max > 0) {
                tmp = tmp.getRelative(BlockFace.DOWN);
                if (tmp.getType() == mat) {
                    h += hboost;
                    max--;
                } else {
                    max = -99;
                }
            }
            tmp = ub;
            max = max_p;
            while (max > 0) {
                tmp = tmp.getRelative(BlockFace.DOWN);
                if (tmp.getType() == mat) {
                    v += vboost;
                    max--;
                } else if (max == max_p) {
                    v = vboost;
                    max = -9;
                } else {
                    max = -99;
                }
            }
            double x = 0.0D;
            double z = 0.0D;
            if ((h < 0.1D) && (hboost > 0.1D)) {
                h = hboost / 2.0D;
            } else if (h < 0.1D) {
                h = 2.0D;
            }
            if (dir.equalsIgnoreCase("West")) {
                x = h;
            } else if (dir.equalsIgnoreCase("NORTH")) {
                z = h;
            } else if (dir.equalsIgnoreCase("EAST")) {
                x = -h;
            } else if (dir.equalsIgnoreCase("SOUTH")) {
                z = -h;
            }
            p.setVelocity(new Vector(x, v, z).multiply(1.1D));
            jogadorpulando.put(p.getName(), "---");
        }
    }
}
