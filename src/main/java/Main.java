import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import sequence.objects.SequenceMap;
import sequence.Sequence;
import tools.Sanitizer;
import java.util.HashMap;


public class Main {

    static HashMap<String, Object> baseMap = new HashMap<>();

    static String inputText = """
                FIELD $data MUTATE OBJECT
                    FIELD $integer INCREMENT 11.99
                    FIELD $string MUTATE "cadena 1"
                    FIELD $boolean MUTATE false
                CLOSE
                FIELD $steps MUTATE LIST
                    10 11 12 13
                CLOSE
                FIELD $test AS OBJECT
                    FIELD $sub-test AS OBJECT
                        FIELD $integer_2 AS 12
                        FIELD $string_2 AS "cadena 2"
                        FIELD $boolean_2 AS true
                    CLOSE
                CLOSE
            """;

    static String baseJson = """
            {
              "$data":{
                "$integer": "1",
                "$string": "empty 1",
                "$boolean": true
              },
              "$steps": [0]
            }
            """;


    public static void main(String[] args) {
        String input = Sanitizer.cleanInput(inputText);
        JsonObject jsonObject = new JsonParser().parse(baseJson).getAsJsonObject();

        Sequence s = new Sequence(input, jsonObject );
        SequenceMap map = new SequenceMap(baseMap);
        s.build(map);

        System.out.println("Close app...");
    }

}