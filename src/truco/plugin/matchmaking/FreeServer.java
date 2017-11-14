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
public class FreeServer {
    
    public FreeServer(int serverId, String serverName) {
        this.serverId = serverId;
        this.serverName = serverName;
    }
    
    public int serverId;
    public String serverName;
}
