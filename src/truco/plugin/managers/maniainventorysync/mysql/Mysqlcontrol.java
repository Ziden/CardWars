/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.managers.maniainventorysync.mysql;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import static truco.plugin.CardWarsPlugin.conn;
import truco.plugin.managers.maniainventorysync.utils.SyncInvUtils;
import truco.plugin.utils.Utils;
import truco.plugin.utils.efeitos.CustomEntityFirework;

/**
 *
 * @author Gabriel
 */
public class Mysqlcontrol {

    public static void InitMysql() {

        try {

            PreparedStatement statement = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `datainv` ( `id` int(11) NOT NULL AUTO_INCREMENT, `uuid` varchar(100) NOT NULL, `nick` varchar(20) NOT NULL, `loc` varchar(100), `inv` blob NOT NULL, UNIQUE KEY `uuid` (`uuid`), KEY `id` (`id`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;");
            statement.executeUpdate();
            SyncInvUtils.AddLog("Mysql Carregado com sucesso!");

        } catch (Exception e) {
            Bukkit.getPluginManager().disablePlugin(CardWarsPlugin._instance);
            ErroMysql(e);
        }
    }

    public static boolean TemSave(Player p) {
        try {

            Statement statement = conn.createStatement();
            String sql = "SELECT uuid FROM datainv WHERE uuid = '" + p.getUniqueId().toString() + "'";
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.next()) {
                return false;
            } else {
                return true;
            }

        } catch (SQLException ex) {
            ErroMysql(ex);
            return false;
        }
    }

    public static boolean InsertInfo(Player p) {

        try {

            Blob inventario = SyncInvUtils.BytesToBlob(SyncInvUtils.SerializeItemStackZipado(p.getInventory().getContents()));

            PreparedStatement statement = conn.prepareStatement("INSERT INTO datainv (uuid, nick, loc, inv) VALUES (?,?,?,?) ");
            statement.setString(1, p.getUniqueId().toString());
            statement.setString(2, p.getName().toLowerCase());
            statement.setString(3, truco.plugin.utils.LocUtils.locationToString(p.getLocation().clone()));
            statement.setBlob(4, inventario);

            statement.executeUpdate();
            return true;

        } catch (SQLException ex) {
            ErroMysql(ex);
            return false;
        }
    }

    public static boolean UpdateInfo(Player p) {
        try {

            Blob inventario = SyncInvUtils.BytesToBlob(SyncInvUtils.SerializeItemStackZipado(p.getInventory().getContents()));

            PreparedStatement statement = conn.prepareStatement("UPDATE datainv SET inv=?, nick=?, loc=? WHERE uuid = ?");
            statement.setBlob(1, inventario);
            statement.setString(2, p.getName().toLowerCase());
            statement.setString(3, truco.plugin.utils.LocUtils.locationToString(p.getLocation().clone()));
            statement.setString(4, p.getUniqueId().toString());
            statement.executeUpdate();
            return true;

        } catch (SQLException ex) {
            ErroMysql(ex);
            return false;
        }
    }

    public static boolean RestauraInfo(Player p) {
        try {

            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM datainv WHERE uuid = '" + p.getUniqueId().toString() + "'";
            ResultSet rs = statement.executeQuery(sql);
            rs.next();

            Blob inv = rs.getBlob("inv");

            if (p != null && p.isOnline()) {

                p.getInventory().setContents(SyncInvUtils.DeserializeItemStackZipado(SyncInvUtils.BlobToBytes(inv)));

                if (rs.getString("loc") != null) {
                    p.teleport(truco.plugin.utils.LocUtils.stringToLocation(rs.getString("loc")));
                }
                if (p.hasPermission("cardwars.vip")) {
                    final Location loc = p.getLocation().clone().add(0, 5, 0);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                        @Override
                        public void run() {
                            CustomEntityFirework.spawn(loc, Utils.LaunchRandomFirework());

                        }
                    });
                }
            }
            return true;

        } catch (SQLException ex) {
            ErroMysql(ex);
            return false;
        }
    }

    public static void ErroMysql(Exception e) {
        //Fazer oq acontece quando da erro no mysql
        SyncInvUtils.AddLog("ERRO MYSQL");
        e.printStackTrace();
    }
}
