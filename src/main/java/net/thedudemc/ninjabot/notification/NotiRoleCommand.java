package net.thedudemc.ninjabot.notification;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.thedudemc.dudeconfig.config.Config;
import net.thedudemc.dudeconfig.config.option.Option;
import net.thedudemc.ninjabot.command.BotCommand;
import net.thedudemc.ninjabot.init.BotConfigs;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class NotiRoleCommand extends BotCommand {
    @Override
    public String getName() {
        return "notirole";
    }

    @Override
    public String getDescription() {
        return "Add or remove emote/reaction for notification roles.";
    }

    @Override
    public void execute(@Nullable Guild guild, @Nullable Member member, MessageChannel channel, Message message, @Nullable String[] args) {
        // notirole add <emote> <role>
        // notirole remove <emote> <role>
        // notirole clear
        if (guild == null || args == null) return; // cannot be done in private message. also, requires some arguments.
        TextChannel notiRoleChannel = (TextChannel) channel;
        if (args.length == 1) {
            if ("clear".equalsIgnoreCase(args[0])) {
                Config config = BotConfigs.getConfig(guild, "NotiRole");
                Option option = config.getOption("reactionRoles");
                option.setValue(new HashMap<String, String>());
                config.markDirty();
                config.save();

            } else if ("list".equalsIgnoreCase(args[0])) {

            } else if ("setup".equalsIgnoreCase(args[0])) {
                if (!notiRoleChannel.getName().equalsIgnoreCase("notification-roles")) return;
                notiRoleChannel.getHistoryFromBeginning(10).queue(messageHistory -> {
                    if (!messageHistory.isEmpty()) {
                        messageHistory.getRetrievedHistory().forEach(m -> m.delete().queue());
                        MessageBuilder builder = new MessageBuilder("React to this message with the following emotes to get their respective role.\n\n");
                        HashMap<String, String> roles = (HashMap<String, String>) BotConfigs.getConfig(guild, "NotiRole").getOption("reactionRoles").getMapValue();
                        roles.forEach((k, v) -> {
                            builder.append(k).append(" - ").append(v).append("\n");
                        });
                        notiRoleChannel.sendMessage(builder.build()).queue();
                    }
                });
            }
        } else if (args.length == 3) {
            Config config = BotConfigs.getConfig(guild, "NotiRole");
            Option option = config.getOption("reactionRoles");
            HashMap<String, String> reactionRoles = (HashMap<String, String>) option.getMapValue();
            String emote = args[1];
            String roleName = args[2];
            if ("add".equalsIgnoreCase(args[0])) {
                reactionRoles.put(emote, roleName);
                System.out.println(reactionRoles);
            } else if ("remove".equalsIgnoreCase(args[0])) {

            }
            config.markDirty();
            config.save();
        }

    }
}
