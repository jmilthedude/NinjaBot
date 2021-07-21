package net.thedudemc.ninjabot.notirole;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public class NotiRole {

    @Expose private String emote;
    @Expose private String role;

    public NotiRole(String emote, String role) {
        this.emote = emote;
        this.role = role;
    }

    public String getEmote() {
        return emote;
    }

    public void setEmote(String emote) {
        this.emote = emote;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotiRole notiRole = (NotiRole) o;
        return this.getEmote().equalsIgnoreCase(notiRole.getEmote()) && this.getRole().equalsIgnoreCase(notiRole.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmote(), getRole());
    }
}
