# HenixCurrency
Currency with a built-in API to allow developers to use a currency system across all their network

# Config file
The config file does not contain a lot but there is some to be explained. The "StorageType" string can be set to either of the following three:
- MongoDB: My favourite database, authentication must be used in this plugin. Set the "Database" string as the authentication database. 
- MySQL: Commonly used database. No further configuration is needed.
- File or YML: Storages the player data as a .yml file. Easy to read and modify and no latency or caching is needed.

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
