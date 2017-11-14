/*

 */
package truco.plugin.events;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import truco.plugin.damage.ModificadorDano;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CustomDamageEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private ArrayList<String> cancelado = new ArrayList();
    private HashMap<String, Double> knockbackmod = new HashMap();
    private ArrayList<ModificadorDano> damagemod = new ArrayList();
    private ArrayList<ModificadorDano> damagemult = new ArrayList();
    private double damage;
    private LivingEntity tomou = null;
    private LivingEntity bateu = null;
    private Player ptomou = null;
    private Player pbateu = null;
    private CausaDano cause = null;
    private Projectile projetil = null;
    private String nome;
    private boolean knockback;
    private double bonus = 0;

    public CausaDano getCause() {
        return cause;
    }

    public double getBonus() {
        return bonus;
    }

    public CustomDamageEvent(double damage, LivingEntity tomou, LivingEntity bateu, boolean kn, CausaDano cause, Projectile projetil, String nome) {
        this.damage = damage;
        if (nome == null) {
            this.nome = "";
        } else {
            this.nome = nome;
        }
        this.knockback = kn;
        if (tomou != null) {
            this.tomou = tomou;
            if (tomou.getType() == EntityType.PLAYER) {
                this.ptomou = (Player) tomou;
            }

        }
        if (bateu != null) {
            this.bateu = bateu;
            if (bateu.getType() == EntityType.PLAYER) {
                this.pbateu = (Player) bateu;
            }

        }
        if (cause != null) {
            this.cause = cause;
        } else {
            this.cause = CausaDano.UNKNOWN;
        }
        if (projetil != null) {
            this.projetil = projetil;
        }
    }

    public boolean isCancelled() {
        return !cancelado.isEmpty();
    }

    public double getInitialDamage() {
        return damage;
    }

    public Projectile getProjetil() {
        return projetil;
    }

    public double getFinalDamage() {
        double damagee = getInitialDamage();

        damagee += bonus;
        for (ModificadorDano mult : this.damagemult) {
            damagee *= mult.getDamage();
        }
        for (ModificadorDano mult : this.damagemod) {
            damagee += mult.getDamage();
        }
        return damagee;
    }

    public LivingEntity getBateu() {
        return bateu;
    }

    public LivingEntity getTomou() {
        return tomou;
    }

    public LivingEntity GetDamagerEntity(boolean ranged) {
        if (ranged) {
            return this.bateu;
        }
        if (this.projetil == null) {
            return this.bateu;
        }
        return null;
    }

    public Player getPlayerBateu() {
        return pbateu;
    }

    public Player getPlayerTomou() {
        return ptomou;
    }

    public void addDamageMult(double mul, String causa) {
        damagemult.add(new ModificadorDano(causa, mul));
    }

    public void setKnockback(boolean knockback) {
        this.knockback = knockback;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void addDamage(double damage) {
        this.bonus += damage;
    }

    public boolean isKnockback() {
        return knockback;
    }

    public String getNome() {
        return nome;
    }

    public HashMap<String, Double> getKnockbackMod() {
        return knockbackmod;
    }

    public void setCancelled(String rasao) {
        cancelado.add(rasao);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public void addKnockBack(String no, double kn) {
        knockbackmod.put(no, kn);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static enum CausaDano {

        ATAQUE(ChatColor.RED),
        MAGIA(ChatColor.LIGHT_PURPLE),
        MAGIA_FOGO(ChatColor.LIGHT_PURPLE),
        MAGIA_AGUA(ChatColor.LIGHT_PURPLE),
        MAGIA_RAIO(ChatColor.LIGHT_PURPLE),
        FLECHA(ChatColor.RED),
        VENENO(ChatColor.DARK_GREEN),
        EXPLOSAO(ChatColor.RED),
        FOGO(ChatColor.GOLD),
        UNKNOWN(ChatColor.GRAY),
        SKILL_ATAQUE(ChatColor.RED),
        FALL(ChatColor.GRAY),
        REAL(ChatColor.AQUA);

        ChatColor cor;

        private CausaDano(ChatColor cor) {
            this.cor = cor;
        }

        public ChatColor getCor() {
            return cor;
        }

    }
}
