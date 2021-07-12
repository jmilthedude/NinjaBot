package net.thedudemc.ninjabot.config;

import net.thedudemc.dudeconfig.config.Config;
import net.thedudemc.dudeconfig.config.option.Option;
import net.thedudemc.dudeconfig.config.option.OptionMap;

public class GeneralConfig extends Config {
    @Override
    public String getName() {
        return "General";
    }

    @Override
    public OptionMap getDefaults() {
        OptionMap map = OptionMap.create();
        map.put("deleteCommands", Option.of(true).withComment("Should the bot delete commands and their output. Default: true"));
        return map;
    }
}
