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

        List<Long> adminRoles = new ArrayList<>();
        adminRoles.add(1234567890L);
        adminRoles.add(9876543210L);
        map.put("adminRoles", Option.of(adminRoles).withComment("A list of role IDs with admin privileges."));

        List<Long> superUserRoles = new ArrayList<>();
        superUserRoles.add(1234567890L);
        superUserRoles.add(9876543210L);
        map.put("superUserRoles", Option.of(superUserRoles).withComment("A list of role IDs with elevated privileges, but not administrator."));

        return map;
    }
}
