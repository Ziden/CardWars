/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.data.mysql;

import br.pj.newlibrarysystem.cashgame.GemManager;
import truco.plugin.menus.shop.CartaAVenda;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.managers.maniainventorysync.utils.SyncInvUtils;

import truco.plugin.menus.ArmorSelectMenu.Armadura;
import truco.plugin.menus.CardSelectorMenu;
import truco.plugin.menus.MenuUtils;

/**
 *
 * @author usuario
 */
public class CardsDB {

    public static Connection conn = null;
    public static int stocksize = Integer.MAX_VALUE;
    public static List<UUID> enderchest = Collections.synchronizedList(new LinkedList<UUID>());

    public static void devolve() {
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM `registros` where acao not like 'ganhou%' LIMIT 10");
            while (rs.next()) {
                int id = rs.getInt("id");
                int qt = rs.getInt("quanto");
                UUID quem = UUID.fromString(rs.getString("uuid"));
                String acao = rs.getString("acao");
                if (acao.contains("gastou")) {
                    System.out.println("Devolvi para " + quem.toString() + " " + qt + " gemas!");
                    GemManager.AddGem(quem, qt);
                }
                conn.createStatement().executeUpdate("DELETE FROM registros WHERE id =" + id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void createTables() {
        try {
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS estoque (uuid VARCHAR(200),pagina INTEGER,itens TEXT)");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS enderchest (uuid VARCHAR(200) PRIMARY KEY,cartas TEXT)");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS shop (id INTEGER PRIMARY KEY AUTO_INCREMENT,carta VARCHAR(80),vendedor VARCHAR(200),nomevendedor VARCHAR(200),armadura VARCHAR(30),raridade VARCHAR(30),preco Integer)");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS registros (`id` INT NOT NULL AUTO_INCREMENT,uuid VARCHAR(200),nick VARCHAR(50),acao VARCHAR(255),quanto INTEGER,quando TIMESTAMP,PRIMARY KEY (`id`)) ");
            //  conn.createStatement().execute("CREATE TABLE IF NOT EXISTS leave (`id` INTEGER PRIMARY KEY AUTO_INCREMENT,uuid VARCHAR(200),pontos INTEGER)");

            /* String execut = "CREATE TABLE IF NOT EXISTS cartasave (uuid VARCHAR(200),";
             for (Armadura r : Armadura.values()) {
             String v = ")";
             if (r != Armadura.values()[Armadura.values().length - 1]) {
             v = ",";
             }
             execut += r.name() + " TEXT" + v;
             }
             conn.createStatement().execute(execut);
             */
        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String CartasToString(List<ItemStack> itens) {
        String resultado = "";
        for (ItemStack item : itens) {

            Carta s = ControleCartas.getCarta(item);
            if (s != null) {
                resultado += s.getNome() + ",";
            } else {
                if (item == itens.get(itens.size() - 1)) {
                    resultado += "null";
                } else {
                    resultado += "null,";
                }
            }
        }

        return resultado;
    }

    public static void startPlayer(Player p) {

    }
// conn.createStatement().execute("CREATE TABLE IF NOT EXISTS vendas (carta VARCHAR(80),vendedor VARCHAR(200),armadura VARCHAR(30),raridade VARCHAR(30),preco Integer)");

    public static boolean pageExists(UUID p, int page) {
        try {
            return conn.createStatement().executeQuery("SELECT 1 FROM estoque WHERE uuid ='" + p.toString() + "' AND pagina ='" + page + "'").next();
        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    /*
     public boolean temPontos(UUID quem) {
     try {
     return conn.createStatement().executeQuery("SELECT 1 FROM leave WHERE uuid='" + quem.toString() + "'").next();
     } catch (SQLException ex) {
     ex.printStackTrace();
     }
     return false;

     }
     */
    /*  public void addPontosLeave(UUID quem, int pts) {
     String exeq;
     if (temPontos(quem)) {
     exeq = "UPDATE `leave` set `pontos` = `pontos` + " + pts + " WHERE `uuid` = '" + quem.toString() + "';";
     } else {
     exeq = "INSERT INTO leave (`uuid`,`pontos`) VALUES('" + quem.toString() + "'," + pts + ")";

     }
     try {
     conn.createStatement().executeUpdate(exeq);
     } catch (SQLException ex) {
     ex.printStackTrace();
     }
     }

     public void removePontosLeave(UUID quem, int pts) {
     String exeq;
     if (temPontos(quem)) {
     exeq = "UPDATE `leave` set `pontos` = `pontos` - " + pts + " WHERE `uuid` = '" + quem.toString() + "';";
     } else {

     exeq = "INSERT INTO leave (`uuid`,`pontos`) VALUES('" + quem.toString() + "'," + (pts * -1) + ")";

     }
     try {
     conn.createStatement().executeUpdate(exeq);
     } catch (SQLException ex) {
     ex.printStackTrace();
     }
     }
     */

    public static boolean hasCardOnStockAndRemove(UUID p, Carta c) {
        boolean acho = false;
        pagesfor:
        for (int x = 1; x < stocksize; x++) {
            if (!pageExists(p, x)) {
                break;
            }

            List<ItemStack> cartas = getCartas(p, x);
            itensfor:
            for (int y = 0; y < cartas.size(); y++) {
                ItemStack item = cartas.get(y);
                if (item != null) {
                    Carta car = ControleCartas.getCarta(item);

                    if (car.getNome().equals(c.getNome())) {
                        acho = true;

                        cartas.set(y, null);
                        saveCards(p, cartas, x);
                        break pagesfor;
                    }
                }
            }
        }
        return acho;

    }

    public static void saveCardsByArmor(UUID p, Armadura ar, List<ItemStack> itens) {
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT 1 FROM cartasave WHERE uuid = '" + p.toString() + "'");
            if (!rs.next()) {
                String execut = "INSERT INTO cartasave (`uuid`,";
                for (Armadura r : Armadura.values()) {
                    String v = ")";
                    if (r != Armadura.values()[Armadura.values().length - 1]) {
                        v = ",";
                    }
                    execut += '`' + r.name() + '`' + v;
                }

                execut += " VALUES('" + p.toString() + "',";
                for (Armadura r : Armadura.values()) {
                    String v = ")";
                    if (r != Armadura.values()[Armadura.values().length - 1]) {
                        v = ",";
                    }
                    String valor;
                    if (r == ar) {
                        valor = CartasToString(itens);
                    } else {
                        valor = "null,null,null,null,null,null,null,null,null";
                    }
                    execut += "'" + valor + "'" + v;
                }

                conn.createStatement().executeUpdate(execut);

            } else {
                conn.createStatement().executeUpdate("UPDATE cartasave SET " + ar.name() + " = '" + CartasToString(itens) + "' WHERE uuid = '" + p.toString() + "'");
            }
            //conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static List<ItemStack> getCardsByArmorAndRemoveFromDB(UUID p, Armadura ar) {
        List<ItemStack> itens;
        try {
            String s = "SELECT " + ar.name() + " FROM cartasave WHERE `uuid` = '" + p.toString() + "'";
            ResultSet rs = conn.createStatement().executeQuery(s);
            if (rs.next()) {
                List<ItemStack> doreturn = new ArrayList();
                List<ItemStack> page = StringToCartas(rs.getString(ar.name()));
                for (int x = 0; x < page.size(); x++) {
                    ItemStack item = page.get(x);

                    Carta c = ControleCartas.getCarta(item);
                    if (c != null) {

                        if (hasCardOnStockAndRemove(p, c)) {
                            doreturn.add(x, item);

                        } else {
                            doreturn.add(x, null);

                        }
                    } else {
                        doreturn.add(x, null);

                    }

                }
                while (doreturn.size() < 9) {
                    doreturn.add(null);
                }
                itens = doreturn;
            } else {
                itens = Arrays.asList(null, null, null, null, null, null, null, null, null);
            }
        } catch (SQLException ex) {
            itens = Arrays.asList(null, null, null, null, null, null, null, null, null);
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itens;

    }

    public static int getCardsVendor(UUID uuid) {
        int x = 0;
        try {

            ResultSet rs = conn.createStatement().executeQuery("SELECT 1 FROM shop WHERE `vendedor` ='" + uuid.toString() + "'");
            while (rs.next()) {
                x++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return x;
    }

    public static ArrayList<CartaAVenda> getCardsByVendor(UUID uuid) {
        ArrayList<CartaAVenda> cartas = new ArrayList<>();
        try {

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM shop WHERE `vendedor` ='" + uuid.toString() + "'");
            while (rs.next()) {
                cartas.add(new CartaAVenda(ControleCartas.getCardByName(rs.getString("carta")), UUID.fromString(rs.getString("vendedor")), rs.getInt("preco"), rs.getInt("id"), rs.getString("nomevendedor")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cartas;
    }

    public static ArrayList<CartaAVenda> getCardsByRaridade(Raridade r, UUID uuid) {
        ArrayList<CartaAVenda> cartas = new ArrayList<>();
        try {

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM shop WHERE `raridade` ='" + r.name() + "' AND `vendedor` !='" + uuid.toString() + "'");
            while (rs.next()) {
                cartas.add(new CartaAVenda(ControleCartas.getCardByName(rs.getString("carta")), UUID.fromString(rs.getString("vendedor")), rs.getInt("preco"), rs.getInt("id"), rs.getString("nomevendedor")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cartas;
    }

    public static ArrayList<CartaAVenda> getCardsByArmor(Carta.Armadura r, UUID uuid) {
        ArrayList<CartaAVenda> cartas = new ArrayList<>();
        try {

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM shop WHERE `armadura` ='" + r.name() + "' and `vendedor` != '" + uuid.toString() + "'");
            while (rs.next()) {
                cartas.add(new CartaAVenda(ControleCartas.getCardByName(rs.getString("carta")), UUID.fromString(rs.getString("vendedor")), rs.getInt("preco"), rs.getInt("id"), rs.getString("nomevendedor")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cartas;
    }

    public static void addAction(final UUID vendedor, final String vendedorname, final String act, final int quanto) {

        //      conn.createStatement().execute("CREATE TABLE IF NOT EXISTS gemas (uuid VARCHAR(200),oque VARCHAR(10),quanto INTEGER)");
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    conn.createStatement().executeUpdate("INSERT INTO registros (`uuid`,`nick`,`acao`,`quanto`,`quando`) VALUES('" + vendedor.toString() + "','" + vendedorname + "','" + act + "'," + quanto + ",CURRENT_TIMESTAMP)");
                    //conn.commit();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

    }

    public static void addCartToShop(UUID vendedor, String nomevendedor, Carta c, int preco) {
        try {
            conn.createStatement().executeUpdate("INSERT INTO shop (`vendedor`,`carta`,`armadura`,`raridade`,`preco`,`nomevendedor`) VALUES('" + vendedor.toString() + "','" + c.getNome() + "','" + c.getArmadura().name() + "','" + c.getRaridade().name() + "','" + preco + "','" + nomevendedor + "')");
            //conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<ItemStack> EnderChest(UUID uuid) {
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT `cartas` FROM enderchest WHERE uuid='" + uuid.toString() + "'");
            if (rs.next()) {
                return StringToCartas(rs.getString("cartas"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Arrays.asList(null, null, null, null, null, null, null, null, null);
    }

    public static void saveEnderchest(final List<ItemStack> itens, final UUID uuid) {

        try {
            String salva = CartasToString(itens);
            ResultSet rs = conn.createStatement().executeQuery("SELECT 1 FROM enderchest WHERE uuid='" + uuid.toString() + "'");
            if (rs.next()) {
                conn.createStatement().executeUpdate("UPDATE enderchest SET cartas ='" + salva + "' WHERE uuid = '" + uuid.toString() + "'");
            } else {

                conn.createStatement().executeUpdate("INSERT INTO enderchest (`uuid`,`cartas`) VALUES('" + uuid.toString() + "','" + salva + "')");

            }

            //conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static List<ItemStack> StringToCartas(String g) {
        List<ItemStack> cartas = new ArrayList<>();
        if (g != null && g.contains(",")) {
            String[] v = g.split(",");
            for (String cv : v) {
                if (!cv.equalsIgnoreCase("null")) {
                    Carta car = ControleCartas.getCardByName(cv);
                    if (car != null) {

                        ItemStack item = car.toItemStack();
                        try {

                            cartas.add(item);

                        } catch (NumberFormatException e) {

                            cartas.add(null);
                        }
                    } else {

                        cartas.add(null);
                    }

                } else {
                    cartas.add(null);
                }
            }

        }
        return cartas;
    }

    public static void addCard(UUID p, ItemStack carta) {
        for (int x = 1; x < Integer.MAX_VALUE; x++) {
            List<ItemStack> itens = getCartas(p, x);
            int freeslots = 0;
            int firstnull = -1;
            for (int c = 0; c < itens.size(); c++) {
                ItemStack i = itens.get(c);
                if (i == null) {
                    freeslots++;
                    if (firstnull == -1) {
                        firstnull = c;
                    }
                }
            }
            if (freeslots > 0) {

                itens.set(firstnull, carta);
                saveCards(p, itens, x);
                break;
            }
        }
    }

    public static void addGeral(final ItemStack i) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ResultSet rs = conn.createStatement().executeQuery("SELECT uid FROM Players");
                    while (rs.next()) {
                        addCard(UUID.fromString(rs.getString("uid")), i);
                    }
                    Bukkit.broadcastMessage("§aTodos tem uma nova carta no estoque!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

    }

    public static int getFreeSlots(List<ItemStack> itens) {
        int x = 0;
        for (ItemStack i : itens) {
            if (i == null) {
                x++;
            }
        }
        return x;
    }

    public static List<ItemStack> getCartas(UUID p, int page) {
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT itens FROM estoque WHERE uuid = '" + p.toString() + "' AND pagina ='" + page + "'");
            if (rs.next()) {
                return Arrays.asList(SyncInvUtils.DeserializeItemStackZipado(SyncInvUtils.BlobToBytes(rs.getBlob("itens"))));
            } else {
                List<ItemStack> itens = new ArrayList<>();
                for (int x = 0; x < 54; x++) {
                    if (x == 45 || x == 53) {
                        itens.add(MenuUtils.getMenuItem(Material.STONE, "null", null));
                    } else {
                        itens.add(null);
                    }
                }
                return itens;

            }
        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<List<ItemStack>> getPages(UUID uuid) {
        ArrayList<List<ItemStack>> pages = new ArrayList();
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT itens FROM estoque WHERE uuid = '" + uuid.toString() + "' ORDER BY pagina ASC");
            while (rs.next()) {
                pages.add(StringToCartas(rs.getString("itens")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pages;
    }

    public static void saveCards(UUID p, List<ItemStack> is, int page) {

        if (page != -1) {
            try {

                ItemStack[] itns = new ItemStack[is.size()];
                itns = is.toArray(itns);
                if (conn.createStatement().executeQuery("SELECT 1 FROM estoque WHERE uuid='" + p.toString() + "' AND pagina = '" + page + "'").next()) {
                    try {
                        PreparedStatement ps = conn.prepareStatement("UPDATE estoque SET itens =?" + " where uuid = '" + p.toString() + "' AND pagina ='" + page + "'");
                        ps.setBlob(1, SyncInvUtils.BytesToBlob(SyncInvUtils.SerializeItemStackZipado(itns)));
                        ps.executeUpdate();

                    } catch (SQLException ex) {
                        Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO estoque (`uuid`,`itens`,`pagina`) VALUES('" + p.toString() + "',?,'" + page + "')");
                    ps.setBlob(1, SyncInvUtils.BytesToBlob(SyncInvUtils.SerializeItemStackZipado(itns)));
                    ps.executeUpdate();
                }
                //conn.commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void startDatabase() {
        if (conn == null) {
            createConnection();
            createTables();
        }

    }

    public static boolean isAvenda(int id) {
        try {
            return conn.createStatement().executeQuery("SELECT 1 FROM shop WHERE id='" + id + "'").next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public static void removeCartaAVenda(int id) {
        try {
            conn.createStatement().executeUpdate("DELETE from shop WHERE id = '" + id + "'");
            //conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createConnection() {

        conn = CardWarsPlugin.conn;
        CardWarsPlugin.log.info("DATA BASE CONECTADO COM SUCESSO!");

        if (conn == null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop §c§l SEM CONEXAO!");
        }
    }

    public static List<CartaAVenda> getCardsSell(UUID uuid) {
        ArrayList<CartaAVenda> cartas = new ArrayList<>();
        try {

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM shop WHERE vendedor !='" + uuid + "'");
            while (rs.next()) {
                cartas.add(new CartaAVenda(ControleCartas.getCardByName(rs.getString("carta")), UUID.fromString(rs.getString("vendedor")), rs.getInt("preco"), rs.getInt("id"), rs.getString("nomevendedor")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return cartas;
    }

    public static CartaAVenda getCartaAVenda(int id) {
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM shop WHERE id ='" + id + "'");
            if (rs.next()) {
                return new CartaAVenda(ControleCartas.getCardByName(rs.getString("carta")), UUID.fromString(rs.getString("vendedor")), rs.getInt("preco"), rs.getInt("id"), rs.getString("nomevendedor"));

            }

        } catch (SQLException ex) {
            Logger.getLogger(CardsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
