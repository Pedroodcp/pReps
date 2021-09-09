package br.com.pedrodcp.preps;

import br.com.pedrodcp.preps.commands.rep;
import br.com.pedrodcp.preps.events.Listeners;
import br.com.pedrodcp.preps.models.database.ConnectionModel;
import br.com.pedrodcp.preps.models.database.MySQLConnection;
import br.com.pedrodcp.preps.models.database.SQLiteConnection;
import br.com.pedrodcp.preps.statements.Statements;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
        loadEvents();
        if (getConfig().getBoolean("Database.mysql") == isEnabled()) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§a[pReps] Plugin carregado com sucesso.");
            Bukkit.getConsoleSender().sendMessage("§7Detectado: §aMySQL");
            Bukkit.getConsoleSender().sendMessage("");
        } else {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§a[pReps] Plugin carregado com sucesso.");
            Bukkit.getConsoleSender().sendMessage("§7Detectado: §8SQLite");
            Bukkit.getConsoleSender().sendMessage("");
        }
    }

    @Override
    public void onDisable() {
        Statements.saveAccounts();
        if (getConfig().getBoolean("Database.mysql") == isEnabled()) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§c[pReps] Plugin desativado com sucesso.");
            Bukkit.getConsoleSender().sendMessage("§7Armazenamento: §aMySQL");
            Bukkit.getConsoleSender().sendMessage("");
        } else {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§c[pReps] Plugin desativado com sucesso.");
            Bukkit.getConsoleSender().sendMessage("§7Armazenamento: §8SQLite");
            Bukkit.getConsoleSender().sendMessage("");
        }
    }

    public void loadCommands() {
        getCommand("rep").setExecutor(new rep());
    }

    public void loadEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Listeners(), this);
    }

    public static pReps getInstance() {
        return instance;
    }
}
