package net.thedudemc.ninjabot.ticket;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.thedudemc.ninjabot.NinjaBot;
import net.thedudemc.ninjabot.data.Data;
import net.thedudemc.ninjabot.init.BotData;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ticket implements Data {

    private final long guildId;
    private final long creatorId;
    private final String creatorName;
    private final long channelId;
    private final String message;
    private final Date creationTime;
    private Date closedTime;
    private boolean closed;

    private Ticket() {
        this.guildId = -1;
        this.creatorId = -1;
        this.creatorName = null;
        this.channelId = -1;
        this.message = null;
        this.creationTime = null;
    }

    public Ticket(Guild guild, Member member, long channelId, String message, Date creationTime) {
        this(guild.getIdLong(), member.getIdLong(), member.getEffectiveName(), channelId, message, creationTime, null, false);
    }

    public Ticket(long guildId, long creatorId, String creatorName, long channelId, String message, Date creationTime, Date closedTime, boolean closed) {
        this.guildId = guildId;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.channelId = channelId;
        this.message = message;
        this.creationTime = creationTime;
        this.closedTime = closedTime;
        this.closed = closed;
    }

    public Ticket(Guild guild, Member member, long channelId, String message, Date creationTime, Date closedTime, boolean closed) {
        this(guild, member, channelId, message, creationTime);
        this.closedTime = closedTime;
        this.closed = closed;
    }

    public long getGuildId() {
        return guildId;
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

    public Date getClosedTime() {
        return closedTime;
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
    public Data create() {
        String query = "CREATE TABLE IF NOT EXISTS \"Tickets\" (" +
                "\"ID\"	INTEGER NOT NULL UNIQUE," +
                "\"ChannelID\" INTEGER NOT NULL UNIQUE," +
                "\"CreatorID\"	INTEGER NOT NULL," +
                "\"CreatorName\" TEXT NOT NULL," +
                "\"Message\" TEXT NOT NULL," +
                "\"CreationTime\" INTEGER NOT NULL," +
                "\"ClosedTime\" INTEGER," +
                "\"Closed\" INTEGER NOT NULL," +
                "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
                ");";

        try (Connection conn = BotData.getConnection(this.guildId)) {
            try (Statement statement = conn.createStatement()) {
                statement.execute(query);
            }
        } catch (SQLException exception) {
            NinjaBot.getLogger().error(exception.getMessage());
        }

        return this;
    }

    @Override
    public int insert() {
        create();
        String query = "INSERT INTO Tickets " +
                "(ChannelID, " +
                "CreatorID, " +
                "CreatorName, " +
                "Message, " +
                "CreationTime, " +
                "ClosedTime, " +
                "Closed) " +
                "VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = BotData.getConnection(this.guildId)) {
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setLong(1, this.channelId);
                statement.setLong(2, this.creatorId);
                statement.setString(3, this.creatorName);
                statement.setString(4, this.message);
                statement.setTimestamp(5, new Timestamp(this.creationTime.getTime()));
                statement.setTimestamp(6, this.closedTime == null ? null : new Timestamp(this.closedTime.getTime()));
                statement.setBoolean(7, this.closed);

                statement.execute();
                try (PreparedStatement stmt = conn.prepareStatement("SELECT ID from Tickets WHERE ChannelID = ?")) {
                    stmt.setLong(1, this.channelId);
                    return stmt.executeQuery().getInt("ID");
                }
            }
        } catch (SQLException exception) {
            logException(exception);
        }
        return -1;
    }

    @Nullable
    @Override
    public Data get(long guildId, long channelId) {
        String query = "SELECT CreatorID, CreatorName, Message, CreationTime, ClosedTime, Closed FROM Tickets WHERE ChannelID = ?";
        Ticket ticket = null;
        try (Connection conn = BotData.getConnection(guildId)) {
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setLong(1, channelId);
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    long creatorId = result.getLong("CreatorID");
                    String creatorName = result.getString("CreatorName");
                    String message = result.getString("Message");
                    Date creationTime = result.getDate("CreationTime");
                    Date closedTime = result.getDate("ClosedTime");
                    boolean closed = result.getBoolean("Closed");
                    ticket = new Ticket(guildId, creatorId, creatorName, channelId, message, creationTime, closedTime, closed);
                }
            }
        } catch (SQLException exception) {
            logException(exception);
        }
        return ticket;
    }

    public static Ticket getTicket(long guildId, long channelId) {
        return (Ticket) new Ticket().get(guildId, channelId);
    }

    private static void logException(SQLException exception) {
        NinjaBot.getLogger().error(exception.getMessage());
        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            NinjaBot.getLogger().error(stackTraceElement.toString());
        }
    }

    @Override
    public List<Data> getAll() {
        return new ArrayList<>();
    }

    public void close() {
        String query = "UPDATE Tickets SET ClosedTime = ?, Closed = ? WHERE ChannelID = ?";

        try (Connection conn = BotData.getConnection(this.guildId)) {
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setTimestamp(1, new Timestamp(new Date().getTime()));
                statement.setBoolean(2, true);
                statement.setLong(3, this.channelId);

                statement.execute();
            }
        } catch (SQLException exception) {
            logException(exception);
        }
    }

    public void reopen() {
        String query = "UPDATE Tickets SET ClosedTime = ?, Closed = ? WHERE ChannelID = ?";

        try (Connection conn = BotData.getConnection(this.guildId)) {
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setTimestamp(1, null);
                statement.setBoolean(2, false);
                statement.setLong(3, this.channelId);

                statement.execute();
            }
        } catch (SQLException exception) {
            logException(exception);
        }
    }
}
