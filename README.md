# HenixCurrency
Currency with a built-in API to allow developers to use a currency system across all their network

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
