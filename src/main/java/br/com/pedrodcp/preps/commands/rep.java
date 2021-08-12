package br.com.pedrodcp.preps.commands;

import br.com.pedrodcp.preps.api.RepsAPI;
import br.com.pedrodcp.preps.models.Account;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static br.com.pedrodcp.preps.pReps.*;

public class rep implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (!(s instanceof Player)) return false;

        if (c.getName().equalsIgnoreCase("rep")) {
            Player p = (Player) s;
            if (RepsAPI.getAccount(p.getName()) == null) {
                Account.accounts.add(new Account(p.getName(), 0, 0, 0));
            }
            Account account = RepsAPI.getAccount(p.getName());
            if (!p.hasPermission(getInstance().getConfig().getString("Config.permissão"))) {
                p.sendMessage(getInstance().getConfig().getString("Mensagens.sem-permissão").replace("&", "§"));

            } else {
                if (args.length >= 1) {
                    String option = args[0];
                    if (option.equalsIgnoreCase("+")) {
                        if (account.getCooldown() == 0) {
                            if (args.length >= 2) {
                                Account target = RepsAPI.getAccount(args[1]);
                                if (target == null) {
                                    p.sendMessage(getInstance().getConfig().getString("Mensagens.jogador-não-encontrado").replace("&", "§"));

                                } else {
                                    target.setPositivo(target.getPositivo() + 1);
                                    p.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-positivo-enviado").replace("&", "§").replace("%jogador%", p.getName()));
                                    long cooldown = TimeUnit.MILLISECONDS.convert(getInstance().getConfig().getInt("Config.cooldown-tempo"), TimeUnit.MINUTES);
                                    account.setCooldown(System.currentTimeMillis() + cooldown);
                                }
                            } else {
                                p.sendMessage(getInstance().getConfig().getString("Mensagens.comando-params").replace("&", "§"));
                            }
                            return false;
                        } else {
                            if (System.currentTimeMillis() > account.getCooldown()) {
                                account.setCooldown(0);
                                if (args.length >= 2) {
                                    Account target = RepsAPI.getAccount(args[1]);
                                    if (target == null) {
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.jogador-não-encontrado").replace("&", "§"));

                                    } else {
                                        target.setPositivo(target.getPositivo() + 1);
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-positivo-enviado").replace("&", "§").replace("%jogador%", p.getName()));
                                        long cooldown = TimeUnit.MILLISECONDS.convert(getInstance().getConfig().getInt("Config.cooldown-tempo"), TimeUnit.MINUTES);
                                        account.setCooldown(System.currentTimeMillis() + cooldown);
                                    }
                                } else {
                                    p.sendMessage(getInstance().getConfig().getString("Mensagens.comando-params").replace("&", "§"));
                                }
                            } else {
                                p.sendMessage(getInstance().getConfig().getString("Mensagens.cooldown-msg").replace("&", "§").replace("%tempo%", getTempo(account.getCooldown() - System.currentTimeMillis())));
                            }
                        }

                    } else {
                        if (option.equalsIgnoreCase("-")) {
                            if (account.getCooldown() == 0) {
                                if (args.length >= 2) {
                                    Account target = RepsAPI.getAccount(args[1]);
                                    if (target == null) {
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.jogador-não-encontrado").replace("&", "§"));

                                    } else {
                                        target.setNegativo(target.getNegativo() + 1);
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-negativo-enviado").replace("&", "§").replace("%jogador%", p.getName()));
                                        long time = TimeUnit.MILLISECONDS.convert(getInstance().getConfig().getInt("Config.cooldown-tempo"), TimeUnit.MINUTES);
                                        account.setCooldown(System.currentTimeMillis() + time);
                                    }
                                } else {
                                    p.sendMessage(getInstance().getConfig().getString("Mensagens.comando-params").replace("&", "§"));
                                }
                                return false;
                            } else {
                                if (System.currentTimeMillis() > account.getCooldown()) {
                                    account.setCooldown(0);
                                    if (args.length >= 2) {
                                        Account target = RepsAPI.getAccount(args[1]);
                                        if (target == null) {
                                            p.sendMessage(getInstance().getConfig().getString("Mensagens.jogador-não-encontrado").replace("&", "§"));

                                        } else {
                                            target.setNegativo(target.getNegativo() + 1);
                                            p.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-negativo-enviado").replace("&", "§").replace("%jogador%", p.getName()));
                                            long time = TimeUnit.MILLISECONDS.convert(getInstance().getConfig().getInt("Config.cooldown-tempo"), TimeUnit.MINUTES);
                                            account.setCooldown(System.currentTimeMillis() + time);
                                        }
                                    } else {
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.comando-params").replace("&", "§"));
                                    }
                                } else {
                                    p.sendMessage(getInstance().getConfig().getString("Mensagens.cooldown-msg").replace("&", "§").replace("%tempo%", getTempo(account.getCooldown() - System.currentTimeMillis())));
                                }
                            }
                        } else {
                            if (option.equalsIgnoreCase("ver")) {
                                if (args.length >= 2) {
                                    Account target = RepsAPI.getAccount(args[1]);
                                    if (target == null) {
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.jogador-não-encontrado").replace("&", "§"));
                                    } else {
                                        String targetPositivos = String.valueOf(target.getPositivo());
                                        String targetNegativos = String.valueOf(target.getNegativo());
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.ver-pontos").replace("&", "§").replace("%jogador%", target.getPlayerName().toUpperCase(Locale.ROOT)).replace("%pontos-positivos%", targetPositivos).replace("%pontos-negativos%", targetNegativos).replace("[", "").replace("]", "").replace(",", "\n"));
                                    }
                                } else {
                                    String accountPositivos = String.valueOf(account.getPositivo());
                                    String accountNegativos = String.valueOf(account.getNegativo());
                                    p.sendMessage(getInstance().getConfig().getString("Mensagens.ver-pontos").replace("&", "§").replace("%jogador%", p.getName().toUpperCase(Locale.ROOT)).replace("%pontos-positivos%", accountPositivos).replace("%pontos-negativos%", accountNegativos).replace("[", "").replace("]", "").replace(",", "\n"));
                                }
                            } else {
                                p.sendMessage(getInstance().getConfig().getString("Mensagens.comando-params").replace("&", "§"));
                            }
                        }
                    }
                } else {
                    p.sendMessage(getInstance().getConfig().getString("Mensagens.comando-params").replace("&", "§"));
                }
            }
        }
        return false;
    }

    public static String getTempo(long time) {
        long variacao = time;
        long vsegundos = variacao / 1000L % 60L;
        long vminutos = variacao / 60000L % 60L;
        long vhoras = variacao / 3600000L % 24L;
        long vdias = variacao / 86400000L % 7L;

        String segundos = String.valueOf(vsegundos).replaceAll("-", "");
        String minutos = String.valueOf(vminutos).replaceAll("-", "");
        String horas = String.valueOf(vhoras).replaceAll("-", "");
        String dias = String.valueOf(vdias).replaceAll("-", "");
        if (dias.equals("0") && horas.equals("0") && minutos.equals("0")) {
            return "" + segundos + "s";
        }
        if (dias.equals("0") && horas.equals("0")) {
            return "" + minutos + "m " + segundos + "s";
        }
        if (dias.equals("0")) {
            return "" + horas + "h " + minutos + "m " + segundos + "s";
        }
        return "" + dias + "d " + horas + "h " + minutos + "m " + segundos + "s ";
    }

}
