import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;

public class TFIDFCalculator {
    
    public static double tf(Trie docTrie, String term) {
        int number_term_in_doc = 0;
        int totalWords = 0;
        // 如果這個文本裡面沒有指定的單字
        // 則不必遍歷整個文本再計數，直接return 0.0就好
        // 不過測資中好像幾乎沒有這種狀況...
        if(docTrie.search(term)==false){
            return 0.0;
        }else{
            for(String word:docTrie.doc.split(" ")){
                if(word.equals(term)){
                    number_term_in_doc++;
                }
                totalWords++;
            }
        }
        return (double)number_term_in_doc / totalWords;
    }

    public static double idf(List<Trie> trieList, String term, Trie allWordsTrie) {
        int number_doc_contain_term = 0;
        /*使用allWordsTrie儲存已經計算過number_doc_contain_term的單字
          下次遇到同一個單字時就不必再遍歷所有的文本一次
          這裡應該是主要節省了時間的地方
          我原本使用HahsMap來完成這件事，但我想要challenge point，所以花了一點時間改寫 */
        if(!allWordsTrie.search(term)){
            for(int i=0; i<trieList.size(); i++) {
                if(trieList.get(i).search(term)){
                    number_doc_contain_term++;
                }
            }
            allWordsTrie.insert(term);
            allWordsTrie.setWordCount(term, number_doc_contain_term);
        }else{
            number_doc_contain_term = allWordsTrie.getWordCount(term);
        }
        return Math.log(trieList.size() / (double)number_doc_contain_term);
    }

    public static double tfIdfCalculate(Trie docTrie, List<Trie> trieList, 
                                        String term, Trie allWordsTrie) {
        return tf(docTrie, term) * idf(trieList, term, allWordsTrie);
    }
    
    public static void main(String[] args){
        String inputFile = args[0];
        String testcaseFile = args[1]; 
        String outputFile = "output.txt";

        /* 每顆Trie代表一個文本(5個句子的字典樹) */
        List<Trie> trieList = new ArrayList<Trie>();
        Trie allWordsTrie = new Trie();
        Trie tempTrie = new Trie();
        int counter = 0; // 計算是否已經讀入5個句子
        try(BufferedReader br = new BufferedReader(new FileReader(inputFile, StandardCharsets.UTF_8))){
            String line = "";
            String temp = "";
            while ((line = br.readLine()) != null) {
                // 將所有非英文字元（包括數字和空格）以空白代替
                line = line.replaceAll("[^a-zA-Z\\s]", " ");
                // 以空格進行詞彙的segmentation
                line = line.replaceAll("\\s+", " ").trim();
                // 將所有英文大寫轉換成小寫
                line = line.toLowerCase();
                // 將新句子串接在後面，每五句形成一個文本
                temp = temp.concat(line+" ");
                counter++;
                if(counter%5==0){
                    // 將文本中每個單字insert到trie裡
                    for (String word:temp.split(" ")) {
                        if(!tempTrie.search(word)){
                            tempTrie.insert(word);
                        }
                    }
                    tempTrie.setDoc(temp);
                    trieList.add(tempTrie);
                    tempTrie = new Trie();
                    temp = "";
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

        /* 計算並將結果寫進output.txt */
        File file = new File(outputFile);
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            for(int i = 0; i < termList.size(); i++){
                // System.out.println(trieList.get(numberList.get(i)).doc);
                double TFIDF = tfIdfCalculate(trieList.get(numberList.get(i)),
                                              trieList, termList.get(i), allWordsTrie);
                writer.write(String.format("%.5f", TFIDF) + " ");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord = false;
    int wordCount = 0;
}

class Trie {
    TrieNode root = new TrieNode();
    String doc = "";

    // 每一顆Trie都對應一個文本(後來我拿來做了其他事，這裡可能不太對了)
    public void setDoc(String doc){
        this.doc = doc;
    }

    // 插入一個單字到 Trie
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new TrieNode();
            }
            node = node.children[c - 'a'];
        }
        node.isEndOfWord = true;
    }

    // 搜尋 Trie 中是否存在該單字
    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
            if (node == null) {
                return false;
            }
        }
        return node.isEndOfWord;
    }

    // 跟search幾乎一樣，只是這會設定這個葉節點的wordCount
    // 與search幾乎一樣的原因就是為了找到葉子
    public void setWordCount(String word, int count) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                break;
            }
            node = node.children[c - 'a'];
        }
        node.wordCount = count;
    }

    // 跟search幾乎一樣，只是這會回傳wordCount
    // 與search幾乎一樣的原因就是為了找到葉子
    public int getWordCount(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
            if (node == null) {
                return 0;
            }
        }
        return node.wordCount;
    }
}
