package domain;

import domain.sequence.Sequence;
import domain.sequence.sentence.sentence.SentenceMap;
import infraestructure.ConsoleGsonOutputInterpreter;
import infraestructure.GsonInputInterpreter;
import tools.Sanitizer;

import java.util.HashMap;
import java.util.Map;

public class SequenceCompiler {

    String base;
    String sentence;
    IInputInterpeter inputInterpreter;
    IOutputInterpeter outputInterpreter;

    public SequenceCompiler(String base, String sentence) {
        this.base = base;
        this.sentence = sentence;
        this.inputInterpreter = new GsonInputInterpreter();
        this.outputInterpreter = new ConsoleGsonOutputInterpreter();
    }

    public String compile() {
        this.sentence = Sanitizer.cleanInput(this.sentence);

        Map<String, Object> updateMap = this.inputInterpreter.read(this.base);
        Map<String, Object> baseMap = new HashMap<>(updateMap);

        SentenceMap sentenceBase = new SentenceMap(baseMap);
        SentenceMap sentenceUpdate = new SentenceMap(updateMap);

        Sequence sequence = new Sequence(this.sentence, sentenceBase);
        sequence.build(sentenceUpdate);

        return this.outputInterpreter.write(updateMap);
    }

}