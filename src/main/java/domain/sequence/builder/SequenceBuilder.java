package domain.sequence.builder;

import tools.ValueParser;

import java.util.*;

public class SequenceBuilder {

    public String build(Map<String, Object> oldStructure, Map<String, Object> newStructure){
        StringBuilder sb = new StringBuilder();

        for (String key: ValueParser.joinMapKeys(oldStructure, newStructure)) {
            SequenceStructure sequence = new SequenceStructure(key);

            if(newStructure.containsKey(key))
                persist(sequence, oldStructure, newStructure);
            else
                sequence.delete();

            sb.append(sequence.build());
        }

        return sb.toString();
    }

    private void persist(SequenceStructure sequence, Map<String, Object> oldStructure, Map<String, Object> newStructure) {
        if(oldStructure != null && oldStructure.containsKey(sequence.name))
            persistAsModify(sequence, oldStructure, newStructure);
        else
            persistAsNew(sequence, newStructure);
    }

    private void persistAsModify(SequenceStructure sequence, Map<String, Object> oldStructure, Map<String, Object> newStructure){
        Object oldValue = oldStructure.get(sequence.name);
        Object newValue = newStructure.get(sequence.name);

        if(oldValue.equals(newValue))
            sequence.clean();
        else{
            sequence.mutate();
            oldValue = ValueParser.typeOf(oldValue, newValue) ? oldValue : null;
            buildValue(sequence ,oldValue, newValue);
        }
    }

    private void persistAsNew(SequenceStructure sequence, Map<String, Object> newStructure){
        Object value2 = newStructure.get(sequence.name);
        sequence.as();
        buildValue(sequence ,null, value2);

    }

    private SequenceStructure buildValue(SequenceStructure sequence, Object objectOld, Object objectNew){
        if(objectNew instanceof List<?>)
            return buildList(sequence, objectOld, objectNew);
        if(objectNew instanceof Map<?,?>)
            return buildObject(sequence, objectOld, objectNew);
        return buildRawValue(sequence, objectOld, objectNew);
    }

    private SequenceStructure buildList(SequenceStructure sequence, Object objectOld, Object objectNew) {
        List<Object> list = joinListValues(sequence, objectOld, objectNew);
        List<String> values = new ArrayList<>();

        for (Object value: list) {
            SequenceStructure valueContainer = new SequenceStructure();
            buildValue(valueContainer, null, value);
            values.add(valueContainer.value);
        }

        sequence.listValue(values);
        return sequence;
    }

    private SequenceStructure buildObject(SequenceStructure sequence, Object objectOld, Object objectNew) {
        String value = build((Map<String, Object>) objectOld, (Map<String, Object>) objectNew);
        sequence.objectValue(value);
        return sequence;
    }

    private SequenceStructure buildRawValue(SequenceStructure sequence, Object objectOld, Object objectNew) {
        if(objectNew instanceof String)
            sequence.rawValueString(objectNew);
        if(objectNew instanceof Boolean)
            sequence.rawValue(objectNew);
        if (ValueParser.instanceOfNumber(objectNew)){
            buildNumericValue(sequence, objectOld, objectNew);
        }
        return sequence;
    }

    private SequenceStructure buildNumericValue(SequenceStructure sequence, Object objectOld, Object objectNew){
        Number difference;
        int multiplier;

        if(objectNew instanceof Double){
            Double doubleOld = objectOld == null ? 0 : ValueParser.parseDouble(objectOld);
            Double doubleNew = ValueParser.parseDouble(objectNew);
            difference = Math.abs(doubleOld - doubleNew);
            multiplier = doubleNew > doubleOld ? -1 : 1;

        } else {
            Integer integerOld = objectOld == null ? 0 : ValueParser.parseInteger(objectOld);
            Integer integerNew = ValueParser.parseInteger(objectNew);
            difference = Math.abs(integerOld - integerNew);
            multiplier = integerNew > integerOld ? -1 : 1;
        }

        sequence.rawValueNumeric(difference, multiplier);
        return sequence;
    }

    private List<Object> joinListValues(SequenceStructure sequence, Object objectOld, Object objectNew){
        List<Object> listOld = sequence.isMutate() ? (List<Object>) objectOld: new ArrayList<>();
        List<Object> listNew = (List<Object>) objectNew;

        return ValueParser.joinListValues(listOld, listNew);
    }

}