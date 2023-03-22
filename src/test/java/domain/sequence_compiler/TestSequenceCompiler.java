package domain.sequence_compiler;

import domain.IInputInterpeter;
import domain.IOutputInterpeter;
import domain.SequenceCompiler;
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
        String expected = "Field does not exists.";

        assertException(base, sentence, expected);
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
        String expected = "Field does not exists.";

        assertException(base, sentence, expected);
    }

    private void assertException(String base, String sentence, String expected) {
        SequenceCompiler compiler = new SequenceCompiler(base, sentence);
        try {
            compiler.compile();
            assertTrue("Exception expected but not thrown", false);
        }
        catch (IllegalArgumentException e){
            assertEquals(expected, e.getMessage());
        }
    }

}