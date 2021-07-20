package net.thedudemc.ninjabot.command;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.thedudemc.ninjabot.command.BotCommand;
import net.thedudemc.ninjabot.ticket.Ticket;
import net.thedudemc.ninjabot.util.BotUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ReopenCommand extends BotCommand {
    @Override
    public String getName() {
        return "reopen";
    }

    @Override
    public String getDescription() {
        return "Reopens a ticket that has been closed.";
    }

    @Override
    public void execute(@Nullable Guild guild, @Nullable Member member, MessageChannel channel, Message message, @Nullable String[] args) {
        if (guild == null) return;
        Ticket ticket = Ticket.getTicket(guild.getIdLong(), channel.getIdLong());
        if (ticket == null) return;
        MessageBuilder builder = new MessageBuilder("You have reopened this ticket. \n");
        List<Role> superRoles = BotUtils.getSuperUserRoles(guild);
        superRoles.forEach(role -> builder.append(role.getAsMention()).append(" "));
        builder.append("will be with you as soon as they are available.");
        channel.sendMessage(builder.build()).queue(m -> {
            Optional<Category> ticketCategory = guild.getCategoriesByName("Tickets", true).stream().findFirst();
            ticketCategory.ifPresent(category -> {
                TextChannel ticketChannel = (TextChannel) channel;
                ticketChannel.getManager().setParent(category).queue();
            });
        });
        ticket.reopen();
    }

}
