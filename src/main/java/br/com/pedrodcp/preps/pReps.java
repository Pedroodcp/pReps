package br.com.pedrodcp.preps;

import br.com.pedrodcp.preps.commands.rep;
import br.com.pedrodcp.preps.events.Listeners;
import br.com.pedrodcp.preps.models.database.ConnectionModel;
import br.com.pedrodcp.preps.models.database.MySQLConnection;
import br.com.pedrodcp.preps.models.database.SQLiteConnection;
import br.com.pedrodcp.preps.statements.Statements;
import br.com.pedrodcp.preps.updater.AutoUpdate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public class pReps extends JavaPlugin {

    private static pReps instance;

    public static File database;
    public static ConnectionModel connectionModel;

    @Override
    public void onEnable() {
        instance = this;
        if (!new File(getDataFolder(), "config.yml").exists()) saveDefaultConfig();
        database = new File(getDataFolder(), "database.db");
        connectionModel = getConfig().getBoolean("Database.mysql") ? new MySQLConnection(getConfig().getString("Database.host"), getConfig().getInt("Database.port"), getConfig().getString("Database.database"), getConfig().getString("Database.user"), getConfig().getString("Database.password")) : new SQLiteConnection();
        Statements.initialize();
        loadCommands();
        loadConfig();
        loadEvents();
        if (getConfig().getBoolean("Database.mysql") == isEnabled()) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§a[pReps] Sistema carregado com sucesso!");
            Bukkit.getConsoleSender().sendMessage("§7Detectado: §aMySQL");
            Bukkit.getConsoleSender().sendMessage("");
        } else {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§a[pReps] Sistema carregado com sucesso!");
            Bukkit.getConsoleSender().sendMessage("§7Detectado: §8SQLite");
            Bukkit.getConsoleSender().sendMessage("");
        }
        new AutoUpdate(this,"https://api.spigotmc.org/legacy/update.php?resource=96330.txt","https://drive.google.com/uc?export=download&id=1pHnrH9jtm0oLNM-l80BW_5WoH1BQ-LWa", true);
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
        try {
            Statements.saveAccounts();
            Statements.connection.close();
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§c[pReps] Sistema desativado com sucesso.");
            Bukkit.getConsoleSender().sendMessage("§7Armazenamento: §aSalvo");
            Bukkit.getConsoleSender().sendMessage("");
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§c[pReps] Sistema desativado com erro no armazenamento.");
            Bukkit.getConsoleSender().sendMessage("§7Armazenamento: §cErro");
            Bukkit.getConsoleSender().sendMessage("");
        }
    }

    public void loadCommands() {
        getCommand("rep").setExecutor(new rep());
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(false);
        saveDefaultConfig();
    }

    public void loadEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Listeners(), this);
    }

    public static pReps getInstance() {
        return instance;
    }

}
