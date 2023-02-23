package sequence.sentence.sentence;

import java.util.HashMap;
import java.util.Map;

public class SentenceMap extends ASentence<HashMap<String, Object>> {

    Map<String, Object> map;

    public SentenceMap(Map<String, Object> map){
        super();
        this.map = map == null ? new HashMap<>() : map;
    }

    @Override
    public void addAttribute(String key, Object value) {
        if(value != null)
            map.put(key, value);
        else
            map.remove(key);
    }

    @Override
    public void removeAttribute(String key) {
        map.remove(key);
    }

    @Override
    public Object getAttribute(String key) {
        return map.get(key);
    }

    @Override
    public Object getObject() {
        return map;
    }

    @Override
    public void setObject(HashMap<String, Object> value) {
        this.map = value;
    }

    @Override
    public void merge(ISentence<?> sentenceObject) {
        Map<String, Object> receiver;
        try {
            receiver = (Map<String, Object>) sentenceObject.getObject();
            receiver.putAll(map);
        }catch (Exception e){
            throw new IllegalArgumentException("Cannot merge the fields, both must be Map type objects");
        }
        map = receiver;
    }

    @Override
    public boolean isNull() {
        return map == null || map.isEmpty();
    }
    
}