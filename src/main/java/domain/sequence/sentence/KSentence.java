package domain.sequence.sentence;

public class KSentence {

    private KSentence(){
    }

    public static final String PREFIX_KEY = "$";
    public static final String SEPARATOR = "\s";

    public static final String FIELD = "FIELD";
    public static final String OBJECT = "OBJECT";
    public static final String LIST = "LIST";
    public static final String CLOSE = "CLOSE";
    public static final String MUTATE = "MUTATE";

    public static final String DELETE = "DELETE";

    public static final String AS = "AS";
    public static final String INCREMENT = "INCREMENT";
    public static final String DECREMENT = "DECREMENT";

}