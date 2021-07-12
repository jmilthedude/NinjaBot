package net.thedudemc.ninjabot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.thedudemc.ninjabot.event.BotSetupEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class NinjaBot extends ListenerAdapter {

    private static Logger LOG = LoggerFactory.getLogger(NinjaBot.class);
    public static JDA JDA;

    public static void main(String[] args) throws LoginException {
        getLogger().info("Starting up...");

        if (args.length < 1) {
            getLogger().error("A token must be provided as the first Program Argument.");
            return;
        }

        String token = args[0];

        JDABuilder builder = JDABuilder.createDefault(token);
        builder.enableIntents(EnumSet.allOf(GatewayIntent.class));
        builder.addEventListeners(new BotSetupEvents());
        JDA = builder.build();
    }

    public static Logger getLogger() {
        return LOG;
    }

    public static JDA getJDA() {
        return JDA;
    }
}
