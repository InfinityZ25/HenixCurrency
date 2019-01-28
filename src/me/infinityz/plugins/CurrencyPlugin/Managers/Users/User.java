package me.infinityz.plugins.CurrencyPlugin.Managers.Users;

import java.util.UUID;

public class User {

    private int coins;
    private UUID uuid;

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int add){
        setCoins(getCoins() + add);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public User(UUID uuid, int coins){
        this.coins = coins;
        this.uuid = uuid;

    }
}
