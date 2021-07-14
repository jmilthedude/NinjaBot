package net.thedudemc.ninjabot.data;

import java.util.List;

public interface Data {

    String getTableName();

    Data create(long guildId);

    int insert(long guildId);

    Data get(long channelId);

    List<Data> getAll();

}
