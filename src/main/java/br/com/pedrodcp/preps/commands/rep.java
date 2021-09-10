package br.com.pedrodcp.preps.commands;

import br.com.pedrodcp.preps.api.RepsAPI;
import br.com.pedrodcp.preps.managers.TimeManager;
import br.com.pedrodcp.preps.models.Account;
import br.com.pedrodcp.preps.statements.Statements;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
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
                                    p.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-positivo-enviado").replace("&", "§").replace("%jogador%", target.getPlayerName()));
                                    Statements.saveAccounts();
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
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-positivo-enviado").replace("&", "§").replace("%jogador%", target.getPlayerName()));
                                        Statements.saveAccounts();
                                        long cooldown = TimeUnit.MILLISECONDS.convert(getInstance().getConfig().getInt("Config.cooldown-tempo"), TimeUnit.MINUTES);
                                        account.setCooldown(System.currentTimeMillis() + cooldown);
                                    }
                                } else {
                                    p.sendMessage(getInstance().getConfig().getString("Mensagens.comando-params").replace("&", "§"));
                                }
                            } else {
                                p.sendMessage(getInstance().getConfig().getString("Mensagens.cooldown-msg").replace("&", "§").replace("%tempo%", TimeManager.getTime(account.getCooldown() - System.currentTimeMillis())));
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
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-negativo-enviado").replace("&", "§").replace("%jogador%", target.getPlayerName()));
                                        Statements.saveAccounts();
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
                                            p.sendMessage(getInstance().getConfig().getString("Mensagens.ponto-negativo-enviado").replace("&", "§").replace("%jogador%", target.getPlayerName()));
                                            Statements.saveAccounts();
                                            long time = TimeUnit.MILLISECONDS.convert(getInstance().getConfig().getInt("Config.cooldown-tempo"), TimeUnit.MINUTES);
                                            account.setCooldown(System.currentTimeMillis() + time);
                                        }
                                    } else {
                                        p.sendMessage(getInstance().getConfig().getString("Mensagens.comando-params").replace("&", "§"));
                                    }
                                } else {
                                    p.sendMessage(getInstance().getConfig().getString("Mensagens.cooldown-msg").replace("&", "§").replace("%tempo%", TimeManager.getTime(account.getCooldown() - System.currentTimeMillis())));
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
                                        List<String> list = getInstance().getConfig().getStringList("Mensagens.ver-pontos");
                                        list.replaceAll(l -> l.replace("&", "§"));
                                        list.replaceAll(l -> l.replace("%jogador%", target.getPlayerName().toUpperCase(Locale.ROOT)));
                                        list.replaceAll(l -> l.replace("%pontos-positivos%", targetPositivos));
                                        list.replaceAll(l -> l.replace("%pontos-negativos%", targetNegativos));
                                        for (String msg : list) {
                                            p.sendMessage(msg);
                                        }
                                    }
                                } else {
                                    String accountPositivos = String.valueOf(account.getPositivo());
                                    String accountNegativos = String.valueOf(account.getNegativo());
                                    List<String> list = getInstance().getConfig().getStringList("Mensagens.ver-pontos");
                                    list.replaceAll(l -> l.replace("&", "§"));
                                    list.replaceAll(l -> l.replace("%jogador%", p.getName().toUpperCase(Locale.ROOT)));
                                    list.replaceAll(l -> l.replace("%pontos-positivos%", accountPositivos));
                                    list.replaceAll(l -> l.replace("%pontos-negativos%", accountNegativos));
                                    for (String msg : list) {
                                        p.sendMessage(msg);
                                    }
                                }
                            } else {
                                if (option.equalsIgnoreCase("top")) {
                                    List<String> lines = getInstance().getConfig().getStringList("Mensagens.msg-top");
                                    lines.replaceAll(l -> l.replace("&", "§"));
                                    List<String> topList = Statements.getTops();
                                    lines.replaceAll(l -> l.replace("%top10%", topList.toString().replace("[", "").replace("]", "").replace(", ", "\n")));
                                    for (String msg : lines) {
                                        p.sendMessage(msg);
                                    }
                                } else {
                                    p.sendMessage(getInstance().getConfig().getString("Mensagens.comando-params").replace("&", "§"));
                                }
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

}
