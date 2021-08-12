package br.com.pedrodcp.preps.statements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.pedrodcp.preps.pReps;
import br.com.pedrodcp.preps.models.Account;

public class Statements {

    private static Connection connection;

    private static void openConnection() {
        Statements.connection = pReps.connectionModel.getConnection();
    }

    public static void initialize() {
        try {
            openConnection();
            PreparedStatement st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS preps_pontos (nome varchar(16), positivo int, negativo int, cooldown long)");
            st.executeUpdate();
            st.close();
            loadAccounts();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadAccounts() {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM preps_pontos");
            ResultSet rs = st.executeQuery();
            while (rs.next()) Account.accounts.add(new Account(rs.getString("nome"), rs.getInt("positivo"), rs.getInt("negativo"), rs.getLong("cooldown")));
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean exists(String playerName) {
        boolean exists = false;
        try {
            PreparedStatement st = connection.prepareStatement("SELECT nome FROM preps_pontos WHERE nome=?");
            st.setString(1, playerName.toLowerCase());
            ResultSet rs = st.executeQuery();
            exists = rs.next();
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public static void saveAccounts() {
        try {
            openConnection();
            PreparedStatement existsStatement = connection.prepareStatement("UPDATE preps_pontos SET positivo=?, negativo=?, cooldown=? WHERE nome=?");
            PreparedStatement st = connection.prepareStatement("INSERT INTO preps_pontos (nome, positivo, negativo, cooldown) VALUES (?, ?, ?, ?)");
            for (Account account : Account.accounts) {
                if (exists(account.getPlayerName())) {
                    existsStatement.setInt(1, account.getPositivo());
                    existsStatement.setInt(2, account.getNegativo());
                    existsStatement.setLong(3, account.getCooldown());
                    existsStatement.setString(4, account.getPlayerName().toLowerCase());
                    existsStatement.executeUpdate();
                }else {
                    st.setString(1, account.getPlayerName().toLowerCase());
                    st.setInt(2, account.getPositivo());
                    st.setInt(3, account.getNegativo());
                    st.setLong(4, account.getCooldown());
                    st.executeUpdate();
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
