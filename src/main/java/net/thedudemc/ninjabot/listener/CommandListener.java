package net.thedudemc.ninjabot.listener;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thedudemc.ninjabot.command.BotCommand;
import net.thedudemc.ninjabot.command.exception.InvalidCommandException;
import net.thedudemc.ninjabot.init.BotCommands;
import net.thedudemc.ninjabot.init.BotConfigs;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        Guild guild = event.isFromType(ChannelType.TEXT) ? event.getGuild() : null;
        Member member = event.getMember();


        String content = event.getMessage().getContentRaw();
        if (!content.startsWith("-")) return; // not a command.

        final boolean deleteCommands = guild != null && BotConfigs.getConfig(guild, "General").getOption("deleteCommands").getBooleanValue();

        try {
            String commandName = content.contains(" ") ? content.substring(1, content.indexOf(" ")) : content.substring(1);
            BotCommand command = BotCommands.getCommand(commandName);
            String[] args = content.contains(" ") ? content.substring(content.indexOf(" ") + 1).split(" ") : null;

            if (command.canExecute(event.getMember())) {
                if (command.requiresAdmin() && !event.getChannel().getName().equalsIgnoreCase("bot-commands")) {
                    if (member == null) return;
                    event.getMessage().delete().queueAfter(2, TimeUnit.SECONDS);
                    event.getChannel().sendMessage(
                            new MessageBuilder()
                                    .append(member.getAsMention()).append(" NinjaBot commands should be executed in the appropriate channel.")
                                    .build())
                            .queue(message -> {
                                if (command.shouldDelete() && deleteCommands) {
                                    message.delete().queueAfter(10, TimeUnit.SECONDS);
                                }
                            });
                    return;
                }
                command.execute(guild, member, event.getChannel(), event.getMessage(), args);
            } else {
                if (member != null) {
                    event.getChannel().sendMessage(
                            new MessageBuilder()
                                    .append(member.getAsMention()).append(" You do not have permission to execute that command.")
                                    .build())
                            .queue(message -> {
                                if (command.shouldDelete() && deleteCommands) {
                                    message.delete().queueAfter(10, TimeUnit.SECONDS);
                                }
                            });
                }
            }

            if (command.shouldDelete() && deleteCommands) {
                event.getMessage().delete().queueAfter(2, TimeUnit.SECONDS);
            }
        } catch (InvalidCommandException exception) {
            event.getChannel().sendMessage(new MessageBuilder(exception.getMessage()).build())
                    .queue(message -> {
                        if (deleteCommands) {
                            message.delete().queueAfter(10, TimeUnit.SECONDS);
                        }
                    });
        }

    }
}
