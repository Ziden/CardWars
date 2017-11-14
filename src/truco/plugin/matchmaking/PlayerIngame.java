/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package truco.plugin.matchmaking;

/**
 *
 * @author usuario
 */
public class PlayerIngame {
    
    public int gameId;
    public String serverName;
    public int team;
    
    public PlayerIngame(int gameId, String serverName, int team) {
        this.gameId = gameId;
        this.serverName = serverName;
        this.team = team;
    }
}
