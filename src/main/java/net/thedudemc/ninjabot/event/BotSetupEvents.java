package net.thedudemc.ninjabot.event;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thedudemc.ninjabot.NinjaBot;
import net.thedudemc.ninjabot.init.BotCommands;
import net.thedudemc.ninjabot.init.BotConfigs;
import net.thedudemc.ninjabot.init.BotData;
import net.thedudemc.ninjabot.init.BotEvents;
import org.jetbrains.annotations.NotNull;

public class BotSetupEvents extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        BotConfigs.register();
        BotData.register();
        BotEvents.register();
        BotCommands.register();

        NinjaBot.getLogger().info("NinjaBot setup complete!");
    }
}
