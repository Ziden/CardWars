/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cards.skills;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.Carta;
import truco.plugin.cards.ControleCartas;
import truco.plugin.functions.game.Mana;
import truco.plugin.data.MetaShit;

/**
 *
 * @author usuario
 */
public abstract class Skill {
    
    public Carta vinculada = null;
    public int cd;
    public int mana;

    /**
     *
     * @param vinculada Carta vinculada
     * @param cud Cooldown da skill
     * @param mana Mana que gasta
     * @param stamina Stamina que gasta
     */
    public static String toEffectLocation(Location l) {
        return l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ();
    }
    
    public Skill(Carta vinculada, int cud, int mana) {
        this.vinculada = vinculada;
        this.cd = cud;
        this.mana = mana;
    }
    
    public static void addCd(Player p, String metadata) {
        if (!p.hasMetadata(metadata)) {
            MetaShit.setMetaObject(metadata, p, (long) System.currentTimeMillis() / 1000);
            return;
        }
        p.removeMetadata(metadata, CardWarsPlugin._instance);
        MetaShit.setMetaObject(metadata, p, (long) System.currentTimeMillis() / 1000);
    }
    
    public static boolean checkCD(Player p, String metadata, int s) {
        if (!p.hasMetadata(metadata)) {
            return true;
        }
        long quando = (long) MetaShit.getMetaObject(metadata, p) + s;
        long agora = System.currentTimeMillis() / 1000;
        boolean b = agora > quando;
        if (!b) {
            p.sendMessage(ChatColor.DARK_PURPLE + "Ainda falta §b" + (((agora - quando) - 1) * -1) + "§5 segundos para usar está habilidade!");
        }
        return b;
    }
    
    public int getChannelingTime() {
        return -1;
    }
    
    public void castSkill(Player p) {
        if (!ControleCartas.canUseCard(p, vinculada)) {
            p.sendMessage(Carta.noPermToUse);
            return;
        }
        if (p.hasMetadata("Channeling") && getChannelingTime() != -1) {
            p.sendMessage("§eVocê já está conjurando outra habilidade!");
            return;
        }
        //int custostamina = checkStamina(stamina, p);
        if (Mana.temMana(p, mana)) {
            if (checkCD(p, vinculada.getNome() + "cdcd", cd)) {
                if (onCast(p)) {
                    addCd(p, vinculada.getNome() + "cdcd");
                    Mana.spendMana(p, mana);
                }
            }
        } else {
            p.sendMessage("§9Falta " + (mana - Mana.getMana(p).mana) + " de mana para usar está skill!");
        }
    }
    
    public abstract String getName();
    
    public abstract boolean onCast(Player p);
    
}
