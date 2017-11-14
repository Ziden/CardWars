/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cards.stats;

import truco.plugin.cards.Carta;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class Stats {

    String nome;
    Carta c ;
    int x;

    public Stats(String nome, Carta c, int x) {
        this.nome = nome;
        this.c = c;
        this.x = x;
    }

    public String getNome() {
        return nome;
    }

    
    public Carta getCarta() {
        return c;
    }

    public int getX() {
        return x;
    }
   
}
