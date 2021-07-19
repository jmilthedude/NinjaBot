package net.thedudemc.ninjabot.config;

import net.thedudemc.dudeconfig.config.Config;
import net.thedudemc.dudeconfig.config.option.Option;
import net.thedudemc.dudeconfig.config.option.OptionMap;

import java.util.HashMap;

public class NotiRoleConfig extends Config {
    @Override
    public String getName() {
        return "NotiRole";
    }

    @Override
    public OptionMap getDefaults() {
        OptionMap map = OptionMap.create();
        HashMap<String, String> reactionRoles = new HashMap<>();

        reactionRoles.put("someEmoteId0", "roleName0");
        reactionRoles.put("someEmoteId1", "roleName1");
        map.put("reactionRoles", Option.of(reactionRoles).withComment("A map of emotes and their respective role to apply when used as a reaction."));

        return map;
    }
}
