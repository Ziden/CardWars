/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.managers;

import java.util.UUID;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class PlayerInfo {

    private int elo;
    private int lvl;
    private int gold;
    private int exp;
    private UUID player;

    public PlayerInfo(int elo, int lvl, int gold, int exp, UUID uid) {
        this.elo = elo;
        this.lvl = lvl;
        this.gold = gold;
        this.exp = exp;
        player = uid;
    }

    public int getElo() {
        return elo;
    }

    public int getExp() {
        return exp;
    }

    public UUID getPlayer() {
        return player;
    }

    public int getLvl() {
        return lvl;
    }

    public int getGold() {
        return gold;
    }

}
