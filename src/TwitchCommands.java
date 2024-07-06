public enum TwitchCommands {
    SEND_PONG("PONG:tmi.twitch.tv"),

    SEND_TOKEN("PASS oauth:"),

    SEND_NICKNAME("NICK "),

    SEND_MEMBERSHIP_REQUEST("CAP REQ :twitch.tv/membership"),

    SEND_JOIN_CHANNEL("JOIN #");

    private final String value;

    TwitchCommands(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
