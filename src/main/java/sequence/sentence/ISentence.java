package sequence.sentence;

public interface ISentence<T> {

    int evaluate(String input, int position, ISentence<?> object);
    void addAttribute(String key, Object value);
    void removeAttribute(String key);
    Object getAttribute(String key);
    Object getObject();
    void setObject(T value);

    void merge(ISentence<?> object);

}