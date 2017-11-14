/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import truco.plugin.CardWarsPlugin;
import static truco.plugin.socket.SocketManager.tosend;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class SocketConnectionThread extends Thread {

    public Socket sock = null;
    public String svname;

    public SocketConnectionThread(String sv) {
        svname = sv;
    }

    public boolean isOnline() {
        if (sock == null) {
            return false;
        }
        if (sock.isClosed()) {
            return false;
        }
        if (!sock.isConnected()) {
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        try {

            sock = new Socket(InetAddress.getLocalHost(), 5000);

            byte[] svnametobytes = svname.getBytes(SocketManager.charset);

            sendBytes(svnametobytes, 0, svnametobytes.length);
            CardWarsPlugin.log.info("Conectado com o Socket!");
            while (true) {
                try {
                    byte[] recebi = readBytes();
                    String msgrecebida = new String(recebi, SocketManager.charset);
                    SocketManager.recebeMsg(msgrecebida);

                } catch (SocketException e) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex1) {
                        ex1.printStackTrace();
                    }
                    System.out.println("Tentando restabelecer conexão");

                    SocketManager.TentaReconnect();
                    break;

                }
            }
        } catch (IOException ex) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex1) {
                ex1.printStackTrace();
            }
            System.out.println("Tentando restabelecer conexão");
            SocketManager.TentaReconnect();

        }
    }

    public void sendBytes(byte[] myByteArray, int start, int len) throws IOException {
        if (len < 0) {
            throw new IllegalArgumentException("Negative length not allowed");
        }
        if (start < 0 || start >= myByteArray.length) {
            throw new IndexOutOfBoundsException("Out of bounds: " + start);
        }

        OutputStream out = sock.getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        dos.writeInt(len);
        if (len > 0) {
            dos.write(myByteArray, start, len);

        }

    }

    public byte[] readBytes() throws IOException {

        InputStream in = sock.getInputStream();
        DataInputStream dis = new DataInputStream(in);

        int len = dis.readInt();
        byte[] data = null;
        if (len > 0 && len < 1000) {
            data = new byte[len];;
            dis.readFully(data);
        }

        return data;
    }

    public void sendFinalMessage(String envi) {
        byte[] tobyte = envi.getBytes(Charset.forName(SocketManager.charset));

        try {
            SocketManager.t.sendBytes(tobyte, 0, tobyte.length);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void sendMessage(String praquem, String msg, String canal) {
        if (!isOnline()) {
            return;
        }
        String enviar = praquem + SocketManager.separador + canal + SocketManager.separador + msg;
        sendFinalMessage(enviar);
    }

    public void sendMessage(String msg, String canal) {
        sendMessage("all", msg, canal);
    }
}
