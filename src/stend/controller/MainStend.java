package stend.controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainStend {

    String name = "Хуета";

    public static void main(String[] args) {

        StringBuilder stringBuilder = new StringBuilder();

        Pattern pat = Pattern.compile("[0-9]+");
        Matcher mat = pat.matcher("10(60)");

        while (mat.find()) {
            stringBuilder.append(mat.group()).append(",");
        }

        System.out.println(new String(stringBuilder));
    }

    private void idiNaXyi() throws IOException {
    }

}
