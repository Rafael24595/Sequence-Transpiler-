package sequence.objects;

import java.util.List;

public class SequenceList extends ASequenceObject<List<Object>> {

    List<Object> list;

    public SequenceList(List<Object> list){
        this.list = list;
    }

    @Override
    public void addAttribute(String key, Object value) {
        list.add(value);
    }

    @Override
    public Object getObject() {
        return list;
    }

    @Override
    public void setObject(List<Object> value) {
        this.list = value;
    }

}