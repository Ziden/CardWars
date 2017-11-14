/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.utils;

/**
 *
 * @author Júnior
 */
public enum IconLib {

    VIDA((char) 37504),
    STAFFICON((char) 37519),
    BEAPRO((char) 37524),
    CARDMASTER((char) 37521),
    PLAYERNORMAL((char) 37487),
    DOMINIONBASE((char) 37522),
    CAVEIRA((char) 37505),
    CARDHERO((char) 37523),
    YOUTUBER((char) 37525);
    char ch;

    private IconLib(char c) {
        ch = c;

    }

    @Override
    public String toString() {
        return ch + "";
    }

    public char getChar() {
        return ch;
    }

}
