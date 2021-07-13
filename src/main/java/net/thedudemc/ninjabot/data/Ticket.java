package net.thedudemc.ninjabot.data;

import net.dv8tion.jda.api.entities.Member;
import net.thedudemc.ninjabot.NinjaBot;
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
                "\"ChannelID\"	INTEGER NOT NULL UNIQUE," +
                "\"CreatorID\"	INTEGER NOT NULL," +
                "\"CreatorName\" TEXT NOT NULL," +
                "\"Message\" TEXT NOT NULL," +
                "\"CreationTime\" INTEGER NOT NULL," +
                "\"ClosedTime\" INTEGER," +
                "\"Closed\" INTEGER NOT NULL," +
                "PRIMARY KEY(\"ChannelID\"" +
                ");";

        try (Connection conn = BotData.getConnection(guildId)) {
            try (Statement statement = conn.createStatement()) {
                statement.executeQuery(query);
            }
        } catch (SQLException exception) {
            NinjaBot.getLogger().error(exception.getMessage());
        }

        return this;
    }

    @Override
    public Data insert(long guildId) {
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
                statement.setLong(0, this.channelId);
                statement.setLong(1, this.creatorId);
                statement.setString(2, this.creatorName);
                statement.setString(3, this.message);
                statement.setTimestamp(4, new Timestamp(this.creationTime.getTime()));
                statement.setTimestamp(5, this.closeTime == null ? null : new Timestamp(this.closeTime.getTime()));
                statement.setBoolean(6, this.closed);

                statement.execute();
            }
        } catch (SQLException exception) {
            NinjaBot.getLogger().error(exception.getMessage());
        }
        return this;
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
