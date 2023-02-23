package domain.sequence;

import static domain.sequence.sentence.KSentence.*;

import domain.sequence.sentence.sentence.ISentence;

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