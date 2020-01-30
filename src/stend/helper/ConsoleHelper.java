package stend.helper;

        import java.io.*;
        import java.util.Properties;

public class ConsoleHelper {

    //Директроия с файлом пропертиес
    private static final String dir = "src/resourseFiles/stendProperties.properties";

    public static Properties properties = getProperties();

    private static Properties getProperties() {
        Properties initProperties = new Properties();
        try {
            initProperties.load(new FileInputStream(new File(dir)));
        } catch (IOException e) {
            System.out.println("Указанный файл properties не найден");
            e.printStackTrace();
        }
        return initProperties;
    }

    public static boolean saveProperties() {
        File propFile = new File(dir);

        try (FileOutputStream fileOutputStream = new FileOutputStream(propFile)) {
            properties.store(fileOutputStream, "My comments");

            properties = getProperties();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

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