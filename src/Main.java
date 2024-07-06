public class Main {
    public static void main(String[] args) throws InterruptedException {
        //first test 10 min
        TwitchStreamObjekt stream = new TwitchStreamObjekt("Twitchchannel");

        Thread.sleep(10 * 60 * 1000);

        stream.stopStream();
        System.out.println("Programm beendet ");
    }

    private static void runConsole(){
        //Todo
    }
}
