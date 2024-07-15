import edu.princeton.cs.introcs.In;
import java.util.List;
import java.util.ArrayList;

public class TrieTester {
    public static void main(String[] args) {
        // 从文件中读取单词
        In in = new In("words.txt");
        List<String> wordList = new ArrayList<>();

        while (!in.isEmpty()) {
            String word = in.readString();
            wordList.add(word);
        }

        // 创建 Trie 并插入单词
        Trie trie = new Trie();
        trie.buildTrie(wordList);

        // 测试一些单词是否在 Trie 中
        String[] testWords = {"example", "test", "trie", "word", "notaword"};

        for (String word : testWords) {
            if (trie.isValidWord(word)) {
                System.out.println(word + " is a valid word.");
            } else {
                System.out.println(word + " is not a valid word.");
            }
        }
    }
}