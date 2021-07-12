package net.thedudemc.ninjabot.event;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thedudemc.ninjabot.command.base.BotCommand;
import net.thedudemc.ninjabot.command.exception.InvalidCommandException;
import net.thedudemc.ninjabot.init.BotCommands;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class CommandEvents extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        if (!content.startsWith("-")) return; // not a command.

        try {
            String commandName = content.contains(" ") ? content.substring(1, content.indexOf(" ")) : content.substring(1);
            BotCommand command = BotCommands.getCommand(commandName);

            String[] args = content.contains(" ") ? content.substring(content.indexOf(" ") + 1).split(" ") : null;

            if (command.canExecute(event.getMember())) {
                command.execute(event.getMember(), event.getChannel(), event.getMessage(), args);
            }
        } catch (InvalidCommandException exception) {
            event.getChannel().sendMessage(new MessageBuilder(exception.getMessage()).build()).queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));
        } finally {
            event.getMessage().delete().queueAfter(2, TimeUnit.SECONDS);
        }

    }
}
