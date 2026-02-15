package bankServerGenerator;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

import bankServerExerciseWithCheckrep.BankServerWithACheckrep;

/**
 * Generates JUnit tests for the BankServer that assert the
 * checkHistoryMatchesCurrentBalance invariant remains true
 * after sequences of operations as the system state changes.
 */
public class BankServerCheckRepTestGenerator {

    /** Number of JUnit test methods to generate */
    private static final int NUM_TESTS = 7;

    /** Number of random operations performed per generated test */
    private static final int OPERATIONS_PER_TEST = 5;

    public static void main(String[] args) throws IOException {

        // Builder for the generated JUnit test class
        TypeSpec.Builder testClass =
            TypeSpec.classBuilder("bankServerGeneratedTest")
                .addModifiers(Modifier.PUBLIC);

        // Fixed seed for reproducible test generation
        Random rng = new Random(123);

        for (int i = 0; i < NUM_TESTS; i++) {
            testClass.addMethod(generateTestMethod(i, rng));
        }

        // Save the generated class into this package
        JavaFile javaFile =
            JavaFile.builder("generatedTest", testClass.build())
                    .build();

        javaFile.writeTo(Path.of("src/test/java"));
    }

    /**
     * Generates a single JUnit test method consisting of a sequence
     * of randomly chosen BankServer operations.
     */
    private static MethodSpec generateTestMethod(int index, Random rng) {

        // Create a new JUnit test method with a unique name
        MethodSpec.Builder m =
            MethodSpec.methodBuilder("generatedTest_" + index)
                .addAnnotation(ClassName.get("org.junit", "Test"))
                .addModifiers(Modifier.PUBLIC)
                .addStatement(
                    "$T server = new $T()",
                    BankServerWithACheckrep.class,
                    BankServerWithACheckrep.class
                );

        // at least one account before random operations begin
        String acct = "A" + index;
        m.addStatement("server.createAccount($S)", acct);
        addCheckRep(m);

        // Perform a sequence of random credit/debit operations.
        // After each operation, the account state is validated.
        for (int i = 0; i < OPERATIONS_PER_TEST; i++) {
            int choice = rng.nextInt(2);

            switch (choice) {
                case 0:
                    int credit = rng.nextInt(500);
                    m.addStatement("server.credit($S, $L)", acct, credit);
                    break;
                case 1:
                    int debit = rng.nextInt(500);
                    m.addStatement("server.debit($S, $L)", acct, debit);
                    break;
            }

            addCheckRep(m);
        }

        return m.build();
    }

    /**
     * Appends an assertion to the generated test method that checks
     * the state of the BankServer is valid.
     * Looks at the main method we are auto generating test for: checkHistoryMatchesCurrentBalance()
     */
    private static void addCheckRep(MethodSpec.Builder m) {
        m.addStatement(
            "$T.assertTrue(server.checkHistoryMatchesCurrentBalance())",
            ClassName.get("org.junit", "Assert")
        );
    }
}
