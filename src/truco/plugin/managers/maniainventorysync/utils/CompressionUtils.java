/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.managers.maniainventorysync.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

/**
 *
 * @author Gabriel
 */
public class CompressionUtils {

    public static byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index  
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();

        deflater.end();

        //System.out.println("Original: " + data.length / 1024 + " Kb");
        //System.out.println("Compressed: " + output.length / 1024 + " Kb");
        return output;
    }

    public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();

        inflater.end();

        //System.out.print("Original: " + data.length);
        //System.out.println("Uncompressed: " + output.length);
        return output;
    }

    public static byte[] Compressgzip(byte[] uncomp) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            GZIPOutputStream compressor = new GZIPOutputStream(os);
            compressor.write(uncomp);
            compressor.close();
            System.out.print("Original: " + uncomp.length);
            System.out.println("Uncompressed: " + os.toByteArray().length);
            return os.toByteArray();
        } catch (IOException ex) {
        }
        return null;
    }

    public static byte[] Descompressgzip(byte[] comp) {
        GZIPInputStream decompressor = null;
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(comp);
            decompressor = new GZIPInputStream(is);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] b = new byte[256];
            int tmp;
            while ((tmp = decompressor.read(b)) != -1) {
                buffer.write(b, 0, tmp);
            }
            buffer.close();
            return buffer.toByteArray();
        } catch (IOException ex) {
        } finally {
            try {
                if (decompressor != null) {
                    decompressor.close();
                }
            } catch (IOException ex) {
            }
        }
        return null;
    }
}
