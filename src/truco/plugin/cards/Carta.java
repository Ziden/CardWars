/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cards;

import truco.plugin.cards.skills.Skill;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.stats.Stats;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDeathEvent;

/**
 *
 * @author usuario
 */
public abstract class Carta {

    public static enum Raridade {

        COMUM, INCOMUM, RARO, EPICO;
    }

    public static enum Armadura {
        // o numero eh a durabilidade do ovo, pra controlar as cores
        // o array de material, é para checar se ele está com a armadura

        TODOS(55, "Todas as Armaduras", new Material[]{Material.GOLD_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.AIR},"Todas"),
        OURO(61, "Ouro", new Material[]{Material.GOLD_BOOTS},null), // priest
        FERRO(51, "Ferro", new Material[]{Material.IRON_BOOTS},null), // barbaro
        CHAIN(56, "Malha", new Material[]{Material.CHAINMAIL_BOOTS},"Chain"), // ladino
        LEATHER(98, "Couro", new Material[]{Material.LEATHER_BOOTS},null), // mago
        PELADO(101, "Sem Armadura", new Material[]{Material.AIR},"Pelado"), // lutador
        DIMA(54, "Diamante", new Material[]{Material.DIAMOND_BOOTS},null), // guerreiro/tanker
        OURO_LEATHER(120, "Ouro ou Couro", new Material[]{Material.GOLD_BOOTS, Material.LEATHER_BOOTS},null), // Invocador
        CHAIN_FERRO(91, "Malha ou Ferro", new Material[]{Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS},"Chain ou Ferro"), // Assassino
        FERRO_DIMA(94, "Ferro ou Diamante", new Material[]{Material.IRON_BOOTS, Material.DIAMOND_BOOTS},null), // executador
        LEATHER_PELADO(90, "Couro ou Sem Armadura", new Material[]{Material.LEATHER_BOOTS, Material.AIR},"Couro ou Pelado"), // monge
        OURO_DIMA(62, "Ouro ou Diamante", new Material[]{Material.GOLD_BOOTS, Material.DIAMOND_BOOTS},null), // paladino
        PELADO_OURO(92, "Sem Armadura ou Ouro", new Material[]{Material.GOLD_BOOTS, Material.AIR},"Pelado ou Ouro"), // paladino
        CHAIN_LEATHER(68, "Couro ou Malha", new Material[]{Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS},"Couro ou Chain"); // elementalista
        public short dur;
        public String desc;
        public HashSet<Material> boots;

        public List<Armadura> getArmors() {
            List s = new ArrayList();
            for (Armadura r : Armadura.values()) {
                if (r == this) {
                    continue;
                }
                if (r.name().contains(this.name())) {
                    s.add(r);
                }

            }
            return s;
        }

        public ItemStack getItemStackToMenu() {
            ItemStack carta = new ItemStack(Material.MONSTER_EGG, 1);
            ItemMeta meta = carta.getItemMeta();
            meta.setDisplayName("§c§l" + desc);
            carta.setDurability(dur);
            carta.setItemMeta(meta);
            return carta;
        }
        public String nomebanco;

        Armadura(int durabilidadeOvo, String desc, Material[] botas, String nomebanco) {
            this.dur = (short) durabilidadeOvo;
            this.desc = desc;
            if (nomebanco == null) {
                this.nomebanco = desc;
            } else {
                this.nomebanco = nomebanco;
            }
            boots = new HashSet<Material>();
            for (Material s : botas) {
                boots.add(s);
            }
        }
    }

    public abstract Raridade getRaridade();

    public float getVersion() {
        return 1;
    }

    public boolean usePermission() {
        return false;
    }

    public abstract String getNome(); // eh o id

    public abstract String[] getDesc(); // lore

    public abstract Armadura getArmadura();

    public Stats[] getStats() {
        return null;
    }

    public ItemStack[] getItems() {
        return null;
    }

    // podem ser implementados ou naum
    public Skill getSkill() {
        return null;
    }

    // eventos
    public static String noPermToUse = "§c§lVocê não pode usar está carta devido a sua armadura!";

    public void interact(PlayerInteractEvent ev) {
    }

    public void move(PlayerMoveEvent ev) {
    }

    public void projetilBateEmAlgo(ProjectileHitEvent ev) {
    }

    public void joinEvent(PlayerJoinEvent ev) {
    }

    public void acertaGolpeEpico(CustomDamageEvent ev, Player bateu) {

    }

    public void onEnable() {

    }

    public void playerTocaFlecha(EntityShootBowEvent ev) {
    }

    public void tomaDano(CustomDamageEvent ev) {
    }

    public void causaDano(Player causador, CustomDamageEvent ev) {
    }

    public void playerDeath(CustomDeathEvent ev) {

    }

    public double alteraVida(double vida) {
        return vida;
    }

    // metodos internos
    public String getDisplayName() {
        return ControleCartas.getCor(getRaridade()) + "§l" + ControleCartas.ICONE_RARIDADE + "§r " + ChatColor.GOLD + "§l" + getNome();
    }

    public ItemStack toItemStack() {
        ItemStack carta = new ItemStack(Material.MONSTER_EGG, 1);
        ItemMeta meta = carta.getItemMeta();
        String ativo = getSkill() == null ? "" : ChatColor.YELLOW + "§l[A] ";
        meta.setDisplayName(getDisplayName());
        List<String> lore = new ArrayList<String>();
        for (String s : getDesc()) {
            lore.add("§r" + "§9" + s);
        }
        Skill s = getSkill();
        if (s != null) {
            lore.add("§d|--------SKILL--------|");
            lore.add("§d| §r" + ativo + ChatColor.AQUA + s.getName());
            lore.add("§d| §r§9Mana: " + s.mana + " §5Recarga: " + s.cd);
            if (s.getChannelingTime() != -1) {
                lore.add("§d| §r§aConjuração: " + s.getChannelingTime() + " segundos");

            }//16
            lore.add("§d|---------------------|");
        }
        lore.add(ChatColor.GOLD + "Armadura: " + ChatColor.GREEN + getArmadura().desc + "§r " + ControleCartas.getCor(getRaridade()) + "[" + getRaridade().name() + "]");

        String chars = "abcdefghijklmnopqrstuvwxyz123456789 !@#$%¨&*()_+{}[]/;";
        String vai = "";
        for (int x = 0; x < 16; x++) {
            vai += chars.charAt(CardWarsPlugin.random.nextInt(chars.length()));

        }

        lore.add("§0" + vai);
        meta.setLore(lore);
        carta.setItemMeta(meta);
        carta.setDurability(getArmadura().dur);
        return carta;
    }
}
