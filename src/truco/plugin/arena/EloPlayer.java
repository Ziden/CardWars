/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truco.plugin.arena;

import java.util.UUID;
/**
 *
 * @author usuario
 */
public class EloPlayer {
    
    public UUID u;
    public int elo;
    public String nome;
    public EloPlayer(UUID u, int elo,String name) {
        this.u = u;
        this.elo = elo;
        this.nome = name;
    }
    
}
