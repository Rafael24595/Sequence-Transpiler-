package domain.sequence.sentence.sentence;

import static domain.sequence.sentence.KSentence.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import domain.sequence.Sequence;
import domain.sequence.sentence.SentenceMixer;

public abstract class ASentence<T> implements ISentence<T> {

    private static final int NAME_POINTER_CORRECTION = 1;
    private static final int ACTION_POINTER_CORRECTION = 2;
    private static final int TYPE_POINTER_CORRECTION = 3;
    private static final int CLOSE_TAG_POINTER_CORRECTION = 1;

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
        if (pointer < startCommands.length){
            String[] endCommands = startCommands[pointer].split(SEPARATOR, pointer + 2);
            return endCommands[0];
        }

        return "";
    }

    private String getNameField(String input, int pointer){
        String command = getCommandField(input, pointer);

        if(!isRawData(command)){
            int keyPointer = pointer + NAME_POINTER_CORRECTION;
            return getCommandField(input, keyPointer);
        }

        return "";
    }

    private String getActionField(String input, int pointer){
        String command = getCommandField(input, pointer);

        if(!isRawData(command)){
            int actionPointer = pointer + ACTION_POINTER_CORRECTION;
            return getCommandField(input, actionPointer);
        }

        return "";
    }

    private String getTypeField(String input, int pointer){
        String command = getCommandField(input, pointer);

        if(!isRawData(command)){
            int typePointer = pointer + TYPE_POINTER_CORRECTION;
            return getCommandField(input, typePointer);
        }

        return "";
    }

    private ISentence<?> buildSentenceUpdate(String input, int pointer, ISentence<?> sentenceBase){
        String command = getCommandField(input, pointer);
        String type = getTypeField(input, pointer);
        
        String field = isRawData(command) ? command : type;
        int fixedPointer = isRawData(command) ? pointer - TYPE_POINTER_CORRECTION : pointer;

        return fillBuildSentenceUpdate(field, input, fixedPointer, sentenceBase);
    }

    private ISentence<?> fillBuildSentenceUpdate(String field, String input, int pointer, ISentence<?> sentenceBase) {
        ISentence<?> sentenceUpdated;

        switch (field){
            case OBJECT:
                sentenceUpdated = new SentenceMap(new HashMap<>());
                buildInnerSequence(input, pointer, sentenceUpdated, sentenceBase);
                break;
            case LIST:
                sentenceUpdated = new SentenceList(new ArrayList<>());
                buildInnerSequence(input, pointer, sentenceUpdated, sentenceBase);
                break;
            default:
                sentenceUpdated = new SentenceObject(field);
                break;
        }
        return sentenceUpdated;
    }

    private ISentence<?> buildSentenceBase(String input, int pointer, ISentence<?> sentenceBase) {
        String command = getCommandField(input, pointer);
        String name = getNameField(input, pointer);
        String type = getTypeField(input, pointer);

        String field = isRawData(command) ? command : type;

        return fillBuildSentenceBase(field, name, sentenceBase);
    }

    private ISentence<?> fillBuildSentenceBase(String field,  String name, ISentence<?> sentenceBase){
        Object baseObjectChild = sentenceBase.getAttribute(name);

            switch (field){
                case OBJECT:
                    try {
                        return new SentenceMap((Map<String, Object>) baseObjectChild);
                    } catch (Exception e) {
                        throw new ClassCastException("Format error, Map type structure required.");
                    }
                case LIST:
                    try {
                        return new SentenceList((List<Object>) baseObjectChild);
                    } catch (Exception e) {
                        throw new ClassCastException("Format error, List type structure required.");
                    }
                default:
                    return new SentenceObject(baseObjectChild);
            }
    }

    private void buildInnerSequence(String input, int pointer, ISentence<?> sentenceUpdated, ISentence<?> sentenceBase) {
        int typePosition = pointer + TYPE_POINTER_CORRECTION;

        int closePointer = calculateComplexPointer(input, pointer);
        String fragment = splitFragment(input, typePosition, closePointer);

        Sequence sequence = new Sequence(fragment, sentenceBase);
        sequence.build(sentenceUpdated);
    }

    private int calculatePointer(String input, int pointer) {
        int actionPointer = pointer + ACTION_POINTER_CORRECTION;
        int typePointer = pointer + TYPE_POINTER_CORRECTION;

        String action = getActionField(input, pointer);
        String type = getTypeField(input, pointer);

        String command = getCommandField(input, pointer);

        if(isRawData(command)){
            return isComplexField(command) ? calculateComplexPointerAcurate(input, pointer) : pointer;
        }
        if(isDeclarationField(action)){
            return actionPointer;
        }
        if(isComplexField(type)){
            return calculateComplexPointerAcurate(input, pointer);
        }

        return typePointer;
    }

    private boolean isRawData(String command){
        return !command.equals(FIELD);
    }

    private boolean isDeclarationField(String action){
        return action.equals(DELETE);
    }

    private boolean isComplexField(String type){
        return type.equals(OBJECT) || type.equals(LIST);
    }

    private int calculateComplexPointerAcurate(String input, int pointer){
        return calculateComplexPointer(input, pointer) - CLOSE_TAG_POINTER_CORRECTION;
    }

    private int calculateComplexPointer(String input, int pointer){
        int keyPointer = pointer + NAME_POINTER_CORRECTION;
        int typePointer = pointer + TYPE_POINTER_CORRECTION;

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