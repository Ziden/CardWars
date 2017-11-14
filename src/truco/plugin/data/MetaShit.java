
package truco.plugin.data;



import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import truco.plugin.CardWarsPlugin;


/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MetaShit implements MetadataValue {

    public static String getMetaString(String meta, Metadatable e) {
        if(!e.hasMetadata(meta)) return null;
        return (String)e.getMetadata(meta).get(0).value();
    }
    
    public static void setMetaString(String meta, Metadatable e, String value) {
        if(e.hasMetadata(meta))
            e.removeMetadata(meta, CardWarsPlugin._instance);
        e.setMetadata(meta, new MetaShit(value));
    }
    
    public static Object getMetaObject(String meta, Metadatable e) {
        if(!e.hasMetadata(meta)) return null;
        return (Object)e.getMetadata(meta).get(0).value();
    }
    public static void addMetaObject(String meta, Metadatable e, Object value) {
        
        e.setMetadata(meta, new FixedMetadataValue(CardWarsPlugin._instance,value));
       
    }
    
    public static void setMetaObject(String meta, Metadatable e, Object value) {
        if(e.hasMetadata(meta))
	    e.removeMetadata(meta, CardWarsPlugin._instance);
        e.setMetadata(meta, new FixedMetadataValue(CardWarsPlugin._instance,value));
    }
    
     public static void setMetaObject(String meta, Metadatable e, Object value, Plugin dono) {
        if(e.hasMetadata(meta))
            e.removeMetadata(meta, dono);
        e.setMetadata(meta, new FixedMetadataValue(dono,value));
    }
    
    private Object shit = null;
    public MetaShit(Object o) {
        shit = o;
    }
    
    @Override
    public Object value() {
        return shit;
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
        return shit instanceof Double ? (Double)shit : 0;
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