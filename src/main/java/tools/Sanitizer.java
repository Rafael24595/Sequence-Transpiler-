package tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static sequence.KSequence.*;

public class Sanitizer {

    public static final String SEPARATOR_AUX = "#";

    private Sanitizer(){
    }

    public static String cleanInput(String input){
        input = brickString(input);
        return input.replaceAll("\\s{2,}|\\n+|\\t+", SEPARATOR).trim();
    }

    private static String brickString(String input){
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(input);

        while (m.find()) {
            String original = m.group(1);
            String fixed = original.replaceAll(SEPARATOR, SEPARATOR_AUX);
            input = input.replaceAll("\"" + original + "\"", "\"" + fixed + "\"");
        }

        return input;
    }

}