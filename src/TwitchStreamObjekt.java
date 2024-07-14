public class TwitchStreamObjekt {
    private String channel;
    private String protokoll;
    private IRC_Controller irc_Controller;
    private boolean consoleOutput;
    private Thread thread;
    private boolean stopParam;
    private int protokollLineLimiter;
    private int protokollLineCounter;
    private String matchcode;

    public boolean getConsoleOuput() {
        return consoleOutput;
    }

    public void setConsoleOutput(boolean consoleOutput){
        this.consoleOutput = consoleOutput;
    }

    public String getMatchcode() {
        return matchcode;
    }

    public int getProtokollLineLimiter(){
        return protokollLineLimiter;
    }

    public void setProtokollLineLimiter(int protokollLineLimiter) {
        this.protokollLineLimiter = protokollLineLimiter;
    }

    public TwitchStreamObjekt(String channelName, String matchcode){
        channel = channelName;
        consoleOutput = false;
        stopParam = false;
        protokoll = "";
        this.matchcode = matchcode;
        protokollLineLimiter = 100;
        irc_Controller = new IRC_Controller(channel);
        initThread();
        startStream();
    }



    public void stopStream() {
        stopParam = true;
        irc_Controller.shutdown();
        System.out.println("Bot für channel " + channel + " wurde heruntergefahren !");
        saveProtokoll();
    }

    //
    //Private Methoden
    //

    private void startStream() {
        //startet einen Thread
        System.out.println("Stream wird gejoint und gestartet");
        thread.start();
    }

    //Initialisiere den Thread
    private void initThread(){
        thread = new Thread( () -> {
            String irc_message = "";
            while ((irc_message = irc_Controller.getIrcMessage()) != null){
                String[] splitline = irc_message.split(" ");

                //Kontrolle für AFK
                if(splitline[0].equals(TwitchKeywords.FETCH_PING)){
                    irc_Controller.write(TwitchCommands.SEND_PONG.toString());
                }

                //Messages encodieren
                switch(splitline[1]) {
                    case TwitchKeywords.FETCH_CONNECTION_SUCCESS:
                        System.out.println("Verbindung war erfolgreich !");
                        joinChannel();
                        break;
                    case TwitchKeywords.FETCH_VIEWER_LIST:
                        processViewerList(irc_message);
                        break;
                    case TwitchKeywords.FETCH_VIEWER_MESSAGE:
                        processViewerMessage(irc_message);
                        break;
                    case TwitchKeywords.FETCH_VIEWER_JOIN:
                        processViewerJoin(irc_message);
                        break;
                    case TwitchKeywords.FETCH_VIEWER_LEFT:
                        processViewerLeft(irc_message);
                        break;
                    default:
                        break;
                }

                if(stopParam){
                    //beendet den Thread
                    irc_Controller.setShutdownAble(true);
                    break;
                }
            }
        });
    }

    //joint den Channel
    private void joinChannel(){
        irc_Controller.write(TwitchCommands.SEND_JOIN_CHANNEL + channel);
    }

    //verarbeitet Viewer Nachrichten
    private void processViewerMessage(String irc_message){
        String user = irc_message.split(" ")[0].split("!")[0];
        user = user.replaceAll(":", "");
        String message = irc_message.split(channel + " :")[1];

        String processed_String = "> " + user + ": " + message;
        addToProtokoll(processed_String);

        if(consoleOutput){
            System.out.println(processed_String);
        }
    }

    //verarbeitet Viewer Join
    private void processViewerJoin(String irc_message) {
        String user = parseUser(irc_message);
        String processed_String = "+ " + user + " joint !";

        addToProtokoll(processed_String);
        if(consoleOutput){
            System.out.println(processed_String);
        }
    }

    //verarbeitet Viewer Left
    private void processViewerLeft(String irc_message) {
        String user = parseUser(irc_message);
        String processed_String = "- " + user + " left !";

        addToProtokoll(processed_String);
        if(consoleOutput){
            System.out.println(processed_String);
        }
    }

    //verarbeitet Viewer Liste
    private void processViewerList(String irc_message) {
        //Todo
    }

    //parst user Nickname
    private String parseUser(String irc_message){
        String userNickname = irc_message.split(" ")[0].split("!")[0];
        userNickname = userNickname.replaceAll(":", "");
        return userNickname;
    }

    //
    private void addToProtokoll(String textLine){
        textLine = FileManager.getTimestamp() + " - " + textLine + "\n";
        protokoll = protokoll + textLine;
        protokollLineCounter++;
        if(protokollLineLimiter == protokollLineCounter){
            protokollLineCounter = 0;
            saveProtokoll();
            protokoll = "";
        }
    }

    private void saveProtokoll(){
        FileManager.writeProtokoll(channel, protokoll);
    }


}
