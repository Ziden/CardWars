/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.functions.groups;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import truco.plugin.utils.SoundUtils;

/**
 *
 * @author Carlos
 */
public class Grupo {

    private int id;
    private ArrayList<UUID> players = new ArrayList<>();

    public Grupo(Player lider, Player dois) {
        id = ControleGrupos.grupos.size();
        players.add(lider.getUniqueId());
        players.add(dois.getUniqueId());
        ControleGrupos.grupos.add(this);
    }

    public int getId() {
        return id;
    }

    public UUID getLider() {
        return players.get(0);
    }

    public boolean isMember(Player p) {
        return players.contains(p.getUniqueId());
    }

    public int size() {
        return players.size();
    }

    public ArrayList<UUID> getPlayers() {
        return players;
    }

    public boolean addMember(Player p) {
        if (!players.contains(p.getUniqueId())) {
            players.add(p.getUniqueId());

            return true;
        }
        return false;
    }

    public boolean removeMember(Player p) {
        if (players.contains(p.getUniqueId())) {
            players.remove(p.getUniqueId());
            sendGroupMsg("§e§l" + p.getName() + " saiu do seu grupo!");
            return true;

        }
        return false;
    }

    public boolean kickMember(Player p) {
        if (players.contains(p.getUniqueId())) {
            players.remove(p.getUniqueId());
            sendGroupMsg("§c§l" + p.getName() + " foi removido do seu grupo!");
            SoundUtils.playSound(SoundUtils.Som.SAIGRUPO, Integer.MAX_VALUE, p);

            return true;

        }
        return false;
    }

    public void disband() {
        sendGroupMsg("§cSeu grupo foi excluido!");
        for (Player p : getOnlinePlayers()) {
            SoundUtils.playSound(SoundUtils.Som.SAIGRUPO, Integer.MAX_VALUE, p);
        }
        ControleGrupos.grupos.remove(this);
    }

    public List<Player> getOnlinePlayers() {
        List<Player> pl = new ArrayList();
        for (UUID uud : getPlayers()) {
            Player p = Bukkit.getPlayer(uud);
            if (p != null) {
                pl.add(p);
            }
        }
        return pl;
    }

    public void sendGroupMsg(String msg) {
        for (UUID uud : getPlayers()) {
            Player p = Bukkit.getPlayer(uud);
            if (p != null) {
                p.sendMessage("§5§l[GRUPO]§r " + msg);
            }
        }
    }

    public void removeGroup() {
        ControleGrupos.grupos.remove(this);
    }
}
