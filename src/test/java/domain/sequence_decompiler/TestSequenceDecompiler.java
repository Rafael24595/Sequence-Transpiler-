package domain.sequence_decompiler;

import org.junit.BeforeClass;
import org.junit.Test;

import domain.IInputInterpeter;
import domain.IOutputInterpeter;
import domain.SequenceDecompiler;
import infraestructure.GsonInputInterpreter;
import infraestructure.GsonOutputInterpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestSequenceDecompiler {

    static IInputInterpeter inputInterpreter;
    static IOutputInterpeter outputInterpreter;

    @BeforeClass
    public static void init() {
        inputInterpreter = new GsonInputInterpreter();
        outputInterpreter = new GsonOutputInterpreter();
    }

    @Test
    public void testAsOk() {
        String base = "{\"test\":\"str\"}";
        String updated = "{\"test\":\"str\",\"test_aux\":\"str aux\"}";
        String expected = "FIELD $test_aux AS \"str aux\"";

        assertDecompile(base, updated, expected);
    }

    @Test
    public void testMutateOk() {
        String base = "{\"test\":\"str\"}";
        String updated = "{\"test\":\"str aux\"}";
        String expected = "FIELD $test MUTATE \"str aux\"";

        assertDecompile(base, updated, expected);
    }

    @Test
    public void testDeleteOk() {
        String base = "{\"test\":\"str\",\"test_aux\":\"str aux\"}";
        String updated = "{\"test_aux\":\"str\"}";
        String expected = "FIELD $test DELETE FIELD $test_aux MUTATE \"str\"";

        assertDecompile(base, updated, expected);
    }

    @Test
    public void testIncrementOk() {
        String base = "{\"test\":1}";
        String updated = "{\"test\":2}";
        String expected = "FIELD $test INCREMENT 1";

        assertDecompile(base, updated, expected);
    }

    @Test
    public void testDecrementOk() {
        String base = "{\"test\":2}";
        String updated = "{\"test\":1}";
        String expected = "FIELD $test DECREMENT 1";

        assertDecompile(base, updated, expected);
    }

    private void assertDecompile(String base, String updated, String expected){
        SequenceDecompiler decompiler = new SequenceDecompiler(base, updated, false);
        
        assertEquals(expected, decompiler.decompile());
    }

}
