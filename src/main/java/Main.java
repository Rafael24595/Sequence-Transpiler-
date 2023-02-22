import com.google.gson.Gson;

import sequence.Sequence;
import sequence.sentence.sentence.SentenceMap;
import tools.Sanitizer;
import java.util.HashMap;
import java.util.Map;

public class Main {

    static Map<String, Object> updateMap = new HashMap<>();

    /*** 
    Sequence structure: 
        1.[COMMAND -> Field | Raw value(used in list type complex structures)]
            Cases:
                - FIELD = Define the beggining of an key-value structure.
                - $value = Define a raw value.
        2.[NAME -> Field key]
            Cases:
                - $Key = Define the key of a field.
        3.[ACTION -> Action]
            Cases:
                - AS = Create new value without merge it with base value.
                - MUTATE = Merge actual value with base value (Raw values, will be overrided).
                - INCREMENT = Add up base value with actual value.
                - DECREMENT = Substract actual value to base value
                - DELETE = Erase base value
        4.[VALUE -> Value] 
            Cases: 
                - OBJECT = Define the beggining of a sub-structure with a key-value pattern, must be closed with CLOSE tag.
                - LIST = Define the beggining of a list type sub-structure, must be closed with CLOSE tag.
                - $value = Define a raw value.
        ***/

    static String inputText = """
                FIELD $data MUTATE OBJECT
                    FIELD $integer INCREMENT 11.99
                    FIELD $string MUTATE "cadena 1"
                    FIELD $boolean MUTATE false
                CLOSE
                FIELD $steps MUTATE LIST
                    10 11 12 13
                CLOSE
                FIELD $complex_list AS LIST
                    OBJECT
                        FIELD $integer AS 311
                        FIELD $string AS "cadena 99"
                    CLOSE
                CLOSE
                FIELD $erase DELETE
                FIELD multi_level_1 MUTATE OBJECT
                    FIELD $integer_4 AS 4
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
                "$boolean": true,
                "test_new": "test_field_1"
              },
              "$steps": [0],
              "$erase": "this_field_out",
              "multi_level_1":{
                "$integer_3": "3",
                "multi_level_2": {
                    "$string": "cadena 2"
                }
              }
            }
            """;


    public static void main(String[] args) {
        String input = Sanitizer.cleanInput(inputText);

        updateMap = new Gson().fromJson(baseJson, HashMap.class);
        Map<String, Object> baseMap = new HashMap<>(updateMap);

        SentenceMap sentenceBase = new SentenceMap(baseMap);
        SentenceMap sentenceUpdate = new SentenceMap(updateMap);

        Sequence sequence = new Sequence(input, sentenceBase);
        sequence.build(sentenceUpdate);

        System.out.println("Close app...");
    }

}