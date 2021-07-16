package net.thedudemc.ninjabot.config.command;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
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
        if (args.length == 1) {
            if ("list".equalsIgnoreCase(args[0])) {
                sendOptionListMessage(guild, channel);
            }
            if ("help".equalsIgnoreCase(args[0])) {
                sendConfigHelpMessage(channel);
            }
        } else if (args.length == 3) {
            if ("get".equalsIgnoreCase(args[0])) {
                String configName = args[1];
                String optionName = args[2];

                Config config = BotConfigs.getConfig(guild, configName);
                Option<?> option = config.getOption(optionName);
                sendOptionGetMessage(channel, optionName, option);

            }
        } else if (args.length == 4) {
            String configName = args[1];
            String optionName = args[2];
            String value = args[3];

            Config config = BotConfigs.getConfig(guild, configName);
            Option<?> option = config.getOption(optionName);

            if ("set".equalsIgnoreCase(args[0])) {
                if (option.getValue() instanceof List) {
                    return;
                } else {
                    Class<?> type = option.getValue().getClass();
                    if (type.isAssignableFrom(Boolean.class)) {
                        config.setOption(optionName, Option.of(Boolean.parseBoolean(value)).withComment(option.getComment()));
                        sendSetSuccessMessage(optionName, value, channel);
                    }
                }
            }
            if ("add".equalsIgnoreCase(args[0])) {
                if (option.getValue() instanceof List) {
                    List<String> list = (List<String>) option.getValue();
                    list.add(value);
                    if (list.contains(value)) sendAddSuccessMessage(optionName, value, channel);
                }
            }
            if ("remove".equalsIgnoreCase(args[0])) {
                if (option.getValue() instanceof List) {
                    List<String> list = (List<String>) option.getValue();
                    if (list.remove(value)) sendRemoveSuccessMessage(optionName, value, channel);
                }
            }
            config.markDirty();
            BotConfigs.saveAll(guild);
        }
    }

    private void sendConfigHelpMessage(MessageChannel channel) {
        MessageBuilder builder = new MessageBuilder();

        builder.append("**Config Command Usage**:\n");
        builder.append("*commands are case sensitive*\n\n");

        builder.append("**List all options:**\n");
        builder.append(" `-config list`\n\n");

        builder.append("**Get a specific value:**\n");
        builder.append(" `-config get <ConfigName> <OptionName>`\n\n");

        builder.append("**Set a specific value:**\n");
        builder.append(" `-config set <ConfigName> <OptionName> <value>`\n\n");

        builder.append("**Add a value to an option list:**\n");
        builder.append(" `-config add <ConfigName> <OptionName> <value>`\n\n");

        builder.append("**Remove a value from an option list:**\n");
        builder.append(" `-config remove <ConfigName> <OptionName> <value>`\n\n");


        Message message = builder.build();
        channel.sendMessage(message).queue();
    }

    private void sendOptionGetMessage(MessageChannel channel, String optionName, Option<?> option) {
        MessageBuilder builder = new MessageBuilder();
        builder.append("*").append(optionName).append("*: \n");
        builder.append("Type: ").append((option.getValue()).getClass().getSimpleName()).append("\n");
        if (option.getValue() instanceof List) {
            List<?> list = (List<?>) option.getValue();
            builder.append("Values: \n");
            list.forEach(obj -> builder.append("- **").append(String.valueOf(obj)).append("**\n"));
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
            config.getDefaults().forEach((name, option) -> builder.append(" - *").append(name).append("*: ").append(option.getComment()).append("\n"));
        });

        Message message = builder.build();
        channel.sendMessage(message).queue();
    }

    private void sendSetSuccessMessage(String optionName, Object value, MessageChannel channel) {
        MessageBuilder builder = new MessageBuilder();
        builder.append("The value for *").append(optionName).append("* has been set to: **").append(value).append("**");
        Message message = builder.build();
        channel.sendMessage(message).queue();
    }

    private void sendAddSuccessMessage(String optionName, Object value, MessageChannel channel) {
        MessageBuilder builder = new MessageBuilder();
        builder.append("The value for *").append(optionName).append("* has been added: **").append(value).append("**");
        Message message = builder.build();
        channel.sendMessage(message).queue();
    }

    private void sendRemoveSuccessMessage(String optionName, Object value, MessageChannel channel) {
        MessageBuilder builder = new MessageBuilder();
        builder.append("The value for *").append(optionName).append("* has been removed: **").append(value).append("**");
        Message message = builder.build();
        channel.sendMessage(message).queue();
    }
}
