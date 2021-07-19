package net.thedudemc.ninjabot.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.thedudemc.ninjabot.init.BotConfigs;

import java.util.ArrayList;
import java.util.List;

public class BotUtils {

    public static List<Role> getSuperUserRoles(Guild guild) {
        List<Role> roles = new ArrayList<>();
        guild.getRoles().forEach(role -> {
            List<String> superRoles = (List<String>) BotConfigs.getConfig(guild, "General").getOption("superUserRoles").getListValue();
            for (String superRole : superRoles) {
                if (role.getName().equalsIgnoreCase(superRole)) {
                    roles.add(role);
                }
            }
        });
        System.out.println(roles);
        return roles;
    }
}
