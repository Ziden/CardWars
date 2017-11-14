/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LocUtils {

    public static double getAngle(Vector vec1, Vector vec2) {
        return vec1.angle(vec2) * 180.0F / 3.141592653589793D;
    }

    public static Location stringToLocation(String g) {
        String[] split = g.split(";");
        if (split.length != 6) {
            return null;
        }
        //world;1047.8923657656458;4.0;962.4730276682263;-2.8499498;179.54938
        return new Location(Bukkit.getWorld(split[0]), Double.valueOf(split[1]), Double.valueOf(split[2]), Double.valueOf(split[3]), Float.valueOf(split[4]), Float.valueOf(split[5]));
    }

    public static String locationToString(Location l) {
        if (l == null) {
            return "null";
        }
        return l.getWorld().getName() + ";" + l.getX() + ";" + l.getY() + ";" + l.getZ() + ";" + l.getYaw() + ";" + l.getPitch();

    }

    public static List<Location> trace(Location one, Location two, int n) {
        n++;
        Location temp = null;
        List<Location> locs = new ArrayList<>();
        Location base = two.clone().subtract(one);
        Vector unit = new Vector(base.getX() / n, base.getY() / n, base.getZ() / n);
        temp = one;
        for (int i = 1; i < n; i++) {
            locs.add(locs.size(), temp.add(unit).clone());

        }
        return locs;
    }

    public static enum TargetType {

        ALIADO, INIMIGO, AMBOS;
    }

    public static Entity getTarget(final Player player, TargetType type) {
        assert player != null;
        Entity target = null;
        double targetDistanceSquared = 0;
        final double radiusSquared = 1;
        final Vector l = player.getEyeLocation().toVector(), n = player.getLocation().getDirection().normalize();
        final double cos45 = Math.cos(Math.PI / 4);
        for (final LivingEntity other : player.getWorld().getEntitiesByClass(LivingEntity.class)) {
            if (other == player) {
                continue;
            }
            if (other instanceof Player) {
                if (((Player) other).getGameMode() == GameMode.SPECTATOR) {
                    continue;
                }

                if (type != TargetType.AMBOS) {
                    if (!TeamUtils.canAttack(player, (Player) other)) {
                        if (type == TargetType.INIMIGO) {
                            continue;
                        }
                    } else {
                        if (type == TargetType.ALIADO) {
                            continue;
                        }

                    }
                }
            }
            if (target == null || targetDistanceSquared > other.getLocation().distanceSquared(player.getLocation())) {
                final Vector t = other.getLocation().add(0, 1, 0).toVector().subtract(l);
                if (n.clone().crossProduct(t).lengthSquared() < radiusSquared && t.normalize().dot(n) >= cos45) {
                    target = other;
                    targetDistanceSquared = target.getLocation().distanceSquared(player.getLocation());
                }
            }
        }
        return target;
    }

    public static List<Location> getRay(LivingEntity e, int range, int divide) {
        Utils.debug("Creating ray from " + e.getLocation().toString() + " range " + range + " dividors " + divide);
        Location one = e.getLocation();
        Location two = e.getEyeLocation().add(e.getEyeLocation().getDirection().normalize().multiply(range));
        Utils.debug("location two " + two.toString());
        List<Location> locs = trace(one, two, divide);
        locs.add(locs.size(), one);
        //locs.add(0, two);
        Utils.debug("Result: " + locs.toString());
        return locs;
    }

    public static List<LivingEntity> getOnSight(LivingEntity le, int angle, int distance) {
        Location loc = le.getLocation();
        List<LivingEntity> onSight = new ArrayList<>();
        List<Entity> onArea = le.getNearbyEntities(distance, distance, distance);
        double minDist = distance + 0.01d;
        LivingEntity bestTarget = null;
        float yaw = loc.getYaw();
        if (yaw > 360) {
            yaw %= 360;
        }
        while (yaw < 0) {
            yaw += 360;
        }
        for (Entity e : onArea) {
            if (!(e instanceof LivingEntity)) {
                continue;
            }
            float vyaw = yawFromTo(loc, e.getLocation());
            if (vyaw > 360) {
                vyaw %= 360;
            } else {
                while (vyaw < 0) {
                    vyaw += 360;
                }
            }
            float rot = Math.abs(vyaw - yaw);
            if (rot <= angle) {
                if (le.hasLineOfSight(e)) {
                    onSight.add((LivingEntity) e);
                    double dist = loc.distance(e.getLocation());
                    if (dist < minDist) {
                        minDist = dist;
                        bestTarget = (LivingEntity) e;
                    }
                }
            }
        }
        if (bestTarget != null) {
            onSight.remove(bestTarget);
            onSight.add(0, bestTarget);
        }
        return onSight;
    }

    public static float yawFromTo(Location loc, Location lookat) {
        loc = loc.clone();
        float yaw = 0;
        double dx = lookat.getX() - loc.getX();
        double dz = lookat.getZ() - loc.getZ();
        if (dx != 0) {
            if (dx < 0) {
                yaw = (float) (1.5 * Math.PI);
            } else {
                yaw = (float) (0.5 * Math.PI);
            }
            yaw = (float) yaw - (float) Math.atan(dz / dx);
        } else if (dz < 0) {
            yaw = (float) Math.PI;
        }
        yaw = -yaw * 180f / (float) Math.PI;

        return yaw;
    }

    public static List<Location> buildLine(Location loc1, Location loc2) {
        List line = new ArrayList();
        if (loc1 == null) {
            return line;
        }
        if (loc2 == null) {
            return line;
        }
        if (loc1.getBlock().equals(loc2.getBlock())) {
            line.add(loc1.getBlock().getLocation());
            return line;
        }
        int dx = Math.max(loc1.getBlockX(), loc2.getBlockX()) - Math.min(loc1.getBlockX(), loc2.getBlockX());
        int dy = Math.max(loc1.getBlockY(), loc2.getBlockY()) - Math.min(loc1.getBlockY(), loc2.getBlockY());
        int dz = Math.max(loc1.getBlockZ(), loc2.getBlockZ()) - Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int x1 = loc1.getBlockX();
        int x2 = loc2.getBlockX();
        int y1 = loc1.getBlockY();
        int y2 = loc2.getBlockY();
        int z1 = loc1.getBlockZ();
        int z2 = loc2.getBlockZ();
        int x = 0;
        int y = 0;
        int z = 0;
        int i = 0;
        int d = 1;
        switch (findHighest(dx, dy, dz)) {
            case 1:
                i = 0;
                d = 1;
                if (x1 > x2) {
                    d = -1;
                }
                x = loc1.getBlockX();
                do {
                    i++;
                    y = y1 + (x - x1) * (y2 - y1) / (x2 - x1);
                    z = z1 + (x - x1) * (z2 - z1) / (x2 - x1);
                    line.add(new Location(loc1.getWorld(), x, y, z));
                    x += d;
                } while (i <= Math.max(x1, x2) - Math.min(x1, x2));
                break;
            case 2:
                i = 0;
                d = 1;
                if (y1 > y2) {
                    d = -1;
                }
                y = loc1.getBlockY();
                do {
                    i++;
                    x = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
                    z = z1 + (y - y1) * (z2 - z1) / (y2 - y1);
                    line.add(new Location(loc1.getWorld(), x, y, z));
                    y += d;
                } while (i <= Math.max(y1, y2) - Math.min(y1, y2));
                break;
            case 3:
                i = 0;
                d = 1;
                if (z1 > z2) {
                    d = -1;
                }
                z = loc1.getBlockZ();
                do {
                    i++;
                    y = y1 + (z - z1) * (y2 - y1) / (z2 - z1);
                    x = x1 + (z - z1) * (x2 - x1) / (z2 - z1);
                    line.add(new Location(loc1.getWorld(), x, y, z));
                    z += d;
                } while (i <= Math.max(z1, z2) - Math.min(z1, z2));
        }

        return line;
    }

    private static int findHighest(int x, int y, int z) {
        if ((x >= y) && (x >= z)) {
            return 1;
        }
        if ((y >= x) && (y >= z)) {
            return 2;
        }
        return 3;
    }
}
