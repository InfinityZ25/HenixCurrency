package me.infinityz.plugins.CurrencyPlugin.Managers.Users;

import java.util.*;

public class UserManager {
    private Map<UUID, User> userList;

    public Map<UUID, User> getUserList() {
        return userList;
    }

    public UserManager(){
        this.userList = new HashMap<>();
    }
}
