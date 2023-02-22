package sequence.sentence;

import static sequence.sentence.KSentence.*;

import sequence.Sequence;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ASentence<T> implements ISentence<T> {

    protected SentenceMixer mixer;

    protected ASentence() {
        this.mixer = new SentenceMixer(this);
    }

    public int evaluate(String input, int pointer, ISentence<?> sentenceBase) {
        String name = getNameField(input, pointer);
        String action = getActionField(input, pointer);

        ISentence<?> sentenceChildBase = buildSentenceBase(input, pointer, sentenceBase);
        ISentence<?> sentenceChildUpdated = buildSentenceUpdate(input, pointer, sentenceChildBase);

        mixer.merge(name, action, sentenceChildUpdated, sentenceChildBase);

        return calculatePointer(input, pointer);
    }

    protected String getCommandField(String input, int pointer){
        String[] startCommands = input.split(SEPARATOR, pointer + 1);
        String[] endCommands = startCommands[pointer].split(SEPARATOR, pointer + 2);
        return endCommands[0];
    }

    private String getNameField(String input, int pointer){
        String command = getCommandField(input, pointer);
        String name = "";

        if(!isRawValue(command)){
            int keyPointer = pointer + 1;
            name = getCommandField(input, keyPointer);
        }

        return name;
    }

    private String getActionField(String input, int pointer){
        String command = getCommandField(input, pointer);
        String action = "";

        if(!isRawValue(command)){
            int keyPointer = pointer + 1;
            int actionPointer = keyPointer + 1;
            action = getCommandField(input, actionPointer);
        }

        return action;
    }

    private String getTypeField(String input, int pointer){
        String command = getCommandField(input, pointer);
        String type = "";

        if(!isRawValue(command)){
            int keyPointer = pointer + 1;
            int actionPointer = keyPointer + 1;
            int typePointer = actionPointer + 1;
            type = getCommandField(input, typePointer);
        }

        return type;
    }

    private ISentence<?> buildSentenceUpdate(String input, int pointer, ISentence<?> sentenceBase){
        String command = getCommandField(input, pointer);
        String type = getTypeField(input, pointer);
        
        ISentence<?> sentenceUpdated = new SentenceObject(command);

        if(!isRawValue(command)){

            switch (type){
                case OBJECT:
                    sentenceUpdated = new SentenceMap(new HashMap<>());
                    buildInnerSequence(input, pointer, sentenceUpdated, sentenceBase);
                    break;
                case LIST:
                    sentenceUpdated = new SentenceList(new ArrayList<>());
                    buildInnerSequence(input, pointer, sentenceUpdated, sentenceBase);
                    break;
                default:
                    sentenceUpdated = new SentenceObject(type);
                    break;
            }
        } 
        
        return sentenceUpdated;
    }

    private ISentence<?> buildSentenceBase(String input, int pointer, ISentence<?> sentenceBase) {
        String command = getCommandField(input, pointer);
        String name = getNameField(input, pointer);
        String type = getTypeField(input, pointer);

        if(!isRawValue(command)){
            Object baseObjectChild = sentenceBase.getAttribute(name);

            switch (type){
                case OBJECT:
                    try {
                        return new SentenceMap((Map<String, Object>) baseObjectChild);
                    } catch (Exception e) {
                        throw new ClassCastException("Format error, Map type structure required, but \"" + baseObjectChild.getClass() + "\" found");
                    }
                case LIST:
                    try {
                        return new SentenceList((List<Object>) baseObjectChild);
                    } catch (Exception e) {
                        throw new ClassCastException("Format error, List type structure required, but \"" + baseObjectChild.getClass() + "\" found");
                    }
                default:
                    return new SentenceObject(baseObjectChild);
            }
        } else {
            return new SentenceObject("");
        }
    }

    

    private void buildInnerSequence(String input, int position, ISentence<?> sentenceUpdated, ISentence<?> sentenceBase) {
        int keyPosition = position + 1;
        int actionPosition = keyPosition + 1;
        int typePosition = actionPosition + 1;

        int closePointer = calculateComplexPointer(input, position);
        String fragment = splitFragment(input, typePosition, closePointer);

        Sequence sequence = new Sequence(fragment, sentenceBase);
        sequence.build(sentenceUpdated);
    }

    private int calculatePointer(String input, int pointer) {
        int keyPointer = pointer + 1;
        int actionPointer = keyPointer + 1;
        int typePointer = actionPointer + 1;

        String action = getActionField(input, pointer);
        String type = getTypeField(input, pointer);

        String command = getCommandField(input, pointer);

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