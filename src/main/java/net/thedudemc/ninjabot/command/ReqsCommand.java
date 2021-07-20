package net.thedudemc.ninjabot.command;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.Nullable;

public class ReqsCommand extends BotCommand {

    @Override
    public String getName() {
        return "reqs";
    }

    @Override
    public String getDescription() {
        return "Gets a list of requirements for the bot. Channels, categories etc.";
    }

    @Override
    public void execute(@Nullable Guild guild, @Nullable Member member, MessageChannel channel, Message message, @Nullable String[] args) {
        MessageBuilder builder = new MessageBuilder("**Welcome to NinjaBot!**\n");
        builder.append("Below you will find a few requirements to use this bot's features.\n\n");

        builder.append("*General*:\n");
        builder.append(" - Channels\n");
        builder.append("    - `bot-commands` 'To run all of the admin level commands. Should be restricted to admins only.'\n");
        builder.append("    - `notification-roles` 'To hold the message which users will react to for role assignment.'\n");
        builder.append("\n");

        builder.append("*Tickets*:\n");
        builder.append(" - Channels\n");
        builder.append("    - `help-request` 'This channel will hold the message which users will react to for a ticket request.'\n");
        builder.append(" - Categories\n");
        builder.append("    - `Tickets` 'This is where active tickets will be.'\n");
        builder.append("    - `Closed Tickets` 'Likewise, where closed tickets will be.'\n");
        builder.append("\n");

        channel.sendMessage(builder.build()).queue();
    }
}
