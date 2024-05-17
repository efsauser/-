import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCounter {

    public static void main(String[] args) {
        String text = "This is a sample text. This text is for testing purposes. This example shows how to count word occurrences.";
        String word = "This";

        int count = countWordOccurrences(text, word);
        int totalWords = countTotalWords(text);

        System.out.println("The word \"" + word + "\" appears " + count + " times in the text.");
        System.out.println("Total number of words in the text: " + totalWords);
    }

    public static int countWordOccurrences(String text, String word) {
        // 使用正則表達式來匹配整個單詞，忽略大小寫
        String regex = "\\b" + Pattern.quote(word) + "\\b";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        int count = 0;
        while (matcher.find()) {
            count++;
        }

        return count;
    }

    public static int countTotalWords(String text) {
        // 使用正則表達式來匹配單詞
        String regex = "\\b\\w+\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        int count = 0;
        while (matcher.find()) {
            count++;
        }

        return count;
    }
}
