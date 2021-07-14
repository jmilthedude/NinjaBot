package net.thedudemc.ninjabot.init;

import net.thedudemc.ninjabot.NinjaBot;
import net.thedudemc.ninjabot.ticket.TicketCommand;
import net.thedudemc.ninjabot.command.base.BotCommand;
import net.thedudemc.ninjabot.command.exception.InvalidCommandException;
import net.thedudemc.ninjabot.command.TestCommand;

import java.util.HashMap;

public class BotCommands {

    private static final HashMap<String, BotCommand> REGISTRY = new HashMap<>();

    public static void register() {
        NinjaBot.getLogger().info("Registering commands...");

        registerCommand(new TestCommand());
        registerCommand(new TicketCommand());
    }

    private static void registerCommand(BotCommand command) {
        REGISTRY.put(command.getName(), command);
    }

    public static BotCommand getCommand(String name) {
        if (REGISTRY.containsKey(name)) return REGISTRY.get(name);
        throw new InvalidCommandException(name);
    }
}
