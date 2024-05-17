import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;


public class TFIDFCalculator {

    public static double tf(List<String> doc, String term) {
        String regex = "\\b\\w+\\b";
        String regexTerm = "\\b" + Pattern.quote(term) + "\\b";
        double number_term_in_doc = 0.0;
        double totalWords = 0.0;
        for(int i=0; i<doc.size(); i++) {
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(doc.get(i));
            Pattern patternTerm = Pattern.compile(regexTerm, Pattern.CASE_INSENSITIVE);
            Matcher matcherTerm = patternTerm.matcher(doc.get(i));
            while (matcher.find()) {
                totalWords++;
                if (matcherTerm.find()) {
                    number_term_in_doc++;
                }
            }
        }
        System.out.println(doc);
        // System.out.println("Number of term in doc: " + number_term_in_doc);
        // System.out.println("Total words: " + totalWords);
        return number_term_in_doc / totalWords;
    }

    public static double idf(List<List<String>> docs, String term) {
        String regexTerm = "\\b" + Pattern.quote(term) + "\\b";
        Pattern patternTerm = Pattern.compile(regexTerm, Pattern.CASE_INSENSITIVE);
        double number_doc_contain_term = 0.0;
        for(int i=0; i<docs.size(); i++) {
            for(int j=0; j<5; j++) {
                Matcher matcherTerm = patternTerm.matcher(docs.get(i).get(j));
                if(matcherTerm.find()) {
                    number_doc_contain_term++;
                    break;
                }
            }
        }
        return Math.log(docs.size() / number_doc_contain_term);
    }

    public static double tfIdfCalculate(List<String> doc, List<List<String>> docs, String term) {
        return tf(doc, term) * idf(docs, term);
    }
    
    public static void main(String[] args){
        String inputFile = args[0];
        String testcaseFile = args[1]; 
        String outputFile = "output.txt";
        
        /* 將所有句子讀進Linklist，每五行為串為一個文本 */
        List<List<String>> docsList = new ArrayList<List<String>>();
        List<String> tempList = new ArrayList<String>();
        int counter = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(inputFile, StandardCharsets.UTF_8))){
            String line = "";
            while ((line = br.readLine()) != null) {
                // 將所有非英文字元（包括數字和空格）以空白代替
                line = line.replaceAll("[^a-zA-Z\\s+]", " ");
                // 以空格進行詞彙的segmentation
                line = line.replaceAll("\\s+", " ").trim();
                // 將所有英文大寫轉換成小寫
                line = line.toLowerCase();
                // 將5個句子合併成一個字串(即一個文本)，句子間同樣以空格分隔
                // temp = temp.concat(line+" ");
                tempList.add(line);
                counter++;
                if(counter%5==0){
                    docsList.add(tempList);
                    tempList = new ArrayList<String>();
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        /* 讀測資 */
        List<String> termList = new ArrayList<String>();
        List<Integer> numberList = new ArrayList<Integer>();
        try(BufferedReader br = new BufferedReader(new FileReader(testcaseFile, StandardCharsets.UTF_8))){
            // 讀第一行，也就是目標單字
            String line = br.readLine();
            String[] temp = line.split(" ");
            for (int i = 0; i < temp.length; i++){
                termList.add(temp[i]);
            }
            // 讀第二行，也就是目標文本
            line = br.readLine();
            temp = line.split(" ");
            for (int i = 0; i < temp.length; i++){
                numberList.add(Integer.parseInt(temp[i]));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        // System.out.println(termList);
        // System.out.println(numberList);

        /* 計算並將結果寫進output.txt */
        File file = new File(outputFile);
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            for(int i = 0; i < termList.size(); i++){
                // double TF = tf(docsList.get(numberList.get(i)), tempList.get(i));
                // double IDF = idf(docsList, tempList.get(i));
                // System.out.println(docsList.get(numberList.get(i)-1));
                double TFIDF = tfIdfCalculate(docsList.get(numberList.get(i)-1), docsList, termList.get(i));
                writer.write(String.format("%.5f", TFIDF) + " ");
            }
            // for(int i = 0; i < docsList.size(); i++){
            //     writer.write(docsList.get(i) + "\n");
            // }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}