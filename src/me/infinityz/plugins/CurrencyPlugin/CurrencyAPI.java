package me.infinityz.plugins.CurrencyPlugin;

import org.bukkit.entity.Player;

import java.util.UUID;

public class CurrencyAPI {

    private Currency instance;
    enum StorageType{
        MONGO, MYSQL, FILE
    }
    private StorageType type;

    public CurrencyAPI(Currency instance, String string){
        this.instance = instance;
        switch(string.toLowerCase()){
            case "mongo":{
                this.type = StorageType.MONGO;
                break;
            }
            case "mysql":{
                this.type = StorageType.MYSQL;
                break;
            }
            default:{
                this.type = StorageType.FILE;
                break;
            }
        }

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
        instance.getUserManager().getUserList().get(uuid).addCoins(coins);
        if(type == StorageType.FILE)return;
        instance.getPlayerDatabase().savePlayer(uuid);

    }

    public void give(Player player, int coins){
        give(player.getUniqueId(), coins);
    }

    public void give(UUID uuid, int coins){
        instance.getUserManager().getUserList().get(uuid).addCoins(coins);
        if(type == StorageType.FILE)return;
        instance.getPlayerDatabase().savePlayer(uuid);
    }


}
