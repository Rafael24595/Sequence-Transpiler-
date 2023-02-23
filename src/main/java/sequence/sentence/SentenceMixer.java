package sequence.sentence;

import tools.Sanitizer;
import tools.ValueParser;

import static sequence.sentence.KSentence.*;

import sequence.sentence.sentence.ISentence;

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
                sentenceReceptor.removeAttribute(name);
                break;
            default:
                asValue(sentenceUpdated, sentenceBase);
                sentenceReceptor.addAttribute(name, sentenceUpdated.getObject());
                break;
        }
    }

    private static void asValue(ISentence sentenceUpdated, ISentence<?> sentenceBase) {
        Object valueObject = sentenceUpdated.getObject();
        Object valueParsed;

        if(ValueParser.isInteger(valueObject)) {
            valueParsed = ValueParser.parseInteger(valueObject);
            sentenceUpdated.setObject(valueParsed);
        }
        else if(ValueParser.isDouble(valueObject)){
            valueParsed = ValueParser.parseDouble(valueObject);
            sentenceUpdated.setObject(valueParsed);
        }
        else if(ValueParser.isBoolean(valueObject)){
            valueParsed = ValueParser.parseBoolean(valueObject);
            sentenceUpdated.setObject(valueParsed);
        }
        else if(ValueParser.isString(valueObject)){
            valueParsed = Sanitizer.unBrickString((String) valueObject);
            sentenceUpdated.setObject(valueParsed);
        }
    }

    private static void mutateValue(ISentence<?> sentenceUpdated, ISentence<?> sentenceBase) throws IllegalArgumentException {
        if(sentenceBase == null)
            throw new IllegalArgumentException("Field does not exists");
        sentenceUpdated.merge(sentenceBase);
        asValue(sentenceUpdated, sentenceBase);
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

    private static Integer calculateInteger(Object updateObject, Object baseObject, int multiplier) {
        Integer newValue = ValueParser.parseInteger(updateObject);
        Integer oldValue = ValueParser.parseInteger(baseObject);
        return oldValue + newValue * multiplier;
    }

}