/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.chat;

import org.bukkit.ChatColor;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import truco.plugin.CardWarsPlugin;

/**
 *
 * @author usuario
 * 
 */

public class ChatMeta implements MetadataValue {
    
    public boolean ignoreTell = false;
    public String inChannel = null;
    public String lastPlayerMessage = null;
    public String talkingTo = null;
    public ChatColor chatColor = null;
    public ChatColor nickColor = null;
    public boolean recebelocal = true;
    public boolean recebeglobal = true;
    
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
