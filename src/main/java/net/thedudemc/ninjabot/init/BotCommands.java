package net.thedudemc.ninjabot.init;

import net.thedudemc.ninjabot.NinjaBot;
import net.thedudemc.ninjabot.command.BotCommand;
import net.thedudemc.ninjabot.command.exception.InvalidCommandException;
import net.thedudemc.ninjabot.config.command.ConfigCommand;
import net.thedudemc.ninjabot.command.NotiRoleCommand;
import net.thedudemc.ninjabot.command.CloseCommand;
import net.thedudemc.ninjabot.command.ReopenCommand;
import net.thedudemc.ninjabot.command.TicketCommand;

import java.util.HashMap;

public class BotCommands {

    private static final HashMap<String, BotCommand> REGISTRY = new HashMap<>();

    public static void register() {
        NinjaBot.getLogger().info("Registering commands...");

        registerCommand(new TicketCommand().setAutoDelete());
        registerCommand(new CloseCommand().requireElevatedPrivileges());
        registerCommand(new ConfigCommand().requireAdmin());
        registerCommand(new ReopenCommand());
        registerCommand(new NotiRoleCommand().requireAdmin());
    }

    private static void registerCommand(BotCommand command) {
        REGISTRY.put(command.getName(), command);
    }

    public static BotCommand getCommand(String name) {
        if (REGISTRY.containsKey(name)) return REGISTRY.get(name);
        throw new InvalidCommandException(name);
    }
}
