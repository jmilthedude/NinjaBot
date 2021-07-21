package net.thedudemc.ninjabot.util;

import com.google.gson.internal.LinkedTreeMap;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.thedudemc.ninjabot.init.BotConfigs;
import net.thedudemc.ninjabot.notirole.NotiRole;

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

    public static List<String> getAdminRoleNames(Guild guild) {
        List<String> roles = new ArrayList<>();
        guild.getRoles().forEach(role -> {
            List<?> adminRoles = BotConfigs.getConfig(guild, "General").getOption("adminRoles").getListValue();
            for (Object o : adminRoles) {
                if (o instanceof String) {
                    String adminRole = (String) o;
                    if (role.getName().equalsIgnoreCase(adminRole)) {
                        roles.add(role.getName());
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

    public static List<String> getSuperUserRoleNames(Guild guild) {
        List<String> roles = new ArrayList<>();
        guild.getRoles().forEach(role -> {
            List<?> superRoles = BotConfigs.getConfig(guild, "General").getOption("superUserRoles").getListValue();
            for (Object o : superRoles) {
                if (o instanceof String) {
                    String superRole = (String) o;
                    if (role.getName().equalsIgnoreCase(superRole)) {
                        roles.add(role.getName());
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

    public static List<NotiRole> getReactionRoles(Guild guild) {
        List<NotiRole> reactionRoles = new ArrayList<>();
        List<?> temp = BotConfigs.getConfig(guild, "NotiRole").getOption("reactionRoles").getListValue();
        for (Object o : temp) {
            if (o instanceof NotiRole) {
                reactionRoles.add((NotiRole) o);
            } else if (o instanceof LinkedTreeMap) {
                reactionRoles.add(deserializeNotiRole((LinkedTreeMap<?, ?>) o));
            }
        }
        return reactionRoles;
    }

    private static NotiRole deserializeNotiRole(LinkedTreeMap<?, ?> original) {
        String emote = (String) original.get("emote");
        String role = (String) original.get("role");
        return new NotiRole(emote, role);
    }

    public static String getRoleByReaction(String emote, List<NotiRole> reactionRoles) {
        for (NotiRole reactionRole : reactionRoles) {
            if (reactionRole.getEmote().equalsIgnoreCase(emote)) return reactionRole.getRole();
        }
        return null;
    }

    public static NotiRole getNotiRole(String emote, List<NotiRole> reactionRoles) {
        for (NotiRole reactionRole : reactionRoles) {
            if (reactionRole.getEmote().equalsIgnoreCase(emote)) return reactionRole;
        }
        return null;
    }
}
