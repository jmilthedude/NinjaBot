package net.thedudemc.ninjabot.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.thedudemc.ninjabot.init.BotConfigs;

import java.util.ArrayList;
import java.util.List;

public class BotUtils {


    public static List<Role> getAdminRoles(Guild guild) {
        List<Role> roles = new ArrayList<>();
        guild.getRoles().forEach(role -> {
            List<?> adminRoles = BotConfigs.getConfig(guild, "General").getOption("adminRoles").getListValue();
            for (Object o : adminRoles) {
                if (o instanceof String) {
                    String adminRole = (String) o;
                    if (role.getName().equalsIgnoreCase(adminRole)) {
                        roles.add(role);
                    }
                }
            }
        });
        return roles;
    }

    public static List<Role> getSuperUserRoles(Guild guild) {
        List<Role> roles = new ArrayList<>();
        guild.getRoles().forEach(role -> {
            List<?> superRoles = BotConfigs.getConfig(guild, "General").getOption("superUserRoles").getListValue();
            for (Object o : superRoles) {
                if (o instanceof String) {
                    String superRole = (String) o;
                    if (role.getName().equalsIgnoreCase(superRole)) {
                        roles.add(role);
                    }
                }
            }
        });
        return roles;
    }

    public static boolean isAdmin(Guild guild, Member member) {
        if (member.isOwner()) return true;
        List<?> roleNames = BotConfigs.getConfig(guild, "General").getOption("adminRoles").getListValue();
        for (Object o : roleNames) {
            if (o instanceof String) {
                String name = (String) o;
                for (Role role : member.getRoles()) {
                    if (role.getName().equalsIgnoreCase(name)) return true;
                }
            }
        }
        return false;
    }

    public static boolean isSuperUser(Guild guild, Member member) {
        List<?> roleNames = BotConfigs.getConfig(guild, "General").getOption("superUserRoles").getListValue();
        for (Object o : roleNames) {
            if (o instanceof String) {
                String name = (String) o;
                for (Role role : member.getRoles()) {
                    if (role.getName().equalsIgnoreCase(name)) return true;
                }
            }
        }
        return false;
    }
}
