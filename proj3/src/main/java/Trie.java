import java.util.*;

public class Trie {
    private static class TrieNode {
        private ArrayList<TrieNode> children;
        private ArrayList<Long> indexs;
        private Character word;

        private TrieNode() {
            this.children = new ArrayList<>();
            this.indexs = new ArrayList<>();
            this.word = null;
        }

        private TrieNode(Character word) {
            this.children = new ArrayList<>();
            this.indexs = new ArrayList<>();
            this.word = word;
        }
    }

    public TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    private static String getCleanedName(String originalName) {
        StringBuilder cleanedName = new StringBuilder();
        for (char c : originalName.toCharArray()) {
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == ' ') {
                cleanedName.append(c);
            }
        }
        return cleanedName.toString().toLowerCase();
    }
    public void insert(long index, GraphDB.Node node) {
        TrieNode p = root;
        String cleanedName = getCleanedName(node.name);
        for (int i = 0; i < cleanedName.length(); ++ i) {
            char c = cleanedName.charAt(i);
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
        p.indexs.add(index);
    }

    public List<Long> completeWords(String prefix) {
        TrieNode p = root;
        for (int i = 0; i < prefix.length(); ++ i) {
            char c = prefix.charAt(i);
            for (TrieNode child : p.children) {
                if (child.word == c) {
                    p = child;
                    break;
                }
                return new ArrayList<>();
            }
        }
        List<Long> completeWords = new ArrayList<>();
        LinkedList<TrieNode> statesQueue = new LinkedList<>();
        statesQueue.addFirst(p);
        while (!statesQueue.isEmpty()) {
            TrieNode currentState = statesQueue.pollFirst();
            if (!currentState.indexs.isEmpty())
                completeWords.addAll(currentState.indexs);
            for (TrieNode child : currentState.children)
                statesQueue.addFirst(child);
        }
        return completeWords;
    }

    public List<Long> completeWordsPrecisely(String word) {
        TrieNode p = root;
        for (int i = 0; i < word.length(); ++ i) {
            char c = word.charAt(i);
            for (TrieNode child : p.children) {
                if (child.word == c) {
                    p = child;
                    break;
                }
                return new ArrayList<>();
            }
        }
        return p.indexs;
    }
}
