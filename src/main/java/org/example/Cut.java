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

    @Argument(usage = "Input file name")
    private String inputFile;

    @Argument(index = 1, usage = "Range")
    private String range;

    public Cut() {
        isCharBased = false;
        isWordBased = false;
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
            while ((line = reader.readLine()) != null) {

                if (range != null) {
                    int start, end;
                    if (range.contains("-")) {
                        // N-K
                        String[] parts = range.split("-");
                        start = Integer.parseInt(parts[0]) - 1;
                        end = Integer.parseInt(parts[1]);
                    } else {
                        // N
                        start = Integer.parseInt(range) - 1;
                        end = line.length();
                    }

                    if (isCharBased) {
                        start = Math.max(start, 0);
                        end = Math.min(end, line.length());
                        line = line.substring(start, end);
                    } else if (isWordBased) {
                        String[] words = line.trim().split("\\s+");
                        start = Math.max(start, 0);
                        end = Math.min(end, words.length);
                        StringBuilder sb = new StringBuilder();
                        for (int i = start; i < end; i++) {
                            sb.append(words[i]);
                            if (i < end - 1) {
                                sb.append(" ");
                            }
                        }
                        line = sb.toString();
                    }

                    if (start > end) {
                        throw new IllegalArgumentException("Invalid range: " + range);
                    }
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