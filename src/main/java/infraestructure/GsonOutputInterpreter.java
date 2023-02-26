package infraestructure;

import com.google.gson.Gson;

import domain.IOutputInterpeter;

public class GsonOutputInterpreter implements IOutputInterpeter {

    @Override
    public String write(Object output) {
        String jsonStr = new Gson().toJson(output);
        System.out.println(jsonStr);
        return jsonStr;
    }
    
}