/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.arena.Dominion;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import truco.plugin.arena.Arena.Team;

/**
 *
 * @author Carlos
 */
public class Base {

    public Team dona;
    public UUID tadominando;
    public ProtectedRegion regiao;
    public String sufixo;
    Dominion d;

    public static Base getBase(ProtectedRegion r, Dominion d) {
        for (Base b : d.bases) {
            if (b.getRegiao().equals(r)) {
                return b;
            }
        }
        return null;
    }

    public Base(ProtectedRegion regiao, Dominion d) {
        this.regiao = regiao;
        this.d = d;
        String ultimaletra = regiao.getId().charAt(regiao.getId().length() - 1) + "";
        if (ultimaletra.equalsIgnoreCase("o")) {
            sufixo = "o";
        }else if(ultimaletra.equalsIgnoreCase("e")|| ultimaletra.equalsIgnoreCase("a")){
            sufixo = "a";
        }else{
            sufixo = "o";
        }

    }

    public ProtectedRegion getRegiao() {
        return regiao;
    }

    public Team getDona() {
        return dona;
    }

    public UUID getTadominando() {
        return tadominando;
    }

    public void setDona(Team dona) {
        this.dona = dona;
        for (Player p : Bukkit.getOnlinePlayers()) {
            d.createScore(p);
        }
    }

    public void setTadominando(UUID tadominando) {
        this.tadominando = tadominando;
    }

}
