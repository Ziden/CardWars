/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.functions.game;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import truco.plugin.cards.ControleCartas;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.stats.MaisRegenMana;
import truco.plugin.cards.stats.Stats;

/**
 *
 * @author usuario
 *
 */
public class Mana {

    public int maxMana = 100;
    public int mana = 100;
    public static HashMap<UUID, Mana> manas = new HashMap<UUID, Mana>();

    public static Mana getMana(Player p) {
        if (manas.containsKey(p.getUniqueId())) {
            return manas.get(p.getUniqueId());
        } else {
            Mana m = new Mana();
            manas.put(p.getUniqueId(), m);
            return m;
        }
    }

    public static void startRegenTimer() {

        Runnable r = new Runnable() {

            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    int base = 5;
                    int regen = base;
                    if (ControleCartas.getStats(p) != null) {
                        for (Stats st : ControleCartas.getStats(p)) {
                            if (st instanceof MaisRegenMana) {
                                regen += (base / 100) * st.getX();
                            }
                        }
                    }

                    changeMana(p, regen);
                }
            }

        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, r, 20 * 5, 20 * 5);
    }

    public static boolean temMana(Player p, int qt) {
        Mana mana = getMana(p);
        return mana.mana >= qt;

    }

    public static boolean spendMana(Player p, int qt) {
        Mana mana = getMana(p);
        if (mana.mana >= qt) {
            changeMana(p, -qt);
            return true;
        } else {
            p.sendMessage(ChatColor.BLUE + "Falta mana em voce: " + mana.mana + "/" + mana.maxMana + " - " + ChatColor.RED + qt);
            return false;
        }

    }

    public static void changeMana(Player p, int qtd) {
        Mana mana = getMana(p);
        mana.maxMana = 100;
        if (mana.mana == mana.maxMana && qtd > 0) {
            return;
        }
        mana.mana += qtd;
        if (mana.mana > mana.maxMana) {
            mana.mana = mana.maxMana;
        }
        if (mana.mana < 0) {
            mana.mana = 0;
        }
        p.setLevel(mana.mana);
        p.setExp(Math.min(0.999F, (float) mana.mana / mana.maxMana));
        p.sendMessage(ChatColor.BLUE + "Mana: " + mana.mana + "/" + mana.maxMana);
    }

    public static void setMana(Player p, int qtd) {
        Mana mana = getMana(p);
        mana.mana = qtd;
        p.setLevel(mana.mana);
        p.setExp(Math.min(0.999F, (float) mana.mana / mana.maxMana));
    }

}
