package tools;

import static domain.sequence.sentence.KSentence.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sanitizer {

    public static final String SEPARATOR_AUX = "#";

    private Sanitizer(){
    }

    public static String cleanInput(String input){
        input = brickString(input);
        return input.replaceAll("\\s{2,}|\\n+|\\t+", SEPARATOR).trim();
    }

    public static String brickString(String input){
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(input);

        while (m.find()) {
            String original = m.group(1);
            String fixed = original.replaceAll(SEPARATOR, SEPARATOR_AUX);
            input = input.replaceAll("\"" + original + "\"", "\"" + fixed + "\"");
        }

        return input;
    }

    public static String unBrickString(String input){
        input = input.replaceAll("^\"|\"$", "");
        return input.replaceAll(SEPARATOR_AUX, SEPARATOR);
    }

    public static String cleanKeyPrefix(String key) {
        return key.replace(PREFIX_KEY, "");
    }

    public static String cleanBlankEndChar(String sentence) {
        String lastChar = sentence.substring(sentence.length() - 1);
        if(lastChar.equals("\s"))
            return sentence.substring(0, sentence.length() - 1);
        return  sentence;
    }

}