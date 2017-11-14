package truco.plugin.cardlist.couro.comum;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import truco.plugin.cards.CC;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EfeitoProjetil;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.ChatUtils;

import truco.plugin.data.MetaShit;
import truco.plugin.cards.skills.skilltypes.Channeling;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 *
 */
public class DardoGelido extends Carta {

    public static DardoGelido dardo;

    @Override
    public void onEnable() {
        dardo = this;
    }
    public Skill regen = new Skill(this, 6, 5) {

        @Override
        public int getChannelingTime() {
            return 1;
        }

        @Override
        public boolean onCast(final Player p) {
            if (p.hasMetadata("Channeling")) {
                p.sendMessage("§aVocê já está conjurando uma magia!");
                return false;
            }

            Runnable run = new Runnable() {

                @Override
                public void run() {
                    Projectile fb = p.launchProjectile(Arrow.class);
                    fb.setVelocity(fb.getVelocity().multiply(3));
                    EfeitoProjetil efeito = new EfeitoProjetil(p, fb) {

                        @Override
                        public void causaEfeito(Player gotHit, Player Shooter, Projectile projectile) {
                            ChatUtils.sendMessage(gotHit, ChatColor.AQUA + "* tomou um dardo gelido *");
               
                            CC.tacaSlow(gotHit, 1, 5 * 20);
                            DamageManager.damage(7, Shooter, gotHit, null, CustomDamageEvent.CausaDano.MAGIA_AGUA, false, vinculada);


                        }
                    };
                    MetaShit.addMetaObject("magia", fb, "true"); // cancela o dano nativo
                    EfeitoProjetil.addEfeito(fb, efeito);
                    MetaShit.setMetaObject("cartaOrigem", fb, DardoGelido.dardo);
                }
            };
            new Channeling(p, 1, this, run);
            return true;
        }

        @Override
        public String getName() {
            return "Dardo Gelido";
        }
    };

    @Override
    public Carta.Raridade getRaridade() {
        return Carta.Raridade.COMUM;
    }

    @Override
    public String getNome() {
        return "Mago da Antartida";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Toca uma seta", "com elemento de agua", "que causa slow"};
    }

    @Override
    public Carta.Armadura getArmadura() {
        return Carta.Armadura.LEATHER;
    }

    @Override
    public Skill getSkill() {
        return regen;
    }
}
