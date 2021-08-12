package br.com.pedrodcp.preps.events;

import br.com.pedrodcp.preps.api.RepsAPI;
import br.com.pedrodcp.preps.models.Account;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (RepsAPI.getAccount(p.getName()) == null) {
            Account.accounts.add(new Account(p.getName(), 0, 0, 0));
        }
    }

}
