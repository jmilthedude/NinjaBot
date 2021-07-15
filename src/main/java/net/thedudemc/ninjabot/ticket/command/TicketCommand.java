package net.thedudemc.ninjabot.ticket.command;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.thedudemc.ninjabot.command.BotCommand;
import net.thedudemc.ninjabot.ticket.listener.TicketListener;
import org.jetbrains.annotations.Nullable;

public class TicketCommand extends BotCommand {
    @Override
    public String getName() {
        return "ticket";
    }

    @Override
    public String getDescription() {
        return "Requests a DM from the bot to open a new ticket.";
    }

    @Override
    public void execute(Guild guild, @Nullable Member member, MessageChannel channel, Message message, @Nullable String[] args) {
        if (guild == null) { // handle dm. tickets will be created from within a server.
            channel.sendMessage(
                    new MessageBuilder("To open a ticket, you must first request one in the appropriate channel and discord server.")
                            .build()).queue();
            return;
        }
        if (member != null) {
            User user = member.getUser();
            user.openPrivateChannel().flatMap(privateChannel ->
                    privateChannel.sendMessage(
                            "Hello! You have initiated a ticket to be opened. " +
                                    "If you did not mean to do this, type cancel. " +
                                    "Otherwise, state your issue."
                    )
            ).queue(msg -> TicketListener.addTicketCreator(guild, member));
        }
    }
}
