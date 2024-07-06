import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class IRC_Controller {
    private final String SERVER_URL = "irc.chat.twitch.tv";
    private final int PORT = 6667;

    private String nick;
    private String oauth;
    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;

    private boolean shutdownAble;

    //public methods and konstruktor :
    public IRC_Controller(String channel){
        FileManager.readKeyFile();
        nick = FileManager.Nickname;
        oauth = FileManager.Token;
        connectToTwitch();
        shutdownAble = false;
    }

    public void setShutdownAble(boolean value){
        shutdownAble = value;
    }

    public void shutdown(){
        System.out.println("Bot wird gestoppt...");
        try{
            while (!shutdownAble){
                //warten bis der Thread fertig ist
                Thread.sleep(300);
            }
            socket.close();
            input.close();
            output.close();
        }catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getIrcMessage(){
        try {
            return input.readLine();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    //Rohzeile an den Server senden
    public void write(String msg){
        try {
            output.write(msg + "\r\n");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //private methods :
    private void connectToTwitch(){

        try {
            //Inititalisieren and Connect
            socket = new Socket(InetAddress.getByName(SERVER_URL), PORT);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()) );
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            //Send Start Commands
            write(TwitchCommands.SEND_TOKEN + oauth);
            write(TwitchCommands.SEND_NICKNAME + nick);
            write(TwitchCommands.SEND_MEMBERSHIP_REQUEST.toString());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
