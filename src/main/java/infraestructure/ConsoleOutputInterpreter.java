package infraestructure;

import domain.IOutputInterpeter;

public class ConsoleOutputInterpreter implements IOutputInterpeter {

    @Override
    public String write(Object output) {
        System.out.println(output);
        return output.toString();
    }
    
}