package net.thedudemc.ninjabot.init;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thedudemc.ninjabot.NinjaBot;
import org.jetbrains.annotations.NotNull;

public class BotSetup extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        BotConfigs.register();
        BotData.register();
        BotListeners.register();
        BotCommands.register();

        NinjaBot.getLogger().info("NinjaBot setup complete!");
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        BotConfigs.registerGuild(event.getGuild());
        BotData.registerGuild(event.getGuild());

        super.onGuildJoin(event);
    }
}
