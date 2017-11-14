/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truco.plugin.cards.skills.skilltypes;

import truco.plugin.cards.StatusEffect;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.Potion.Tier;
import org.bukkit.potion.PotionType;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author usuario
 *
 */
public abstract class CustomPotion {

    public static HashMap<String, CustomPotion> potions = new HashMap<String, CustomPotion>();

    // some common potions that everylittlethen might be used
    public static CustomPotion silence = new CustomPotion("Pocao do Silencio", new Potion(PotionType.NIGHT_VISION, Tier.ONE, true), false) {
        @Override
        public void applySplashEffect(Player p,Player shooter) {
            StatusEffect.addStatusEffect(p, StatusEffect.StatusMod.SILENCE, 4);
        }
    };
    public static CustomPotion stun = new CustomPotion("Pocao de Atordoamento", new Potion(PotionType.SLOWNESS, Tier.ONE, true), false) {

        @Override
        public void applySplashEffect(Player p,Player shooter) {
            StatusEffect.addStatusEffect(p, StatusEffect.StatusMod.STUN, 3);
        }
    };
       public static CustomPotion raio = new CustomPotion("Pocao Eletrica", new Potion(PotionType.SPEED, Tier.ONE, true), false) {

        @Override
        public void applySplashEffect(Player p,Player shooter) {
           DamageManager.damage(10, shooter, p, CustomDamageEvent.CausaDano.MAGIA_RAIO,  "Pocao Eletrica");
            p.getWorld().strikeLightningEffect(p.getLocation());
        }
    };
    public String potionName;
    public Potion icon;
    public boolean isBeneficial = false;

    public CustomPotion(String potionName, Potion icon, boolean isBeneficial) {
        this.potionName = potionName;
        this.icon = icon;
        this.isBeneficial = isBeneficial;
        if (!potions.containsKey(potionName)) {
            potions.put(potionName, this);
        }
    }

    public ItemStack toItemStack() {
        ItemStack ss = icon.toItemStack(1);
        PotionMeta meta = (PotionMeta) ss.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + potionName);
        meta.clearCustomEffects();

        ss.setItemMeta(meta);

        return ss;
    }

    public static CustomPotion isCustomPotion(ItemStack ss) {
        ItemMeta meta = ss.getItemMeta();
        String name = ChatColor.stripColor(meta.getDisplayName());
        if (meta.getDisplayName() != null && potions.containsKey(name)) {
            return potions.get(name);
        } else {
            return null;
        }
    }

    public abstract void applySplashEffect(Player p,Player shooter);

}
