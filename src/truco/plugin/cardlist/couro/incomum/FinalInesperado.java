/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cardlist.couro.incomum;

import br.pj.newlibrarysystem.utils.MetaShit;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDeathEvent;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.LocUtils;
import truco.plugin.utils.TeamUtils;
import truco.plugin.utils.efeitos.CustomEntityFirework;

/**
 *
 * @author Júnior
 */
public class FinalInesperado extends Carta {

    Skill ski = new Skill(this, 17, 15) {

        @Override
        public String getName() {
            return "Bomba Radioativa";
        }

        @Override
        public boolean onCast(final Player p) {

            Entity alvo = LocUtils.getTarget(p, LocUtils.TargetType.INIMIGO);
            if (alvo == null || alvo.getType() != EntityType.PLAYER || alvo.getLocation().distance(p.getLocation()) > 10) {
                p.sendMessage(ChatColor.RED + "Voce precisa de um alvo !");
                return false;
            }
            final Player aliado = (Player) alvo;
            if (!TeamUtils.canAttack(p, (Player) alvo)) {
                p.sendMessage(ChatColor.RED + "Voce so pode fazer isto em inimigos !");
                return false;
            }
            if (aliado.getInventory().getHelmet() != null && aliado.getInventory().getHelmet().getType() == Material.TNT) {
                p.sendMessage("§aEsse jogador já está com uma bomba!");
                return false;
            }
            ChatUtils.sendMessage(aliado, "§e" + p.getName() + " lhe colocou uma bomba!");
            p.sendMessage("§cVocê colocou uma bomba na cabeça dele!");
            ChatUtils.tellAction(aliado, "tem uma bomba inimiga na cabeça vai explodir em 5 segundos ");
            MetaShit.setMetaObject("tacobomba", alvo, p);
            aliado.getInventory().setHelmet(new ItemStack(Material.TNT));
            final UUID uuid = aliado.getUniqueId();
            int id = Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    explode(uuid, p);
                }
            }, 20 * 5);
            praesplodir.put(aliado.getUniqueId(), id);

            CustomEntityFirework.spawn(p.getLocation(), FireworkEffect.builder().withColor(Color.BLUE).with(FireworkEffect.Type.BALL_LARGE).build());

            return true;
        }
    };
    public static HashMap<UUID, Integer> praesplodir = new HashMap();

    public static void explode(UUID uuid, Player p) {
        if (praesplodir.containsKey(uuid)) {
            praesplodir.remove(uuid);

            Player aliadocheck = Bukkit.getPlayer(uuid);
            if (aliadocheck == null) {
                return;
            }

            aliadocheck.getInventory().setHelmet(null);
            List<Entity> ents = aliadocheck.getNearbyEntities(5, 5, 5);
            ents.add(aliadocheck);
            aliadocheck.getWorld().playEffect(aliadocheck.getLocation(), Effect.EXPLOSION_HUGE, 1);
            for (Entity ent : ents) {
                if (ent instanceof Player) {
                    Player perto = (Player) ent;
                    if (TeamUtils.canAttack(p, perto)) {
                        DamageManager.damage(10, p, perto, CustomDamageEvent.CausaDano.MAGIA, "Final Inesperado");
                        CustomEntityFirework.spawn(perto.getLocation(), FireworkEffect.builder().withColor(Color.YELLOW).with(FireworkEffect.Type.BURST).build());

                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {
                for (UUID uid : praesplodir.keySet()) {
                    Player p = Bukkit.getPlayer(uid);
                    if (p != null) {

                        CustomEntityFirework.spawn(p.getLocation(), FireworkEffect.builder().withColor(Color.RED).withColor(Color.WHITE).with(FireworkEffect.Type.BURST).build());

                    }
                }
            }
        }, 10, 10);
    }

    @Override
    public Skill getSkill() {
        return ski;
    }

    @Override
    public Raridade getRaridade() {
        return Raridade.INCOMUM;
    }

    @Override
    public String getNome() {
        return "Final Inesperado";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Coloca uma bomba na cabeça", "de um inimigo que", "explode em 5 segundos"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }

}
