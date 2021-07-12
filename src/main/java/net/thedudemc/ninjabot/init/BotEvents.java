package net.thedudemc.ninjabot.init;

import net.thedudemc.ninjabot.NinjaBot;
import net.thedudemc.ninjabot.event.CommandEvents;

public class BotEvents {

    public static void register() {
        NinjaBot.getLogger().info("Registering events...");

        NinjaBot.getJDA().addEventListener(
                new CommandEvents()
        );
    }
}
