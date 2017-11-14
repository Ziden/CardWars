/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truco.plugin.matchmaking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author usuario
 * 
 */

public class PossibleMatch {

    public static int teamNumber = 5;
    
    public int closedTeamId = -1;
    
    public HashSet<PlayerBlock> players = new HashSet<PlayerBlock>();

    public int needPlayers() {
        int n = 0;
        for(PlayerBlock b : players)
            n+= b.players.size();
        return teamNumber-n;
    }
    
    public List<UUID> getAllPlayers() {
        List<UUID> pls = new ArrayList<UUID>();
        for(PlayerBlock b : players)
            pls.addAll(b.players);
        return pls;
    }

}
