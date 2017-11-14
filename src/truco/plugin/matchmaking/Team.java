/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truco.plugin.matchmaking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author usuario
 */
public class Team {
    
    public List<UUID> players = new ArrayList<UUID>(5);
    public int teamId;
    
}
