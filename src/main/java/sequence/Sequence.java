package sequence;

import sequence.sentence.ISentence;

import static sequence.sentence.KSentence.*;

public class Sequence {

    private final String input;
    private final ISentence<?> sentenceBase;

    public Sequence(String input, ISentence<?> sentence){
        this.input = input;
        this.sentenceBase = sentence;
    }

    public void build(ISentence<?> sentence) {
        String[] commands = input.split(SEPARATOR);
        int pointer = 0;

        while(pointer < commands.length){
            pointer = sentence.evaluate(input, pointer, sentenceBase);
            pointer++;
        }
    }

}