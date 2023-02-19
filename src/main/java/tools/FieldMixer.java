package tools;

import com.google.gson.JsonElement;
import sequence.objects.ISequenceObject;

import static sequence.KSequence.*;
import static sequence.KSequence.DECREMENT;

public class FieldMixer {

    private FieldMixer(){
    }

    public static boolean merge(String action, JsonElement jsonObject, ISequenceObject<?> value) throws IllegalArgumentException {
        switch (action){
            case AS:
                return asValue(jsonObject, value);
            case MUTATE:
                return mutateValue(jsonObject, value);
            case INCREMENT:
                return incrementValue(jsonObject, value);
            case DECREMENT:
                return decrementValue(jsonObject, value);
            default:
                return true;
        }
    }

    private static boolean asValue(JsonElement jsonObject, ISequenceObject<?> value) {
        return true;
    }

    private static boolean mutateValue(JsonElement jsonObject, ISequenceObject<?> value) throws IllegalArgumentException {
        if(jsonObject == null)
            throw new IllegalArgumentException("Field does not exists");
        return asValue(jsonObject, value);
    }

    private static boolean decrementValue(JsonElement jsonObject, ISequenceObject value) throws IllegalArgumentException {
        return calculateValue(jsonObject, value, - 1);
    }

    private static boolean incrementValue(JsonElement jsonObject, ISequenceObject value) throws IllegalArgumentException {
        return calculateValue(jsonObject, value, 1);
    }

    private static boolean calculateValue(JsonElement jsonObject, ISequenceObject value, int multiplier) throws IllegalArgumentException {
        mutateValue(jsonObject, value);
        Object object = value.getObject();

        if(ValueParser.isDouble(object)) {
            Double newValue = ValueParser.parseDouble(object);
            Double oldValue = jsonObject.getAsDouble();
            value.setObject(oldValue + newValue * multiplier);
            return true;
        } else if(ValueParser.isNumeric(object)) {
            Integer newValue = ValueParser.parseInteger(object);
            Integer oldValue = jsonObject.getAsInt();
            value.setObject(oldValue + newValue * multiplier);
            return true;
        }

        throw new IllegalArgumentException("Bad Format");
    }

}