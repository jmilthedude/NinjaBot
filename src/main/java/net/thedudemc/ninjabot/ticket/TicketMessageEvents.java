package net.thedudemc.ninjabot.ticket;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thedudemc.ninjabot.NinjaBot;
import net.thedudemc.ninjabot.util.StringUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TicketMessageEvents extends ListenerAdapter {

    private static final Set<TicketCreator> ticketCreators = new HashSet<>();

    public static void addTicketCreator(Guild guild, Member member) {
        ticketCreators.add(new TicketCreator(guild.getIdLong(), member.getIdLong()));
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        long userId = event.getAuthor().getIdLong();
        boolean isTicketCreator = ticketCreators.stream().anyMatch(ticketCreator -> userId == ticketCreator.getMemberId());

        if (!isTicketCreator) {
            event.getChannel().sendMessage(
                    new MessageBuilder("To open a ticket, you must first request one in the appropriate channel and discord server.")
                            .build()).queue();
            return;
        }

        Optional<TicketCreator> creatorOptional = ticketCreators.stream().filter(ticketCreator -> userId == ticketCreator.getMemberId()).findFirst();
        creatorOptional.ifPresent(ticketCreator -> {
            if (!event.getMessage().getContentRaw().startsWith("cancel")) {
                createNewTicket(ticketCreator, event.getMessage());
            } else {
                event.getChannel().sendMessage(
                        new MessageBuilder("You have cancelled! If you still need help, request a ticket in the appropriate channel and discord server.")
                                .build())
                        .queue();
            }

            ticketCreators.remove(ticketCreator);
        });
    }

    private void createNewTicket(TicketCreator creator, Message message) {
        Guild guild = NinjaBot.getJDA().getGuildById(creator.getGuildId());
        if (guild == null) return;
        Member member = guild.getMemberById(creator.getMemberId());
        if (member == null) return;

        Optional<Category> tickets = guild.getCategoriesByName("tickets", true).stream().findFirst();
        tickets.ifPresent(category -> createTicketChannel(message, guild, member, category));
    }

    private void createTicketChannel(Message message, Guild guild, Member member, Category category) {
        category.createTextChannel(StringUtilities.stripName(member.getEffectiveName()))
                .addMemberPermissionOverride(member.getIdLong(), getPermissions(), null)
                .queue(textChannel -> {
                    //TODO set channel name to include ID
                    sendTicketOpenMessage(message, guild, member, textChannel);
                });
    }

    private void sendTicketOpenMessage(Message message, Guild guild, Member member, TextChannel textChannel) {
        textChannel.sendMessage(new MessageBuilder()
                .append(member.getAsMention()).append(" here is your ticket. ")
                .append(Objects.requireNonNull(guild.getRoleById(856998269079257109L)).getAsMention())
                .append(" will be with you when they are available. Feel free to add more information if needed!")
                .append("\n \n >>> **Request**: ")
                .append(message.getContentRaw())
                .build())
                .queue(ticket -> sendReplyToPrivate(member));
    }

    private void sendReplyToPrivate(Member member) {
        User user = member.getUser();
        user.openPrivateChannel().flatMap(privateChannel ->
                privateChannel.sendMessage(
                        "You have successfully submitted a ticket! " +
                                "You will receive a notification in the `Tickets` category when it is created. " +
                                "Someone will be with you as soon as they are available.")
        ).queue();
    }

    private static EnumSet<Permission> getPermissions() {
        EnumSet<Permission> permissions = EnumSet.of(Permission.MESSAGE_READ);
        permissions.add(Permission.MESSAGE_HISTORY);
        permissions.add(Permission.MESSAGE_EMBED_LINKS);
        permissions.add(Permission.MESSAGE_ATTACH_FILES);
        permissions.add(Permission.MESSAGE_ADD_REACTION);
        permissions.add(Permission.MESSAGE_WRITE);
        return permissions;
    }
}
