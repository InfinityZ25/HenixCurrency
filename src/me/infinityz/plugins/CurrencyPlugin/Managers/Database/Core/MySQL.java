package me.infinityz.plugins.CurrencyPlugin.Managers.Database.Core;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.Properties;

public class MySQL {
    private String username;
    private String password;
    private String database;
    private String host;
    private int port;
    private Connection connection;

    public MySQL(String host, String username, String password, String database, int port){
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        this.port = port;
        initConnection(true);
    }

    private void initConnection(boolean bol){
        try {
            final Properties properties = new Properties();
            properties.setProperty("user", this.username);
            properties.setProperty("password", this.password);
            properties.setProperty("autoReconnect", "true");
            properties.setProperty("verifyServerCertificate", "false");
            properties.setProperty("useSSL", "false");
            properties.setProperty("requireSSL", "false");
            properties.setProperty("connectTimeout", "500");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, properties);
            if(bol){
                System.out.println("[Currency] Mysql has been successfully established!");
            }
        } catch (SQLException e) {
            if(bol){
                System.out.println("[Currency] Mysql connection could not be established due to " + e.getCause().getLocalizedMessage());
                System.out.println("[Currency] jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database);
            }
            //Bukkit.getServer().getPluginManager().disablePlugin(HenixCore.getInstance());
            //e.printStackTrace();
        }
    }

    private boolean isConnected() {
        return this.connection != null;
    }

    public  void disconnect() {
        if (!isConnected())return;
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void update(final String qry) {
        if (isConnected()) {
            try {
                connection.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet getResult(final String qry) {
        if (isConnected()) {
            try {
                return connection.createStatement().executeQuery(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getFirstString(final ResultSet rs, final int l, final String re, final int t) {
        try {
            while (rs.next()) {
                if (rs.getString(l).equalsIgnoreCase(re)) {
                    return rs.getString(t);
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public int getFirstInt(final ResultSet rs, final int l, final String re, final int t) {
        try {
            while (rs.next()) {
                if (rs.getString(l).equalsIgnoreCase(re)) {
                    return rs.getInt(t);
                }
            }
        } catch (Exception ex) {
        }
        return 0;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeRessources(final ResultSet rs, final PreparedStatement st) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ex2) {
            }
        }
    }

    public void close(final PreparedStatement st, final ResultSet rs) {
        try {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception ex) {
        }
    }

    public void executeUpdate(final String statement) {
        try {
            final PreparedStatement st = connection.prepareStatement(statement);
            st.executeUpdate();
            close(st, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeUpdate(final PreparedStatement statement) {
        try {
            statement.executeUpdate();
            close(statement, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(final String statement) {
        try {
            final PreparedStatement st = connection.prepareStatement(statement);
            return st.executeQuery();
        } catch (Exception ex) {
            return null;
        }
    }

    public ResultSet executeQuery(final PreparedStatement statement) {
        try {
            return statement.executeQuery();
        } catch (Exception ex) {
            return null;
        }
    }

    public ResultSet query(final String query) throws SQLException {
        final Statement stmt = connection.createStatement();
        try {
            stmt.executeQuery(query);
            return stmt.getResultSet();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + e.getMessage());
            return null;
        }
    }
}
