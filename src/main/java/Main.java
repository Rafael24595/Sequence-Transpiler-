import domain.IInputInterpeter;
import domain.IOutputInterpeter;
import domain.sequence.Sequence;
import domain.sequence.sentence.sentence.SentenceMap;
import infraestructure.ConsoleOutputInterpreter;
import infraestructure.GsonInputInterpreter;
import infraestructure.GsonOutputInterpreter;
import test.KInput;
import tools.Sanitizer;
import domain.sequence.builder.SequenceBuilder;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        IInputInterpeter inputInterpreter = new GsonInputInterpreter();
        IOutputInterpeter outputInterpreter = new GsonOutputInterpreter();

        String base = KInput.BASE_TEST_1;
        String sequence = KInput.SEQUENCE_TEST_1;

        compile(base, sequence, inputInterpreter, outputInterpreter);

        outputInterpreter = new ConsoleOutputInterpreter();
        base = KInput.BASE_TEST_2;
        String update = KInput.CHANGE_TEST_1;

        decompile(base, update, inputInterpreter, outputInterpreter);

        System.out.println("Close app...");
    }

    private static void compile(String base, String definition, IInputInterpeter inputInterpreter, IOutputInterpeter outputInterpreter){
        definition = Sanitizer.cleanInput(definition);

        Map<String, Object> updateMap = inputInterpreter.read(base);
        Map<String, Object> baseMap = new HashMap<>(updateMap);

        SentenceMap sentenceBase = new SentenceMap(baseMap);
        SentenceMap sentenceUpdate = new SentenceMap(updateMap);

        Sequence sequence = new Sequence(definition, sentenceBase);
        sequence.build(sentenceUpdate);

        outputInterpreter.write(updateMap);
    }

    private static void decompile(String base, String update, IInputInterpeter inputInterpreter, IOutputInterpeter outputInterpreter){
        Map<String, Object> m1 = inputInterpreter.read(base);
        Map<String, Object> m2 = inputInterpreter.read(update);

        SequenceBuilder sb = new SequenceBuilder(m1, m2);
        sb.enableFormat();
        String sequence = sb.build();

        outputInterpreter.write(sequence);
    }

}