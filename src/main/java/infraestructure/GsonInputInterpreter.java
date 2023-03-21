package infraestructure;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import domain.IInputInterpeter;

public class GsonInputInterpreter implements IInputInterpeter {

    @Override
    public Map<String, Object> read(String input) {
        return new GsonBuilder()
                .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .create()
                .fromJson(input, HashMap.class);
    }
    
}