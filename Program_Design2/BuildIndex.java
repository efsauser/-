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
        /* �C��Trie�N��@�Ӥ奻(5�ӥy�l���r���) */
        List<Trie> trieList = new ArrayList<Trie>();
        Trie tempTrie = new Trie();
        int counter = 0; // �p��O�_�w�gŪ�J5�ӥy�l
        try(BufferedReader br = new BufferedReader(new FileReader(inputFile, StandardCharsets.UTF_8))){
            String line = "";
            String temp = "";
            while ((line = br.readLine()) != null) {
                // �N�Ҧ��D�^��r���]�]�A�Ʀr�M�Ů�^�H�ťեN��
                line = line.replaceAll("[^a-zA-Z\\s]", " ");
                // �H�Ů�i����J��segmentation
                line = line.replaceAll("\\s+", " ").trim();
                // �N�Ҧ��^��j�g�ഫ���p�g
                line = line.toLowerCase();
                // �N�s�y�l�걵�b�᭱�A�C���y�Φ��@�Ӥ奻
                temp = temp.concat(line+" ");
                counter++;
                if(counter%5==0){
                    // �N�奻���C�ӳ�rinsert��trie��
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

            // �ǦC��
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

