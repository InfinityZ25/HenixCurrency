package me.infinityz.plugins.CurrencyPlugin.Listeners;

import me.infinityz.plugins.CurrencyPlugin.Currency;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
    private Currency instance;

    public EventListener(Currency instance){
        this.instance = instance;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e){
        System.out.println("Login: " + e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent e){
        System.out.println("Pre-login: " + e.getUniqueId());
        instance.getPlayerDatabase().cacheUser(e.getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        instance.getPlayerDatabase().loadPlayer(e.getPlayer());
        System.out.println("Join: " + e.getPlayer().getUniqueId());

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        instance.getPlayerDatabase().savePlayer(p);
        instance.getUserManager().getUserList().remove(p.getUniqueId());
    }

}
