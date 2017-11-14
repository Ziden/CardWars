/*

 */
package truco.plugin.damage;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ModificadorDano {

    private String nome;
    private double mod;

    public ModificadorDano(String nome, double mod) {
        this.nome = nome;
        this.mod = mod;
    }

    public String getNome() {
        return nome;
    }

    public double getDamage() {
        return mod;
    }

}
