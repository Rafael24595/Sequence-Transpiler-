package sequence.sentence.sentence;

public class SentenceObject extends ASentence<Object> {

    Object object;

    public SentenceObject(java.lang.Object object){
        super();
        this.object = object;
    }

    @Override
    public void addAttribute(String key, Object value) {
        object = value;
    }

    @Override
    public void removeAttribute(String key) {
        object = null;
    }

    @Override
    public Object getAttribute(String key) {
        return object;
    }

    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public void setObject(Object value) {
        this.object = value;
    }

    @Override
    public void merge(ISentence<?> sentenceObject) {
    }

    @Override
    public boolean isNull() {
        return object == null;
    }

}