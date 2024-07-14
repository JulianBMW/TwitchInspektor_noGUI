import java.util.HashMap;
import java.util.Map;

//Todo: Error Handling CLI coming soon
public class ConsoleBase {

    //Switchparam
    private static final String TOOL_START_BOT = "start";
    private static final String BOT_STOP = "stop";
    private static final String BOT_PROTOKOLL_LIMITER = "limit";
    private static final String BOT_CONSOLE_OUTPUT = "out";
    private static final String TOOL_SHUTDOWN = "shutdown";

    private static String[] arguments;
    private static Map<String, TwitchStreamObjekt> bots = new HashMap<>();

    public static String ask_ConsoleCommander( String input ){

        arguments = input.split(" ");

        if(bots.containsKey(arguments[0])){
            return manageBotOperations(arguments[1], bots.get(arguments[0]));
        }
        else {
            return manageToolOperations(arguments[0]);
        }
    }

    private static String manageBotOperations(String operation, TwitchStreamObjekt bot){
        switch(operation) {
            case(BOT_STOP):
                //command: matchcode - stop
                bot.stopStream();
                bots.remove(bot.getMatchcode());
                return String.format("Bot %s wurde entfernt.", bot.getMatchcode());
            case(BOT_PROTOKOLL_LIMITER):
                //command: matchcode - limit - intvalue
                int valueLimiter = Integer.parseInt(arguments[2]);
                bot.setProtokollLineLimiter(valueLimiter);
                //return "Bot " + bot.getMatchcode() + " limiter " + valueLimiter;
                return String.format("Botlimitter %s wurde auf %d gesetzt.", bot.getMatchcode(), valueLimiter);
            case(BOT_CONSOLE_OUTPUT):
                //command: matchcode - out - bool
                boolean valueOutput = Boolean.parseBoolean(arguments[2]);
                bot.setConsoleOutput(valueOutput);
                return String.format("Bot %s Konsolenoutput wurde auf %b gesetzt.", bot.getMatchcode(), valueOutput);
            default:
                return "wrong input !\n";
        }
    }

    private static String manageToolOperations(String operation){
        switch(operation) {
            case(TOOL_START_BOT):
                //command: start - channelname - match code
                bots.put(arguments[2], new TwitchStreamObjekt(arguments[1], arguments[2]));
                return String.format("Bot %s wurde hinzugefügt und gestartet.", arguments[2]);
            case(TOOL_SHUTDOWN):
                //command: shutdown
                for (TwitchStreamObjekt bot : bots.values()){
                    bot.stopStream();
                }
                bots = null;
                return "Systemexit";
            default:
                return "wrong input !\n";
        }
    }
}
