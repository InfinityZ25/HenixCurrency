package me.infinityz.plugins.CurrencyPlugin;

import me.infinityz.plugins.CurrencyPlugin.Commands.CurrencyCommand;
import me.infinityz.plugins.CurrencyPlugin.Listeners.EventListener;
import me.infinityz.plugins.CurrencyPlugin.Managers.Database.PlayerDataInterface;
import me.infinityz.plugins.CurrencyPlugin.Managers.Database.Types.MongoDB;
import me.infinityz.plugins.CurrencyPlugin.Managers.Database.Types.vFile;
import me.infinityz.plugins.CurrencyPlugin.Managers.Database.Types.vMySQL;
import me.infinityz.plugins.CurrencyPlugin.Managers.Users.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Currency extends JavaPlugin {

    public static CurrencyAPI getCurrencyAPI() {
        return currencyAPI;
    }

    private PlayerDataInterface playerDatabase;
    private static CurrencyAPI currencyAPI;

    public UserManager getUserManager() {
        return userManager;
    }

    private UserManager userManager;

    public PlayerDataInterface getPlayerDatabase() {
        return playerDatabase;
    }

    @Override
    public void onEnable(){
        configDefault();
        handleStorage();
        this.userManager = new UserManager();
        loadCommands();
        loadListeners();

        currencyAPI = new CurrencyAPI(this, getConfig().getString("Stats.StorageType").toLowerCase());

    }

    @Override
    public void onDisable(){

    }

    private void loadCommands(){
        //Method to inject the command to bukkit
        getCommand("coins").setExecutor(new CurrencyCommand(this));
    }

    private void loadListeners(){

        //Method to inject the Listener to bukkit
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    private void handleStorage(){
        switch(getConfig().getString("Stats.StorageType").toLowerCase()){
            case "mongodb":
            case "mongo":{
                this.playerDatabase = new MongoDB(this,
                        getConfig().getString("Stats.Username"),
                        getConfig().getString("Stats.Database"),
                        getConfig().getString("Stats.Password"),
                        getConfig().getString("Stats.Ip"),
                        getConfig().getInt("Stats.Port"));
                break;
            }
            case "mariadb":
            case "sql":
            case "mysql":{
                this.playerDatabase = new vMySQL(this,
                        getConfig().getString("Stats.Username"),
                        getConfig().getString("Stats.Database"),
                        getConfig().getString("Stats.Password"),
                        getConfig().getString("Stats.Ip"),
                        getConfig().getInt("Stats.Port"));
                break;
            }
            default:{
                this.playerDatabase = new vFile(this);
                break;
            }
        }

    }

    private void configDefault() {
        FileConfiguration config = getConfig();

        config.addDefault("Stats.StorageType", "MongoDB");
        config.addDefault("Stats.Ip", "ds211275.mlab.com");
        config.addDefault("Stats.Port", 11275);
        config.addDefault("Stats.Database", "perms");
        config.addDefault("Stats.Username", "henix");
        config.addDefault("Stats.Password", "Juanorlando1!");

        config.options().copyDefaults(true);
        saveConfig();
    }
}
