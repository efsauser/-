import java.io.Serializable;
import java.util.List;

public class Indexer implements Serializable{
    private static final long serialVersionUID = 1L;
    private List<String> listOfContent;

    public Indexer(List<String> listOfContent) {
        this.listOfContent = listOfContent;
    }

    public List<String> getListOfContent() {
        return this.listOfContent;
    }

    public String getDocContent(int docID) {
        return this.listOfContent.get(docID);
    }

}

