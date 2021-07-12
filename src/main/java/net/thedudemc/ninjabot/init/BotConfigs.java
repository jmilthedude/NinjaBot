package net.thedudemc.ninjabot.init;

import net.thedudemc.dudeconfig.config.Config;
import net.thedudemc.dudeconfig.config.ConfigRegistry;
import net.thedudemc.ninjabot.NinjaBot;
import net.thedudemc.ninjabot.config.GeneralConfig;

public class BotConfigs {

    private static final ConfigRegistry REGISTRY = new ConfigRegistry("./config/");

    public static void register() {
        NinjaBot.getLogger().info("Registering configs...");

        register(new GeneralConfig());
    }

    private static void register(Config config) {
        REGISTRY.register(config);
    }

    public static Config getConfig(String name) {
        return REGISTRY.getConfig(name);
    }
}
