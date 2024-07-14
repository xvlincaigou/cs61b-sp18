import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        for (char symbol : inputSymbols)
            frequencyTable.put(symbol, frequencyTable.getOrDefault(symbol, 0) + 1);
        return frequencyTable;
    }

    private static BitSequence charToBitSequence(char symbol) {
        BitSequence bitSequence = new BitSequence();
        // 只遍历char的低8位
        for (int i = 7; i >= 0; i--) {
            // 检查第i位是否为1
            int bit = (symbol >> i) & 1;
            bitSequence = bitSequence.appended(bit);
        }
        return bitSequence;
    }

    public static void main(String[] args) {
        // Read the file
        char[] symbols = FileUtils.readFile(args[0]);
        // Build the frequency table
        Map<Character, Integer> frequencyTable = buildFrequencyTable(symbols);
        // Build the Huffman trie
        BinaryTrie trie = new BinaryTrie(frequencyTable);
        // Write the trie to the file
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        ow.writeObject(trie);
        ow.writeObject(symbols.length);
        // Build the lookup table
        Map<Character, BitSequence> lookupTable = trie.buildLookupTable();
        // Encode the characters
        List<BitSequence> bitSequences = new ArrayList<>();
        for (char symbol : symbols) {
            Match match = trie.longestPrefixMatch(charToBitSequence(symbol));
            BitSequence bitSequence = lookupTable.get(match.getSymbol());
            bitSequences.add(bitSequence);
        }
        // Assemble all BitSequences into one large BitSequence
        BitSequence assembledSequence = BitSequence.assemble(bitSequences);
        // Write the assembled BitSequence to the output file
        ow.writeObject(assembledSequence);
    }
}
