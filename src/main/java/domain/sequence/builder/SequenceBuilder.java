package domain.sequence.builder;

import tools.ValueParser;

import java.util.*;

public class SequenceBuilder {

    private Map<String, Object> oldStructure;
    private Map<String, Object> newStructure;

    private int level;

    private boolean format;

    public SequenceBuilder(Map<String, Object> oldStructure, Map<String, Object> newStructure){
        this(oldStructure, newStructure, 0);
    }

    public SequenceBuilder(Map<String, Object> oldStructure, Map<String, Object> newStructure, int level){
        this.oldStructure = oldStructure;
        this.newStructure = newStructure;
        this.level = level;

        disableFormat();
    }

    public void enableFormat() {
        this.format = true;
    }

    public void disableFormat() {
        this.format = false;
    }

    private void setFormat(boolean format) {
        this.format = format;
    }

    public String build(){
        StringBuilder sb = new StringBuilder();

        for (String key: ValueParser.joinMapKeys(oldStructure, newStructure)) {
            SequenceStructure sequence = new SequenceStructure(key);
            sequence.format = format;
            sequence.setLevel(level);

            if(newStructure.containsKey(key))
                persist(sequence);
            else
                sequence.delete();

            sb.append(sequence.build());
        }

        return sb.toString();
    }

    private void persist(SequenceStructure sequence) {
        if(oldStructure != null && oldStructure.containsKey(sequence.name))
            persistAsModify(sequence);
        else
            persistAsNew(sequence);
    }

    private void persistAsModify(SequenceStructure sequence){
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

    private void persistAsNew(SequenceStructure sequence){
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
            SequenceStructure sequenceChild = new SequenceStructure();
            sequenceChild.format = format;
            sequenceChild.setLevel(sequence.level + 1);

            buildValue(sequenceChild, null, value);
            values.add(sequenceChild.value);
        }

        sequence.listValue(values);
        return sequence;
    }

    private SequenceStructure buildObject(SequenceStructure sequence, Object objectOld, Object objectNew) {
        SequenceBuilder sb = new SequenceBuilder((Map<String, Object>) objectOld, (Map<String, Object>) objectNew, sequence.level + 1);
        sb.setFormat(format);
        String value = sb.build();
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