# HenixCurrency
Currency with a built-in API to allow developers to use a currency system across all their network. This Currency System caches all the data and only writes to the database when needed.

# Config file
The config file contains certain parameters that must be explained for those that are not entirely sure about what they mean.
The String Storage.Type can take any of the three storage types (MongoDB, MySQL, or YML).

MongoDB:
 - If you want to connect to mongo you'll have to provide an URI (more info here https://bit.ly/2dPu67Q).
 - A database name is also required, even if you have already invoked it on the URI.
 
 MySQL:
 - All the field must be provided for the connection to be established.
 - The properties string, by default, contains recommended settings, however, if you need to perform any changes on it just follow the      same syntax: "property=value;property2=value2".
 
 YML:
 - Flat File storage does not need much configuration besides the name of the yml file if you wish to have a different one.
 
 **If a connection to a database fails, YML will be set as the StorageType for that instance.**
 

# API Usage
Using this plugin's API is not hard at all. The method to invoke the API is:

CurrencyAPI currencyAPI = Currency.getCurrencyAPI();

Then the usable methods are:
- look(UUID uuid): Returns the player's current points.
- take(UUID uuid): Takes away points from a player. Negative values are possible, so please check before doing a transaction.
- add(UUID uuid): Adds points to a player.

# Installation
1.Clone the source code

2.Compile with desired modifications

2.Drag compiled plugin with dependencies(if still needed) onto the plugins folder.

3.Start your server.

4.Stop server and configure all the values
