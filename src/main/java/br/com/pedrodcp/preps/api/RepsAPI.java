package br.com.pedrodcp.preps.api;

import org.bukkit.entity.Player;
import br.com.pedrodcp.preps.models.Account;

public class RepsAPI {

    public static Account getAccount(Player player) {
        return getAccount(player.getName());
    }

    public static Account getAccount(String player) {
        for (Account account : Account.accounts)
            if (account.getPlayerName().equalsIgnoreCase(player.toLowerCase())) return account;
        return null;
    }

    public static boolean setPositivo(String player, int P) {
        Account account = getAccount(player);
        if (account == null) return false;
        account.setPositivo(P);
        return true;
    }

    public static boolean getPositivo(String player, int P) {
        Account account = getAccount(player);
        return account != null && account.getPositivo() == P;
    }

    public static boolean setNegativo(String player, int N) {
        Account account = getAccount(player);
        if (account == null) return false;
        account.setNegativo(N);
        return true;
    }

    public static boolean getNegativo(String player, int N) {
        Account account = getAccount(player);
        return account != null && account.getNegativo() == N;
    }

    public static boolean setCooldown(String player, long cooldown) {
        Account account = getAccount(player);
        if (account == null) return false;
        account.setCooldown(cooldown);
        return true;
    }

    public static boolean getCooldown(String player, long cooldown) {
        Account account = getAccount(player);
        return account != null && account.getCooldown() == cooldown;
    }

    //

    public static boolean setPositivo(Player player, int P) {
        return getPositivo(player.getName(), P);
    }

    public static boolean getPositivo(Player player, int P) {
        return getPositivo(player.getName(), P);
    }

    public static boolean setNegativo(Player player, int N) {
        return getNegativo(player.getName(), N);
    }

    public static boolean getNegativo(Player player, int N) {
        return getNegativo(player.getName(), N);
    }

    public static boolean setCooldown(Player player, long cooldown) {
        return getCooldown(player.getName(), cooldown);
    }

    public static boolean getCooldown(Player player, long cooldown) {
        return getCooldown(player.getName(), cooldown);
    }

}
