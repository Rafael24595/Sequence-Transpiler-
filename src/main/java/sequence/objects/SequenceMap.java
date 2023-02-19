package sequence.objects;

import java.util.HashMap;
import java.util.Map;

public class SequenceMap extends ASequenceObject<HashMap<String, Object>> {

    Map<String, Object> map;

    public SequenceMap(Map<String, Object> map){
        this.map = map;
    }

    @Override
    public void addAttribute(String key, Object value) {
        map.put(key, value);
    }

    @Override
    public Object getObject() {
        return map;
    }

    @Override
    public void setObject(HashMap<String, Object> value) {
        this.map = value;
    }

}