/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cards;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import truco.plugin.data.MetaShit;

/**
 *
 * @author usuario
 */
public abstract class EfeitoProjetil {

    public static void addEfeito(Projectile proj, EfeitoProjetil efeito) {
        List<EfeitoProjetil> efeitos ;
        if(proj.hasMetadata("efeito")){
            efeitos = (List<EfeitoProjetil>)MetaShit.getMetaObject("efeito", proj);
        }else{
            efeitos = new ArrayList();
        }
        efeitos.add(efeito);
        MetaShit.setMetaObject("efeito", proj, efeitos);
    }

    public EfeitoProjetil(Player shooter, Projectile p) {
        this.shooter = shooter;
        this.projectile = p;
    }
    private Player shooter;
    private Projectile projectile;

    public Player getShooter() {
        return shooter;
    }

    public abstract void causaEfeito(Player gotHit, Player Shooter, Projectile projectile);
}
