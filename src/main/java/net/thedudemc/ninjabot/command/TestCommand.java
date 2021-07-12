package net.thedudemc.ninjabot.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.thedudemc.ninjabot.NinjaBot;
import net.thedudemc.ninjabot.command.base.BotCommand;

import javax.annotation.Nullable;
import java.util.Arrays;

public class TestCommand extends BotCommand {
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "This is a test command";
    }

    @Override
    public void execute(Member member, MessageChannel channel, Message message, @Nullable String[] args) {
        NinjaBot.getLogger().info("Member: " + member.getEffectiveName());
        NinjaBot.getLogger().info("Channel: " + channel.getName());
        NinjaBot.getLogger().info("Content: " + message.getContentDisplay());
        if (args != null) NinjaBot.getLogger().info("Arguments: " + Arrays.toString(args));
    }
}
