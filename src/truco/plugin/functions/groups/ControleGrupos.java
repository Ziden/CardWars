/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.functions.groups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import truco.plugin.CardWarsPlugin;
import truco.plugin.managers.PermManager;
import truco.plugin.matchmaking.PossibleMatch;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.SoundUtils;

/**
 *
 * @author Carlos
 */
public class ControleGrupos {

    public static ArrayList<Grupo> grupos = new ArrayList<Grupo>();

    public static Grupo getGrupo(Player p) {
        for (Grupo g : grupos) {
            if (g.isMember(p)) {
                return g;
            }
        }
        return null;
    }
    public static HashMap<UUID, UUID> convites = new HashMap();

    public static void MandaConviteGrupo(final Player p, final Player membro) {

        if (convites.containsKey(p.getUniqueId()) || convites.containsValue(membro.getUniqueId())) {
            p.sendMessage("§5§l[GRUPO]§r §c§lVocê já está convidando alguem ou essa pessoa já esta sendo convidada!");
            return;
        }
        Grupo liderg = getGrupo(p);
        if (liderg != null && liderg.getLider() != p.getUniqueId()) {
            p.sendMessage("§5§l[GRUPO]§r §c§lVocê já está em um grupo e não é lider!");
            return;
        }
        if (!PermManager.GRUPO.playerHas(p) && (liderg != null && liderg.size() >= 3)) {
            p.sendMessage("§5§l[GRUPO]§r §cSomente pros conseguem criar grupos com 4+ pessoas!");
            return;
        }
        if (liderg != null && liderg.size() >= PossibleMatch.teamNumber) {
            p.sendMessage("§5§l[GRUPO]§r §c§lSeu grupo já atingiu " + PossibleMatch.teamNumber + " membros!");
            return;
        }
        if (getGrupo(membro) != null) {
            p.sendMessage("§5§l[GRUPO]§r §c§lEsse jogador já tem um grupo!");
            return;
        }
        {
            int quatro = 0;
            int elomedioporgrupo = 3800;
            int tem = 0;
            int pradividir = 0;
            if (getGrupo(p) != null) {

                for (Player pf : getGrupo(p).getOnlinePlayers()) {
                    int elo = MatchMaker.db.getElo(pf.getUniqueId());
                    tem += elo;
                    if (elo > 4000) {
                        quatro++;
                    }
                    pradividir++;
                }
                pradividir++;
                tem += MatchMaker.db.getElo(membro.getUniqueId());
            } else {
                pradividir += 2;
                int elo = MatchMaker.db.getElo(p.getUniqueId());
                tem += elo;
                int elom = MatchMaker.db.getElo(membro.getUniqueId());
                tem += elom;
                if (elo > 4000) {
                    quatro++;
                }
                if (elom > 4000) {
                    quatro++;
                }
            }
            tem = tem / pradividir;
            if (quatro >= 2) {
               p.sendMessage("§5§l[GRUPO]§r §dSeu grupo possui dois jogadores com mais de 4000 de elo!!");
               return;
            }
            if (tem > elomedioporgrupo) {
                p.sendMessage("§5§l[GRUPO]§r §dCom este jogador ultrapassa o elo medio do grupo de " + elomedioporgrupo + " !");
                return;
            }

        }
        SoundUtils.playSound(SoundUtils.Som.CONVITE, Integer.MAX_VALUE, membro);
        p.sendMessage("§5§l[GRUPO]§r §a§lVocê convidou o jogador " + membro.getName() + " para jogar!");
        membro.sendMessage("§5§l[GRUPO]§r §a§lVocê foi convidado para jogar por " + p.getName() + " use \"/grupo aceitar\" para jogar!");
        convites.put(p.getUniqueId(), membro.getUniqueId());
        Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                for (Entry<UUID, UUID> ent : convites.entrySet()) {
                    if (membro != null && p != null) {
                        if (ent.getValue().equals(membro.getUniqueId()) && ent.getKey().equals(p.getUniqueId())) {
                            p.sendMessage("§5§l[GRUPO]§r §bO convite que você mandou expirou!");
                            membro.sendMessage("§5§l[GRUPO]§r §bO convite que estava pendente expirou!");
                            convites.remove(ent.getKey());
                            break;
                        }
                    }
                }
            }
        }, 20 * 30);
    }

    public static void aceitaConvite(Player p) {
        if (convites.containsValue(p.getUniqueId())) {
            if (getGrupo(p) != null) {
                p.sendMessage("§5§l[GRUPO]§r §c§lVocê já tem um grupo!");
                return;
            }
            UUID lider = null;
            for (Entry<UUID, UUID> ent : convites.entrySet()) {
                if (ent.getValue().equals(p.getUniqueId())) {
                    lider = ent.getKey();
                }
            }
            if (lider != null) {
                convites.remove(lider);
                Player plider = Bukkit.getPlayer(lider);
                if (plider != null) {
                    Grupo gl = getGrupo(plider);
                    if (gl == null) {
                        tentaCriarGrupo(plider, p);
                    } else {
                        if (gl.size() < 5) {
                            gl.sendGroupMsg("§bO jogador " + p.getName() + " entrou no seu grupo!");
                            gl.addMember(p);
                            p.sendMessage("§5§l[GRUPO]§r §2Você entrou no grupo do(a) " + plider.getName() + " !");
                        } else {
                            p.sendMessage("§5§l[GRUPO]§r §c§lEste grupo já está lotado!!");
                        }
                    }
                }
            } else {
                ChatUtils.sendMessage(p, "§c§lAconteceu algum erro!");
            }
        } else {
            p.sendMessage("§5§l[GRUPO]§r §c§lNinguem convidou você!");
        }
    }

    public static void tentaCriarGrupo(Player p, Player membro) {

        if (getGrupo(p) != null) {
            p.sendMessage("§5§l[GRUPO]§r §c§lVocê já está em um grupo!");
            return;
        }
        if (getGrupo(membro) != null) {
            p.sendMessage("§5§l[GRUPO]§r §cA pessoa convidada já está em um grupo!");
            return;
        }
        membro.sendMessage("§5§l[GRUPO]§r §eVocê entrou no grupo do " + p.getName() + " !");
        p.sendMessage("§5§l[GRUPO]§r §eGrupo criado com sucesso como membro o jogador " + membro.getName() + " !");
        new Grupo(p, membro);

    }

}
