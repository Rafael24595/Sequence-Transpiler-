package infraestructure;

import java.util.Map;

import com.google.gson.Gson;

import domain.IOutputInterpeter;

public class GsonOutputInterpeter implements IOutputInterpeter {

    @Override
    public String write(Map<String, Object> output) {
        String jsonStr = new Gson().toJson(output);
        System.out.println(jsonStr);
        return jsonStr;
    }
    
}