package me.infinityz.plugins.CurrencyPlugin.Managers.Database.Types;

import me.infinityz.plugins.CurrencyPlugin.Currency;
import me.infinityz.plugins.CurrencyPlugin.Managers.Database.Core.ConfigFile;
import me.infinityz.plugins.CurrencyPlugin.Managers.Database.PlayerDataInterface;
import me.infinityz.plugins.CurrencyPlugin.Managers.Users.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class vFile implements PlayerDataInterface {

    private Currency instance;
    private ConfigFile file;

    public vFile(Currency instance){
        this.instance = instance;
        file = new ConfigFile(instance, instance.getConfig().getString("Storage.YML.Name"));
        file.save();
        file.reload();
    }


    @Override
    public void loadPlayer(Player p) {
        cacheUser(p.getUniqueId());
    }

    @Override
    public void savePlayer(Player p) {
        savePlayer(p.getUniqueId());
    }

    @Override
    public void savePlayer(UUID uuid) {
        if(!isInStorage(uuid)){
            file.set("Coins." + uuid + ".name", Bukkit.getOfflinePlayer(uuid).getName());
            file.set("Coins." + uuid + ".coins", 0);
            file.save();
            file.reload();
            return;
        }

        User user = instance.getUserManager().getUserList().get(uuid);
        file.set("Coins." + uuid + ".name", Bukkit.getOfflinePlayer(uuid).getName());
        file.set("Coins." + uuid + ".coins", user.getCoins());
        file.save();
        file.reload();

    }

    @Override
    public void cacheUser(UUID uuid) {
        if(!isInStorage(uuid)){
            file.set("Coins." + uuid + ".name", Bukkit.getOfflinePlayer(uuid).getName());
            file.set("Coins." + uuid + ".coins", 0);
            file.save();
            file.reload();
            return;
        }
        int i = file.getInt("Coins." + uuid + ".coins");
        User user = new User(uuid, i);
        instance.getUserManager().getUserList().put(uuid, user);

    }

    @Override
    public StorageType getStoragetype() {
        return StorageType.FILE;
    }

    private boolean isInStorage(UUID uuid){
        return file.getString("Coins." + uuid.toString() + ".name") != null;
    }
}
