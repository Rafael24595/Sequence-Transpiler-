package domain.sequence.builder;

import java.util.List;

import static domain.sequence.sentence.KSentence.*;

public class SequenceStructure {

    int level;
    boolean format;

    String field;
    String name;
    String action;
    String value;

    public SequenceStructure(String name) {
        this();
        this.field = FIELD;
        this.name = name;
    }

    public SequenceStructure() {
        this.field = FIELD;
        this.name = "";
        this.action = "";
        this.value = "";

        this.format = false;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isMutate() {
        return this.action.equals(MUTATE);
    }

    public void clean() {
        this.field = "";
        this.name = "";
        this.action = "";
        this.value = "";
    }

    public void as(String value) {
        this.as();
        this.value = value;
    }

    public void as() {
        this.action = AS;
    }

    public void mutate(String value) {
        this.mutate();
        this.value = value;
    }

    public void mutate() {
        this.action = MUTATE;
    }

    public void delete() {
        this.action = DELETE;
        this.value = "";
    }

    public void increment(Number number) {
        this.action = INCREMENT;
        this.value = number.toString();
    }

    public void decrement(Number number) {
        this.action = DECREMENT;
        this.value = number.toString();
    }

    public void rawValue(Object value) {
        this.value = value.toString();
    }

    public void rawValueString(Object value) {
        this.value = "\"" + value + "\"";
    }

    public void rawValueNumeric(Number number, int multiplier) {
        if(multiplier < 0)
            increment(number);
        else
            decrement(number);
    }

    public void listValue(List<String> list) {
        StringBuilder sb = new StringBuilder();
        sb.append(LIST);
        sb.append(SEPARATOR);

        sb.append(getIndentFormat(1));

        for (Object element: list) {
            sb.append(element);
            sb.append(SEPARATOR);
        }

        sb.append(getIndentFormat());

        sb.append(CLOSE);

        this.value = sb.toString();
    }

    public void objectValue(String value) {
        String indent = getIndentFormat();
        this.value = OBJECT + SEPARATOR + value + indent + CLOSE;
    }

    public String build(){
        StringBuilder sb = new StringBuilder();

        if(!value.isEmpty())
            sb.append(getIndentFormat());

        sb.append(field);
        if(!field.isEmpty())
            sb.append("\s");
        if(!name.isEmpty())
            sb.append(PREFIX_KEY);
        sb.append(name);
        if(!name.isEmpty())
            sb.append("\s");
        sb.append(action);
        if(!action.isEmpty())
            sb.append("\s");
        sb.append(value);
        if(!value.isEmpty())
            sb.append("\s");

        return sb.toString();
    }

    private String getIndentFormat(){
        return getIndentFormat(0);
    }

    private String getIndentFormat(int increment){
        if(!format)
            return "";

        StringBuilder sb = new StringBuilder();

        if(!field.isEmpty())
            sb.append("\n");

        for (int i = 0; i < level + increment; i++) {
            sb.append("\t");
        }

        return sb.toString();
    }

}