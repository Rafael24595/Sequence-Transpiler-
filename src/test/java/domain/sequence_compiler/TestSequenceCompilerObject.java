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

public class TestSequenceCompilerObject {

    static IInputInterpeter inputInterpreter;
    static IOutputInterpeter outputInterpreter;

    @BeforeClass
    public static void init() {
        inputInterpreter = new GsonInputInterpreter();
        outputInterpreter = new GsonOutputInterpreter();
    }

    @Test
    public void testAsObjectOk() {
        String base = "{\"test\":{\"test_str\":\"str\",\"test_int\":1,\"test_bol\":true}}";
        String sentence = "FIELD test AS OBJECT FIELD test_str_aux AS \"str_aux\" FIELD test_int_aux AS 2 FIELD test_bol_aux AS false CLOSE";
        String expected = "{\"test\":{\"test_str_aux\":\"str_aux\",\"test_bol_aux\":false,\"test_int_aux\":2}}";

        assertCompile(base, sentence, expected);
    }

    @Test
    public void testMutateObjectOk() {
        String base = "{\"test\":{\"test_str\":\"str\",\"test_int\":1}}";
        String sentence = "FIELD test MUTATE OBJECT FIELD test_str MUTATE \"str_aux\" FIELD test_int MUTATE 2 FIELD test_bol AS true CLOSE";
        String expected = "{\"test\":{\"test_str\":\"str_aux\",\"test_int\":2,\"test_bol\":true}}";

        assertCompile(base, sentence, expected);
    }

    @Test
    public void testMutateObjectKo() {
        String base = "{\"test\":\"{\\\"test_str\\\":\\\"str\\\",\\\"test_int\\\":1,\\\"test_bol\\\":true}\"}";
        String sentence = "FIELD test MUTATE OBJECT FIELD test_str_aux MUTATE \"str_aux\" FIELD test_int_aux MUTATE 2 FIELD test_bol_aux MUTATE false CLOSE";
        String expected = "Format error, Map type structure required.";

        assertException(base, sentence, expected);
    }

    @Test
    public void testIncrementObjectOk() {
        String base = "{\"test\":\"{\\\"test_str\\\":\\\"str\\\",\\\"test_int\\\":1,\\\"test_bol\\\":true}\"}";
        String sentence = "FIELD test MUTATE OBJECT FIELD test_str_aux MUTATE \"str_aux\" FIELD test_int_aux MUTATE 2 FIELD test_bol_aux MUTATE false CLOSE";
        String expected = "Format error, Map type structure required.";

        assertException(base, sentence, expected);
    }

    @Test
    public void testDecrementObjectOk() {
        String base = "{\"test\":\"{\\\"test_str\\\":\\\"str\\\",\\\"test_int\\\":1,\\\"test_bol\\\":true}\"}";
        String sentence = "FIELD test MUTATE OBJECT FIELD test_str_aux MUTATE \"str_aux\" FIELD test_int_aux MUTATE 2 FIELD test_bol_aux MUTATE false CLOSE";
        String expected = "Format error, Map type structure required.";

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