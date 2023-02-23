package domain.sequence.sentence.sentence;

import java.util.ArrayList;
import java.util.List;

public class SentenceList extends ASentence<List<Object>> {

    List<Object> list;

    public SentenceList(List<Object> list) {
        super();
        this.list = list == null ? new ArrayList<>() : list;
    }

    @Override
    public void addAttribute(String key, Object value) {
        if(value != null)
            list.add(value);
    }

    @Override
    public void removeAttribute(String key) {
    }

    @Override
    public Object getAttribute(String key) {
        return null;
    }

    @Override
    public Object getObject() {
        return list;
    }

    @Override
    public void setObject(List<Object> value) {
        this.list = value;
    }

    @Override
    public void merge(ISentence<?> sentenceObject) {
        List<Object> receiver;
        try {
            receiver = (List<Object>) sentenceObject.getObject();
            receiver.addAll(list);
        }catch (Exception e){
            throw new IllegalArgumentException("Cannot merge the fields, both must be List type objects");
        }
        list = receiver;
    }

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }

}