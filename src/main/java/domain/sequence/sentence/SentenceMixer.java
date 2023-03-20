package domain.sequence.sentence;

import static domain.sequence.sentence.KSentence.*;

import domain.sequence.sentence.sentence.ISentence;
import tools.Sanitizer;
import tools.ValueParser;

public class SentenceMixer {
    
    ISentence<?> sentenceReceptor;

    public SentenceMixer(ISentence<?> sentenceReceptor){
        this.sentenceReceptor = sentenceReceptor;
    }

    public void merge(String name, String action, ISentence<?> sentenceUpdated, ISentence<?> sentenceBase) throws IllegalArgumentException {
        switch (action){
            case MUTATE:
                mutateValue(sentenceUpdated, sentenceBase);
                sentenceReceptor.addAttribute(name, sentenceUpdated.getObject());
                break;
            case INCREMENT:
                incrementValue(sentenceUpdated, sentenceBase);
                sentenceReceptor.addAttribute(name, sentenceUpdated.getObject());
                break;
            case DECREMENT:
                decrementValue(sentenceUpdated, sentenceBase);
                sentenceReceptor.addAttribute(name, sentenceUpdated.getObject());
                break;
            case DELETE:
                if(!sentenceReceptor.exists(name))
                    throw new IllegalArgumentException("Field does not exists.");
                sentenceReceptor.removeAttribute(name);
                break;
            default:
                asValue(sentenceUpdated, sentenceBase);
                sentenceReceptor.addAttribute(name, sentenceUpdated.getObject());
                break;
        }
    }

    private static void asValue(ISentence sentenceUpdated, ISentence<?> sentenceBase) {
        Object valueBase = sentenceBase.getObject();
        Object valueObject = sentenceUpdated.getObject();
        Object valueParsed;

        if(ValueParser.isInteger(valueObject)) {
            if(valueBase != null && !ValueParser.instanceOfNumber(valueBase))
                throw new IllegalArgumentException("Cannot merge the fields, both must be Numeric type objects");
            valueParsed = ValueParser.parseInteger(valueObject);
            sentenceUpdated.setObject(valueParsed);
        }
        else if(ValueParser.isDouble(valueObject)){
            if(valueBase != null && !ValueParser.instanceOfNumber(valueBase))
                throw new IllegalArgumentException("Cannot merge the fields, both must be Numeric type objects");
            valueParsed = ValueParser.parseDouble(valueObject);
            sentenceUpdated.setObject(valueParsed);
        }
        else if(ValueParser.isBoolean(valueObject)){
            if(valueBase != null && !ValueParser.instanceOfBoolean(valueBase))
                throw new IllegalArgumentException("Cannot merge the fields, both must be Boolean type objects");
            valueParsed = ValueParser.parseBoolean(valueObject);
            sentenceUpdated.setObject(valueParsed);
        }
        else if(ValueParser.isString(valueObject)){
            if(valueBase != null && !ValueParser.instanceOfString(valueBase))
                throw new IllegalArgumentException("Cannot merge the fields, both must be String type objects");
            valueParsed = Sanitizer.unBrickString((String) valueObject);
            sentenceUpdated.setObject(valueParsed);
        }
    }

    /*private static void asValue(ISentence sentenceUpdated, ISentence<?> sentenceBase) {
        Object valueBase = sentenceBase.getObject();
        Object valueObject = sentenceUpdated.getObject();
        Object valueParsed = null;

        if(ValueParser.isInteger(valueObject))
            valueParsed = asIntegerValue(valueBase, valueObject);
        if(ValueParser.isDouble(valueObject))
            valueParsed = asDoubleValue(valueBase, valueObject);
        if(ValueParser.isBoolean(valueObject))
            valueParsed = asBooleanValue(valueBase, valueObject);
        if(ValueParser.isString(valueObject))
            valueParsed = asStringValue(valueBase, valueObject);

        if(valueParsed != null)
            sentenceUpdated.setObject(valueParsed);
    }*/

    private static Object asIntegerValue(Object valueBase, Object valueObject){
        if(valueBase != null && !ValueParser.instanceOfNumber(valueBase))
            throw new IllegalArgumentException("Cannot merge the fields, both must be Numeric type objects");
        return ValueParser.parseInteger(valueObject);
    }

    private static Object asDoubleValue(Object valueBase, Object valueObject){
        if(valueBase != null && !ValueParser.instanceOfNumber(valueBase))
            throw new IllegalArgumentException("Cannot merge the fields, both must be Numeric type objects");
        return ValueParser.parseDouble(valueObject);
    }

    private static Object asBooleanValue(Object valueBase, Object valueObject){
        if(valueBase != null && !ValueParser.instanceOfBoolean(valueBase))
            throw new IllegalArgumentException("Cannot merge the fields, both must be Boolean type objects");
        return ValueParser.parseBoolean(valueObject);
    }

    private static Object asStringValue(Object valueBase, Object valueObject){
        if(valueBase != null && !ValueParser.instanceOfString(valueBase))
            throw new IllegalArgumentException("Cannot merge the fields, both must be String type objects");
        return Sanitizer.unBrickString((String) valueObject);
    }

    private static void mutateValue(ISentence<?> sentenceUpdated, ISentence<?> sentenceBase) throws IllegalArgumentException {
        if(sentenceBase.isNull())
            throw new IllegalArgumentException("Field does not exists.");
        asValue(sentenceUpdated, sentenceBase);
        sentenceUpdated.merge(sentenceBase);
    }

    private static void decrementValue(ISentence<?> sentenceUpdated, ISentence<?> sentenceBase) throws IllegalArgumentException {
        calculateNumber(sentenceUpdated, sentenceBase, - 1);
    }

    private static void incrementValue(ISentence<?> sentenceUpdated, ISentence<?> sentenceBase) throws IllegalArgumentException {
        calculateNumber(sentenceUpdated, sentenceBase, 1);
    }

    private static void calculateNumber(ISentence sentenceUpdated, ISentence<?> sentenceBase, int multiplier) throws IllegalArgumentException {
        mutateValue(sentenceUpdated, sentenceBase);

        Object updateObject = sentenceUpdated.getObject();
        Object baseObject = sentenceBase.getObject();

        if(!ValueParser.instanceOfNumber(baseObject) || !ValueParser.instanceOfNumber(updateObject))
            throw new IllegalArgumentException("Cannot add up non numeric objects.");

        Object value = null;

        if(ValueParser.isDouble(updateObject))
            value = calculateDouble(updateObject, baseObject, multiplier);
        else if(ValueParser.isNumeric(updateObject)) 
            value = calculateInteger(updateObject, baseObject, multiplier);
        else
            throw new IllegalArgumentException("Cannot merge the fields, both must be Number type objects");

        sentenceUpdated.setObject(value);
    }

    private static Double calculateDouble(Object updateObject, Object baseObject, int multiplier) {
        Double newValue = ValueParser.parseDouble(updateObject);
        Double oldValue = ValueParser.parseDouble(baseObject);
        return oldValue + newValue * multiplier;
    }

    private static Double calculateInteger(Object updateObject, Object baseObject, int multiplier) {
        Integer newValue = ValueParser.parseInteger(updateObject);
        Double oldValue = ValueParser.parseDouble(baseObject);
        return oldValue + newValue * multiplier;
    }

}