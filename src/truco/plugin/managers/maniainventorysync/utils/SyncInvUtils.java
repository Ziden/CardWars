/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.managers.maniainventorysync.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import truco.plugin.managers.maniainventorysync.ManiaInventorySync;

/**
 *
 * @author Gabriel
 */
public class SyncInvUtils {

    public static void AddLog(String msg) {
        ManiaInventorySync.log.info("[PJInv] " + msg);
    }

    public static byte[] serializeItemStacks(ItemStack[] inv) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BukkitObjectOutputStream bos = null;
            try {
                bos = new BukkitObjectOutputStream(os);
            } catch (IOException ex) {
                Logger.getLogger(SyncInvUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            bos.writeObject(inv);
            return os.toByteArray();
        } catch (IOException ex) {
            //throw new SerializationException(inv, ex);
            return null;
        }
    }

    public static byte[] SerializeItemStackZipado(ItemStack[] inv) {
        try {
            byte[] dado = serializeItemStacks(inv);
            return CompressionUtils.compress(dado);
        } catch (Exception e) {
            return null;
        }
    }

    public static ItemStack[] DeserializeItemStackZipado(byte[] b) {
        try {
            byte[] dado = CompressionUtils.decompress(b);
            return deserializeItemStacks(dado);
        } catch (Exception e) {
            return null;
        }
    }

    public static ItemStack[] deserializeItemStacks(byte[] b) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(b);
            BukkitObjectInputStream bois = new BukkitObjectInputStream(bais);
            return (ItemStack[]) bois.readObject();
        } catch (Exception ex) {
            return null;
        }
    }

    public static byte[] BlobToBytes(Blob blob) {
        int blobLength;
        byte[] blobAsBytes = null;
        try {
            blobLength = (int) blob.length();
            blobAsBytes = blob.getBytes(1, blobLength);
            blob.free();
        } catch (SQLException ex) {
            Logger.getLogger(SyncInvUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return blobAsBytes;
    }

    public static Blob BytesToBlob(byte[] bit) {
        try {
            return new javax.sql.rowset.serial.SerialBlob(bit);
        } catch (SQLException ex) {
            Logger.getLogger(SyncInvUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
