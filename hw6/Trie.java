import java.util.ArrayList;
import java.util.List;

public class Trie {
    private static class TrieNode {
        private ArrayList<TrieNode> children;
        private boolean isWord;
        private Character word;

        private TrieNode() {
            children = new ArrayList<>();
            isWord = false;
            word = null;
        }

        private TrieNode(Character word) {
            children = new ArrayList<>();
            isWord = false;
            this.word = word;
        }
    }

    public TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    private void insert(String word) {
        TrieNode p = root;
        for (int i = 0; i < word.length(); ++ i) {
            char c = word.charAt(i);
            boolean findExistedNode = false;
            for (TrieNode child : p.children) {
                if (child.word == c) {
                    p = child;
                    findExistedNode = true;
                    break;
                }
            }
            if (!findExistedNode) {
                TrieNode newNode = new TrieNode(c);
                p.children.add(newNode);
                p = newNode;
            }
        }
        p.isWord = true;
    }

    public void buildTrie(List<String> wordTable) {
        for (String word : wordTable) {
            insert(word);
        }
    }

    public boolean isValidWord(String word) {
        TrieNode p = root;
        for (int i = 0; i < word.length(); ++ i) {
            char c = word.charAt(i);
            boolean findExistedNode = false;
            for (TrieNode child : p.children) {
                if (child.word == c) {
                    p = child;
                    findExistedNode = true;
                    break;
                }
            }
            if (!findExistedNode) {
                return false;
            }
        }
        return p.isWord;
    }

    public boolean isValidPrefix(String prefix) {
        TrieNode p = root;
        for (int i = 0; i < prefix.length(); ++ i) {
            char c = prefix.charAt(i);
            boolean findExistedNode = false;
            for (TrieNode child : p.children) {
                if (child.word == c) {
                    p = child;
                    findExistedNode = true;
                    break;
                }
            }
            if (!findExistedNode) {
                return false;
            }
        }
        return true;
    }
}
