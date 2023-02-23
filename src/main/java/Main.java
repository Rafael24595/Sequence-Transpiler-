import domain.IInputInterpeter;
import domain.IOutputInterpeter;
import domain.sequence.Sequence;
import domain.sequence.sentence.sentence.SentenceMap;
import infraestructure.GsonInputInterpeter;
import infraestructure.GsonOutputInterpeter;
import test.KInput;
import tools.Sanitizer;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        IInputInterpeter inputInterpeter = new GsonInputInterpeter();
        IOutputInterpeter outputInterpeter = new GsonOutputInterpeter();

        String base = KInput.BASE;
        String sequence = KInput.SEQUENCE;
        
        run(base, sequence, inputInterpeter, outputInterpeter);
    }

    private static void run(String baseInput, String sequenceDefinition, IInputInterpeter inputInterpeter, IOutputInterpeter outputInterpeter){
        sequenceDefinition = Sanitizer.cleanInput(sequenceDefinition);

        Map<String, Object> updateMap = inputInterpeter.read(baseInput);
        Map<String, Object> baseMap = new HashMap<>(updateMap);

        SentenceMap sentenceBase = new SentenceMap(baseMap);
        SentenceMap sentenceUpdate = new SentenceMap(updateMap);

        Sequence sequence = new Sequence(sequenceDefinition, sentenceBase);
        sequence.build(sentenceUpdate);

        outputInterpeter.write(updateMap);
  
        System.out.println("Close app...");
    }

}