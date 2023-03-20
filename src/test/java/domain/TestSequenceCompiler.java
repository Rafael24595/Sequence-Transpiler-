package domain;

import infraestructure.GsonInputInterpreter;
import infraestructure.GsonOutputInterpreter;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestSequenceCompiler {

    static IInputInterpeter inputInterpreter;
    static IOutputInterpeter outputInterpreter;

    @BeforeClass
    public static void init() {
        inputInterpreter = new GsonInputInterpreter();
        outputInterpreter = new GsonOutputInterpreter();
    }

    @Test
    public void testMutateKo() {
        String base = "{\"test_aux\":\"1\"}";
        String sentence = "FIELD test MUTATE \"1\"";

        SequenceCompiler compiler = new SequenceCompiler(base, sentence);
        try {
            compiler.compile();
            assertTrue("Exception expected but not thrown", false);
        }
        catch (IllegalArgumentException e){
            assertEquals("Field does not exists.", e.getMessage());
        }
    }

    @Test
    public void testDeleteOk() {
        String base = "{\"test\":\"1\"}";
        String sentence = "FIELD test DELETE";
        String expected = "{}";

        SequenceCompiler compiler = new SequenceCompiler(base, sentence);

        assertEquals(expected, compiler.compile());
    }

    @Test
    public void testDeleteKo() {
        String base = "{\"test_aux\":\"1\"}";
        String sentence = "FIELD test DELETE";

        SequenceCompiler compiler = new SequenceCompiler(base, sentence);
        try {
            compiler.compile();
            assertTrue("Exception expected but not thrown", false);
        }
        catch (IllegalArgumentException e){
            assertEquals("Field does not exists.", e.getMessage());
        }
    }

}