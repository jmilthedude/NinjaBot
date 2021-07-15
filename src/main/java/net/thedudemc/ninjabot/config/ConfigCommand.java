package net.thedudemc.ninjabot.config;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.thedudemc.dudeconfig.config.Config;
import net.thedudemc.dudeconfig.config.option.Option;
import net.thedudemc.ninjabot.command.BotCommand;
import net.thedudemc.ninjabot.init.BotConfigs;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigCommand extends BotCommand {
    @Override
    public String getName() {
        return "config";
    }

    @Override
    public String getDescription() {
        return "Set config options via command.";
    }

    @Override
    public void execute(@Nullable Guild guild, @Nullable Member member, MessageChannel channel, Message message, @Nullable String[] args) {
        if (guild == null || member == null) return; // must be executed within the server.
        if (args == null || args.length == 0) return;

        if ("list".equalsIgnoreCase(args[0])) {
            sendOptionListMessage(guild, channel);
        } else if ("modify".equalsIgnoreCase(args[0])) {
            String configName = args[1];
            String optionName = args[2];
            Config config = BotConfigs.getConfig(guild, configName);
            Option<?> option = config.getOption(optionName);
            if (option.getValue() instanceof List) {

            } else {
                Class<?> type = option.getValue().getClass();
                String value = args[3];
                if (type.isAssignableFrom(Boolean.class)) {
                    config.setOption(optionName, Option.of(Boolean.parseBoolean(value)).withComment(option.getComment()));
                    BotConfigs.saveAll(guild);
                }
            }
        } else if ("get".equalsIgnoreCase(args[0])) {
            String configName = args[1];
            String optionName = args[2];

            Config config = BotConfigs.getConfig(guild, configName);
            Option<?> option = config.getOption(optionName);
            sendOptionGetMessage(channel, optionName, option);

        }
    }

    private void sendOptionGetMessage(MessageChannel channel, String optionName, Option<?> option) {
        MessageBuilder builder = new MessageBuilder();
        builder.append("*").append(optionName).append("*: \n");
        builder.append("Type: ").append((option.getValue()).getClass().getSimpleName()).append("\n");
        if (option.getValue() instanceof List) {
            List<?> list = (List<?>) option.getValue();
            builder.append("Values: \n");
            list.forEach(obj -> {
                builder.append("- **").append(String.valueOf(obj)).append("**\n");
            });
        } else {
            builder.append("Value: **").append(option.getValue()).append("**");
        }
        Message message = builder.build();
        channel.sendMessage(message).queue();
    }

    private void sendOptionListMessage(Guild guild, MessageChannel channel) {
        MessageBuilder builder = new MessageBuilder();
        builder.append("This is a list of available options to configure. \n \n");

        BotConfigs.getAllConfigs(guild).forEach(config -> {
            builder.append("**").append(config.getName()).append(" Config** - name: *").append(config.getName()).append("*\n");
            config.getDefaults().forEach((name, option) -> {
                builder.append(" - *").append(name).append("*: ").append(option.getComment()).append("\n");
            });
        });

        Message message = builder.build();
        channel.sendMessage(message).queue();
    }
}
