package sequence.objects;

public class SequenceObject extends ASequenceObject<Object> {

    Object object;

    public SequenceObject(java.lang.Object object){
        this.object = object;
    }

    @Override
    public void addAttribute(String key, Object value) {
        object = value;
    }

    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public void setObject(Object value) {
        this.object = value;
    }

}