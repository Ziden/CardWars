package truco.plugin.managers;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import truco.plugin.CardWarsPlugin;
import truco.plugin.cards.ControleCartas;
import truco.plugin.itens.CustomItem;
import truco.plugin.itens.Items;
import truco.plugin.utils.ItemUtils;
import truco.plugin.utils.mobapi.mobs.MobsApi;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeSkeleton;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeZombie;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class BixosLTutorial {

    public static HashMap<String, UUID> bixosl = new HashMap();

    public static void bixosArmor() {
        /*
         COURO -70.5,18,13.5 
         CHAIN -68.5,18,13.5 
         OURO -66.5,18,13.5 
         DIMA -63.5,18,13.5 
         FERRO -61.5,18,13.5 
         PELADO -59.5,18,13.5 
         */
        World w = Bukkit.getWorld("world");
        {
            FreezeZombie fb = new FreezeZombie(w);
            Zombie z = (Zombie) MobsApi.summonCustomEntity(fb, new Location(w, 6.5, 80.5, -10.5));
            z.setCustomName("§a§lArmadura de Couro!");
            z.setCustomNameVisible(true);
            z.getEquipment().setItemInHand(new ItemStack(Material.BOOK));

            z.getEquipment().setChestplate(ItemUtils.pinta(new ItemStack(Material.LEATHER_CHESTPLATE), Color.PURPLE));
            z.getEquipment().setLeggings(ItemUtils.pinta(new ItemStack(Material.LEATHER_LEGGINGS), Color.PURPLE));
            z.getEquipment().setBoots(ItemUtils.pinta(new ItemStack(Material.LEATHER_BOOTS), Color.PURPLE));
            bixosl.put("§a§lArmadura de Couro!", z.getUniqueId());
            MobsApi.bixos.put(z.getUniqueId(), new ClickAction() {

                @Override
                public void click(Player p, Entity t) {
                    p.sendMessage(" ");
                    p.sendMessage("§a§l§m==========================================");
                    p.sendMessage("§cArmadura de Couro");
                    p.sendMessage("§ePrincipal função dar alto dano em media distancia!");
                    p.sendMessage("§eSuas cartas são quase sempre magias!");
                    p.sendMessage("§eCaso alguem chegue perto de você, levara uma surra!");
                    p.sendMessage("§a§l§m==========================================");
                    p.sendMessage(" ");
                }
            });
        }
        {
            FreezeSkeleton fb = new FreezeSkeleton(w);
            Skeleton z = (Skeleton) MobsApi.summonCustomEntity(fb, new Location(w, 6.5, 80.5, -8.5));
            z.setCustomName("§a§lArmadura de Malha!");
            z.getEquipment().setItemInHand(new ItemStack(Material.BOW));

            z.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
            z.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
            z.getEquipment().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
            z.setCustomNameVisible(true);
            bixosl.put("§a§lArmadura de Malha!", z.getUniqueId());
            MobsApi.bixos.put(z.getUniqueId(), new ClickAction() {

                @Override
                public void click(Player p, Entity t) {
                    p.sendMessage(" ");
                    p.sendMessage("§a§l§m==========================================");
                    p.sendMessage("§cArmadura de Malha");
                    p.sendMessage("§eConsegue causar grande dano de longa distancia!");
                    p.sendMessage("§eSuas cartas são feitas basicamente em cima de um arco!");
                    p.sendMessage("§eAlem de grande dano a longa distancia, você se garante a curta!");
                    p.sendMessage("§eSistema de armadilhas!");
                    p.sendMessage("§a§l§m==========================================");
                    p.sendMessage(" ");
                }
            });
        }
        {
            FreezeZombie fb = new FreezeZombie(w);
            Zombie z = (Zombie) MobsApi.summonCustomEntity(fb, new Location(w, 6.5, 80.5, -6.5));
            z.setCustomName("§a§lArmadura de Ouro!");
            z.getEquipment().setItemInHand(new Potion(PotionType.INSTANT_HEAL, 1, true).toItemStack(1));

            z.getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
            z.getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
            z.setCustomNameVisible(true);
            z.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
            bixosl.put("§a§lArmadura de Ouro!", z.getUniqueId());
            MobsApi.bixos.put(z.getUniqueId(), new ClickAction() {

                @Override
                public void click(Player p, Entity t) {
                    p.sendMessage(" ");
                    p.sendMessage("§a§l§m==========================================");
                    p.sendMessage("§cArmadura de Ouro");
                    p.sendMessage("§eSua principal função é ajudar o time a alcancar os objetivos!");
                    p.sendMessage("§eConsegue segurar um pouco de porada!");
                    p.sendMessage("§eVocê não tem quase dano nenhum!");
                    p.sendMessage("§eSe souber jogar direito, levara seu time para a vitoria!!");
                    p.sendMessage("§a§l§m==========================================");
                    p.sendMessage(" ");
                }
            });
        }
        {
            FreezeZombie fb = new FreezeZombie(w);
            Zombie z = (Zombie) MobsApi.summonCustomEntity(fb, new Location(w, -5.5, 80.5, -10.5));
            z.setCustomName("§a§lArmadura de Diamante!");
            z.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SPADE));

            z.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            z.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
            z.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
            z.setCustomNameVisible(true);
            bixosl.put("§a§lArmadura de Diamante!", z.getUniqueId());
            MobsApi.bixos.put(z.getUniqueId(), new ClickAction() {

                @Override
                public void click(Player p, Entity t) {
                    p.sendMessage(" ");
                    p.sendMessage("§a§l§m==========================================");
                    p.sendMessage("§cArmadura de Diamante");
                    p.sendMessage("§eSua principal função é aguentar pohhada!");
                    p.sendMessage("§eTem grande controle de grupo!");
                    p.sendMessage("§eO dano dele é quase nulo!");
                    p.sendMessage("§eSuas cartas vão lhe fazer ficar monstro!!");
                    p.sendMessage("§a§l§m==========================================");
                    p.sendMessage(" ");
                }
            });
        }
        {
            FreezeZombie fb = new FreezeZombie(w);
            Zombie z = (Zombie) MobsApi.summonCustomEntity(fb, new Location(w, -5.5, 80.5, -8.5));
            z.setCustomName("§a§lArmadura de Ferro!");
            z.getEquipment().setItemInHand(new ItemStack(Material.IRON_AXE));

            z.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            z.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
            z.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
            z.setCustomNameVisible(true);
            bixosl.put("§a§lArmadura de Ferro!", z.getUniqueId());
            MobsApi.bixos.put(z.getUniqueId(), new ClickAction() {

                @Override
                public void click(Player p, Entity t) {
                    p.sendMessage(" ");
                    p.sendMessage("§a§l§m==========================================");
                    p.sendMessage("§cArmadura de Ferro");
                    p.sendMessage("§eVocê dara um dano mediano e ainda conseguira aguentar pohhada!");
                    p.sendMessage("§eFacil de jogar!");
                    p.sendMessage("§eFraco contra magias!");
                    p.sendMessage("§eSuas cartas são focadas em dano e defesa!!");
                    p.sendMessage("§a§l§m==========================================");
                    p.sendMessage(" ");
                }
            });
        }
        {
            FreezeZombie fb = new FreezeZombie(w);
            Zombie z = (Zombie) MobsApi.summonCustomEntity(fb, new Location(w, -5.5, 80.5, -6.5));
            z.setCustomName("§a§lSem Armadura!");
            z.getEquipment().setItemInHand(new ItemStack(Material.GOLD_HOE));

            z.setCustomNameVisible(true);
            bixosl.put("§a§lSem Armadura!", z.getUniqueId());
            MobsApi.bixos.put(z.getUniqueId(), new ClickAction() {

                @Override
                public void click(Player p, Entity t) {
                    p.sendMessage(" ");
                    p.sendMessage("§a§l§m==========================================");
                    p.sendMessage("§cSem armadura");
                    p.sendMessage("§eAlta mobilidade!");
                    p.sendMessage("§eDificil de jogar!");
                    p.sendMessage("§eAlto dano individual!");
                    p.sendMessage("§eJogo divertido!!");
                    p.sendMessage("§eMorre facil!");
                    p.sendMessage("§a§l§m==========================================");
                    p.sendMessage(" ");
                }
            });
        }
    }

    public static void tacaBixo(double x, double y, double z, String name, final String[] st, final RunnablePlayer r) {
        {

            Location l = new Location(Bukkit.getWorld("world"), x, y, z);
            if (!l.getChunk().isLoaded()) {
                return;
            }
            FreezeSkeleton fb = new FreezeSkeleton(l.getWorld());
            Skeleton vi = (Skeleton) MobsApi.summonCustomEntity(fb, l);
            vi.setCustomName(name);
            vi.setSkeletonType(Skeleton.SkeletonType.WITHER);
            vi.getEquipment().setItemInHand(new ItemStack(Material.BOOK_AND_QUILL));
            vi.setCustomNameVisible(true);
            bixosl.put(name, vi.getUniqueId());
            MobsApi.bixos.put(vi.getUniqueId(), new ClickAction() {

                @Override
                public void click(Player p, Entity t) {
                    if (r != null) {
                        r.run(p);
                    }
                    p.sendMessage("§2§l§m========================================");
                    p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
                    for (String m : st) {
                        p.sendMessage("§7- " + m);
                    }

                }
            });
        }
    }

    public static abstract class RunnablePlayer {

        public abstract void run(Player p);

    }

    public static void tacaBixosnoTutorial() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

            @Override
            public void run() {

                for (Entity e : Bukkit.getWorld("world").getEntities()) {
                    if (e instanceof LivingEntity) {
                        LivingEntity le = (LivingEntity) e;
                        if (le.getCustomName() != null && bixosl.containsKey(le.getCustomName())) {
                            bixosl.remove(le.getCustomName());
                            MobsApi.bixos.remove(le.getUniqueId());
                            le.remove();
                        }
                    }
                }

                bixosArmor();

                tacaBixo(19.5, 80, 2.5, "§4§lCardWars", new String[]{
                    "§dBem vindo ao §4CardWars §d siga o tutorial clicando nos npcs",
                    "§bO Servidor é um pouco complexo para você aprender a jogar é preciso fazer o tutorial!",
                    "§bSiga em frente para pegar sua primeira carta"

                }, null);
                tacaBixo(1.5, 80, 3.5, "§b§lPrimeira Carta", new String[]{
                    "§dPegue a carta do bau e coloque no seu inventário."

                }, null);
                tacaBixo(-2.5, 80, -1.5, "§c§lTutorial 1", new String[]{
                    "§aCada carta possue armaduras especificas",
                    "§aQue podem ser vistas colocando o mouse em cima dela no inventário.",
                    "§aExemplo §c'Armadura: TODAS' §apode ser usada por todas as armaduras.",
                    "§aArmaduras podem ser trocadas a qualquer momento no Lobby!",
                    "§aExistem cartas que servem para mais de uma armadura",
                    "§cExemplo: Malha ou Ferro"

                }, null);
                //4.4,80,-18.5
                tacaBixo(-3.5, 80, -18.5, "§c§lTutorial 2", new String[]{
                    "§eVocê ganhou uma carta que serve para todas as armaduras",
                    "§eCartas tem uma raridade pode ser: Comum,§aIncomum,§9Rara,§5Epica. Respectivamente na ordem de dificuldade de conseguir!",
                    "§eEquipe ela clicando com o botao direito no EQUIP e soltando em um lugar desejado",
                    "§eVocê só pode equipar 9 cartas e não pode usar SHIFT!",
                    "§eVocê só pode equipar cartas de acordo com sua armadura e sem serem repetidas!"

                }, null);
                //-2.5,80,-29.5
                tacaBixo(4.4, 80, -18.5, "§c§lTutorial 3", new String[]{
                    "§cA carta que você ganhou é passiva ou sejá não pode ser ativada",
                    "§cCom essa carta você ganha uma espada de madeira ao nascer",
                    "§cE mais um bonus de 1 de dano com ela."

                }, null);
                tacaBixo(-2.5, 80, -29.5, "§c§lTutorial 4", new String[]{
                    "§7Geralmente cartas que não são ativas lhe dão itens ou atributos ou ambos juntos",
                    "§7Pegue estas cartas e equipe de acordo com seu gosto",
                    "§cLembrando da sua armadura!",
                    "§9Após equipar as cartas você pode usar §c'/vercartas'§7 para ver as cartas equipadas e os itens que você ganha com elas.",},
                        null);

                tacaBixo(-3.5, 80, -41.5, "§c§lTutorial 5", new String[]{
                    "§bCartas ativas podem ser notadas pelo simbolo §e§l[A] §b na carta",
                    "§bElas tem tempo de recarga/mana",
                    "§bO tempo de recarga é o intervalo que você pode usar a habilidade",
                    "§bA mana é o custo da magia, você começa com 100 de mana e vai recuperando com o tempo",
                    "§bExistem cartas que aumentam a regenração de mana!"}, null);

                tacaBixo(4.5, 80, -42.5, "§c§lTutorial 6", new String[]{
                    "§aPara ativar uma carta você precisa clicar com o lançador de habilidades na mão com o botão direito!",
                    "§aApós isso irá aparecer todas suas cartas ativas na hotbar, e um X caso não sejá ativa!",
                    "§aSegure a carta que desejá ativar na mão e clique com o direito",
                    "§aAssim você ativa a carta!"}, new RunnablePlayer() {

                    @Override
                    public void run(Player p) {
                        if (CustomItem.containsInv(p.getInventory(), Items.icaster) || PlayerManager.itensquickbar.containsKey(p)) {
                            return;
                        }
                        ControleCartas.updateInventoryCards(p, false);
                    }
                });

                tacaBixo(0.5, 80, -70.5, "§9§lJogos", new String[]{
                    "§aApós você terminar o tutorial, caso queira jogar é preciso ir na caverna de jogos e clicar no jogar",
                    "§aAssim você será colocado na fila para jogar, após fechar os times 5x5 você poderá jogar em um modo Aleatório",
                    "§6Ao ganhar a partida você ganha moedas que podem ser gastas comprando cartas",
                    "§cVocê pode ganhar ou perder elo calculado em base no elo médio da sua equipe e da equipe inimiga",
                    "§3O elo é o que te classificam no ranking!"
                }, null);
                tacaBixo(0.5, 80, -82.5, "§b§lControle de Grupo", new String[]{
                    "§bDentro do jogo existem formas de controlar um jogador por meio do controle de grupo",
                    "§bSegue os tipos dos controles de grupo",
                    "§cSTUN §a- §enão deixa o inimigo se mover, atacar, usar habilidades!",
                    "§cSILENCE §a- §enão deixa usar habilidades!",
                    "§cSNARE §a- §enão deixa o inimigo se mover!"}, null);

                tacaBixo(-4.5, 80, -76.5, "§a§lModo de Jogo: §5§lDominio", new String[]{
                    "§bNeste modo de jogo o objetivo é dominar as construções",
                    "§bPara você dominar uma construção bata no bloco de dominio",
                    "§bE aquarde alguns segundos dentro da construção sem tomar dano",
                    "§bCaso você tome dano ou saia da construção, você para de dominar",
                    "§bA cada 30 segundos, cada time ganha 1 ponto por base dominada",
                    "§bA partida acaba ao chegar a 40 pontos!"}, null);
                tacaBixo(0.5, 80, -76.5, "§a§lModo de Jogo: §4§lTeamDeathMatch", new String[]{
                    "§bCada jogador tem 4 vidas, assim que perder as 4 vidas você é removido do jogo",
                    "§bGanha a primeira equipe que conseguir eliminar todos os inimigos",
                    "§7§oExistem cartas que aumentam as vidas!"}, null);
                tacaBixo(5.5, 80, -76.5, "§a§lModo de Jogo: §2§lConquista", new String[]{
                    "§bCada equipe tem um castelo que deve ser protegido da equipe inimiga",
                    "§bCada castelo possui uma placa, que ao bater nela você tira vida do castelo",
                    "§bCada castelo tem 100 de vida o primeiro time que conseguir zerar a vida do castelo inimigo ganha"}, null);

                //0.5,0,-110.5
                tacaBixo(0.5, 80, -110.5, "§a§lPasso Final", new String[]{
                    "§aAgora você terá que responder algumas perguntas para terminar o tutorial",
                    "§aEntre no portal e leia as instruções!"}, null);

                //PERGUNTA 1
                tacaBixo(5.5, 80, -156.5, "§a§l9 cartas", new String[]{
                    "§a§lVocê acertou a resposta, continue!",
                    "§a§lProxima pergunta!"}, new RunnablePlayer() {

                    @Override
                    public void run(Player p) {
                        p.teleport(new Location(p.getWorld(), 0.5, 80, -170, 179, 0));
                    }
                });

                tacaBixo(-4.5, 80, -156.5, "§a§l10 cartas", new String[]{
                    "§c§lVocê errou a respost,", " tente refazer o tutorial para responder esta pergunta!"}, new RunnablePlayer() {

                    @Override
                    public void run(Player p) {
                        p.teleport(p.getWorld().getSpawnLocation());
                    }
                });

                tacaBixo(0.5, 80, -156.5, "§a§l5 cartas", new String[]{
                    "§c§lVocê errou a resposta", " tente refazer o tutorial para responder esta pergunta!"}, new RunnablePlayer() {

                    @Override
                    public void run(Player p) {
                        p.teleport(p.getWorld().getSpawnLocation());
                    }
                });
                //PERGUNTA 2
                tacaBixo(-4.5, 80, -180.5, "§a§lClicando com o botao direito", new String[]{
                    "§a§lVocê acertou a resposta, continue!",
                    "--------"}, new RunnablePlayer() {

                    @Override
                    public void run(Player p) {
                        p.teleport(new Location(p.getWorld(), 0.5, 80, -200.5, 179, 0));
                    }
                });
                tacaBixo(0.5, 80, -180.5, "§a§lClicando com o item no inventário", new String[]{
                    "§c§lVocê errou a resposta, tente refazer o tutorial para responder esta pergunta!",
                    "------------"}, new RunnablePlayer() {

                    @Override
                    public void run(Player p) {
                        p.teleport(p.getWorld().getSpawnLocation());
                    }
                });
                tacaBixo(5.5, 80, -180.5, "§a§lApertando a tecla 'A'", new String[]{
                    "§c§lVocê errou a resposta, tente refazer o tutorial para responder esta pergunta!",
                    "------"}, new RunnablePlayer() {

                    @Override
                    public void run(Player p) {
                        p.teleport(p.getWorld().getSpawnLocation());
                    }
                });
                //PERGUNTA 3
                tacaBixo(5.5, 80, -212.5, "§a§l6 tipos", new String[]{
                    "§a§lVocê acertou a resposta, continue!",
                    "-------"}, new RunnablePlayer() {

                    @Override
                    public void run(Player p) {
                        p.teleport(new Location(p.getWorld(), 0.5, 80, -232.5, 179, 0));
                    }
                });
                tacaBixo(0.5, 80, -212.5, "§a§l10 tipos", new String[]{
                    "§c§lVocê errou a resposta, tente refazer o tutorial para responder esta pergunta!",
                    "-------"}, new RunnablePlayer() {

                    @Override
                    public void run(Player p) {
                        p.teleport(p.getWorld().getSpawnLocation());
                    }
                });
                tacaBixo(-4.5, 80, -212.5, "§a§l3 tipos", new String[]{
                    "§c§lVocê errou a resposta, tente refazer o tutorial para responder esta pergunta!",
                    "----------"}, new RunnablePlayer() {

                    @Override
                    public void run(Player p) {
                        p.teleport(p.getWorld().getSpawnLocation());
                    }
                });

                //5.5,80,-212.5
            }

        }, 10 * 20, 10 * 20);

        //
    }

}
