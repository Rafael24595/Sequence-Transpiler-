package sequence.objects;

import com.google.gson.JsonElement;

public interface ISequenceObject<T> {

    int evaluate(String input, int position, JsonElement jsonObject );
    void addAttribute(String key, Object value);
    Object getObject();
    void setObject(T value);


}