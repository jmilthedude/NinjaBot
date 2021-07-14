package net.thedudemc.ninjabot.data;

import java.util.List;

public interface Data {

    String getTableName();

    Data create();

    int insert();

    Data get(long guildId, long channelId);

    List<Data> getAll();

}
