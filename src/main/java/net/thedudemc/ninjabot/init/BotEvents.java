package net.thedudemc.ninjabot.init;

import net.thedudemc.ninjabot.NinjaBot;
import net.thedudemc.ninjabot.event.CommandEvents;
import net.thedudemc.ninjabot.ticket.TicketMessageEvents;

public class BotEvents {

    public static void register() {
        NinjaBot.getLogger().info("Registering events...");

        NinjaBot.getJDA().addEventListener(
                new CommandEvents(),
                new TicketMessageEvents()
        );
    }
}
