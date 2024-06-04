import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class TFIDFSearch {

    // 直接複製自TFIDFCalculator
    public static double tf(Trie docTrie, String term) {
        int number_term_in_doc = 0;
        int totalWords = 0;
        /* 如果這個文本裡面沒有指定的單字
           則不必遍歷整個文本再計數，直接return 0.0就好
           不過測資中好像幾乎沒有這種狀況... */
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

    public static double idf(List<Trie> trieList, String term, HashMap<String, List<Integer>> containMap) {
        int number_doc_contain_term = 0;
        if(!containMap.containsKey(term)){
            return 0.0;
        }else{
            number_doc_contain_term = containMap.get(term).size();
            if (number_doc_contain_term==0){
                return 0.0;
            }else{
                return Math.log(trieList.size() / (double)number_doc_contain_term);
            }
        }
    }

    public static double tfIdfCalculate(Trie docTrie, List<Trie> trieList, 
                                        String term, HashMap<String, List<Integer>> containMap) {
        return tf(docTrie, term) * idf(trieList, term, containMap);
    }

    public static void writeContainSet(List<Trie> trieList,String term, 
                                       List<Integer> set1, HashMap<String, List<Integer>> containMap){
        List<Integer> tempSet = new ArrayList<Integer>();
        if(!containMap.containsKey(term)){
            for(int i=0; i<trieList.size(); i++){
                if(trieList.get(i).search(term)){
                    tempSet.add(i);
                }
            }
            containMap.put(term, tempSet);
        }
    }

    public static List<Integer> findIntersection(List<Integer> set1, List<Integer> set2){
        List<Integer> intersectionSet = new ArrayList<Integer>();
        intersectionSet.addAll(set1);
        intersectionSet.retainAll(set2);
        return intersectionSet;
    }

    public static List<Integer> findUnion(List<Integer> set1, List<Integer> set2){
        // (B-A) U A = B U A || A,B是集合，U是聯集符號
        // 這個做法的缺點是會搞亂set的順序，所以我又排序了一下
        List<Integer> unionSet = new ArrayList<Integer>();
        unionSet.addAll(set1);
        unionSet.removeAll(set2);
        unionSet.addAll(set2);
        unionSet.sort(Comparator.naturalOrder());
        return unionSet;
    }

    public static void main(String[] args) {
    boolean debug = false;
    String inputFile = args[0];
    String testcaseFile = args[1]; 
    String outputFile = "./output.txt";
    HashMap<String, List<Integer>> containMap = new HashMap<String, List<Integer>>();

    try {
        // 解序列化
        FileInputStream fis = new FileInputStream(inputFile+".ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Indexer deserializedIdx = (Indexer) ois.readObject();
        List<String> contentList = deserializedIdx.getListOfContent();
        List<Trie> trieList = new ArrayList<Trie>();
        Trie tempTrie = new Trie();

        /* 將文本中每個單字insert到trie裡，即上一次作業的讀文本階段 */
        for (int i=0; i<contentList.size(); i++){
            String tempContent = contentList.get(i);
            for (String word:tempContent.split(" ")) {
                if(!tempTrie.search(word)){
                    tempTrie.insert(word);
                }
            }
            tempTrie.setDoc(tempContent);
            trieList.add(tempTrie);
            tempTrie = new Trie();
        }

        /* 讀測資 */
        try(BufferedReader br = new BufferedReader(new FileReader(testcaseFile, StandardCharsets.UTF_8))){
            String line = br.readLine();
            int N = Integer.parseInt(line);
            while((line = br.readLine()) != null){
                // 讀第二行之後，也就是目標單字
                String[] terms = line.split(" ");
                List<Integer> resultSet = new ArrayList<Integer>();
                if(terms.length==1){
                    if(!containMap.containsKey(terms[0])){
                        writeContainSet(trieList, terms[0], resultSet, containMap);
                    }
                    if(debug){System.out.println(terms[0]+"="+containMap.get(terms[0]));}
                    resultSet = containMap.get(terms[0]);

                }else if(terms[1].equals("AND")){
                    HashSet<String> tempSet = new HashSet<String>();
                    for (int i = 0; i < terms.length; i+=2){
                        if(!containMap.containsKey(terms[i])){
                            writeContainSet(trieList, terms[i], resultSet, containMap);
                        }
                        if(debug){System.out.println(terms[i]+"="+containMap.get(terms[i]));}
                        if(i==0){ // 這裡真的混亂，之後有時間再大改
                            resultSet = containMap.get(terms[i]);
                        }else{
                            // 這是檢查輸入單字中有沒有重複的，專門對付tc3這種測資
                            if(tempSet.contains(terms[i])){
                                continue; 
                            }else{
                                tempSet.add(terms[i]);
                                resultSet = findIntersection(resultSet, containMap.get(terms[i]));
                            }
                        }
                    }
                    if(debug){System.out.println("intersection="+resultSet);}

                }else if(terms[1].equals("OR")){
                    HashSet<String> tempSet = new HashSet<String>();
                    for (int i = 0; i < terms.length; i+=2){
                        if(!containMap.containsKey(terms[i])){
                            writeContainSet(trieList, terms[i], resultSet, containMap);
                        }
                        if(debug){System.out.println(terms[i]+"="+containMap.get(terms[i]));}
                        if(i==0){ 
                            resultSet = containMap.get(terms[i]);
                        }else{
                            // 這是檢查輸入單字中有沒有重複的，專門對付tc3這種測資
                            if(tempSet.contains(terms[i])){
                                continue; 
                            }else{
                                tempSet.add(terms[i]);
                                resultSet = findUnion(resultSet, containMap.get(terms[i]));
                            }
                        }
                    }
                    if(debug){System.out.println("union="+resultSet);}
                }
                List<double[]> tfidfPair = new ArrayList<double[]>();
                Comparator<double[]> tfidfPairComparator = new Comparator<double[]>() {
                    @Override
                    // 助教根本沒提到如果tfidf一樣大的時候要怎麼排序，
                    // 我自己猜依照文本編號排序還真的對了= =
                    public int compare(double[] e1, double[] e2) {
                        if(e1[0]>e2[0]){
                            return -1;
                        }else if(e1[0]==e2[0]){
                            if(e1[1]>e2[1]){
                                return 1;
                            }else{
                                return -1;
                            }
                        }else{
                            return 1;
                        }
                    }
                };
                for (int i = 0; i < resultSet.size(); i++){
                    if(debug){
                        for (int j = 0; j < terms.length; j+=2){
                            System.out.print("TFIDF("+ terms[j]+","+resultSet.get(i)+ ") is " +
                            tfIdfCalculate(trieList.get(resultSet.get(i)),
                            trieList, terms[j], containMap)
                            + "\n");
                        }
                    }
                    double tfidfSum = 0.0;
                    for (int j = 0; j < terms.length; j+=2){
                        tfidfSum += tfIdfCalculate(
                                    trieList.get(resultSet.get(i)),trieList, terms[j], containMap);
                    }
                    tfidfPair.add(new double[]{tfidfSum, (double)resultSet.get(i)});
                    if(debug){
                        System.out.print("BEFORE SORT:[");
                        for(int k = 0; k < tfidfPair.size(); k++)
                            System.out.print((int)tfidfPair.get(k)[1] + ",");
                        System.out.print("]\n");
                    }
                }
                if(debug){
                    System.out.print("BEFORE SORT:[");
                    for(int k = 0; k < N; k++)
                        System.out.print((int)tfidfPair.get(k)[1] + ",");
                    System.out.print("]\n");
                    System.out.print("BEFORE SORT:[");
                    for(int k = 0; k < N; k++)
                        System.out.print(String.format("%.5f", (int)tfidfPair.get(k)[0]) + ",");
                    System.out.print("]\n");
                }

                tfidfPair.sort(tfidfPairComparator);
                if(debug){
                    System.out.print("AFTER SORT:[");
                    for(int k = 0; k < N; k++)
                        System.out.print((int)tfidfPair.get(k)[1] + ",");
                    System.out.print("]\n");
                    System.out.print("AFTER SORT:[");
                    for(int k = 0; k < N; k++)
                        System.out.print(String.format("%.5f", (int)tfidfPair.get(k)[0]) + ",");
                    System.out.print("]\n");
                }

                File file = new File(outputFile);
                try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8, true)) {
                    for (int k=0; k<N; k++){
                        if(k<tfidfPair.size()){
                            writer.write((int)tfidfPair.get(k)[1] + " ");
                        }else{
                            writer.write(-1 + " ");
                        }
                    }
                    writer.write("\n");
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        ois.close();
        fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }
}

class TrieNode{
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord = false;
    int wordCount = 0;
}

class Trie{
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
}
