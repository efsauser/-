import java.io.Serializable;
import java.util.List;

public class Indexer implements Serializable{
    private static final long serialVersionUID = 1L;
    private List<Trie> trieListOfDocs;

    public Indexer(List<Trie> trieListOfDocs) {
        // this.docContent = docContent;
        // this.docTrie = docTrie;
        this.trieListOfDocs = trieListOfDocs;
    }

    public String getDocContent(int docID) {
        return this.trieListOfDocs.get(docID).doc;
    }

    public List<Trie> getTrieListOfDocs() {
        return this.trieListOfDocs;
    }

    public boolean search(String word) {
        return trieListOfDocs.get(0).search(word);
    }
}

class TrieNode implements Serializable{
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord = false;
    int wordCount = 0;
}

class Trie implements Serializable{
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