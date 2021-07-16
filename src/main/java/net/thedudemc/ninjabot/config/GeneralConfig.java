package net.thedudemc.ninjabot.config;

import net.thedudemc.dudeconfig.config.Config;
import net.thedudemc.dudeconfig.config.option.Option;
import net.thedudemc.dudeconfig.config.option.OptionMap;

import java.util.ArrayList;
import java.util.List;

public class GeneralConfig extends Config {
    @Override
    public String getName() {
        return "General";
    }

    @Override
    public OptionMap getDefaults() {
        OptionMap map = OptionMap.create();
        map.put("deleteCommands", Option.of(true).withComment("Should the bot delete commands and their output. Default: true"));

        List<String> adminRoles = new ArrayList<>();
        adminRoles.add("Administrator");
        adminRoles.add("AnotherAdmin");
        map.put("adminRoles", Option.of(adminRoles).withComment("A list of role names with admin privileges."));

        List<String> superUserRoles = new ArrayList<>();
        superUserRoles.add("Moderator");
        superUserRoles.add("AnotherMod");
        map.put("superUserRoles", Option.of(superUserRoles).withComment("A list of role names with elevated privileges, but not administrator."));

        return map;
    }
}
