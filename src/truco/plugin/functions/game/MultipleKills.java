/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.functions.game;

import java.util.HashMap;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import truco.plugin.arena.Arena.Team;
import truco.plugin.data.mysql.MatchHistoryDB;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.SoundUtils;
import truco.plugin.utils.SoundUtils.Som;
import truco.plugin.utils.Utils;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class MultipleKills {

    public static HashMap<UUID, Integer> mkills = new HashMap();
    private static HashMap<UUID, Integer> tasks = new HashMap();
    private static boolean first = false;
    public static HashMap<UUID, Integer> semorrer = new HashMap();
    private static HashMap<UUID, Integer> bonusgold = new HashMap();
    private static HashMap<UUID, Integer> bonusexp = new HashMap();
    private static HashMap<UUID, HashMap<KillType, Integer>> kills = new HashMap();

    public static enum KillType {

        FIRSTBLOOD, DOUBLEKILL, TRIPLEKILL, QUADRAKILL, PENTAKILL;
    }

    public static void mata(final Player p, final Player morto) {
        if (CardWarsPlugin.getArena() != null) {
            if (p == morto) {
                return;
            }
            Team t = CardWarsPlugin.getArena().getTeam(p.getUniqueId());
            if (t == null) {
                return;
            }
            if (!first) {
                firstBlood(p, t);
                first = true;
            }

            final UUID uuid = p.getUniqueId();
            final UUID morreuuuid = morto.getUniqueId();
            int tem = 0;
            if (semorrer.containsKey(morreuuuid)) {
                if (semorrer.get(morreuuuid) > 2) {

                    ChatUtils.broadcastMessage("§4" + p.getName() + " §6acabou com o kill streak de §4" + morto.getName() + "§c(" + semorrer.get(morreuuuid) + ") §6!");

                    semorrer.remove(morreuuuid);

                }
            }
            int mtsemmorrer = 1;
            if (semorrer.containsKey(uuid)) {
                mtsemmorrer += semorrer.get(uuid);
                semorrer.remove(uuid);
            }
            semorrer.put(uuid, mtsemmorrer);
            mataSemMorrer(mtsemmorrer, p);
            if (mkills.containsKey(uuid)) {
                tem = mkills.get(uuid);
                mkills.remove(uuid);
            }
            tem++;
            final int mp = tem;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    OfflinePlayer of1 = Bukkit.getOfflinePlayer(uuid);
                    OfflinePlayer of2 = Bukkit.getOfflinePlayer(morreuuuid);
                    MatchHistoryDB.addKill(of1, of2, CardWarsPlugin.getArena(), mp);
                }
            }).start();
            if (tem == 2) {
                doubleKill(p, t);
            } else if (tem == 3) {
                tripleKill(p, t);
            } else if (tem == 4) {
                quadraKill(p, t);
            } else if (tem == 5) {
                pentaKill(p, t);
            } else {
                if (mtsemmorrer < 3) {
                    p.playSound(p.getLocation(), Sound.LEVEL_UP, 1000, 1);
                }

            }
            if (tasks.containsKey(uuid)) {
                Bukkit.getScheduler().cancelTask(tasks.get(uuid));
                tasks.remove(uuid);
            }

            mkills.put(uuid, tem);
            tasks.put(uuid, Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    if (mkills.containsKey(uuid)) {
                        mkills.remove(uuid);
                        tasks.remove(uuid);
                    }
                }
            }, 8 * 20));
        }
    }

    public static HashMap<KillType, Integer> getKillsTypes(UUID uuid) {
        if (kills.containsKey(uuid)) {
            return kills.get(uuid);
        }
        return null;
    }

    public static void runnableAfter(final Player p, final Runnable r, int tempo) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                if (p != null) {

                    r.run();
                }
            }
        }, tempo);

    }

    public static void mataSemMorrer(int qt, final Player p) {
        String titlemsg = null;
        String subtitlemsg = null;
        Som s = null;
        if (qt == 3) {
            titlemsg = "§6Killing Spree";
            subtitlemsg = "§7Matar 3 inimigos sem morrer!";
            s = Som.KILLINGSPREE;
        } else if (qt == 4) {
            titlemsg = "§6Dominating";
            subtitlemsg = "§7Matar 4 inimigos sem morrer!";
            s = Som.DOMINATING;
        } else if (qt == 5) {
            titlemsg = "§6Mega Kill";
            subtitlemsg = "§7Matar 5 inimigos sem morrer!";
            s = Som.MEGAKILL;
        } else if (qt == 6) {
            titlemsg = "§6Unstoppable";
            subtitlemsg = "§7Matar 6 inimigos sem morrer!";
            s = Som.UNSTOPPABLE;
        } else if (qt == 7) {
            titlemsg = "§6Wicked Sick";
            subtitlemsg = "§7Matar 7 inimigos sem morrer!";
            s = Som.WICKEDSICK;
        } else if (qt == 8) {
            titlemsg = "§6Monster kill";
            subtitlemsg = "§7Matar 8 inimigos sem morrer!";
            s = Som.MONSTERKILL;
        } else if (qt == 9) {
            titlemsg = "§6Godlike";
            subtitlemsg = "§7Matar 9 inimigos sem morrer!";
            s = Som.GODLIKE;
        } else if (qt >= 10) {
            titlemsg = "§6Beyond Godlike";
            subtitlemsg = "§7Matar 10 ou mais inimigos sem morrer!";
            s = Som.HOLYSHIT;
        }
        if (s != null && titlemsg != null && subtitlemsg != null) {

            final Som sr = s;
            final String titlemsgr = titlemsg;
            final String subtitlemsgr = subtitlemsg;
            ChatUtils.broadcastMessage("§4" + ChatColor.stripColor(titlemsgr) + "§6 de §4" + p.getName() + " §6!");
            runnableAfter(p, new Runnable() {

                @Override
                public void run() {

                    SoundUtils.playSound(sr, Integer.MAX_VALUE, p);
                    Utils.sendTitle(p, titlemsgr, subtitlemsgr, 5, 10, 5);

                }
            }, 15);
        }
    }

    public static void addBonusGold(UUID uuid, int gold) {
        int tem = gold;
        if (bonusgold.containsKey(uuid)) {
            tem += bonusgold.get(uuid);
            bonusgold.remove(uuid);
        }
        bonusgold.put(uuid, tem);
    }

    public static void addBonusExp(UUID uuid, int gold) {
        int tem = gold;
        if (bonusexp.containsKey(uuid)) {
            tem += bonusexp.get(uuid);
            bonusexp.remove(uuid);
        }
        bonusexp.put(uuid, tem);
    }

    public static int getBonusGold(UUID uuid) {
        if (bonusgold.containsKey(uuid)) {
            return bonusgold.get(uuid);
        }
        return 0;
    }

    public static int getBonusExp(UUID uuid) {
        if (bonusgold.containsKey(uuid)) {
            return bonusgold.get(uuid);
        }
        return 0;
    }

    public static void addKillType(KillType k, UUID jogador) {

        if (!kills.containsKey(jogador)) {
            HashMap<KillType, Integer> ki = new HashMap();
            for (KillType kt : KillType.values()) {
                ki.put(kt, 0);
            }
            kills.put(jogador, ki);
        }
        HashMap<KillType, Integer> killsdojogador = kills.get(jogador);
        int vai = killsdojogador.get(k) + 1;
        killsdojogador.remove(k);
        killsdojogador.put(k, vai);
    }

    public static void firstBlood(Player p, Team t) {
        ChatUtils.broadcastMessage("§aFirst Blood de " + t.getCor() + p.getName());
        SoundUtils.playSound(SoundUtils.Som.FIRSTBLOOD, Integer.MAX_VALUE, p);
        addKillType(KillType.FIRSTBLOOD, p.getUniqueId());
    }

    public static void doubleKill(Player p, Team t) {
        ChatUtils.broadcastMessage("§aDouble kill de " + t.getCor() + p.getName() + " !");
        Utils.sendTitle(p, "§aDouble Kill", "§7Dois kills seguidos", 0, 30, 10);
        SoundUtils.playSound(SoundUtils.Som.DOUBLEKILL, Integer.MAX_VALUE, p);
        addKillType(KillType.DOUBLEKILL, p.getUniqueId());
    }

    public static void tripleKill(Player p, Team t) {
        ChatUtils.broadcastMessage("§cTriple kill de " + t.getCor() + p.getName() + " !");
        Utils.sendTitle(p, "§cTriple Kill", "§7Quatro kills seguidos", 0, 30, 10);

        SoundUtils.playSound(SoundUtils.Som.TRIPLEKILL, Integer.MAX_VALUE, p);
        addBonusExp(p.getUniqueId(), 2);
        addBonusGold(p.getUniqueId(), 1);
        addKillType(KillType.TRIPLEKILL, p.getUniqueId());
    }

    public static void quadraKill(Player p, Team t) {
        ChatUtils.broadcastMessage("§4§lUltra kill de §r" + t.getCor() + "§l" + p.getName() + " !");
        SoundUtils.playSound(SoundUtils.Som.QUADRAKILL, Integer.MAX_VALUE, p);
        Utils.sendTitle(p, "§4Ultra Kill", "§7Quatro kills seguidos", 0, 30, 10);

        addBonusExp(p.getUniqueId(), 4);
        addBonusGold(p.getUniqueId(), 2);
        addKillType(KillType.QUADRAKILL, p.getUniqueId());
    }

    public static void pentaKill(Player p, Team t) {
        ChatUtils.broadcastMessage("§5§lRampage de §r" + t.getCor() + "§l" + p.getName() + " !");
        SoundUtils.playSound(SoundUtils.Som.PENTAKILL, Integer.MAX_VALUE, p);
        addBonusExp(p.getUniqueId(), 6);
        Utils.sendTitle(p, "§5Rampage", "§7Cinco kills seguidos", 0, 30, 10);

        addBonusGold(p.getUniqueId(), 3);
        addKillType(KillType.PENTAKILL, p.getUniqueId());
    }
}
