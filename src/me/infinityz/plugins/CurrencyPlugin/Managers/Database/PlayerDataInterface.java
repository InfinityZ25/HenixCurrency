package me.infinityz.plugins.CurrencyPlugin.Managers.Database;

import org.bukkit.entity.Player;

import java.util.UUID;


public interface PlayerDataInterface {

    void loadPlayer(Player p);

    void savePlayer(Player p);

    void savePlayer(UUID uuid);

    void cacheUser(UUID uuid);

    StorageType getStoragetype();

    enum StorageType{
        MONGO, MYSQL, FILE
    }

}
