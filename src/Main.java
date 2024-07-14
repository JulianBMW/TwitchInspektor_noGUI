import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        runConsole();
        System.out.println("Programm beendet ");
    }

    private static void runConsole(){
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        boolean onRepeat = true;
        String output = null;

        while (onRepeat) {
            try {
                output = ConsoleBase.ask_ConsoleCommander(input.readLine());

                //close Operation
                if(output.equals("Systemexit")) {
                    //onRepeat = false;
                    return;
                }

                System.out.println(output);
            } catch (IOException e) {
                onRepeat = false;
                e.printStackTrace();
            }
        }
    }
}
