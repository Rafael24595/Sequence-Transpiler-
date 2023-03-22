package infraestructure;

import com.google.gson.Gson;

import domain.IOutputInterpeter;

public class ConsoleGsonOutputInterpreter implements IOutputInterpeter {

    @Override
    public String write(Object output) {
        String json = new Gson().toJson(output);
        System.out.println(json);
        return json;
    }
    
}