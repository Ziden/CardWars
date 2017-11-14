/*

 */
package truco.plugin.utils.efeitos;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ParticleManager {

    
    
    public static List<Location> buildLine(Location loc1, Location loc2) {
        List<Location> line = new ArrayList();
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
