package domain.sequence_decompiler;

import domain.IInputInterpeter;
import domain.IOutputInterpeter;
import domain.SequenceDecompiler;
import infraestructure.GsonInputInterpreter;
import infraestructure.GsonOutputInterpreter;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestSequenceDecompilerList {

    static IInputInterpeter inputInterpreter;
    static IOutputInterpeter outputInterpreter;

    @BeforeClass
    public static void init() {
        inputInterpreter = new GsonInputInterpreter();
        outputInterpreter = new GsonOutputInterpreter();
    }

    @Test
    public void testListOk() {
        String base = "{\"test\":[1,2,3]}";
        String updated = "{\"test\":[1,2,3,4,5,6]}";
        String expected = "FIELD $test MUTATE LIST 4 5 6 CLOSE";

        assertDecompile(base, updated, expected);
    }

    @Test
    public void testListKo() {
        String base = "{\"test\":[1,2,3]}";
        String updated = "{\"test\":[1,2,3,4,5,6]}";
        String expected = "FIELD $test MUTATE LIST 1 2 3 4 5 6 CLOSE";

        assertNotDecompile(base, updated, expected);
    }

    private void assertNotDecompile(String base, String updated, String expected){
        SequenceDecompiler decompiler = new SequenceDecompiler(base, updated, false);

        assertNotEquals(expected, decompiler.decompile());
    }

    private void assertDecompile(String base, String updated, String expected){
        SequenceDecompiler decompiler = new SequenceDecompiler(base, updated, false);
        
        assertEquals(expected, decompiler.decompile());
    }

}