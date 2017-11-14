/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.menus.shop;

import truco.plugin.cards.Carta;
import java.util.UUID;

/**
 *
 * @author Carlos
 */
public class CartaAVenda {

    public Carta c;
    public UUID vendedor;
    public int preco;
    public int id;
    public String nomevendedor;

    public CartaAVenda(Carta c, UUID vendedor, int preco, int ide, String nomevendedor) {
        this.c = c;
        id = ide;
        this.nomevendedor = nomevendedor;
        this.vendedor = vendedor;
        this.preco = preco;
    }

}
