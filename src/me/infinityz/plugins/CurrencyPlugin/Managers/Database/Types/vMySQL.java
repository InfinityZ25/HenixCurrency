package me.infinityz.plugins.CurrencyPlugin.Managers.Database.Types;

import me.infinityz.plugins.CurrencyPlugin.Currency;
import me.infinityz.plugins.CurrencyPlugin.Managers.Database.Core.MySQL;
import me.infinityz.plugins.CurrencyPlugin.Managers.Database.PlayerDataInterface;
import me.infinityz.plugins.CurrencyPlugin.Managers.Users.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class vMySQL implements PlayerDataInterface{

    private Currency instance;
    private MySQL mysql;
    private Map<UUID, User> cached_user;

    public vMySQL(Currency instance, String username, String databasename, String password, String hostAddress, Integer port, String arguments) throws Exception{
        this.instance = instance;
        this.cached_user = new HashMap<>();
        this.mysql = new MySQL(instance, hostAddress, username, password, databasename, port, arguments);
        this.mysql.update("CREATE TABLE IF NOT EXISTS Currency (UUID VARCHAR(100), Name VARCHAR(20), Coins Integer);");}


    @Override
    public void loadPlayer(Player p) {
        User user = cached_user.get(p.getUniqueId());
        instance.getUserManager().getUserList().put(p.getUniqueId(), user);
        cached_user.remove(p.getUniqueId());
    }

    @Override
    public void savePlayer(Player p){
        if (!isInDatabase(p)) {
            mysql.update("INSERT INTO Currency (UUID, Name, Coins) VALUES ('" + p.getUniqueId() + "', '" + p.getName() + "', '0');");
            return;
        }
        User user = instance.getUserManager().getUserList().get(p.getUniqueId());
        mysql.update("UPDATE Currency SET Coins='" + user.getCoins() + "', Name='" + p.getName() + "' WHERE UUID='" + p.getUniqueId() + "';");


    }

    @Override
    public void savePlayer(UUID uuid) {
        if (!isInDatabase(uuid)) {
            mysql.update("INSERT INTO Currency (UUID, Name, Coins) VALUES ('" + uuid + "', '" + Bukkit.getOfflinePlayer(uuid).getName() + "', '0');");
            return;
        }
        User user = instance.getUserManager().getUserList().get(uuid);
        mysql.update("UPDATE Currency SET Coins='" + user.getCoins() + "', Name='" + Bukkit.getOfflinePlayer(uuid).getName() + "' WHERE UUID='" + uuid + "';");


    }

    @Override
    public void cacheUser(UUID uuid) {
        try {
            User user = new User(uuid, 0);
            if (!isInDatabase(uuid)) {
                mysql.update("INSERT INTO Currency (UUID, Name, Coins) VALUES ('" + uuid + "', '" + Bukkit.getOfflinePlayer(uuid).getName() + "', '0');");
                cached_user.put(uuid, user);
                return;
            }
            ResultSet userCached = mysql.query("SELECT * FROM Currency WHERE UUID='" + uuid + "'");
            if(userCached.next()){
                user.setCoins(userCached.getInt("Coins"));
            }
            cached_user.put(uuid, user);

        }catch (Exception io){
            io.printStackTrace();
        }

    }

    @Override
    public StorageType getStoragetype() {
        return StorageType.MYSQL;
    }

    private boolean isInDatabase(UUID uuid) {
        try {
            final ResultSet rs = mysql.query("SELECT * FROM Currency WHERE UUID='" + uuid + "'");
            return rs.next() && rs.getString("UUID") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean isInDatabase(Player p) {
        return isInDatabase(p.getUniqueId());
    }
}
