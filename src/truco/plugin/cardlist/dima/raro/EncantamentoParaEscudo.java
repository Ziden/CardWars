/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */

package truco.plugin.cardlist.dima.raro;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.cards.stats.MenosManaEscudo;
import truco.plugin.cards.stats.Stats;
/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class EncantamentoParaEscudo extends Carta{

    @Override
    public Raridade getRaridade() {
        return Raridade.RARO;
    }


    @Override
    public String getNome() {
        return "Encantamento Para Escudo";
    }


    @Override
    public String[] getDesc() {
        return new String[]{"Ao defender com o escudo gasta menos 10 de mana"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.DIMA;
    }

    @Override
    public Stats[] getStats() {
     return new Stats[]{new MenosManaEscudo(this, 10)};
    }

    

   




}
