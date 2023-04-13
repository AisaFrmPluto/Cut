package org.example;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;

public class Cut {

    @Option(name = "-o", usage = "Output file name")
    private String outputFile;

    @Option(name = "-c", usage = "by characters")
    private boolean isCharBased;

    @Option(name = "-w", usage = "by words")
    private boolean isWordBased;

    @Argument(index = 0, usage = "Input file name")
    private String inputFile;

    @Argument(index = 1, usage = "Range")
    private String range;

    public Cut() {
        this.isCharBased = true;
        this.isWordBased = false;
    }

    public static void main(String[] args) {
        new Cut().run(args);
    }

    public void run(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)))) {

            String line;
            while ((line = reader.readLine()) != null) { // Use the while loop condition to check for end-of-file

                if (range != null) {
                    String[] parts = range.split("-");
                    if (parts.length == 0 || parts.length > 2) {
                        throw new IllegalArgumentException("Invalid range format: " + range);
                    }

                    int start = Integer.parseInt(parts[0]) - 1;
                    int end = (parts.length == 1) ? line.length() : Integer.parseInt(parts[1]);

                    if (isCharBased) {
                        start = Math.max(start, 0);
                        end = Math.min(end, line.length());
                    } else if (isWordBased) {
                        String[] words = line.split("\\s+");
                        start = Math.max(start - 1, 0);
                        end = Math.min(end, words.length) + 1;
                        end = (end == 0) ? 0 : words[end - 1].length() + ((end < words.length) ? 1 : 0);
                    }

                    if (start > end) {
                        throw new IllegalArgumentException("Invalid range: " + range);
                    }

                    line = line.substring(start, end);
                }
                writer.write(line);
                writer.newLine();
            }
            writer.flush();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

    }
}