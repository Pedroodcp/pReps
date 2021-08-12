package br.com.pedrodcp.preps.models;

import java.util.ArrayList;

public class Account {

    public static ArrayList<Account> accounts = new ArrayList<>();

    private String playerName;

    private int positivo;
    private int negativo;

    private long cooldown;

    public Account(String playerName, int positivo, int negativo, long cooldown) {
        this.playerName = playerName;
        this.positivo = positivo;
        this.negativo = negativo;
        this.cooldown = cooldown;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPositivo() {
        return positivo;
    }

    public void setPositivo(int positivo) {
        this.positivo = positivo;
    }

    public int getNegativo() {
        return negativo;
    }

    public void setNegativo(int negativo) {
        this.negativo = negativo;
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

}
