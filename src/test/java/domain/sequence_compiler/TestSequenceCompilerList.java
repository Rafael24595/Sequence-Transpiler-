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

public class TestSequenceCompilerList {

    static IInputInterpeter inputInterpreter;
    static IOutputInterpeter outputInterpreter;

    @BeforeClass
    public static void init() {
        inputInterpreter = new GsonInputInterpreter();
        outputInterpreter = new GsonOutputInterpreter();
    }

    @Test
    public void testAsListOk() {
        String base = "{\"test\":[1,2,3]}";
        String sentence = "FIELD $test AS LIST 4 5 6 CLOSE";
        String expected = "{\"test\":[4,5,6]}";

        assertCompile(base, sentence, expected);
    }

    @Test
    public void testMutateListOk() {
        String base = "{\"test\":[1,2,3]}";
        String sentence = "FIELD $test MUTATE LIST 4 5 6 CLOSE";
        String expected = "{\"test\":[1,2,3,4,5,6]}";

        assertCompile(base, sentence, expected);
    }

    @Test
    public void testMutateListKo() {
        String base = "{\"test\":\"[1,2,3]\"}";
        String sentence = "FIELD $test MUTATE LIST 4 5 6 CLOSE";
        String expected = "Format error, List type structure required.";

        assertException(base, sentence, expected);
    }

    @Test
    public void testIncrementListOk() {
        String base = "{\"test\":\"[1,2,3]\"}";
        String sentence = "FIELD $test INCREMENT LIST 4 5 6 CLOSE";
        String expected = "Format error, List type structure required.";

        assertException(base, sentence, expected);
    }

    @Test
    public void testDecrementListOk() {
        String base = "{\"test\":\"[1,2,3]\"}";
        String sentence = "FIELD $test DECREMENT LIST 4 5 6 CLOSE";
        String expected = "Format error, List type structure required.";

        assertException(base, sentence, expected);
    }

    private void assertException(String base, String sentence, String expected) {
        SequenceCompiler compiler = new SequenceCompiler(base, sentence);
        try {
            compiler.compile();
            assertTrue("Exception expected but not thrown", false);
        }
        catch (ClassCastException e){
            assertEquals(expected, e.getMessage());
        }
    }

    private void assertCompile(String base, String sentence, String expected){
        SequenceCompiler compiler = new SequenceCompiler(base, sentence);

        assertEquals(expected, compiler.compile());
    }

}