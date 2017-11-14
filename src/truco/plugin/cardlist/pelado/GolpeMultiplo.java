/*

 */
package truco.plugin.cardlist.pelado;

import java.util.UUID;
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
public class GolpeMultiplo implements MetadataValue {

    public UUID alvo = null;
    public GPasso passo = GPasso.NULL;

    public void setPasso(GPasso passo) {
        this.passo = passo;
    }

    public void setAlvo(UUID alvo) {
        this.alvo = alvo;
    }

    public static void clear(Player p) {
        p.removeMetadata("golpemultiplo", CardWarsPlugin._instance);
    }

    public static GolpeMultiplo getGolpeMultiplo(Player p) {

        if (p.hasMetadata("golpemultiplo")) {
            GolpeMultiplo meta = (GolpeMultiplo) p.getMetadata("golpemultiplo").get(0);
            return meta;
        } else {
            GolpeMultiplo gm = new GolpeMultiplo();
            p.setMetadata("golpemultiplo", gm);
            return gm;

        }
    }

    @Override
    public Object value() {
        return this;
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
        return (byte) 0;
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

    public static enum GPasso {

        ATIVEIDUPLO, ACERTEIDUPLO, ACERTEITRIPLO, NULL;
    }

}
