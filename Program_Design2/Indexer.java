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

    // �C�@��Trie�������@�Ӥ奻(��ӧڮ��Ӱ��F��L�ơA�o�̥i�ण�ӹ�F)
    public void setDoc(String doc){
        this.doc = doc;
    }

    // ���J�@�ӳ�r�� Trie
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

    // �j�M Trie ���O�_�s�b�ӳ�r
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

    // ��search�X�G�@�ˡA�u�O�o�|�]�w�o�Ӹ��`�I��wordCount
    // �Psearch�X�G�@�˪���]�N�O���F��츭�l
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

    // ��search�X�G�@�ˡA�u�O�o�|�^��wordCount
    // �Psearch�X�G�@�˪���]�N�O���F��츭�l
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