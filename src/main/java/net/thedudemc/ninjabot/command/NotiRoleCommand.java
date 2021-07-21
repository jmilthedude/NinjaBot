package net.thedudemc.ninjabot.command;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.thedudemc.dudeconfig.config.Config;
import net.thedudemc.dudeconfig.config.option.Option;
import net.thedudemc.ninjabot.init.BotConfigs;
import net.thedudemc.ninjabot.notirole.NotiRole;
import net.thedudemc.ninjabot.util.BotUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
                option.setValue(new ArrayList<NotiRole>());
                config.markDirty();
                config.save();
            } else if ("list".equalsIgnoreCase(args[0])) {
                if (notiRoleChannel.getName().equalsIgnoreCase("notification-roles")) return;
                MessageBuilder builder = new MessageBuilder("Available emotes and their respective roles: \n\n");
                List<NotiRole> reactionRoles = BotUtils.getReactionRoles(guild);
                reactionRoles.forEach(reactionRole -> builder.append(reactionRole.getEmote()).append(" - ").append(reactionRole.getRole()).append("\n"));
                channel.sendMessage(builder.build()).queue();
            } else if ("setup".equalsIgnoreCase(args[0])) {
                if (!notiRoleChannel.getName().equalsIgnoreCase("notification-roles")) return;
                notiRoleChannel.getHistoryFromBeginning(10).queue(messageHistory -> {
                    if (!messageHistory.isEmpty()) {
                        messageHistory.getRetrievedHistory().forEach(m -> m.delete().queue());
                        MessageBuilder builder = new MessageBuilder("React to this message with the following emotes to get their respective role.\n\n");
                        List<NotiRole> reactionRoles = BotUtils.getReactionRoles(guild);
                        reactionRoles.forEach(reactionRole -> {
                            builder.append(reactionRole.getEmote()).append(" - ").append(reactionRole.getRole()).append("\n");
                        });
                        notiRoleChannel.sendMessage(builder.build()).queue();
                    }
                });
            }
        } else if (args.length == 2) {
            if ("remove".equalsIgnoreCase(args[0])) {
                Config config = BotConfigs.getConfig(guild, "NotiRole");
                Option option = config.getOption("reactionRoles");
                List<NotiRole> reactionRoles = BotUtils.getReactionRoles(guild);
                String emote = args[1];
                NotiRole role = BotUtils.getNotiRole(emote, reactionRoles);
                if (role != null) {
                    if (reactionRoles.remove(role)) {
                        option.setValue(reactionRoles);
                        config.markDirty();
                        config.save();
                    }
                }
            }
        } else if (args.length == 3) {

            if ("add".equalsIgnoreCase(args[0])) {
                Config config = BotConfigs.getConfig(guild, "NotiRole");
                Option option = config.getOption("reactionRoles");
                List<NotiRole> reactionRoles = BotUtils.getReactionRoles(guild);
                String emote = args[1];
                String roleName = args[2];
                reactionRoles.add(new NotiRole(emote, roleName));
                option.setValue(reactionRoles);
                config.markDirty();
                config.save();
            }
        }

    }
}
