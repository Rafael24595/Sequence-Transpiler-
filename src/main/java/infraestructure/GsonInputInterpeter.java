package infraestructure;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import domain.IInputInterpeter;

public class GsonInputInterpeter implements IInputInterpeter {

    @Override
    public Map<String, Object> read(String input) {
        return new Gson().fromJson(input, HashMap.class);
    }
    
}