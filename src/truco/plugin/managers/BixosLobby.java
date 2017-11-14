/*

 */
package truco.plugin.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import truco.plugin.CardWarsPlugin;
import truco.plugin.data.MetaShit;
import truco.plugin.functions.Cooldown;
import truco.plugin.menus.Menu;
import truco.plugin.menus.shop.GemShop;
import truco.plugin.utils.BookUtil;
import truco.plugin.utils.ItemUtils;
import truco.plugin.utils.Utils;
import truco.plugin.utils.efeitos.CustomEntityFirework;
import truco.plugin.utils.mobapi.mobs.MobsApi;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeCreeper;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeEnderman;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeSkeleton;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeWitch;
import truco.plugin.utils.mobapi.mobs.monsters.FreezeZombie;
import truco.plugin.utils.mobapi.mobs.passives.FreezeIronGolem;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class BixosLobby {

    public static List<UUID> bixosl = new ArrayList();

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
            Zombie z = (Zombie) MobsApi.summonCustomEntity(fb, new Location(w, -70.5, 18, -13.5));
            z.setCustomName("§a§lArmadura de Couro!");
            z.setCustomNameVisible(true);
            z.getEquipment().setItemInHand(new ItemStack(Material.BOOK));

            z.getEquipment().setChestplate(ItemUtils.pinta(new ItemStack(Material.LEATHER_CHESTPLATE), Color.PURPLE));
            z.getEquipment().setLeggings(ItemUtils.pinta(new ItemStack(Material.LEATHER_LEGGINGS), Color.PURPLE));
            z.getEquipment().setBoots(ItemUtils.pinta(new ItemStack(Material.LEATHER_BOOTS), Color.PURPLE));
            bixosl.add(z.getUniqueId());
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
            Skeleton z = (Skeleton) MobsApi.summonCustomEntity(fb, new Location(w, -68.5, 18, -13.5));
            z.setCustomName("§a§lArmadura de Malha!");
            z.getEquipment().setItemInHand(new ItemStack(Material.BOW));

            z.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
            z.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
            z.getEquipment().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
            z.setCustomNameVisible(true);
            bixosl.add(z.getUniqueId());
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
            Zombie z = (Zombie) MobsApi.summonCustomEntity(fb, new Location(w, -66.5, 18, -13.5));
            z.setCustomName("§a§lArmadura de Ouro!");
            z.getEquipment().setItemInHand(new Potion(PotionType.INSTANT_HEAL, 1, true).toItemStack(1));

            z.getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
            z.getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
            z.setCustomNameVisible(true);
            z.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
            bixosl.add(z.getUniqueId());
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
            Zombie z = (Zombie) MobsApi.summonCustomEntity(fb, new Location(w, -63.5, 18, -13.5));
            z.setCustomName("§a§lArmadura de Diamante!");
            z.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SPADE));

            z.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            z.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
            z.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
            z.setCustomNameVisible(true);
            bixosl.add(z.getUniqueId());
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
            Zombie z = (Zombie) MobsApi.summonCustomEntity(fb, new Location(w, -61.5, 18, -13.5));
            z.setCustomName("§a§lArmadura de Ferro!");
            z.getEquipment().setItemInHand(new ItemStack(Material.IRON_AXE));

            z.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            z.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
            z.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
            z.setCustomNameVisible(true);
            bixosl.add(z.getUniqueId());
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
            Zombie z = (Zombie) MobsApi.summonCustomEntity(fb, new Location(w, -59.5, 18, -13.5));
            z.setCustomName("§a§lSem Armadura!");
            z.getEquipment().setItemInHand(new ItemStack(Material.GOLD_HOE));

            z.setCustomNameVisible(true);
            bixosl.add(z.getUniqueId());
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

    public static boolean startou = false;

    public static void tacaBixosnoLobby() {
        if (!startou) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    CustomEntityFirework.spawn(new Location(Bukkit.getWorld("world"), -29.5, 30, 71.5), Utils.LaunchRandomFirework());
                }
            }, 100, 100);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(CardWarsPlugin._instance, new Runnable() {

                @Override
                public void run() {
                    for (UUID bi : bixosl) {
                        FOR1:
                        for (Entity e : Bukkit.getWorld("world").getEntities()) {
                            if (e.getUniqueId() == bi) {
                                e.remove();
                                break FOR1;
                            }
                        }
                        MobsApi.bixos.remove(bi);

                    }
                    bixosl.clear();
                    tacaBixosnoLobby();
                }
            }, 30 * 20, 30 * 20);

            startou = true;

        }
        World w = Bukkit.getWorld("world");

        //
        bixosArmor();
        {
            FreezeEnderman fb = new FreezeEnderman(w);
            Enderman b = (Enderman) MobsApi.summonCustomEntity(fb, new Location(w, -29.5, 20, 69.5));

            b.setCustomName("§a§lLoja de Gemas!");
            b.setCustomNameVisible(true);
            b.setCarriedMaterial(new MaterialData(Material.EMERALD_BLOCK));
            bixosl.add(b.getUniqueId());
            MobsApi.bixos.put(b.getUniqueId(), new ClickAction() {

                @Override
                public void click(Player p, Entity t) {
                    Menu.menus.get(GemShop.menuname).openInventory(p);
                }
            });
            //-38.5,19,78.5

        }
        //-130.5,11,20.5

        {
            FreezeWitch fb = new FreezeWitch(w);
            Witch b = (Witch) MobsApi.summonCustomEntity(fb, new Location(w, -38.5, 19, 78.5));

            b.setCustomName("§a§lLojas!");
            b.setCustomNameVisible(true);

            bixosl.add(b.getUniqueId());
            MobsApi.bixos.put(b.getUniqueId(), new ClickAction() {

                @Override
                public void click(Player p, Entity t) {

                    ItemStack writen = new ItemStack(Material.WRITTEN_BOOK);
                    BookUtil.setAuthor(writen, "Feldmann");
                    BookUtil.setTitle(writen, "§cSistema de Lojas");
                    BookUtil.AddPage(writen, "§2Você pode comprar pacotes de cartas clicando em PACK.  "
                            + " Esses pacotes tem grande chance de vir uma carta comum , média chance de vim uma Incomum , pequena chance de vir Rara, e quase nula de vir Epica!");

                    BookUtil.AddPage(writen, "§2Você pode comprar cartas de outros jogadores no SHOP,"
                            + " Clique no filtro de cartas desejado, passe o mouse por cima das cartas para ver o preço, caso queira comprar clique nela §4PS:Quem vende estas cartas são jogadores!"
                    );
                    BookUtil.AddPage(writen, "§1Caso queira vender uma carta, pegue ela na mao e use o comando /vender {PRECO} e espere alguem comprar ela!,    Você pode remover a carta a venda clicando no bloco de redstone e na carta desejada,");

                    BookUtil.AddPage(writen, "§5Você pode comprar pacotes especificos na loja de gemas. Ex: um pacote que só vem cartas epicas.  Clique no npc e de um olhada!");
                    if (!p.hasMetadata("pegouloja")) {
                        if (p.getInventory().firstEmpty() != -1) {
                            p.getInventory().addItem(writen);
                            p.sendMessage("§b§lLeia o livro para aprender a usar a loja!");
                            p.updateInventory();
                            MetaShit.setMetaObject("pegouloja", p, "");

                        } else {
                            p.sendMessage("§aSeu inventário está lotado!");
                        }
                    } else {
                        p.sendMessage("§aVocê já pegou esse livro!");
                    }
                }
            });
            //-38.5,19,78.5

        }
        {
            FreezeCreeper fb = new FreezeCreeper(w);
            Creeper b = (Creeper) MobsApi.summonCustomEntity(fb, new Location(w, -130.5, 11, 20.5));
            b.setPowered(true);
            b.setCustomName("§a§lEstoque!");
            b.setCustomNameVisible(true);

            bixosl.add(b.getUniqueId());
            MobsApi.bixos.put(b.getUniqueId(), new ClickAction() {

                @Override
                public void click(Player p, Entity t) {
                    p.sendMessage("   ");
                    p.sendMessage("§2§l§m==========================================");
                    p.sendMessage("§eCaso seu inventário estejá lotado");
                    p.sendMessage("§eVocê pode usar o BAU para guardar qualquer item!");
                    p.sendMessage("§eNele você tem infinitos slots para guardar quantos itens precisar!");
                    p.sendMessage("§eOs itens ficam salvos no slot que deixou!");
                    p.sendMessage("§2§l§m==========================================");
                    p.sendMessage("   ");
                }
            });
            //-38.5,19,78.5

        }
        {
            FreezeIronGolem fb = new FreezeIronGolem(w);
            IronGolem b = (IronGolem) MobsApi.summonCustomEntity(fb, new Location(w, -61.5, 13, 26.3));

            b.setCustomName("§a§lComandos!");
            b.setCustomNameVisible(true);

            bixosl.add(b.getUniqueId());
            MobsApi.bixos.put(b.getUniqueId(), new ClickAction() {

                @Override
                public void click(Player p, Entity t) {
                    p.sendMessage("   ");
                    p.sendMessage("§2§l§m==========================================");
                    p.sendMessage("§eVocê pode ver todos os comandos do servidor usando §c/cws!");
                    p.sendMessage("§eSeu level influencia para algumas coisas, você ira perceber!");
                    p.sendMessage("§eA cada 5 niveis você ganha §a1 gema§e!");
                    p.sendMessage("§2§l§m==========================================");
                    p.sendMessage("   ");
                }
            });
            //-38.5,19,78.5

        }
        //-61.5,13,26.3

    }

}
