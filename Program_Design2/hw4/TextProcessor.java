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
                // 處理每一行的內容
                String processedLine = processLine(line);
                // 寫入結果到輸出文件
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
        // 將所有非英文字元（包括數字和空格）以空白代替，但保留單詞
        line = line.replaceAll("[^a-zA-Z\\s]", " ");
        // 將所有英文大寫轉換成小寫
        line = line.toLowerCase();
        // 以空白進行詞彙的segmentation
        line = line.replaceAll("\\s+", " ").trim();
        return line;
    }
}
