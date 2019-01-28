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


        currencyAPI = new CurrencyAPI(this);

        loadListeners();

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

    public void setToYML(){
    }

    private void handleStorage(){
        switch(getConfig().getString("Storage.Type").toLowerCase()){
            case "mongodb":
            case "mongo":{
                try {
                    this.playerDatabase = new MongoDB(this,
                            getConfig().getString("Storage.MongoDB.URI"),
                            getConfig().getString("Storage.MongoDB.Database"));
                } catch (Exception io) {
                    this.playerDatabase = new vFile(this);
                }
                break;
            }
            case "mariadb":
            case "sql":
            case "mysql":{
                try {
                    this.playerDatabase = new vMySQL(this,
                            getConfig().getString("Storage.MySQL.Username"),
                            getConfig().getString("Storage.MySQL.Database"),
                            getConfig().getString("Storage.MySQL.Password"),
                            getConfig().getString("Storage.MySQL.Ip"),
                            getConfig().getInt("Storage.MySQL.Port"),
                            getConfig().getString("Storage.MySQL.Properties"));
                }catch (Exception io){
                    this.playerDatabase = new vFile(this);
                }

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

        config.addDefault("Storage.Type", "MongoDB");
        config.addDefault("Storage.MongoDB.URI", "mongodb://username:password@my.mongohost.com:27017/authDatabase?connectTimeoutMS=5000");
        config.addDefault("Storage.MongoDB.Database", "my-database-name");
        config.addDefault("Storage.MySQL.Ip", "my.mysqlhost.com");
        config.addDefault("Storage.MySQL.Port", 3306);
        config.addDefault("Storage.MySQL.Database", "Database");
        config.addDefault("Storage.MySQL.Username", "root");
        config.addDefault("Storage.MySQL.Password", "password");
        config.addDefault("Storage.MySQL.Properties", "autoReconnect=true;verifyServerCertificate=false;useSSL=false;requireSSL=false;connectTimeout=500");
        config.addDefault("Storage.YML.Name", "data.yml");

        config.options().copyDefaults(true);
        saveConfig();
    }
}
