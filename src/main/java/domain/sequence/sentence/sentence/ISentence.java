package domain.sequence.sentence.sentence;

public interface ISentence<T> {
    int evaluate(String input, int position, ISentence<?> object);
    Object getObject();
    void setObject(T value);
    Object getAttribute(String key);
    void addAttribute(String key, Object value);
    void removeAttribute(String key);
    boolean exists(String key);
    void merge(ISentence<?> object);
    boolean isNull();
}