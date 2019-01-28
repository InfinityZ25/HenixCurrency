package me.infinityz.plugins.CurrencyPlugin.Commands;

import me.infinityz.plugins.CurrencyPlugin.Currency;
import me.infinityz.plugins.CurrencyPlugin.Managers.Users.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CurrencyCommand implements CommandExecutor {
    private Currency instance;

    //Probably needs to be set as public when not used localized
    public CurrencyCommand(Currency instance){
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("coins")){
            Player p = (Player)commandSender;
            User user = instance.getUserManager().getUserList().get(p.getUniqueId());
            if(strings.length == 0) {
                commandSender.sendMessage("Coins: " + user.getCoins());
                return true;
            }
            switch(strings[0].toLowerCase()){
                case "add":{
                    user.addCoins(Integer.parseInt(strings[1]));
                    break;
                }
                case "set":{
                    break;
                }
                case "take":{
                    break;
                }
                default:{
                    commandSender.sendMessage("Command usage: /coins [add:set:take]");
                    break;
                }

            }


            return true;
        }
        return false;
    }
}
