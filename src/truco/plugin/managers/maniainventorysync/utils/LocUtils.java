/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.managers.maniainventorysync.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 *
 * @author Gabriel
 */
public class LocUtils {

  

    public static boolean inArea(Location targetLocation, Location inAreaLocation1, Location inAreaLocation2) {
        if (inAreaLocation1.getWorld().getName() == inAreaLocation2.getWorld().getName()) { // Check for worldName location1, location2
            if (targetLocation.getWorld().getName() == inAreaLocation1.getWorld().getName()) { // Check for worldName targetLocation, location1
                if ((targetLocation.getBlockX() >= inAreaLocation1.getBlockX() && targetLocation.getBlockX() <= inAreaLocation2.getBlockX()) || (targetLocation.getBlockX() <= inAreaLocation1.getBlockX() && targetLocation.getBlockX() >= inAreaLocation2.getBlockX())) { // Check X value
                    if ((targetLocation.getBlockZ() >= inAreaLocation1.getBlockZ() && targetLocation.getBlockZ() <= inAreaLocation2.getBlockZ()) || (targetLocation.getBlockZ() <= inAreaLocation1.getBlockZ() && targetLocation.getBlockZ() >= inAreaLocation2.getBlockZ())) { // Check Z value
                        if ((targetLocation.getBlockY() >= inAreaLocation1.getBlockY() && targetLocation.getBlockY() <= inAreaLocation2.getBlockY()) || (targetLocation.getBlockY() <= inAreaLocation1.getBlockY() && targetLocation.getBlockY() >= inAreaLocation2.getBlockY())) { // Check Y value
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean isSameLocation(Location l1, Location l2) {
        if (l1.getBlockX() == l2.getBlockX()) {
            if (l1.getBlockZ() == l2.getBlockZ()) {
                return true;
            }
        }
        return false;
    }

    public static int[] getChunkLocation(Location l) {
        l = l.getChunk().getBlock(0, 0, 0).getLocation();
        return new int[]{(int) l.getX() / 16, (int) l.getZ() / 16};
    }

    public static Location locOfChunk(String w, int x, int z) {
        return Bukkit.getWorld(w).getChunkAt(x * 16, z * 16).getBlock(0, 0, 0).getLocation();
    }
}
