package net.thedudemc.ninjabot.command;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.thedudemc.ninjabot.command.BotCommand;
import net.thedudemc.ninjabot.ticket.Ticket;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CloseCommand extends BotCommand {
    @Override
    public String getName() {
        return "close";
    }

    @Override
    public String getDescription() {
        return "Command used by super users to close a ticket.";
    }

    @Override
    public void execute(@Nullable Guild guild, @Nullable Member member, MessageChannel channel, Message message, @Nullable String[] args) {
        if (guild == null) return; // command has to be run from a ticket channel.
        if (member == null) return;
        Ticket ticket = Ticket.getTicket(guild.getIdLong(), channel.getIdLong());
        if (ticket == null) {
            // TODO: tell user there is not a ticket.
            return;
        }
        Optional<Category> archive = guild.getCategoriesByName("Closed Tickets", true).stream().findFirst();

        archive.ifPresent(category -> {
            sendCloseMessage(channel, category);
            ticket.close();
        });
    }

    private void sendCloseMessage(MessageChannel channel, Category category) {

        channel.sendMessage(new MessageBuilder(
                "This ticket has been closed. " +
                        "If you still have an issue, feel free to reopen with the command '-reopen'.")
                .build())
                .queue(msg -> moveTicket(channel, category));
    }

    private void moveTicket(MessageChannel channel, Category category) {
        ((GuildChannel) channel).getManager().setParent(category).queue();
    }
}
