import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TextProcessor {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java TextProcessor <inputFile> <outputFile>");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile, StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, StandardCharsets.UTF_8))) {

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                // �B�z�C�@�檺���e
                String processedLine = processLine(line);
                // �g�J���G���X���
                writer.write(lineNumber + "  " + processedLine);
                writer.newLine();
                lineNumber++;
            }

            System.out.println("Processing completed. Output written to " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processLine(String line) {
        // �N�Ҧ��D�^��r���]�]�A�Ʀr�M�Ů�^�H�ťեN���A���O�d���
        line = line.replaceAll("[^a-zA-Z\\s]", " ");
        // �N�Ҧ��^��j�g�ഫ���p�g
        line = line.toLowerCase();
        // �H�ťնi����J��segmentation
        line = line.replaceAll("\\s+", " ").trim();
        return line;
    }
}
