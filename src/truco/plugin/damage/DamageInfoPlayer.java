/*

 */
package truco.plugin.damage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import truco.plugin.CardWarsPlugin;
import truco.plugin.chat.ChatMeta;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class DamageInfoPlayer implements MetadataValue {

    private List<Dano> danos = new ArrayList();

    public static DamageInfoPlayer getInfo(Player p) {
        if (p.hasMetadata("damageinfo")) {
            DamageInfoPlayer meta = (DamageInfoPlayer) p.getMetadata("damageinfo").get(0);
            return meta;
        } else {
            DamageInfoPlayer cMeta = new DamageInfoPlayer();
            p.setMetadata("damageinfo", cMeta);
            return cMeta;
        }
    }

    public void addDano(Dano d) {

        danos.add(d);
    }

    public Dano getLastPlayerDamage() {
        for (int x = danos.size(); x > 0; x--) {
            Dano d = danos.get(x - 1);
            if (!d.getQuem().equalsIgnoreCase("Sem Causador")) {
                long ate = d.getQuando() + 10000;
                if (System.currentTimeMillis()<ate) {
                    return d;
                }
            }
        }
        return null;
    }

    public Dano getLastDano() {
        for (int x = danos.size(); x > 0; x--) {
            Dano d = danos.get(x - 1);
            if (d.getQuem() != null) {
                return d;
            }
        }
        return null;
    }

    public void reset() {
        danos = new ArrayList();

    }

    @Override
    public Object value() {
        return null;
    }

    @Override
    public int asInt() {
        return 0;
    }

    @Override
    public float asFloat() {
        return 0;
    }

    @Override
    public double asDouble() {
        return 0;
    }

    @Override
    public long asLong() {
        return 0;
    }

    @Override
    public short asShort() {
        return 0;
    }

    @Override
    public byte asByte() {
        return 0;
    }

    @Override
    public boolean asBoolean() {
        return false;
    }

    @Override
    public String asString() {
        return null;
    }

    @Override
    public Plugin getOwningPlugin() {
        return CardWarsPlugin._instance;
    }

    @Override
    public void invalidate() {

    }

}
