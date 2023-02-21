package sequence.sentence;

import static sequence.sentence.KSentence.*;

import sequence.Sequence;
import tools.ValueParser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ASentence<T> implements ISentence<T> {

    public int evaluate(String input, int pointer, ISentence<?> sentenceBase) {
        String name = getName(input, pointer);
        String action = getAction(input, pointer);

        ISentence<?> sentenceChildBase = buildSentenceBase(input, pointer, sentenceBase);
        ISentence<?> sentenceChildUpdated = buildSentenceUpdate(input, pointer, sentenceChildBase);

        mergeValues(name, action, sentenceChildUpdated, sentenceChildBase);

        return calculatePointer(input, pointer);
    }

    private ISentence<?> buildSentenceUpdate(String input, int pointer, ISentence<?> sentenceChildBase){
        String command = getCommand(input, pointer);
        String type = getType(input, pointer);
        
        ISentence<?> sentenceChildUpdated = new SentenceObject(command);

        if(!isRawValue(command)){

            switch (type){
                case OBJECT:
                    sentenceChildUpdated = new SentenceMap(new HashMap<>());
                    buildComplexField(input, pointer, sentenceChildUpdated, sentenceChildBase);
                    break;
                case LIST:
                    sentenceChildUpdated = new SentenceList(new ArrayList<>());
                    buildComplexField(input, pointer, sentenceChildUpdated, sentenceChildBase);
                    break;
                default:
                    sentenceChildUpdated = new SentenceObject(type);
                    break;
            }
        } 
        
        return sentenceChildUpdated;
    }

    private ISentence<?> buildSentenceBase(String input, int pointer, ISentence<?> sentenceBase) {
        String command = getCommand(input, pointer);
        String name = getName(input, pointer);
        String type = getType(input, pointer);

        if(!isRawValue(command)){
            Object baseObjectChild = sentenceBase.getAttribute(name);

            switch (type){
                case OBJECT:
                    return new SentenceMap((Map<String, Object>) baseObjectChild);
                case LIST:
                    return new SentenceList((List<Object>) baseObjectChild);
                default:
                    return new SentenceObject(baseObjectChild);
            }
        } else {
            return new SentenceObject("");
        }
    }

    private String getName(String input, int pointer){
        String command = getCommand(input, pointer);
        String name = "";

        if(!isRawValue(command)){
            int keyPointer = pointer + 1;
            name = getCommand(input, keyPointer);
        }

        return name;
    }

    private String getAction(String input, int pointer){
        String command = getCommand(input, pointer);
        String action = "";

        if(!isRawValue(command)){
            int keyPointer = pointer + 1;
            int actionPointer = keyPointer + 1;
            action = getCommand(input, actionPointer);
        }

        return action;
    }

    private String getType(String input, int pointer){
        String command = getCommand(input, pointer);
        String type = "";

        if(!isRawValue(command)){
            int keyPointer = pointer + 1;
            int actionPointer = keyPointer + 1;
            int typePointer = actionPointer + 1;
            type = getCommand(input, typePointer);
        }

        return type;
    }

    private void mergeValues(String name, String action, ISentence<?> sentenceUpdated, ISentence<?> sentenceBase) throws IllegalArgumentException {
        switch (action){
            case AS:
                asValue(sentenceUpdated, sentenceBase);
                addAttribute(name, sentenceUpdated.getObject());
                break;
            case MUTATE:
                mutateValue(sentenceUpdated, sentenceBase);
                addAttribute(name, sentenceUpdated.getObject());
                break;
            case INCREMENT:
                incrementValue(sentenceUpdated, sentenceBase);
                addAttribute(name, sentenceUpdated.getObject());
                break;
            case DECREMENT:
                decrementValue(sentenceUpdated, sentenceBase);
                addAttribute(name, sentenceUpdated.getObject());
                break;
            case DELETE:
                removeAttribute(name);
                break;
            default:
                addAttribute(name, sentenceUpdated.getObject());
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

        if(ValueParser.isDouble(valueObject)) {
            Double newValue = ValueParser.parseDouble(valueObject);
            Double oldValue = ValueParser.parseDouble(baseObject);
            sentenceUpdated.setObject(oldValue + newValue * multiplier);
        } else if(ValueParser.isNumeric(valueObject)) {
            Integer newValue = ValueParser.parseInteger(valueObject);
            Integer oldValue = ValueParser.parseInteger(baseObject);
            sentenceUpdated.setObject(oldValue + newValue * multiplier);
        } else{
            throw new IllegalArgumentException("Cannot merge the fields, both must be Number type objects");
        }
    }

    private void buildComplexField(String input, int position, ISentence<?> sentenceUpdated, ISentence<?> sentenceBase) {
        int keyPosition = position + 1;
        int actionPosition = keyPosition + 1;
        int typePosition = actionPosition + 1;

        int closePointer = calculateComplexPointer(input, position);
        String fragment = splitFragment(input, typePosition, closePointer);

        Sequence s = new Sequence(fragment, sentenceBase);
        s.build(sentenceUpdated);
    }

    private int calculatePointer(String input, int pointer) {
        int keyPointer = pointer + 1;
        int actionPointer = keyPointer + 1;
        int typePointer = actionPointer + 1;

        String action = getAction(input, pointer);
        String type = getType(input, pointer);

        String command = getCommand(input, pointer);

        if(isRawValue(command)){
            return pointer;
        }
        if(isDeclarationField(action)){
            return actionPointer;
        }
        if(isComplexField(type)){
            return calculateComplexPointer(input, pointer) -1;
        }

        return typePointer;
    }

    private boolean isRawValue(String command){
        return !command.equals(FIELD);
    }

    private boolean isDeclarationField(String action){
        return action.equals(DELETE);
    }

    private boolean isComplexField(String type){
        return type.equals(OBJECT) || type.equals(LIST);
    }

    private int calculateComplexPointer(String input, int pointer){
        int keyPointer = pointer + 1;
        int actionPointer = keyPointer + 1;
        int typePointer = actionPointer + 1;

        int closerClosePosition = findNextReference(input, keyPointer, CLOSE);
        String fragmentPreview = splitFragment(input, typePointer, closerClosePosition);

        return correctClosePosition(input, fragmentPreview, closerClosePosition);
    }

    private int correctClosePosition(String input, String fragmentPreview, int closePosition){
        Pattern p = Pattern.compile(OBJECT + "|" + LIST);
        Matcher m = p.matcher(fragmentPreview);

        while (m.find())
            closePosition = findNextReference(input, closePosition, CLOSE);

        return closePosition;
    }

    protected String getCommand(String input, int pointer){
        String[] startCommands = input.split(SEPARATOR, pointer + 1);
        String[] endCommands = startCommands[pointer].split(SEPARATOR, pointer + 2);
        return endCommands[0];
    }

    protected int findNextReference(String input, int pointer, String reference){
        String[] startCommands = input.split(SEPARATOR, pointer);
        String endFragment = startCommands[startCommands.length - 1];
        String middleFragment = endFragment.split(reference, 2)[0];
        String[] middleCommands = middleFragment.split(SEPARATOR);
        return startCommands.length + middleCommands.length;
    }

    protected String splitFragment(String input, int startPointer, int endPointer){
        String[] startCommands = input.split(SEPARATOR, endPointer);
        List<String> list = new ArrayList<>(Arrays.asList(startCommands));
        list = list.subList(startPointer + 1, list.size() - 1);
        return String.join(SEPARATOR, list);
    }

}