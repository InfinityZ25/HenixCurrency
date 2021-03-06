package me.infinityz.plugins.CurrencyPlugin.Managers.Database.Types;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.infinityz.plugins.CurrencyPlugin.Currency;
import me.infinityz.plugins.CurrencyPlugin.Managers.Database.PlayerDataInterface;
import me.infinityz.plugins.CurrencyPlugin.Managers.Users.User;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class MongoDB implements PlayerDataInterface {

    private Map<UUID, User> cached_users;

    private Currency instance;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;

    public MongoDB(Currency instance, String connectionURL, String databasename) throws Exception {
        this.instance = instance;
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.WARNING); // e.g. or Log.WARNING, etc.
        MongoClient mongoClient = MongoClients.create(connectionURL);
        if(connectionURL.equalsIgnoreCase("mongodb://username:password@my.mongohost.com:27017/authDatabase?connectTimeoutMS=5000")){
            instance.getLogger().severe("Connection with Mongo cannot be established, go to you config and edit the URI!");
            instance.getLogger().warning("Proceeding with YML as the storage type!");
            throw new Exception("MongoDB cannot be reached!");
        }


        mongoDatabase = mongoClient.getDatabase(databasename);
        mongoCollection = mongoDatabase.getCollection("CurrencyPlugin");
        cached_users = new HashMap<>();

        instance.getLogger().info("Attempting to connect to the database...");

        try{
            pingMongo();
            instance.getLogger().info("Connection has been successful!");
        }catch (Exception io){
            instance.getLogger().severe("Connection has failed due to: " + io.getLocalizedMessage());
            instance.getLogger().warning("Proceeding with YML as the storage type!");
            throw new Exception("MongoDB cannot be reached!");
        }

    }

    private void pingMongo(){
        Document ping = new Document("ping", "1");
        mongoDatabase.runCommand(ping);
    }

    @Override
    public StorageType getStoragetype() {
        return StorageType.MONGO;
    }

    @Override
    public void loadPlayer(Player p) {
        User user = cached_users.get(p.getUniqueId());
        instance.getUserManager().getUserList().put(p.getUniqueId(), user);
        cached_users.remove(p.getUniqueId());

    }

    @Override
    public void savePlayer(Player p) {
        if(!isInCollection(p)){
            createDocument(p);
            return;
        }
        User user = instance.getUserManager().getUserList().get(p.getUniqueId());
        mongoCollection.updateOne(eq("_id", p.getUniqueId()), new Document("$set", new Document("coins", user.getCoins())));


    }

    @Override
    public void savePlayer(UUID uuid) {
        if(!isInCollection(uuid)){
            createDocumentOffline(uuid);
            return;
        }
        User user = instance.getUserManager().getUserList().get(uuid);
        mongoCollection.updateOne(eq("_id", uuid), new Document("$set", new Document("coins", user.getCoins())));
    }

    @Override
    public void cacheUser(UUID uuid) {
        User user = new User(uuid, 0);

        if(isInCollection(uuid)){
            Document found = findDocument(uuid);
            user.setCoins(found.getInteger("coins"));
            cached_users.put(uuid, user);
            return;
        }
        cached_users.put(uuid, user);
        createDocumentOffline(uuid);


    }

    private Document findDocument(UUID uuid){
        return mongoCollection.find(
                eq("_id", uuid)
        ).limit(1).first();
    }

    private void createDocument(Player p){
        Document doc = new Document("_id", p.getUniqueId())
                .append("name", p.getName())
                .append("coins", 10);
        mongoCollection.insertOne(doc);
    }
    private void createDocumentOffline(UUID uuid){
        Document doc = new Document("_id", uuid)
                .append("name", Bukkit.getOfflinePlayer(uuid).getName())
                .append("coins", 10);
        mongoCollection.insertOne(doc);
    }

    private boolean isInCollection(Player p) {
        return isInCollection(p.getUniqueId());
    }

    private boolean isInCollection(UUID uuid) {
        return mongoCollection.find(eq("_id", uuid)).limit(1).first() != null;
    }
}
