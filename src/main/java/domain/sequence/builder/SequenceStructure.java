package domain.sequence.builder;

import java.util.List;

import static domain.sequence.sentence.KSentence.*;

public class SequenceStructure {

    String field;
    String name;
    String action;
    String value;

    public SequenceStructure() {
        this.field = FIELD;
        this.name = "name";
        this.action = "";
        this.value = "";
    }

    public SequenceStructure(String name) {
        this.field = FIELD;
        this.name = name;
        this.action = "";
        this.value = "";
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

        for (Object element: list) {
            sb.append(element);
            sb.append(SEPARATOR);
        }

        sb.append(CLOSE);

        this.value = sb.toString();
    }

    public void objectValue(String value) {
        this.value = OBJECT + SEPARATOR + value + SEPARATOR + CLOSE;
    }

    public String build(){
        StringBuilder sb = new StringBuilder();

        sb.append(field);
        if(!field.isEmpty())
            sb.append("\s");
        sb.append(name);
        if(!field.isEmpty())
            sb.append("\s");
        sb.append(action);
        if(!field.isEmpty())
            sb.append("\s");
        sb.append(value);
        if(!field.isEmpty())
            sb.append("\s");

        return sb.toString();
    }

}