package truco.plugin.cardlist.ferro.epico;


import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import truco.plugin.cards.Carta;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.skills.Skill;
import truco.plugin.utils.ChatUtils;
import truco.plugin.utils.ItemUtils;
import truco.plugin.data.MetaShit;
import truco.plugin.cards.StatusEffect;
import truco.plugin.events.CustomDamageEvent;
import truco.plugin.utils.efeitos.CustomEntityFirework;

/**
 *
 * @author Michael Markus Ackermann
 */
public class FuriaDeJabu extends Carta {

    @Override
    public Carta.Raridade getRaridade() {
        return Carta.Raridade.EPICO;
    }

    @Override
    public String getNome() {
        return "Furia de Jabu";
    }

    @Override
    public String[] getDesc() {
        return new String[]{"Cada ataque concede 1 de furia", "com 5 de furia concede bonus e remove os pontos", "pás dão stun", "espadas dão silence", "machados 3x dano"};
    }

    @Override
    public Carta.Armadura getArmadura() {
        return Carta.Armadura.FERRO;
    }

    @Override
    public Skill getSkill() {
        return s;
    }
    Skill s = new Skill(this, 8, 20) {

        @Override
        public String getName() {
            return "Furioso Ataque";
        }

        @Override
        public boolean onCast(final Player p) {
            if (getPontosFuria(p) != 5) {
                p.sendMessage("§cVocê precisa 5 de furia para ativar essa skill!");
                return false;
            }
            if (p.hasMetadata("furioso5")) {
                p.sendMessage("§aVocê já ativou está skill primeiro use ela!");
                return false;
            }
            int task = Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    if (p != null) {
                        if (p.hasMetadata("furioso5")) {
                            p.removeMetadata("furioso5", CardWarsPlugin._instance);
                            p.sendMessage("§4Você se acalmou e perdeu a sua furia!");
                        }
                    }
                }
            }, 20 * 5);
            MetaShit.setMetaObject("furioso5", p, task);
            p.sendMessage("§4Seu proximo ataque vai causar um bonus dependendo do item!");

            return true;
        }
    };

    @Override
    public void causaDano(final Player donoDaCarta, CustomDamageEvent ev) {

        if (getPontosFuria(donoDaCarta) == 5) {
            if (ev.getPlayerTomou() == null) {
                return;
            }
            if (donoDaCarta.hasMetadata("furioso5")) {
                removeFuria(donoDaCarta);
                int task = (int) MetaShit.getMetaObject("furioso5", donoDaCarta);
                Bukkit.getScheduler().cancelTask(task);
                donoDaCarta.removeMetadata("furioso5", CardWarsPlugin._instance);
                Material namao = donoDaCarta.getItemInHand().getType();
              Color cor = null;
                if (ItemUtils.isAxe(namao)) {
                    ev.addDamageMult(3, "Furia de Jabu");
                    donoDaCarta.sendMessage("§cVocê causou 3x o dano!");
                    ev.getPlayerTomou().sendMessage("§cSeu inimigo estáva furioso e lhe causou 3x o dano!");
                    cor = Color.RED;
                } else if (ItemUtils.isSword(namao)) {
                    StatusEffect.addStatusEffect(ev.getPlayerTomou(), StatusEffect.StatusMod.SILENCE, 3);
                    donoDaCarta.sendMessage("§cVocê sileciou o inimico por 3 segundos!");
                   ev.getPlayerTomou().sendMessage("§cSeu inimigo estáva furioso e lhe silenciou por 3 segundos!");
                    cor = Color.BLUE;
                } else if (ItemUtils.isSpade(namao)) {
                    StatusEffect.addStatusEffect(ev.getPlayerTomou(), StatusEffect.StatusMod.STUN, 3);
                    donoDaCarta.sendMessage("§cVocê paralizou o inimico por 3 segundos!");
                    ev.getPlayerTomou().sendMessage("§cSeu inimigo estáva furioso e lhe paralizou por 3 segundos!");
                    cor = Color.GRAY;
                } else {
                    donoDaCarta.sendMessage("§cVocê não soube gastar sua furia!");
                }
                if (cor != null) {
               
                    CustomEntityFirework.spawn(ev.getPlayerBateu().getLocation(),FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(cor).build());
                }
            }
        } else {
            if (donoDaCarta.hasMetadata("taskrfuria")) {
                Bukkit.getScheduler().cancelTask((int) MetaShit.getMetaObject("taskrfuria", donoDaCarta));
                donoDaCarta.removeMetadata("taskrfuria", CardWarsPlugin._instance);
            }
            addPontoFuria(donoDaCarta);
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    if (donoDaCarta != null && donoDaCarta.hasMetadata("taskrfuria")) {

                        removePontoFuria(donoDaCarta);
                        if (FuriaDeJabu.getPontosFuria(donoDaCarta) <= 0) {
                            int task = (int) MetaShit.getMetaObject("taskrfuria", donoDaCarta);
                            Bukkit.getScheduler().cancelTask(task);
                            donoDaCarta.removeMetadata("taskrfuria", CardWarsPlugin._instance);
                        }

                    }
                }
            };
            int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, r, 20 * 6, 20 * 6);
            MetaShit.setMetaObject("taskrfuria", donoDaCarta, task);
        }

    }

    public void removeFuria(Player p) {
        if (p.hasMetadata("furia")) {
            p.removeMetadata("furia", CardWarsPlugin._instance);

        }
        p.sendMessage("§4Furia 0/5");
    }

    public void addPontoFuria(Player p) {
        int tem = 0;
        if (p.hasMetadata("furia")) {
            tem = (int) MetaShit.getMetaObject("furia", p);

        }
        tem++;
        MetaShit.setMetaObject("furia", p, tem);
        p.sendMessage("§4Furia " + (tem) + "/5");
        if ((tem) == 5) {
            ChatUtils.tellAction(p, "está furioso");
        }
    }

    public void removePontoFuria(Player p) {
        int tem = 0;
        if (p.hasMetadata("furia")) {
            tem = (int) MetaShit.getMetaObject("furia", p);

        }
        if (tem == 0) {
            return;
        }
        MetaShit.setMetaObject("furia", p, tem - 1);
        p.sendMessage("§4Furia " + (tem - 1) + "/5");

    }

    public static int getPontosFuria(Player p) {
        if (p.hasMetadata("furia")) {
            return (int) MetaShit.getMetaObject("furia", p);

        }
        return 0;
    }
}
