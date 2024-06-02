import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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

    // 目前還沒處理IDF為0的情況
    public static double idf(List<Trie> trieList, String term, HashMap<String, List<Integer>> containMap) {
        
        int number_doc_contain_term = 0;
        if(!containMap.containsKey(term)){
            List<Integer> tempSet = new ArrayList<Integer>();
            for(int i=0; i<trieList.size(); i++){
                if(trieList.get(i).search(term)){
                    tempSet.add(i);
                    number_doc_contain_term++;
                }
            }
            containMap.put(term, tempSet);
        }else{
            number_doc_contain_term = containMap.get(term).size();
        }
        return Math.log(trieList.size() / (double)number_doc_contain_term);
    }

    // public static double tfIdfCalculate(Trie docTrie, List<Trie> trieList, 
    //                                     String term, Trie allWordsTrie) {
    public static double tfIdfCalculate(Trie docTrie, List<Trie> trieList, 
                                        String term, HashMap<String, List<Integer>> containMap) {
        return tf(docTrie, term) * idf(trieList, term, containMap);
    }

    public static void writeContainSet(List<Trie> trieList,String term, 
                                       List<Integer> set1, HashMap<String, List<Integer>> containMap){
        List<Integer> tempSet = new ArrayList<Integer>();
        for(int i=0; i<trieList.size(); i++){
            if(trieList.get(i).search(term)){
                tempSet.add(i);
            }
        }
        containMap.put(term, tempSet);
    }

    public static List<Integer> findIntersection(List<Integer> set1, List<Integer> set2){
        List<Integer> intersectionSet = new ArrayList<Integer>();
        intersectionSet.addAll(set1);
        intersectionSet.retainAll(set2);
        return intersectionSet;
    }

    public static List<Integer> findUnion(List<Integer> set1, List<Integer> set2){
        // (B-A) U A = B U A || A,B是集合，U是聯集符號
        // 這個做法的缺點是會搞亂set的順序
        List<Integer> unionSet = new ArrayList<Integer>();
        unionSet.addAll(set1);
        unionSet.removeAll(set2);
        unionSet.addAll(set2);
        return unionSet;
    }

    public static void main(String[] args) {
    String inputFile = args[0];
    String testcaseFile = args[1]; 

    Trie allWordsTrie = new Trie();
    HashMap<String, List<Integer>> containMap= new HashMap<String, List<Integer>>();
    List<Double> TFIDFList = new ArrayList<Double>();
    try {
        // 讀測資
        FileInputStream fis = new FileInputStream(inputFile+".ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Indexer deserializedIdx = (Indexer) ois.readObject();
        List<Trie> trieList = deserializedIdx.getTrieListOfDocs();

        /* 讀測資 */
        //如果效能很差的話就改成HashSet?
        List<String> termList = new ArrayList<String>();
        List<Integer> numberList = new ArrayList<Integer>();
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
                }else if(terms[1].equals("AND")){
                    for (int i = 0; i < terms.length; i+=2){
                        if(!containMap.containsKey(terms[i])){
                            writeContainSet(trieList, terms[i], resultSet, containMap);
                        }
                        System.out.println(terms[i]+"="+containMap.get(terms[i]));
                        if(i==0){ // 這裡真的混亂，之後有時間再大改
                            resultSet = containMap.get(terms[i]);
                        }else{
                            resultSet = findIntersection(resultSet, containMap.get(terms[i]));
                        }
                    }
                    System.out.println("intersection="+resultSet);
                }else if(terms[1].equals("OR")){
                    for (int i = 0; i < terms.length; i+=2){
                        if(!containMap.containsKey(terms[i])){
                            writeContainSet(trieList, terms[i], resultSet, containMap);
                        }
                        System.out.println(terms[i]+"="+containMap.get(terms[i]));
                        if(i==0){ 
                            resultSet = containMap.get(terms[i]);
                        }else{
                            resultSet = findUnion(resultSet, containMap.get(terms[i]));
                        }
                    }
                    resultSet.sort(Comparator.naturalOrder());
                    System.out.println("union="+resultSet);
                }
                for (int i = 0; i < resultSet.size(); i++){
                    // System.out.println("parameter:"+"trieList.get("+resultSet.get(i)+"),trieList,"+terms[2*i]+",containMap");
                    for (int j = 0; j < terms.length; j+=2){
                        System.out.print("TFIDF("+ terms[j]+","+resultSet.get(i)+ ") is " +
                                        tfIdfCalculate(trieList.get(resultSet.get(i)),
                                        trieList, terms[j], containMap)
                                        + "\n");
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        
        // System.out.println(deserializedIdx.getDocContent(0));
        // System.out.println(deserializedIdx.search("know"));


        ois.close();
        fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }
}
