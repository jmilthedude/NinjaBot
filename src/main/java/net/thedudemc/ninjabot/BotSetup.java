package net.thedudemc.ninjabot;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BotSetup extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        NinjaBot.getLogger().info("NinjaBot ready!");
        super.onReady(event);
    }
}
