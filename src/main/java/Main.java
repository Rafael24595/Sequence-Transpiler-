
import domain.SequenceCompiler;
import domain.SequenceDecompiler;
import test.KInput;

public class Main {

    public static void main(String[] args) {
        SequenceCompiler compiler = new SequenceCompiler(KInput.BASE_TEST_1, KInput.SEQUENCE_TEST_1);
        String compileResult = compiler.compile();
        System.out.println(compileResult);

        SequenceDecompiler decompiler = new SequenceDecompiler(KInput.BASE_TEST_2, KInput.UPDATE_TEST_1);
        String decompileResult = decompiler.decompile();
        System.out.println(decompileResult);

        System.out.println("Close app...");
    }

}