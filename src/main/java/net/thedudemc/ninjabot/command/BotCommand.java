package net.thedudemc.ninjabot.command;

import net.dv8tion.jda.api.entities.*;
import net.thedudemc.dudeconfig.config.Config;
import net.thedudemc.ninjabot.init.BotConfigs;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BotCommand {

    private boolean requiresElevatedPrivileges;
    private boolean requiresAdminPrivileges;
    private boolean deletable;

    public abstract String getName();

    public abstract String getDescription();

    public abstract void execute(@Nullable Guild guild, @Nullable Member member, MessageChannel channel, Message message, @Nullable String[] args);

    public BotCommand requireElevatedPrivileges() {
        requiresElevatedPrivileges = true;
        return this;
    }

    public BotCommand requireAdmin() {
        requiresAdminPrivileges = true;
        return this;
    }

    public BotCommand setAutoDelete() {
        this.deletable = true;
        return this;
    }

    public boolean shouldDelete() {
        return deletable;
    }

    public boolean canExecute(Member member) {
        if (member == null) return false;
        if (member.isOwner()) return true;
        if (this.requiresElevatedPrivileges || this.requiresAdminPrivileges) {
            List<Role> memberRoles = member.getRoles();
            Config config = BotConfigs.getConfig(member.getGuild(), "General");
            if (requiresAdminPrivileges) {
                List<String> adminRoles = (List<String>) config.getOption("adminRoles").getListValue();
                if (adminRoles.isEmpty()) adminRoles.add("Administrator");
                for (Role memberRole : memberRoles) {
                    if (adminRoles.contains(memberRole.getName())) return true;
                }
            }
            if (requiresElevatedPrivileges) {
                List<String> adminRoles = (List<String>) config.getOption("adminRoles").getListValue();
                if (adminRoles.isEmpty()) adminRoles.add("Administrator");
                List<String> superUserRoles = (List<String>) config.getOption("superUserRoles").getListValue();
                for (Role memberRole : memberRoles) {
                    if (adminRoles.contains(memberRole.getName())) return true;
                    if (superUserRoles.contains(memberRole.getName())) return true;
                }
            }
            return false;
        }
        return true;
    }


}
