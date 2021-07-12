package net.thedudemc.ninjabot.event;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thedudemc.ninjabot.init.BotCommands;
import net.thedudemc.ninjabot.init.BotEvents;
import org.jetbrains.annotations.NotNull;

public class BotSetupEvents extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        BotEvents.register();
        BotCommands.register();

        super.onReady(event);
    }
}
