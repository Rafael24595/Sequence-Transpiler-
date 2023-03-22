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

public class TestSequenceCompilerNumeric {

    static IInputInterpeter inputInterpreter;
    static IOutputInterpeter outputInterpreter;

    @BeforeClass
    public static void init() {
        inputInterpreter = new GsonInputInterpreter();
        outputInterpreter = new GsonOutputInterpreter();
    }

    @Test
    public void testAsNumericOk() {
        String base = "{\"test\":1}";
        String sentence = "FIELD $test AS 2";
        String expected = "{\"test\":2}";

        assertCompile(base, sentence, expected);
    }

    @Test
    public void testMutateNumericOk() {
        String base = "{\"test\":1}";
        String sentence = "FIELD $test MUTATE 2";
        String expected = "{\"test\":2}";

        assertCompile(base, sentence, expected);
    }

    @Test
    public void testMutateNumericKo() {
        String base = "{\"test\":\"1\"}";
        String sentence = "FIELD $test MUTATE 1";
        String expected = "Cannot merge the fields, both must be Numeric type objects";

        assertException(base, sentence, expected);
    }

    @Test
    public void testIncrementNumericOk_1() {
        String base = "{\"test\":1}";
        String sentence = "FIELD $test INCREMENT 1";
        String expected = "{\"test\":2}";

        assertCompile(base, sentence, expected);
    }

    @Test
    public void testIncrementNumericOk_2() {
        String base = "{\"test\":1.0}";
        String sentence = "FIELD $test INCREMENT 1";
        String expected = "{\"test\":2.0}";

        assertCompile(base, sentence, expected);
    }

    @Test
    public void testIncrementNumericOk_3() {
        String base = "{\"test\":1}";
        String sentence = "FIELD $test INCREMENT 1.0";
        String expected = "{\"test\":2.0}";

        assertCompile(base, sentence, expected);
    }

    @Test
    public void testDecrementNumericOk_1() {
        String base = "{\"test\":3}";
        String sentence = "FIELD $test DECREMENT 1";
        String expected = "{\"test\":2}";

        assertCompile(base, sentence, expected);
    }

    @Test
    public void testDecrementNumericOk_2() {
        String base = "{\"test\":3.0}";
        String sentence = "FIELD $test DECREMENT 1";
        String expected = "{\"test\":2.0}";

        assertCompile(base, sentence, expected);
    }

    @Test
    public void testDecrementNumericOk_3() {
        String base = "{\"test\":3}";
        String sentence = "FIELD $test DECREMENT 1.0";
        String expected = "{\"test\":2.0}";

        assertCompile(base, sentence, expected);
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

    private void assertCompile(String base, String sentence, String expected){
        SequenceCompiler compiler = new SequenceCompiler(base, sentence);

        assertEquals(expected, compiler.compile());
    }

}