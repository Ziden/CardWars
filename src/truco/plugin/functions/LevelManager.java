/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.functions;

import br.pj.newlibrarysystem.cashgame.GemManager;
import java.util.UUID;
import org.bukkit.entity.Player;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.ChatUtils;

/**
 *
 * @author Júnior
 */
public class LevelManager {

    public static void levelUp(Player p, int lvl) {
        for (LevelBonus lb : LevelBonus.values()) {
            if (lb.getLvl() == lvl) {
                ChatUtils.sendMessage(p, "§b§n" + lb.getMsg());
            }
        }
        if (lvl % 5 == 0) {
            ChatUtils.sendMessage(p, "§fVocê ganhou §auma gema §f!");
            CardsDB.addAction(p.getUniqueId(), p.getName(), "ganhou upando", 1);
            GemManager.AddGem(p.getUniqueId(), 1);
        }
    }

    public static boolean canUse(LevelBonus lb, UUID uuid) {
        int level = MatchMaker.db.getLevel(uuid);
        return level >= lb.getLvl();
    }

    public static enum LevelBonus {

        GOLDLEVEL(2, "Você agora pode dar dinheiro para jogadores e receber!"),
        SHOPLEVEL(2, "Você agora pode usar a loja de compra e vendas de cartas!"),
        GROUPLEVEL(1, "Você agora pode usar o sistema de grupos use /grupo para ver os comandos!");

        private int lvl;

        private String msg;

        public int getLvl() {
            return lvl;
        }

        public String getMsg() {
            return msg;
        }

        private LevelBonus(int lvl, String msg) {
            this.lvl = lvl;
            this.msg = msg;
        }

    }

}
