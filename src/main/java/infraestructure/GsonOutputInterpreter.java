package infraestructure;

import com.google.gson.Gson;

import domain.IOutputInterpeter;

public class GsonOutputInterpreter implements IOutputInterpeter {

    @Override
    public String write(Object output) {
        return new Gson().toJson(output);
    }
    
}