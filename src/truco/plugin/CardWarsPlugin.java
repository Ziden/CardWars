package truco.plugin;

import truco.plugin.data.mysql.CardsDB;
import truco.plugin.cards.Carta;
import truco.plugin.cards.EventosCartas;
import truco.plugin.cards.ControleCartas;
import br.pj.newlibrarysystem.eventos.EventoDispatcher;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import truco.plugin.arena.Arena;
import truco.plugin.arena.ArenaSave;
import truco.plugin.arena.CmdArena;
import truco.plugin.arena.DBArena;
import truco.plugin.chat.ChatManager;
import truco.plugin.cmds.*;
import truco.plugin.itens.Items;
import truco.plugin.listeners.BothListener;
import truco.plugin.listeners.GameListener;
import truco.plugin.listeners.LobbyListener;
import truco.plugin.matchmaking.DBHandler;
import truco.plugin.matchmaking.Threads.MatchMaker;
import static truco.plugin.matchmaking.Threads.MatchMaker.db;
import truco.plugin.matchmaking.Threads.PlayerManagerThread;
import truco.plugin.data.mysql.MatchHistoryDB;
import truco.plugin.cards.CardLoader;
import truco.plugin.data.ConfGlobalManager;
import truco.plugin.functions.lobby.LobbyMessages;
import truco.plugin.menus.Menu;
import truco.plugin.functions.game.Mana;
import truco.plugin.functions.fun.DoubleJump;
import truco.plugin.functions.fun.MobStack;
import truco.plugin.functions.lobby.hologramas.ControleHologramas;
import truco.plugin.listeners.LibraryListener;
import truco.plugin.socket.SocketListener;
import truco.plugin.socket.SocketManager;
import truco.plugin.data.MetaShit;
import truco.plugin.cards.skills.skilltypes.Channeling;
import truco.plugin.cards.StatusEffect;
import truco.plugin.cards.skills.skilltypes.Trap;
import truco.plugin.damage.DamageManager;
import truco.plugin.functions.Teleporter;
import truco.plugin.managers.BixosLTutorial;
import truco.plugin.managers.lobbys.MultipleLobbysManager;
import truco.plugin.managers.BixosLobby;
import truco.plugin.managers.PlayerManager;
import truco.plugin.managers.maniainventorysync.ManiaInventorySync;

import truco.plugin.utils.Utils;
import truco.plugin.utils.mobapi.mobs.EntityTypes;
import truco.plugin.utils.mobapi.mobs.MobsApi;

/**
 *
 * @author USER
 *
 */
public class CardWarsPlugin extends JavaPlugin {
    
    public static String serverName = null;
    public static CardWarsPlugin _instance = null;
    public static Logger log = null;
    public static Random random = new Random();
    public static WorldGuardPlugin worldGuard;
    public static WorldEditPlugin worldEdit;
    public static Arena mainarena = null;
    public static boolean matchmaking = false;
    
    public static Connection conn = null;
    public static ServerType server = null;
    
    public static void log(String g) {
        System.out.println("[CardWarsLog] " + g);
    }
    
    public static MultipleLobbysManager lobbymanager;
    
    private static java.sql.Connection createConnection() {
        try {
            Connection c = null;
            String connS = ConfGlobalManager.getStringSq("mysqlconn");
            String user = ConfGlobalManager.getStringSq("mysqluser");
            String senha = ConfGlobalManager.getStringSq("mysqlpass");
            try {
                
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
            c = DriverManager.getConnection(connS, user, senha);

            /*
             * try { Class.forName("org.sqlite.JDBC"); } catch
             * (ClassNotFoundException ex) { ex.printStackTrace(); } connection
             * = DriverManager.getConnection("jdbc:sqlite:" +
             * Main._instance.getDataFolder().getPath() + File.separator +
             * "DataBaseMatchMaking.db"); connection.setAutoCommit(true);
             */
            return c;
        } catch (Throwable ex) {
            ex.printStackTrace();
            
        }
        return null;
    }
    
    private String getSvName() {
        String path = null;
        try {
            path = new File(".").getCanonicalPath();
            path = path.substring(path.lastIndexOf(File.separator) + 1);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return path;
    }
    
    @Override
    public void onEnable() {
        
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        
        File f = this.getDataFolder();
        
        f.mkdirs();
        _instance = this;
        new EntityTypes();
        new StatusEffect();
        new ChatManager();
        Channeling.playEffeitos();
        for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (e instanceof Item) {
                    e.remove();
                }
            }
        }
        worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        worldGuard = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        log = getLogger();
        
        ConfGlobalManager.LoadConf();
        
        serverName = getSvName();
        
        SocketManager.start();
        server = ServerType.valueOf(ConfGlobalManager.getString("ServerType"));
        if (ConfGlobalManager.getString("MatchMaking").equalsIgnoreCase("true") && server == ServerType.LOBBY) {
            matchmaking = true;
        }
        startDatabase();
        new MatchHistoryDB();
        MatchMaker.db = new DBHandler();
        CardsDB.startDatabase();
        
        if (server == ServerType.LOBBY) {
            int tempo = (20 * 60 * 60 * 5) + (20 * 60 * new Random().nextInt(60));
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                
                @Override
                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.kickPlayer("§f§lServidor Reiniciando\nEntre novamente!");
                    }
                    Bukkit.shutdown();
                }
            }, tempo);
            ManiaInventorySync.onEnable(this);
            if (matchmaking) {
                new MatchMaker().start();
                
            }
            new PlayerManagerThread();
            new MobStack();
            
            new ControleHologramas();
            new DoubleJump();
            
            BixosLobby.tacaBixosnoLobby();
            
        }
        if (server == ServerType.TUTORIAL) {
            BixosLTutorial.tacaBixosnoTutorial();
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                
                @Override
                public void run() {
                    if (Bukkit.getOnlinePlayers().size() == 0) {
                        Bukkit.shutdown();
                    }
                }
            }, 20 * 60 * 120, 20 * 60 * 30);
        }
        lobbymanager = new MultipleLobbysManager();
        if (server == ServerType.TUTORIAL || server == ServerType.GAME) {
            Mana.startRegenTimer();
        }
        if (server == ServerType.GAME) {
            Utils.deleteDatasFolders();
            new DamageManager();
            
            try {
                db.freeServerPlayers(serverName);
                db.freeServerGame(CardWarsPlugin.serverName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
        }
        
        registerEvents();
        registersCmds();
        CardLoader.load();
        
        if (server == ServerType.LOBBY || server == ServerType.TUTORIAL) {
            
            new LobbyMessages();
        }
        Items.start();
        DBArena.startDatabase();
        ArenaSave.loadArenas();
        
        if (server == ServerType.GAME) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                
                public void run() {
                    Trap.tickEffect();
                }
            }, 10, 10);
        }
        Menu.Start();
        getServer().getPluginManager().registerEvents(new SocketListener(), this);
    }
    
    public void startDatabase() {
        conn = createConnection();
        try {
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void cardsToFile(String nome) {
        try {
            File fi = new File(nome);
            
            fi.createNewFile();
            FileWriter fw = new FileWriter(fi);
            for (Carta c : ControleCartas.getCards()) {
                String desc = "\"";
                for (String st : c.getDesc()) {
                    desc += st + ",";
                }
                desc += "\"";
                fw.write(c.getNome() + " - " + desc + " - " + (c.getSkill() != null ? (c.getSkill().getName() + "{CD:" + c.getSkill().cd + ",MANA:" + c.getSkill().mana + ",CONJ:" + c.getSkill().getChannelingTime() + "}") : "Sem Skill") + " - " + c.getRaridade().name() + " - " + c.getArmadura() + "\n");
                
            }
            fw.close();
            fi = null;
        } catch (IOException ex) {
            
            ex.printStackTrace();
        }
    }
    
    public static Entity getEntity(UUID uid) {
        for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (e.getUniqueId() == uid) {
                    return e;
                }
            }
        }
        return null;
    }
    
    @Override
    public void onDisable() {
        for (UUID t : MobsApi.bixos.keySet()) {
            Entity et = getEntity(t);
            if (et != null) {
                et.remove();
            }
        }
        if (server == ServerType.LOBBY) {
            
            ManiaInventorySync.onDisable();
        }
        
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (PlayerManager.itensquickbar.containsKey(p)) {
                
                p.updateInventory();
                int task = (int) MetaShit.getMetaObject("cspell", p);
                Bukkit.getScheduler().cancelTask(task);
                PlayerManager.acabaConj(p);
            }
            
        }
        
    }
    
    public static Arena getArena() {
        return mainarena;
    }
    
    public void registersCmds() {
        Bukkit.getPluginCommand("cws").setExecutor(new CmdPrincipal());
        Bukkit.getPluginCommand("arena").setExecutor(new CmdArena());
        Bukkit.getPluginCommand("vercartas").setExecutor(new CmdVerCartas());
        Bukkit.getPluginCommand("cadm").setExecutor(new CmdAdmin());
        Bukkit.getPluginCommand("spawn").setExecutor(new CmdSpawn());
        Bukkit.getPluginCommand("mv").setExecutor(new CmdMV());
        Bukkit.getPluginCommand("staff").setExecutor(new CmdStaff());
       
        if (server == ServerType.LOBBY) {
            
            Bukkit.getPluginCommand("darmoedas").setExecutor(new CmdDarMoedas());
            Bukkit.getPluginCommand("vender").setExecutor(new CmdVender());
            
            Bukkit.getPluginCommand("mostrar").setExecutor(new CmdMostrar());
            
            Bukkit.getPluginCommand("grupo").setExecutor(new CmdGrupo());
            Bukkit.getPluginCommand("sairfila").setExecutor(new CmdSairFila());
            
        }
    }
    
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new VoidWorldGenerator();
    }
    
    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new BothListener(), this);
        getServer().getPluginManager().registerEvents(new Teleporter(), this);
        if (server == ServerType.LOBBY || server == ServerType.TUTORIAL) {
            getServer().getPluginManager().registerEvents(new LobbyListener(), this);
            EventoDispatcher.getInstance().addListener(new LibraryListener());
        }
        if (server == ServerType.GAME) {
            getServer().getPluginManager().registerEvents(new GameListener(), this);
        }
        
        if (server == ServerType.GAME || server == ServerType.TUTORIAL) {
            getServer().getPluginManager().registerEvents(new EventosCartas(), this);
        }
        
    }
    public static int serverstatus = -1;
    
    public static enum ServerType {
        
        LOBBY(), TUTORIAL(), GAME();
    }
}
