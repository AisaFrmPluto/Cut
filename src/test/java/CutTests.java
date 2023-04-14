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
        String[] args = {"-c", "-o", outputFile, inputFile, "1-3"};
        writeToFile(inputFile);
        cut.run(args);
        Assertions.assertEquals("""
                a b
                dd\s
                ghi
                """, readFromFile(outputFile));
    }

    @Test
    public void testWordBasedCut() throws IOException {
        String[] args = {"-w", "-o", outputFile, inputFile, "2-3"};
        writeToFile(inputFile);
        cut.run(args);
        Assertions.assertEquals("""
            b c
            eee ffff
            jkl ggg
            """, readFromFile(outputFile));
    }

    @Test
    public void testNoRangeCut() throws IOException {
        String[] args = {"-c", "-o", outputFile, inputFile};
        writeToFile(inputFile);
        cut.run(args);
        Assertions.assertEquals("""
                a b c
                dd eee ffff
                ghi jkl ggg vvv
                """, readFromFile(outputFile));
    }

    private void writeToFile(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("a b c\ndd eee ffff\nghi jkl ggg vvv\n");
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