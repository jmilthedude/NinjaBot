package net.thedudemc.ninjabot.ticket;

import net.dv8tion.jda.api.entities.Member;
import net.thedudemc.ninjabot.NinjaBot;
import net.thedudemc.ninjabot.data.Data;
import net.thedudemc.ninjabot.init.BotData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ticket implements Data {

    private final long creatorId;
    private final String creatorName;
    private final long channelId;
    private final String message;
    private final Date creationTime;
    private Date closeTime;
    private boolean closed;

    public Ticket(Member member, long channelId, String message, Date creationTime) {
        this.creatorId = member.getIdLong();
        this.creatorName = member.getEffectiveName();
        this.channelId = channelId;
        this.message = message;
        this.creationTime = creationTime;
        this.closeTime = null;
        this.closed = false;
    }

    public Ticket(Member member, long channelId, String message, Date creationTime, Date closeTime, boolean closed) {
        this(member, channelId, message, creationTime);
        this.closeTime = closeTime;
        this.closed = closed;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public long getChannelId() {
        return channelId;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    // -------------------------------------------------------------------- //

    @Override
    public String getTableName() {
        return "Tickets";
    }

    @Override
    public Data create(long guildId) {
        String query = "CREATE TABLE IF NOT EXISTS \"Tickets\" (" +
                "\"ID\"	INTEGER NOT NULL UNIQUE," +
                "\"ChannelID\"	INTEGER NOT NULL UNIQUE," +
                "\"CreatorID\"	INTEGER NOT NULL," +
                "\"CreatorName\" TEXT NOT NULL," +
                "\"Message\" TEXT NOT NULL," +
                "\"CreationTime\" INTEGER NOT NULL," +
                "\"ClosedTime\" INTEGER," +
                "\"Closed\" INTEGER NOT NULL," +
                "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
                ");";

        try (Connection conn = BotData.getConnection(guildId)) {
            try (Statement statement = conn.createStatement()) {
                statement.execute(query);
            }
        } catch (SQLException exception) {
            NinjaBot.getLogger().error(exception.getMessage());
        }

        return this;
    }

    @Override
    public int insert(long guildId) {
        create(guildId);
        String query = "INSERT INTO Tickets " +
                "(ChannelID, " +
                "CreatorID, " +
                "CreatorName, " +
                "Message, " +
                "CreationTime, " +
                "ClosedTime, " +
                "Closed) " +
                "VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = BotData.getConnection(guildId)) {
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setLong(1, this.channelId);
                statement.setLong(2, this.creatorId);
                statement.setString(3, this.creatorName);
                statement.setString(4, this.message);
                statement.setTimestamp(5, new Timestamp(this.creationTime.getTime()));
                statement.setTimestamp(6, this.closeTime == null ? null : new Timestamp(this.closeTime.getTime()));
                statement.setBoolean(7, this.closed);

                statement.execute();
                try (Statement stmt = conn.createStatement()) {
                    return stmt.executeQuery("SELECT ID from Tickets WHERE ChannelID = " + this.channelId).getInt("ID");
                }
            }
        } catch (SQLException exception) {
            NinjaBot.getLogger().error(exception.getMessage());
            for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
                NinjaBot.getLogger().error(stackTraceElement.toString());
            }
        }
        return -1;
    }

    @Override
    public Data get(long channelId) {
        return this;
    }

    @Override
    public List<Data> getAll() {
        return new ArrayList<>();
    }
}
