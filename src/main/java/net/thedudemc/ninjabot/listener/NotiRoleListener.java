package net.thedudemc.ninjabot.listener;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thedudemc.dudeconfig.config.Config;
import net.thedudemc.dudeconfig.config.option.Option;
import net.thedudemc.ninjabot.NinjaBot;
import net.thedudemc.ninjabot.init.BotConfigs;
import net.thedudemc.ninjabot.notirole.NotiRole;
import net.thedudemc.ninjabot.util.BotUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class NotiRoleListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (!event.getChannel().getName().equalsIgnoreCase("notification-roles")) return;
        Config config = BotConfigs.getConfig(event.getGuild(), "NotiRole");
        Option option = config.getOption("reactionRoles");
        List<NotiRole> reactionRoles = BotUtils.getReactionRoles(event.getGuild());

        event.retrieveMember().queue(member -> handleReaction(event.getGuild(), member, event.getChannel(), reactionRoles, event.getReactionEmote(), true));


    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        if (!event.getChannel().getName().equalsIgnoreCase("notification-roles")) return;
        Config config = BotConfigs.getConfig(event.getGuild(), "NotiRole");
        Option option = config.getOption("reactionRoles");
        List<NotiRole> reactionRoles = BotUtils.getReactionRoles(event.getGuild());
        event.retrieveMember().queue(member -> handleReaction(event.getGuild(), member, event.getChannel(), reactionRoles, event.getReactionEmote(), false));
    }

    private void handleReaction(Guild guild, Member member, TextChannel channel, List<NotiRole> reactionRoles, MessageReaction.ReactionEmote reaction, boolean add) {
        if (member.isOwner()) return;

        channel.getHistoryFromBeginning(1).queue(messageHistory -> {
            if (!messageHistory.isEmpty()) {
                Optional<Message> messageOptional = messageHistory.getRetrievedHistory().stream().findFirst();
                messageOptional.ifPresent(message -> {
                    try {
                        String roleName = null;
                        if (reaction.isEmoji()) {
                            String emojiString = reaction.getAsReactionCode();
                            roleName = BotUtils.getRoleByReaction(emojiString, reactionRoles);
                        }
                        if (reaction.isEmote()) {
                            Emote emote = reaction.getEmote();
                            String emoteString = emote.getAsMention();
                            roleName = BotUtils.getRoleByReaction(emoteString, reactionRoles);
                        }
                        if (roleName != null) {
                            for (Role role : guild.getRoles()) {
                                if (role.getName().equalsIgnoreCase(roleName)) {
                                    if (add) guild.addRoleToMember(member, role).queue();
                                    else guild.removeRoleFromMember(member, role).queue();
                                    return;
                                }
                            }
                        }
                    } catch (Exception ex) {
                        NinjaBot.getLogger().error(ex.getMessage());
                    }
                });
            }
        });
    }
}
