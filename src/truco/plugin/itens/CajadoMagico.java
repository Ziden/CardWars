/*

 */
package truco.plugin.itens;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import truco.plugin.CardWarsPlugin;
import truco.plugin.CardWarsPlugin.ServerType;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.arena.GameState;
import truco.plugin.cards.EfeitoProjetil;
import truco.plugin.cards.StatusEffect;
import truco.plugin.damage.DamageManager;
import truco.plugin.data.MetaShit;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.functions.Cooldown;
import truco.plugin.functions.game.Mana;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.TeamUtils;
import truco.plugin.utils.Utils;
import truco.plugin.utils.efeitos.CustomEntityFirework;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class CajadoMagico extends CustomItem {

    public CajadoMagico() {
        super("Cajado Magico", Material.WOOD_PICKAXE, "§cToca flechas magicas", ChatColor.YELLOW, '☼', ItemRaridade.EPICO);
    }

    @Override
    public void interactGame(Player p) {
        if (server == ServerType.TUTORIAL || server == ServerType.GAME) {
            if (p.getVehicle() != null) {
                return;
            }
            if (Cooldown.isCooldown(p, "CajadoMagicoCd")) {
                return;
            }
            if (!Mana.spendMana(p, 7)) {
                return;
            }
            if (CardWarsPlugin.getArena() != null) {
                if (CardWarsPlugin.getArena().getState() != GameState.INGAME) {
                    return;
                }
            }
            if (StatusEffect.hasStatusEffect(p, StatusEffect.StatusMod.SILENCE)) {
                Utils.sendTitle(p, "§9§lSILENCE", "§7Você está silenciado!", 0, 20, 0);
                return;

            }
            if (StatusEffect.hasStatusEffect(p, StatusEffect.StatusMod.STUN)) {

                Utils.sendTitle(p, "§1§lSTUN", "§7Você está atordoado!!", 0, 20, 0);
                return;
            }
            Cooldown.addCoolDown(p, "CajadoMagicoCd", 1000);
            Arrow s = p.launchProjectile(Arrow.class);
            s.setVelocity(s.getVelocity().multiply(2.7));
            MetaShit.setMetaObject("magia", s, true);
            EfeitoProjetil.addEfeito(s, new EfeitoProjetil(p, s) {

                @Override
                public void causaEfeito(Player gotHit, Player Shooter, Projectile projectile) {
                    if (TeamUtils.canAttack(Shooter, gotHit)) {
                        DamageManager.damage(2, Shooter, gotHit, CustomDamageEvent.CausaDano.MAGIA, "Cajado Magico");
                        Shooter.playSound(Shooter.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
                        CustomEntityFirework.spawn(gotHit.getLocation(), FireworkEffect.builder().withColor(Color.PURPLE).withColor(Color.RED).with(FireworkEffect.Type.BALL).build());
                    }
                }
            });
        }

    }

}
