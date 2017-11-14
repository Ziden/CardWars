/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.comum;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.events.CustomDamageEvent.CausaDano;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class ChamaArdente extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Chama Ardente";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Dano de fogo causa ignite", "dano de fogo em alvos com ignite", "espalha o fogo em inimigos proximos"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }

    @Override
    public void causaDano(Player causador, CustomDamageEvent ev) {
        if (ev.getCause() == CausaDano.MAGIA_FOGO) {
            if (ev.getTomou().getFireTicks() > 0) {
                new FirenovaAnimation(causador);
            } else {
                DamageManager.addMagicFireTicks(ev.getTomou(), 60, causador);
            }
        }
    }

    private class FirenovaAnimation implements Runnable {

        Player player;
        int i;
        Block center;
        HashSet<Block> fireBlocks;
        int taskId;

        public FirenovaAnimation(Player player) {
            this.player = player;
            //Events.ganhaExp(15, player);

            player.getWorld().playEffect(player.getLocation(), Effect.LAVA_POP, 1);
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 140, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, 1));
            player.getWorld().createExplosion(player.getLocation(), 0);
            this.i = 0;
            this.center = player.getLocation().getBlock();
            this.fireBlocks = new HashSet();

            this.taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, this, 0L, 4L);
        }

        public void run() {
            for (Block block : this.fireBlocks) {
                if (block.getType() == Material.FIRE) {
                    byte b = 0;
                    block.setTypeIdAndData(0, b, false);
                }
            }
            this.fireBlocks.clear();

            this.i += 1;
            if (this.i <= 5) {
                byte byt = 15;
                int bx = this.center.getX();
                int y = this.center.getY();
                int bz = this.center.getZ();
                for (int x = bx - this.i; x <= bx + this.i; x++) {
                    for (int z = bz - this.i; z <= bz + this.i; z++) {
                        if ((Math.abs(x - bx) == this.i) || (Math.abs(z - bz) == this.i)) {
                            Block b = this.center.getWorld().getBlockAt(x, y, z);
                            if ((b.getType() == Material.AIR) || ((b.getType() == Material.LONG_GRASS))) {
                                Block under = b.getRelative(BlockFace.DOWN);
                                if ((under.getType() == Material.AIR) || ((under.getType() == Material.LONG_GRASS))) {
                                    b = under;
                                }

                                b.setTypeIdAndData(Material.FIRE.getId(), byt, false);
                                this.fireBlocks.add(b);
                            } else if ((b.getRelative(BlockFace.UP).getType() == Material.AIR) || ((b.getRelative(BlockFace.UP).getType() == Material.LONG_GRASS))) {
                                b = b.getRelative(BlockFace.UP);
                                b.setTypeIdAndData(Material.FIRE.getId(), byt, false);
                                this.fireBlocks.add(b);
                            }
                        }
                    }
                }
            } else if (this.i > 5 + 1) {
                Bukkit.getServer().getScheduler().cancelTask(this.taskId);
                // Mago.this.fireImmunity.remove(this.player);
            }
        }
    }
}
