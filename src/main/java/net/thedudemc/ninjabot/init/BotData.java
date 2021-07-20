package net.thedudemc.ninjabot.init;

import net.dv8tion.jda.api.entities.Guild;
import net.thedudemc.ninjabot.NinjaBot;
import net.thedudemc.ninjabot.data.DatabaseManager;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class BotData {

    private static final HashMap<Long, DatabaseManager> DATABASE_MANAGERS = new HashMap<>();

    public static void register() {
        NinjaBot.getLogger().info("Registering database...");
        createFile(new File("./data/"));

        NinjaBot.getJDA().getGuilds().forEach(BotData::registerGuild);
    }

    private static boolean createFile(File file) {
        return file.mkdirs();
    }

    public static void registerGuild(Guild guild) {
        DATABASE_MANAGERS.put(guild.getIdLong(), new DatabaseManager(guild.getIdLong()));
    }

    public static DatabaseManager getDatabaseManager(long guildId) {
        return DATABASE_MANAGERS.computeIfAbsent(guildId, DatabaseManager::new);
    }

    public static Connection getConnection(long guildId) throws SQLException {
        return getDatabaseManager(guildId).getConnection();
    }
}
