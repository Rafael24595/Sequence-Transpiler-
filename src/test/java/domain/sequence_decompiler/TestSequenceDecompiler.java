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
    public void testAsKo() {
        String base = "{\"test\":\"str\"}";
        String updated = "{\"test\":\"str\",\"test_aux\":\"str aux\"}";
        String expected = "FIELD $test_aux AS \"str aux\"";

        assertDecompile(base, updated, expected);
    }

    private void assertDecompile(String base, String updated, String expected){
        SequenceDecompiler decompiler = new SequenceDecompiler(base, updated, false);
        
        assertEquals(expected, decompiler.decompile());
    }

}
