package truco.plugin.matchmaking;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author usuario
 */
public class PlayerBlock {
        
        
    
        public PlayerBlock(List<UUID> players) {
            this.players = players;
        }
        
        public List<UUID> players = new ArrayList<UUID>(5);
    }