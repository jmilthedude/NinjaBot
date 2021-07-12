package net.thedudemc.ninjabot.command.base;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.annotation.Nullable;

public abstract class BotCommand {

    private boolean requiresElevatedPrivileges;

    public abstract String getName();

    public abstract String getDescription();

    public abstract void execute(Guild guild, @Nullable Member member, MessageChannel channel, Message message, @Nullable String[] args);

    public BotCommand requireElevatedPrivileges() {
        requiresElevatedPrivileges = true;
        return this;
    }

    public boolean canExecute(Member member) {
        if (this.requiresElevatedPrivileges) {
            if (member == null) return false;
            // TODO: check member privileges
            return false;
        }
        return true;
    }


}
