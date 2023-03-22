package domain;

import domain.sequence.builder.SequenceBuilder;
import infraestructure.ConsoleOutputInterpreter;
import infraestructure.GsonInputInterpreter;

import java.util.Map;

public class SequenceDecompiler {

    String base;
    String update;
    boolean format;
    IInputInterpeter inputInterpreter;
    IOutputInterpeter outputInterpreter;

    public SequenceDecompiler(String base, String update) {
        this(base, update, true);
    }

    public SequenceDecompiler(String base, String update, boolean format) {
        this.base = base;
        this.update = update;
        this.format = format;
        this.inputInterpreter = new GsonInputInterpreter();
        this.outputInterpreter = new ConsoleOutputInterpreter();
    }

    public String decompile(){
        Map<String, Object> m1 = this.inputInterpreter.read(this.base);
        Map<String, Object> m2 = this.inputInterpreter.read(this.update);

        SequenceBuilder sb = new SequenceBuilder(m1, m2);
        sb.setFormat(this.format);
        String sequence = sb.build();

        return this.outputInterpreter.write(sequence);
    }

}