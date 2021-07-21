package net.thedudemc.ninjabot.config;

import net.thedudemc.dudeconfig.config.Config;
import net.thedudemc.dudeconfig.config.option.Option;
import net.thedudemc.dudeconfig.config.option.OptionMap;
import net.thedudemc.ninjabot.notirole.NotiRole;

import java.util.ArrayList;
import java.util.List;

public class NotiRoleConfig extends Config {
    @Override
    public String getName() {
        return "NotiRole";
    }

    @Override
    public OptionMap getDefaults() {
        OptionMap map = OptionMap.create();
        List<NotiRole> reactionRoles = new ArrayList<>();

        reactionRoles.add(new NotiRole("someEmoteId0", "roleName0"));
        reactionRoles.add(new NotiRole("someEmoteId1", "roleName1"));
        map.put("reactionRoles", Option.of(reactionRoles).withComment("A list of emotes and their respective role to apply when used as a reaction."));

        return map;
    }
}
