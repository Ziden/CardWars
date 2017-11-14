/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truco.plugin.arena;

/**
 *
 * @author usuario
 */
public enum ArenaType {

    DEATHMATCH("tdm"),
    KOMQUISTA("komquista"),
    DOMINIO("dominio");

    public String name;
     
    private ArenaType(String name) {
        this.name = name;
    }
    
}
