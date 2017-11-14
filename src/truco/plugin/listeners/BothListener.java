package truco.plugin.listeners;

import java.util.ArrayList;
import truco.plugin.data.mysql.CardsDB;
import truco.plugin.menus.shop.PackSellerMenu;
import truco.plugin.menus.shop.CardSellerMenu;
import truco.plugin.cards.ControleCartas;
import truco.plugin.functions.SomeNegada;
import java.util.List;
import java.util.UUID;

import org.bukkit.Art;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import truco.plugin.*;
import truco.plugin.CardWarsPlugin.ServerType;
import static truco.plugin.CardWarsPlugin.server;
import truco.plugin.functions.fun.Impulsao;
import truco.plugin.itens.CustomItem;
import truco.plugin.matchmaking.Threads.MatchMaker;
import truco.plugin.menus.*;
import truco.plugin.menus.Menu.MenuType;
import truco.plugin.signs.SignUtils;
import truco.plugin.functions.Cooldown;
import truco.plugin.utils.ItemUtils;
import truco.plugin.data.MetaShit;
import truco.plugin.damage.DamageManager;
import truco.plugin.events.CardsLoadedEvent;
import truco.plugin.functions.game.Mana;
import truco.plugin.managers.BixosLTutorial;
import truco.plugin.managers.BixosLobby;
import truco.plugin.managers.PlayerManager;
import truco.plugin.matchmaking.DBHandler;
import truco.plugin.utils.mobapi.mobs.MobsApi;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class BothListener implements Listener {
    
    public static Art jogar = Art.KEBAB;
    public static Art jogargrande = Art.MATCH;
    public static Art cartas = Art.AZTEC;
    public static Art estoque = Art.AZTEC2;
    public static Art armaduras = Art.ALBAN;
    public static Art shop = Art.BOMB;
    public static Art pacote = Art.PLANT;
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void inventario(final InventoryClickEvent ev) {
        if (ev.getSlotType() == InventoryType.SlotType.ARMOR || (ev.getSlotType() == InventoryType.SlotType.QUICKBAR && PlayerManager.itensquickbar.containsKey((Player) ev.getWhoClicked()))) {
            ev.setCancelled(true);
            Bukkit.getScheduler().runTaskLater(CardWarsPlugin._instance, new Runnable() {
                
                public void run() {
                    ((Player) ev.getWhoClicked()).updateInventory();
                }
            }, 2L);
            
        }
        if (ev.getCurrentItem() != null) {
            
            if (MenuUtils.isIlusorio(ev.getCurrentItem()) && !ev.getInventory().getTitle().contains("!")) {
                ev.setCurrentItem(null);
                ev.setCancelled(true);
                ((Player) ev.getWhoClicked()).sendMessage("§b~ o  item ilusorio sumiu ~");
                Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {
                    
                    @Override
                    public void run() {
                        ((Player) ev.getWhoClicked()).updateInventory();
                    }
                }, 2);
            }
        }
        
        for (Menu m : Menu.menus.values()) {
            if (m.getTipo() != MenuType.AMBOS) {
                if (m.getTipo() == MenuType.LOBBY && (server != CardWarsPlugin.ServerType.LOBBY && server != CardWarsPlugin.ServerType.TUTORIAL)) {
                    continue;
                }
                if (m.getTipo() == MenuType.JOGO && server != ServerType.GAME) {
                    continue;
                }
                
            }
            m.clickInventory(ev);
        }
    }
    
    @EventHandler
    public void onApple(PlayerItemConsumeEvent e) {
        final Player p = e.getPlayer();
        ItemStack hand = p.getInventory().getItemInHand();
        
        if ((hand.getType() == Material.GOLDEN_APPLE)) {
            int cura = 4;
            if (hand.getDurability() == 1) {
                cura += 4;
            }
            DamageManager.cura(p, cura);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {
                
                public void run() {
                    p.removePotionEffect(PotionEffectType.ABSORPTION);
                }
            }, 2L);
        }
        
    }
    
    @EventHandler
    public void trocaplaca(SignChangeEvent ev) {
        if (ev.getPlayer().isOp()) {
            for (int x = 0; x < 4; x++) {
                String linha = ev.getLine(x);
                if (linha != null) {
                    ev.setLine(x, linha.replace("&", "§"));
                }
            }
        }
    }
    
    @EventHandler
    public void fomemuda(FoodLevelChangeEvent ev) {
        ev.setFoodLevel(20);
        ev.setCancelled(true);
    }
    
    @EventHandler
    public void chove(WeatherChangeEvent ev) {
        if (ev.toWeatherState()) {
            ev.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlocoTrocaFisica(BlockFadeEvent event) {
        event.setCancelled(true);
    }
    
    @EventHandler
    public void sheep(EntityChangeBlockEvent ev) {
        if (ev.getEntity().getType() == EntityType.SHEEP) {
            ev.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPhysics(BlockFormEvent event) {
        if (event.getNewState().getType() == Material.ICE || event.getNewState().getType() == Material.SNOW) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void ignitefire(BlockIgniteEvent e) {
        if (!e.getCause().equals(BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void tomadano(EntityDamageEvent ev) {
        Impulsao.onPlayerDamage(ev);
        
    }
    
    @EventHandler
    public void destroiItem(HangingBreakEvent ev) {
        if (ev instanceof HangingBreakByEntityEvent) {
            HangingBreakByEntityEvent eev = (HangingBreakByEntityEvent) ev;
            if (eev.getRemover() instanceof Player) {
                if (!((Player) eev.getRemover()).isOp() && ((Player) eev.getRemover()).getGameMode() != GameMode.CREATIVE) {
                    ev.setCancelled(true);
                    return;
                }
            }
            
            ev.setCancelled(true);
        }
    }
    
    @EventHandler
    public void changeweather(WeatherChangeEvent ev) {
        if (ev.toWeatherState()) {
            ev.setCancelled(true);
        }
    }
    
    public static void interactt(final PlayerInteractEvent e) {
        if (e.getAction() != Action.PHYSICAL) {
            if (e.getPlayer().getItemInHand() != null) {
                if (MenuUtils.isIlusorio(e.getPlayer().getItemInHand())) {
                    e.setCancelled(true);
                    
                }
            }
        }
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            String citem = CustomItem.getItem(e.getPlayer().getItemInHand());
            if (e.getPlayer().getItemInHand().getType() == Material.MONSTER_EGG) {
                e.setCancelled(true);
                
            }
            
            if (citem != null) {
                
                if (CardWarsPlugin.getArena() != null) {
                    if (!CardWarsPlugin.getArena().getSpecs().contains(e.getPlayer().getUniqueId())) {
                        CustomItem.getCustomItem(citem).interact(e.getPlayer());
                    }
                }
            }
            PlayerManager.usaSkill(e.getPlayer());
            
        }
        
    }
    
    @EventHandler
    public void boom(EntityExplodeEvent ev) {
        ev.blockList().clear();
    }
    
    @EventHandler
    public void spawnMob(CreatureSpawnEvent ev) {
        if (MobsApi.bixos.containsKey(ev.getEntity())) {
            ev.setCancelled(false);
            return;
        }
        if (ev.getEntity().getType() == EntityType.ARMOR_STAND) {
            return;
        }
        
        if (ev.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
            ev.setCancelled(true);
        }
    }
    
    @EventHandler
    public void entityInteract(EntityInteractEvent ev) {
        if (ev.getBlock().getType() == Material.SOIL) {
            ev.setCancelled(true);
        }
    }
    
    @EventHandler
    public void breakblock(BlockBreakEvent ev
    ) {
        
        if (!(ev.getPlayer().isOp() && ev.getPlayer().getGameMode() == GameMode.CREATIVE)) {
            ev.setCancelled(true);
        }
    }
    
    @EventHandler
    public void entityTarget(EntityTargetEvent ev
    ) {
        if (server == ServerType.LOBBY) {
            ev.setCancelled(true);
        }
    }
    
    @EventHandler
    public void breakplace(BlockPlaceEvent ev
    ) {
        if (!(ev.getPlayer().isOp() && ev.getPlayer().getGameMode() == GameMode.CREATIVE)) {
            ev.setCancelled(true);
        }
    }
    
    @EventHandler
    public void interact(final PlayerInteractEvent e
    ) {
        
        if (e.getAction() == Action.PHYSICAL) {
            if (e.getClickedBlock().getType() == Material.SOIL) {
                e.setCancelled(true);
            }
        }
        SignUtils.clicaplaca(e);
        interactt(e);
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            
            if (e.getPlayer().getItemInHand() != null) {
                if (ItemUtils.isArmor(e.getPlayer().getItemInHand().getType())) {
                    e.setCancelled(true);
                }
                
            }
        }
        
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            
            if (!(e.getPlayer().isOp() && e.getPlayer().getGameMode() == GameMode.CREATIVE)) {
                if (e.getClickedBlock().getType() == Material.TRAP_DOOR
                        || e.getClickedBlock().getType() == Material.WOODEN_DOOR
                        || e.getClickedBlock().getType() == Material.WOOD_DOOR
                        || e.getClickedBlock().getType() == Material.FENCE_GATE
                        || e.getClickedBlock().getType() == Material.HOPPER
                        || e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE
                        || e.getClickedBlock().getType() == Material.CHEST
                        || e.getClickedBlock().getType() == Material.BED_BLOCK
                        || e.getClickedBlock().getType() == Material.ANVIL
                        || e.getClickedBlock().getType() == Material.BEACON
                        || e.getClickedBlock().getType() == Material.WORKBENCH
                        || e.getClickedBlock().getType() == Material.LEVER
                        || e.getClickedBlock().getType() == Material.STONE_BUTTON
                        || e.getClickedBlock().getType() == Material.WOOD_BUTTON
                        || e.getClickedBlock().getType() == Material.REDSTONE_COMPARATOR_OFF
                        || e.getClickedBlock().getType() == Material.REDSTONE_COMPARATOR_ON
                        || e.getClickedBlock().getType() == Material.DIODE_BLOCK_ON
                        || e.getClickedBlock().getType() == Material.DIODE_BLOCK_OFF
                        || e.getClickedBlock().getType() == Material.ENDER_PORTAL_FRAME) {
                    e.setCancelled(true);
                }
                if (e.getClickedBlock().getType() == Material.GRASS || e.getClickedBlock().getType() == Material.DIRT) {
                    Material item = e.getPlayer().getItemInHand().getType();
                    if (item == Material.WOOD_HOE || item == Material.STONE_HOE || item == Material.IRON_HOE || item == Material.GOLD_HOE || item == Material.DIAMOND_HOE) {
                        e.setCancelled(true);
                    }
                }
            }
            
            if (e.getClickedBlock().getType() == Material.ENDER_CHEST) {
                e.setCancelled(true);
                
            }
        }
    }
    
    @EventHandler
    public void chun(ChunkUnloadEvent ev) {
        List<String> nomes = new ArrayList();
        List<UUID> uids = new ArrayList();
        for (Entity e : ev.getChunk().getEntities()) {
            if (server == ServerType.TUTORIAL) {
                if (BixosLTutorial.bixosl.containsValue(e.getUniqueId())) {
                    if (!uids.contains(e.getUniqueId())) {
                        uids.add(e.getUniqueId());
                    }
                    if (!nomes.contains(e.getCustomName())) {
                        nomes.add(e.getCustomName());
                    }
                    e.remove();
                    
                }
            }
            if (server == ServerType.LOBBY) {
                if (BixosLobby.bixosl.contains(e.getUniqueId())) {
                    MobsApi.bixos.remove(e.getUniqueId());
                    BixosLobby.bixosl.remove(e.getUniqueId());
                    e.remove();
                }
            }
        }
        for (UUID uid : uids) {
            MobsApi.bixos.remove(uid);
        }
        for (String name : nomes) {
            BixosLTutorial.bixosl.remove(name);
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void tomadano(EntityDamageByEntityEvent ev
    ) {
        if (ev.getDamager() instanceof Player) {
            Player p = (Player) ev.getDamager();
            if ((ev.getEntity() instanceof ArmorStand || ev.getEntity() instanceof Painting || ev.getEntity() instanceof ItemFrame)) {
                if (!p.isOp() || p.getGameMode() != GameMode.CREATIVE) {
                    ev.setCancelled(true);
                    
                }
            }
        }
        
        if (ev.getDamager() instanceof TNTPrimed || ev.getDamager() instanceof SmallFireball || ev.getDamager() instanceof Fireball) {
            
            if ((ev.getEntity() instanceof ArmorStand || ev.getEntity() instanceof Painting || ev.getEntity() instanceof ItemFrame)) {
                
                ev.setCancelled(true);
            }
            
        }
        
        if (ev.getEntity() instanceof Player) {
            
            Player p = (Player) ev.getEntity();
            
            for (ItemStack parte : p.getInventory().getArmorContents()) {
                if (parte.getDurability() > parte.getType().getMaxDurability() / 2) {
                    parte.setDurability((short) 0);
                }
                
            }
            p.updateInventory();
            
        }
        
    }
    
    @EventHandler
    public void mudaPlaca(SignChangeEvent ev
    ) {
        SignUtils.colocaplaca(ev);
    }
    
    @EventHandler
    public void quit(PlayerQuitEvent e
    ) {
        if (CardsDB.enderchest.contains(e.getPlayer().getUniqueId())) {
            CardsDB.enderchest.remove(e.getPlayer().getUniqueId());
        }
        if (PlayerManager.itensquickbar.containsKey(e.getPlayer())) {
            
            e.getPlayer().updateInventory();
            int task = (int) MetaShit.getMetaObject("cspell", e.getPlayer());
            Bukkit.getScheduler().cancelTask(task);
            PlayerManager.acabaConj(e.getPlayer());
        }
        e.setQuitMessage(null);
    }
    
    @EventHandler
    public void quit(PlayerKickEvent e
    ) {
        if (CardsDB.enderchest.contains(e.getPlayer().getUniqueId())) {
            CardsDB.enderchest.remove(e.getPlayer().getUniqueId());
        }
        if (PlayerManager.itensquickbar.containsKey(e.getPlayer())) {
            
            e.getPlayer().updateInventory();
            int task = (int) MetaShit.getMetaObject("cspell", e.getPlayer());
            Bukkit.getScheduler().cancelTask(task);
            PlayerManager.acabaConj(e.getPlayer());
        }
        e.setLeaveMessage(null);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void join(final PlayerJoinEvent ev
    ) {
        ev.setJoinMessage(null);
        MatchMaker.db.resetBuffers(ev.getPlayer().getUniqueId());
        CardsDB.enderchest.add(ev.getPlayer().getUniqueId());
        ev.getPlayer().getEnderChest().clear();
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                final List<ItemStack> itens = CardsDB.EnderChest(ev.getPlayer().getUniqueId());
                final ArmorSelectMenu.Armadura ar = MatchMaker.db.getArmor(ev.getPlayer().getUniqueId());
                Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {
                    
                    @Override
                    public void run() {
                        if (ar != null) {
                            ev.getPlayer().getInventory().setArmorContents(ar.getArmorContents());
                        }
                        if (itens != null) {
                            for (int x = 0; x < itens.size(); x++) {
                                ev.getPlayer().getEnderChest().setItem(x, itens.get(x));
                            }
                            
                        }
                        
                        if (ev.getPlayer() != null) {
                            
                            CardsDB.enderchest.remove(ev.getPlayer().getUniqueId());
                            ControleCartas.updateStats(ev.getPlayer());
                            ControleCartas.calculaVida(ev.getPlayer());
                            Bukkit.getPluginManager().callEvent(new CardsLoadedEvent(ev.getPlayer()));
                            
                        }
                    }
                });
            }
        }).start();
        
        Mana mana = Mana.getMana(ev.getPlayer());
        if (server == ServerType.GAME || server == ServerType.TUTORIAL) {
            ev.getPlayer().setLevel(mana.mana);
            ev.getPlayer().setExp(Math.min(0.999F, (float) mana.mana / mana.maxMana));
        } else {
            ev.getPlayer().setExp(0.999F);
            ev.getPlayer().setLevel(0);
        }
    }
    
    @EventHandler
    public void move(PlayerMoveEvent ev
    ) {
        if (ev.getPlayer().getGameMode() != GameMode.SPECTATOR) {
            Impulsao.onPlayerMoveonBlock(ev);
        }
    }
    
    @EventHandler
    public void dropitem(final PlayerDropItemEvent ev
    ) {
        ev.setCancelled(true);
        Bukkit.getScheduler().scheduleSyncDelayedTask(CardWarsPlugin._instance, new Runnable() {
            
            @Override
            public void run() {
                ev.getPlayer().updateInventory();
                
            }
        }, 2);
    }
    
    @EventHandler
    public void close(InventoryCloseEvent ev
    ) {
        for (Menu m : Menu.menus.values()) {
            
            if (m.getTipo() != MenuType.AMBOS) {
                if (m.getTipo() == MenuType.LOBBY && (server != CardWarsPlugin.ServerType.LOBBY && server != CardWarsPlugin.ServerType.TUTORIAL)) {
                    continue;
                }
                if (m.getTipo() == MenuType.JOGO && server != ServerType.GAME) {
                    continue;
                }
                
            }
            
            m.closeInventory((Player) ev.getPlayer(), ev.getInventory());
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void entient(final PlayerInteractEntityEvent ev
    ) {
        
        if (ev.getPlayer().isOp() && ev.getRightClicked().getType() != EntityType.PLAYER) {
            if (ev.getPlayer().getItemInHand().getType() == Material.BONE) {
                ev.getRightClicked().remove();
                return;
            }
        }
        if (ev.getRightClicked() instanceof ArmorStand) {
            if (!ev.getPlayer().isOp() || ev.getPlayer().getGameMode() != GameMode.CREATIVE) {
                ev.setCancelled(true);
            }
            
        }
        if (ev.getRightClicked() instanceof ItemFrame) {
            if (!ev.getPlayer().isOp()) {
                ev.setCancelled(true);
            }
            
            ItemFrame s = (ItemFrame) ev.getRightClicked();
            if (s.getItem() == null) {
                return;
            }
            if (clicaItemFrame(s, ev.getPlayer())) {
                ev.setCancelled(true);
            }
        }
        if (ev.getRightClicked() instanceof Painting) {
            final Painting pi = (Painting) ev.getRightClicked();
            
            clicaPintura(pi, ev.getPlayer());
            
        }
    }
    
    public boolean clicaItemFrame(ItemFrame s, Player p) {
        boolean doreturn = false;
        if (s.getItem().getType() == Material.WATCH) {
            if (p.getItemInHand().getType() != Material.WATCH) {
                doreturn = true;
            }
            if (server == ServerType.LOBBY) {
                if (!Cooldown.isCooldown(p, "clicoverjogadores")) {
                    
                    Cooldown.addCoolDown(p, "clicoverjogadores", 6000);
                    SomeNegada.usaCmd(p);
                    
                } else {
                    p.sendMessage("§cEspere um pouco para fazer isso novamente!");
                }
            }
        }
        if (s.getItem().getType() == Material.COMPASS) {
            if (p.getItemInHand().getType() != Material.COMMAND) {
                doreturn = true;
                
            }
            if (CardWarsPlugin.server == ServerType.LOBBY) {
                Menu.menus.get(TeleporterMenu.menuname).openInventory(p);
                
            }
        }
        return doreturn;
    }
    
    public void clicaPintura(final Painting pi, final Player p) {
        if (pi.getArt() == jogar || pi.getArt() == jogargrande || pi.getArt() == shop || pi.getArt() == armaduras || pi.getArt() == estoque || pi.getArt() == pacote || pi.getArt() == cartas) {
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                    if (server == ServerType.LOBBY && MatchMaker.db.getIngamePlayer(p) == null || server == ServerType.TUTORIAL) {
                        
                        if (pi.getArt() == jogar || pi.getArt() == jogargrande) {
                            if (server != ServerType.TUTORIAL) {
                                
                                if (MatchMaker.db.isPlayerInQueue(p.getUniqueId()) == DBHandler.PlayerStatus.NONE) {
                                    MatchMaker.addPlayerToSoloQueue(p);
                                }
                            }
                        }
                        boolean feztuto = MatchMaker.db.fezTutorial(p.getUniqueId());
                        if (feztuto && server == ServerType.TUTORIAL) {
                            p.sendMessage("§aVocê não pode abrir isso agora no tutorial!");
                            return;
                        }
                        if (pi.getArt() == armaduras) {
                            Menu.menus.get(ArmorSelectMenu.menuname).openInventory(p);
                        }
                        if (pi.getArt() == shop) {
                            if (server != ServerType.TUTORIAL) {
                                Menu.menus.get(CardSellerMenu.menuname).openInventory(p);
                            }
                        }
                        if (pi.getArt() == cartas) {
                            Menu.menus.get(CardSelectorMenu.menuname).openInventory(p);
                        }
                        if (pi.getArt() == estoque) {
                            Menu.menus.get(CardStockMenu.menuname).openInventory(p);
                        }
                        if (pi.getArt() == pacote) {
                            if (server != ServerType.TUTORIAL) {
                                Menu.menus.get(PackSellerMenu.menuname).openInventory(p);
                            }
                        }
                    }
                    
                }
            }
            ).start();
        }
    }
}
