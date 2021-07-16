package net.thedudemc.ninjabot.command;

import net.dv8tion.jda.api.entities.*;
import net.thedudemc.dudeconfig.config.Config;
import net.thedudemc.ninjabot.init.BotConfigs;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BotCommand {

    private boolean requiresElevatedPrivileges;
    private boolean requiresAdminPrivileges;

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

    public boolean canExecute(Member member) {
        if (this.requiresElevatedPrivileges || this.requiresAdminPrivileges) {
            if (member == null) return false;
            List<Role> memberRoles = member.getRoles();
            Config config = BotConfigs.getConfig(member.getGuild(), "General");
            if (requiresAdminPrivileges) {
                List<String> adminRoles = (List<String>) config.getList("adminRoles");
                if (adminRoles.isEmpty()) adminRoles.add("Administrator");
                for (Role memberRole : memberRoles) {
                    if (adminRoles.contains(memberRole.getName())) return true;
                }
            }
            if (requiresElevatedPrivileges) {
                List<String> adminRoles = (List<String>) config.getList("adminRoles");
                if (adminRoles.isEmpty()) adminRoles.add("Administrator");
                List<String> superUserRoles = (List<String>) config.getList("superUserRoles");
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
