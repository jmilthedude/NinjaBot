package net.thedudemc.ninjabot.init;

import net.dv8tion.jda.api.entities.Guild;
import net.thedudemc.dudeconfig.config.Config;
import net.thedudemc.dudeconfig.config.ConfigRegistry;
import net.thedudemc.ninjabot.NinjaBot;
import net.thedudemc.ninjabot.config.GeneralConfig;

import java.util.HashMap;

public class BotConfigs {

    private static final HashMap<Long, ConfigRegistry> REGISTRIES = new HashMap<>();

    public static void register() {
        NinjaBot.getLogger().info("Registering configs...");
        NinjaBot.getJDA().getGuilds().forEach(guild -> {
            REGISTRIES.put(guild.getIdLong(), new ConfigRegistry("./config/" + guild.getId() + "/"));
            register(guild, new GeneralConfig());
        });

    }

    private static void register(Guild guild, Config config) {
        REGISTRIES.get(guild.getIdLong()).register(config);
    }

    public static Config getConfig(Guild guild, String name) {
        return REGISTRIES.get(guild.getIdLong()).getConfig(name);
    }
}
