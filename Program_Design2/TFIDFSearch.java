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

    // �����ƻs��TFIDFCalculator
    public static double tf(Trie docTrie, String term) {
        
        int number_term_in_doc = 0;
        int totalWords = 0;
        /* �p�G�o�Ӥ奻�̭��S�����w����r
           �h�����M����Ӥ奻�A�p�ơA����return 0.0�N�n
           ���L���ꤤ�n���X�G�S���o�ت��p... */
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

    // �ثe�٨S�B�zIDF��0�����p
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
        // (B-A) U A = B U A || A,B�O���X�AU�O�p���Ÿ�
        // �o�Ӱ��k�����I�O�|�d��set������
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
        // Ū����
        FileInputStream fis = new FileInputStream(inputFile+".ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Indexer deserializedIdx = (Indexer) ois.readObject();
        List<Trie> trieList = deserializedIdx.getTrieListOfDocs();

        /* Ū���� */
        //�p�G�į�ܮt���ܴN�令HashSet?
        List<String> termList = new ArrayList<String>();
        List<Integer> numberList = new ArrayList<Integer>();
        try(BufferedReader br = new BufferedReader(new FileReader(testcaseFile, StandardCharsets.UTF_8))){
            String line = br.readLine();
            int N = Integer.parseInt(line);
            while((line = br.readLine()) != null){
                // Ū�ĤG�椧��A�]�N�O�ؼг�r
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
                        if(i==0){ // �o�̯u���V�áA���ᦳ�ɶ��A�j��
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
