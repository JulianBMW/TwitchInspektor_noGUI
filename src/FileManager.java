import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileManager {
    private final static String FILE_PATH_KEY = "key.txt";
    private final static String FILE_PATH_PROTOKOLLE = "Protokolle";

    private final static String PROTOKOLL_PRAEFIX = "Protokoll_";
    
    public static String Nickname;
    public static String Token;

    public static void readKeyFile() {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_PATH_KEY))){
            Nickname = bufferedReader.readLine();
            Token = bufferedReader.readLine();
        }catch(IOException e){
            System.out.println("Error: File not Found!");
        }
    }

    public static void writeProtokoll(String twichChannel, String protokoll) {
        try{
            File directory = new File(FILE_PATH_PROTOKOLLE);
            if(!directory.exists()){
                directory.mkdirs();
            }

            Files.writeString(Paths.get(FILE_PATH_PROTOKOLLE ,PROTOKOLL_PRAEFIX + twichChannel + "_" + getTimestamp() + ".txt"), protokoll);
            System.out.println("Protokoll wurde geschrieben");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String getTimestamp() {
        LocalDateTime timeNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        return timeNow.format(formatter);
    }



}
