package me.infinityz.plugins.CurrencyPlugin;

import me.infinityz.plugins.CurrencyPlugin.Managers.Database.PlayerDataInterface;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CurrencyAPI {

    private Currency instance;
    private PlayerDataInterface.StorageType type;

    public CurrencyAPI(Currency instance){
        this.instance = instance;
        this.type = instance.getPlayerDatabase().getStoragetype();

    }

    public int look(UUID uuid){
        return instance.getUserManager().getUserList().get(uuid).getCoins();
    }

    public int look(Player player){
        return look(player.getUniqueId());
    }

    public void take(Player player, int coins){
        take(player.getUniqueId(), coins);

    }

    public void take(UUID uuid, int coins){
        instance.getUserManager().getUserList().get(uuid).addCoins(-coins);
        if(type == PlayerDataInterface.StorageType.FILE)return;
        instance.getPlayerDatabase().savePlayer(uuid);

    }

    public void give(Player player, int coins){
        give(player.getUniqueId(), coins);
    }

    public void give(UUID uuid, int coins){
        instance.getUserManager().getUserList().get(uuid).addCoins(coins);
        if(type == PlayerDataInterface.StorageType.FILE)return;
        instance.getPlayerDatabase().savePlayer(uuid);
    }


}
