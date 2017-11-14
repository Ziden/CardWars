/*

 */
package truco.plugin.damage;

import truco.plugin.cards.Carta;
import truco.plugin.events.CustomDamageEvent.CausaDano;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class Dano {

    private double quanto;
    private String quem;
    private CausaDano causa;
    private long quando;
    private String nome;
    
    public Dano(double quanto, String quem, CausaDano causa, long quando, String nome) {
        this.quanto = quanto;
        this.quem = quem;
        this.causa = causa;
        this.quando = quando;
        this.nome = nome;
    }

    public String getQuem() {
        return quem;
    }

    public double getQuanto() {
        return quanto;
    }

    public long getQuando() {
        return quando;
    }

    public CausaDano getCausa() {
        return causa;
    }

    public String getNome() {
        return nome;
    }

   
}
