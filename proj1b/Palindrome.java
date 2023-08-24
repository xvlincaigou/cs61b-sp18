public class Palindrome {
    public Deque<Character> wordToDeque(String word){
        Deque<Character> p = new LinkedListDeque<>();
        for(int i = 0;i < word.length();++ i){
            p.addLast(word.charAt(i));
        }
        return p;
    }
    public boolean isPalindrome(String word){
        Deque<Character> p = wordToDeque(word);
        if(p.size() <= 1){
            return true;
        }
        for(int i = 0;2 * i <= p.size();++ i){
            if(!(p.get(i) == p.get(p.size() - 1 - i))){
                return false;
            }
        }
        return true;
    }
    public boolean isPalindrome(String word,CharacterComparator cc){
        Deque<Character> p = wordToDeque(word);
        if(p.size() <= 1){
            return true;
        }
        for(int i = 0;2 * i <= p.size();++ i){
            if((!(cc.equalChars(p.get(i),p.get(p.size() - 1 - i))))&&(2 * i + 1 != p.size())){
                return false;
            }
        }
        return true;
    }
}
