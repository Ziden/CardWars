/*
 * Nao roba não moço eu fiz isso
 * Tem Direitos Autoriais Aqui Cuidado!
 * Espero que os troxas acreditem ops.
 */
package truco.plugin.cardlist.couro.epico;


import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import truco.plugin.cards.Carta.Raridade;
import truco.plugin.cards.Carta.Armadura;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.data.MetaShit;
import truco.plugin.utils.TeamUtils;
import truco.plugin.cards.skills.skilltypes.Channeling;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CustomDamageEvent;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class BolaKhaphita extends Carta {

    @Override
    public Raridade getRaridade() {
        return Raridade.EPICO;
    }
    Skill s = new Skill(this, 16, 15) {

        @Override
        public String getName() {
            return "Bola de Fogo Gigante";
        }

        @Override
        public int getChannelingTime() {
            return 5;
        }

        @Override
        public boolean onCast(final Player p) {
            if (p.hasMetadata("Channeling")) {
                p.sendMessage("§aVocê já está conjurando uma magia!");
                return false;
            }

            new Channeling(p, 5, this, new Runnable() {

                @Override
                public void run() {
                    Fireball fb = p.launchProjectile(Fireball.class);
                    fb.setVelocity(fb.getVelocity().multiply(0.2));
                    fb.setShooter(p);
                    MetaShit.setMetaObject("khaphita", fb, true);
                    MetaShit.setMetaObject("magia", fb, true);

                    p.sendMessage("§dVocê conseguiu juntar forças para invocar a bola de khaphita!");
                }
            });

            return true;
        }
    };

    @Override
    public String getNome() {
        return "Bola de Khaphita";
    }

    @Override
    public Skill getSkill() {
        return s;
    }

    @Override
    public void projetilBateEmAlgo(ProjectileHitEvent ev) {
        if (ev.getEntity().hasMetadata("khaphita")) {
            Player shooter = (Player) ev.getEntity().getShooter();
           
            for (Entity ent : ev.getEntity().getNearbyEntities(5, 5, 5)) {
                if (ent instanceof Player) {
                    Player alvo = (Player) ent;
                    if (TeamUtils.canAttack(shooter, alvo)) {
                        alvo.sendMessage("§cUma bola gigante lhe acertou!");
                        
                        
                        DamageManager.damage(14 + CardWarsPlugin.random.nextInt(10), shooter, alvo, CustomDamageEvent.CausaDano.MAGIA_FOGO, "Bola Khaphita");

                    }
                }
            }
        }
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Invoca uma bola de fogo gigante"};
    }

    @Override
    public Armadura getArmadura() {
        return Armadura.LEATHER;
    }
}
