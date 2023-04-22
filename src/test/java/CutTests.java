import org.example.Cut;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

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
    public void testCharBasedCutNK() throws IOException {
        String[] args1 = {"-c", "-o", outputFile, inputFile, "2-3"};
        writeToFile(inputFile);
        cut.run(args1);
        Assertions.assertEquals("""
                ir
                ec
                hi
                """, readFromFile(outputFile));
    }

    @Test
    public void testCharBasedCutN() throws IOException {
        String[] args2 = {"-c", "-o", outputFile, inputFile, "3"};
        writeToFile(inputFile);
        cut.run(args2);
        Assertions.assertEquals("""
                rst second third
                cond third fourth fifth
                ird fourth fifth sixth
                """, readFromFile(outputFile));
    }

    @Test
    public void testWordBasedCutNK() throws IOException {
        String[] args1 = {"-w", "-o", outputFile, inputFile, "2-3"};
        writeToFile(inputFile);
        cut.run(args1);
        Assertions.assertEquals("""
                second third
                third fourth
                fourth fifth
                """, readFromFile(outputFile));
    }

    @Test
    public void testWordBasedCutN() throws IOException {
        String[] args2 = {"-w", "-o", outputFile, inputFile, "3"};
        writeToFile(inputFile);
        cut.run(args2);
        Assertions.assertEquals("""
                third
                fourth fifth
                fifth sixth
                """, readFromFile(outputFile));
    }

    @Test
    public void testConsole() throws IOException {
        String[] args = new String[5];
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter cut option (-c or -w): ");
        if (scanner.hasNextLine()) args[0] = System.console().readLine();

        System.out.print("Enter output file option (-o outputFile or press Enter to skip): ");
        if (scanner.hasNextLine()) {
            String outputFileOption = System.console().readLine();
            if ("-o".equals(outputFileOption) && scanner.hasNextLine()) {
                args[1] = outputFileOption;
                args[2] = System.console().readLine();
            } else {
                args[1] = "";
                args[2] = "";
            }
        }

        System.out.print("Enter input file name: ");
        if (scanner.hasNextLine()) args[3] = System.console().readLine();

        System.out.print("Enter cut positions: ");
        if (scanner.hasNextLine()) args[4] = System.console().readLine();

        if (args[0] == null || args[3] == null || args[4] == null) {
            System.err.println("Missing input. Please provide all the required arguments.");
            return;
        }
        writeToFile(args[3]);
        cut.run(args);

        System.out.println("Output:");
        System.out.println(outputFile);
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