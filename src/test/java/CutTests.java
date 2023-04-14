import org.example.Cut;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

public class CutTests {
    private Cut cut;
    private String inputFile;
    private String outputFile;

    @BeforeEach
    public void setUp() {
        cut = new Cut();
        inputFile = "input.txt";
        outputFile = "output.txt";
    }

    @Test
    public void testCharBasedCut() throws IOException {
        String[] args1 = {"-c", "-o", outputFile, inputFile, "2-3"};
        writeToFile(inputFile);
        cut.run(args1);
        Assertions.assertEquals("""
                ir
                ec
                hi
                """, readFromFile(outputFile));

        String[] args2 = {"-c", "-o", outputFile, inputFile, "3"};
        writeToFile(inputFile);
        cut.run(args2);
        Assertions.assertEquals("""
                rst second third
                cond third fourth fifth
                ird fourth fifth sixth
                """, readFromFile(outputFile));

        String[] args3 = {"-c", "-o", outputFile, inputFile, "-3"};
        writeToFile(inputFile);
        cut.run(args3);
        Assertions.assertEquals("""
                fir
                sec
                thi
                """, readFromFile(outputFile));
    }

    @Test
    public void testWordBasedCut() throws IOException {
        String[] args1 = {"-w", "-o", outputFile, inputFile, "2-3"};
        writeToFile(inputFile);
        cut.run(args1);
        Assertions.assertEquals("""
                second third
                third fourth
                fourth fifth
                """, readFromFile(outputFile));

        String[] args2 = {"-w", "-o", outputFile, inputFile, "3"};
        writeToFile(inputFile);
        cut.run(args2);
        Assertions.assertEquals("""
                third
                fourth fifth
                fifth sixth
                """, readFromFile(outputFile));

        String[] args3 = {"-w", "-o", outputFile, inputFile, "-3"};
        writeToFile(inputFile);
        cut.run(args3);
        Assertions.assertEquals("""
                first second third
                second third fourth
                third fourth fifth
                """, readFromFile(outputFile));
    }

    @Test
    public void testNoRangeCut() throws IOException {
        String[] args = {"-c", "-o", outputFile, inputFile};
        writeToFile(inputFile);
        cut.run(args);
        Assertions.assertEquals("""
                first second third
                second third fourth fifth
                third fourth fifth sixth
                """, readFromFile(outputFile));
    }

    private void writeToFile(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("first second third\nsecond third fourth fifth\nthird fourth fifth sixth\n");
        }
    }

    private String readFromFile(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}