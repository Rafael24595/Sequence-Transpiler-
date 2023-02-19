package sequence;

import com.google.gson.JsonElement;
import sequence.objects.ISequenceObject;
import static sequence.KSequence.*;

public class Sequence {

    private final String input;
    private final JsonElement jsonObject;

    public Sequence(String input, JsonElement jsonObject ){
        this.input = input;
        this.jsonObject = jsonObject;
    }

    public void build(ISequenceObject<?> object) {
        String[] commands = input.split(SEPARATOR);
        int pointer = 0;

        while(pointer < commands.length){
            pointer = object.evaluate(input, pointer, jsonObject);
            pointer++;
        }
    }

}