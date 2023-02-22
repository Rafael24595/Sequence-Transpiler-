package sequence.sentence;

import tools.ValueParser;

import static sequence.sentence.KSentence.*;

public class SentenceMixer {
    
    ISentence<?> sentenceReceptor;

    public SentenceMixer(ISentence<?> sentenceReceptor){
        this.sentenceReceptor = sentenceReceptor;
    }

    public void merge(String name, String action, ISentence<?> sentenceUpdated, ISentence<?> sentenceBase) throws IllegalArgumentException {
        switch (action){
            case AS:
                asValue(sentenceUpdated, sentenceBase);
                sentenceReceptor.addAttribute(name, sentenceUpdated.getObject());
                break;
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
                sentenceReceptor.addAttribute(name, sentenceUpdated.getObject());
                break;
        }
    }

    private static void asValue(ISentence<?> sentenceUpdated, ISentence<?> sentenceBase) {
    }

    private static void mutateValue(ISentence<?> sentenceUpdated, ISentence<?> sentenceBase) throws IllegalArgumentException {
        if(sentenceBase == null)
            throw new IllegalArgumentException("Field does not exists");
        sentenceUpdated.merge(sentenceBase);
        asValue(sentenceUpdated, sentenceBase);
    }

    private static void decrementValue(ISentence<?> sentenceUpdated, ISentence<?> sentenceBase) throws IllegalArgumentException {
        calculateValue(sentenceUpdated, sentenceBase, - 1);
    }

    private static void incrementValue(ISentence<?> sentenceUpdated, ISentence<?> sentenceBase) throws IllegalArgumentException {
        calculateValue(sentenceUpdated, sentenceBase, 1);
    }

    private static void calculateValue(ISentence sentenceUpdated, ISentence<?> sentenceBase, int multiplier) throws IllegalArgumentException {
        mutateValue(sentenceUpdated, sentenceBase);

        Object valueObject = sentenceUpdated.getObject();
        Object baseObject = sentenceBase.getObject();

        Object value = null;

        if(ValueParser.isDouble(valueObject))
            value = calculateDouble(valueObject, baseObject, multiplier);
        else if(ValueParser.isNumeric(valueObject)) 
            value = calculateInteger(valueObject, baseObject, multiplier);
        else
            throw new IllegalArgumentException("Cannot merge the fields, both must be Number type objects");

        sentenceUpdated.setObject(value);
    }

    private static Double calculateDouble(Object valueObject, Object baseObject, int multiplier){
        Double newValue = ValueParser.parseDouble(valueObject);
        Double oldValue = ValueParser.parseDouble(baseObject);
        return oldValue + newValue * multiplier;
    }

    private static Integer calculateInteger(Object valueObject, Object baseObject, int multiplier){
        Integer newValue = ValueParser.parseInteger(valueObject);
        Integer oldValue = ValueParser.parseInteger(baseObject);
        return oldValue + newValue * multiplier;
    }

}
