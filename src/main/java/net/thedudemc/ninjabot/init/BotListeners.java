package net.thedudemc.ninjabot.init;

import net.thedudemc.ninjabot.NinjaBot;
import net.thedudemc.ninjabot.command.listener.CommandListener;
import net.thedudemc.ninjabot.ticket.listener.TicketListener;

public class BotListeners {

    public static void register() {
        NinjaBot.getLogger().info("Registering events...");

        NinjaBot.getJDA().addEventListener(
                new CommandListener(),
                new TicketListener()
        );
    }
}
