package domain.sequence_compiler;

import static org.junit.Assert.*;

import domain.IInputInterpeter;
import domain.IOutputInterpeter;
import domain.SequenceCompiler;
import org.junit.BeforeClass;
import org.junit.Test;

import infraestructure.GsonInputInterpreter;
import infraestructure.GsonOutputInterpreter;

public class TestSequenceCompilerString {

    static IInputInterpeter inputInterpreter;
    static IOutputInterpeter outputInterpreter;

    @BeforeClass
    public static void init() {
        inputInterpreter = new GsonInputInterpreter();
        outputInterpreter = new GsonOutputInterpreter();
    }

    @Test
    public void testAsStringOk() {
        String base = "{\"test\":\"string test\"}";
        String sentence = "FIELD test AS \"string test as\"";
        String expected = "{\"test\":\"string test as\"}";

        assertCompile(base, sentence, expected);
    }

    @Test
    public void testMutateStringOk() {
        String base = "{\"test\":\"string test\"}";
        String sentence = "FIELD test MUTATE \"string test mutate\"";
        String expected = "{\"test\":\"string test mutate\"}";

        assertCompile(base, sentence, expected);
    }

    @Test
    public void testMutateStringKo() {
        String base = "{\"test\":true}";
        String sentence = "FIELD test MUTATE \"string test\"";
        SequenceCompiler compiler = new SequenceCompiler(base, sentence);
        try {
            compiler.compile();
            assertTrue("Exception expected but not thrown", false);
        }
        catch (IllegalArgumentException e){
            assertEquals("Cannot merge the fields, both must be String type objects", e.getMessage());
        }
    }

    @Test
    public void testIncrementStringOk() {
        String base = "{\"test\":\"string test\"}";
        String sentence = "FIELD test INCREMENT \"1\"";

        SequenceCompiler compiler = new SequenceCompiler(base, sentence);
        try {
            compiler.compile();
            assertTrue("Exception expected but not thrown", false);
        }
        catch (IllegalArgumentException e){
            assertEquals("Cannot add up non numeric objects.", e.getMessage());
        }
    }

    @Test
    public void testDecrementStringOk() {
        String base = "{\"test\":\"string test\"}";
        String sentence = "FIELD test DECREMENT \"1\"";

        SequenceCompiler compiler = new SequenceCompiler(base, sentence);
        try {
            compiler.compile();
            assertTrue("Exception expected but not thrown", false);
        }
        catch (IllegalArgumentException e){
            assertEquals("Cannot add up non numeric objects.", e.getMessage());
        }
    }

    private void assertCompile(String base, String sentence, String expected){
        SequenceCompiler compiler = new SequenceCompiler(base, sentence);

        assertEquals(expected, compiler.compile());
    }

}