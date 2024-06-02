import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BuildIndex{
    public static void main(String[] args) {
        String inputFile = args[0];
        /* 每顆Trie代表一個文本(5個句子的字典樹) */
        List<Trie> trieList = new ArrayList<Trie>();
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

            // 序列化
            Indexer idx = new Indexer(trieList);
            try {
                String s = args[0].replaceAll("txt$", "ser");
                FileOutputStream fos = new FileOutputStream(s);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(idx);
                
                oos.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();	
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

