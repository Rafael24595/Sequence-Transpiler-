package sequence.objects;

import static sequence.KSequence.*;

import com.google.gson.JsonElement;
import sequence.Sequence;
import tools.FieldMixer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ASequenceObject<T> implements ISequenceObject<T> {

    public int evaluate(String input, int position, JsonElement jsonObject) {
        String command = getNext(input, position);
        String name = "";
        ISequenceObject<?> value = new SequenceObject(command) ;

        if(command.equals(FIELD)){
            int keyPosition = position + 1;
            int actionPosition = keyPosition + 1;
            int typePosition = actionPosition + 1;

            name = getNext(input, keyPosition);
            String action = getNext(input, actionPosition);
            String type = getNext(input, typePosition);

            JsonElement jsonChild = (jsonObject != null) ? jsonObject.getAsJsonObject().get(name) : null;

            switch (type){
                case OBJECT:
                    value = new SequenceMap(new HashMap<>());
                    position = buildComplexField(input, position, value, jsonChild);
                    break;
                case LIST:
                    value = new SequenceList(new ArrayList<>());
                    position = buildComplexField(input, position, value, jsonChild);
                    break;
                default:
                    value = new SequenceObject(type);
                    position = typePosition;
                    break;
            }

            FieldMixer.merge(action, jsonChild, value);
        }

        addAttribute(name, value.getObject());

        return position;
    }

    private int buildComplexField(String input, int position, ISequenceObject<?> e, JsonElement jsonChild) {
        int keyPosition = position + 1;
        int actionPosition = keyPosition + 1;
        int typePosition = actionPosition + 1;

        int closerClosePosition = findNextReference(input, keyPosition, CLOSE);
        String fragmentPreview = splitFragment(input, typePosition, closerClosePosition);

        int correctedClosePosition = correctClosePosition(input, fragmentPreview, closerClosePosition);
        String fragment = splitFragment(input, typePosition, correctedClosePosition);

        Sequence s = new Sequence(fragment, jsonChild);
        s.build(e);

        return correctedClosePosition - 1;
    }

    private int correctClosePosition(String input, String fragmentPreview, int closePosition){
        Pattern p = Pattern.compile(OBJECT + "|" + LIST);
        Matcher m = p.matcher(fragmentPreview);

        while (m.find())
            closePosition = findNextReference(input, closePosition, CLOSE);

        return closePosition;
    }

    protected String getNext(String input, int position){
        String[] startCommands = input.split(SEPARATOR, position + 1);
        String[] endCommands = startCommands[position].split(SEPARATOR, position + 2);
        return endCommands[0];
    }

    protected int findNextReference(String text, int base, String reference){
        String[] startCommands = text.split(SEPARATOR, base);
        String endFragment = startCommands[startCommands.length - 1];
        String middleFragment = endFragment.split(reference, 2)[0];
        String[] middleCommands = middleFragment.split(SEPARATOR);
        return startCommands.length + middleCommands.length;
    }

    protected String splitFragment(String text, int startPosition, int endPosition){
        String[] startCommands = text.split(SEPARATOR, endPosition);
        List<String> list = new ArrayList<>(Arrays.asList(startCommands));
        list = list.subList(startPosition + 1, list.size() - 1);
        return String.join(SEPARATOR, list);
    }

}