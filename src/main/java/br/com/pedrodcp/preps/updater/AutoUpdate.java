package br.com.pedrodcp.preps.updater;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import br.com.pedrodcp.preps.Project;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class AutoUpdate {

    private Plugin plugin;
    private String urlVersionCheck;
    private String urlDownload;
    private String currentVersion;

    private boolean autoDownload;

    private String BroadcastMessage = "Reinicie o plugin para que a nova atualizacao seja implementada.";

    public AutoUpdate(Plugin plugin, String UrlVersion, String UrlDownload) {
        this(plugin, UrlVersion, UrlDownload, true);
    }

    public AutoUpdate(Plugin plugin, String UrlVersion, String UrlDownload, boolean AutoDownload) {
        this.plugin = plugin;
        this.urlVersionCheck = UrlVersion;
        this.urlDownload = UrlDownload;
        this.currentVersion = plugin.getDescription().getVersion();
        this.autoDownload = AutoDownload;
        start();
    }

    private void start() {
        String v = null;
        try {
            v = getText(urlVersionCheck);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§c[Project] Ocorreu um erro ao detectar uma nova versao para o sistema.");
        }
        if (!v.equalsIgnoreCase(currentVersion)) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§e[Project] Atualizacao disponivel! - (§c" + Project.getInstance().getDescription().getVersion() + " §e» §a" + v + "§e)");
            Bukkit.getConsoleSender().sendMessage("§e[Project] Download: §7Instalando...");
            Bukkit.getConsoleSender().sendMessage("");
            if (autoDownload)
                startDownload();
        } else {
            Bukkit.getConsoleSender().sendMessage("§e[Project] Nenhuma nova versao foi encontrada.");
        }
    }

    private boolean startDownload() {
        try {
            URL download = new URL(this.urlDownload);
            BufferedInputStream in = null;
            FileOutputStream fout = null;
            try {
                Bukkit.getConsoleSender().sendMessage("§8[Project] Baixando nova versao...");
                in = new BufferedInputStream(download.openStream());
                fout = new FileOutputStream("plugins" + System.getProperty("file.separator") + plugin.getName() + ".jar");

                final byte data[] = new byte[1024];
                int count;
                while ((count = in.read(data, 0, 1024)) != -1) {
                    fout.write(data, 0, count);
                }
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§8[Project] Nao foi possivel prosseguir com a atualizacao do plugin.");
                return false;
            } finally {
                if (in != null) {
                    in.close();
                }
                if (fout != null) {
                    fout.close();
                }
            }
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§9[Project] A nova versao foi instalada!");
            Bukkit.getConsoleSender().sendMessage("§9[Project] §7Download: §a100%");
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§9[Project] Aviso: §7" + BroadcastMessage);
            Bukkit.getConsoleSender().sendMessage("");
            return true;
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("§8[Project] Nao foi possivel prosseguir com a atualizacao do plugin.");
            return false;
        }
    }

    private String getText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
