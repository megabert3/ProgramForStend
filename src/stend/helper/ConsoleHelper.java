package stend.helper;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static void getMessage(String mess) {
        System.out.println(mess);
    }

    public static String entString() throws IOException {
        return bufferedReader.readLine();
    }

    public static void sleep(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            ConsoleHelper.getMessage("Произошла остановка Thread");
            e.printStackTrace();
        }
    }
}