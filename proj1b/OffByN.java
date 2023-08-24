public class OffByN implements CharacterComparator{
    int n;
    OffByN(int _n){n = _n;}
    @Override
    public boolean equalChars(char x, char y) {
        return (Math.abs(x - y) == n);
    }
}
