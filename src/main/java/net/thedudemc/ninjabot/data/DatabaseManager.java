package net.thedudemc.ninjabot.data;

import net.thedudemc.ninjabot.NinjaBot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private final long guildId;

    public DatabaseManager(long guildId) {
        this.guildId = guildId;

        try {
            this.createDatabase();
        } catch (SQLException ex) {
            NinjaBot.getLogger().error(ex.getMessage());
        }
    }

    private String getConnectionString() {
        return "jdbc:sqlite:data/" + guildId + ".db";
    }

    public long getGuildId() {
        return guildId;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getConnectionString());
    }

    public void createDatabase() throws SQLException {
        Connection conn = this.getConnection();
        conn.close();
    }
}
