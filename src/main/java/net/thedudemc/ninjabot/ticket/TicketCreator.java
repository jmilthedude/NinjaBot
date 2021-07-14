package net.thedudemc.ninjabot.ticket;

import java.util.Objects;

public class TicketCreator {

    private final long guildId;
    private final long memberId;

    public TicketCreator(long guildId, long memberId) {
        this.guildId = guildId;
        this.memberId = memberId;
    }

    public long getGuildId() {
        return guildId;
    }

    public long getMemberId() {
        return memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketCreator that = (TicketCreator) o;
        return this.guildId == that.guildId && this.memberId == that.memberId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.guildId, this.memberId);
    }
}
