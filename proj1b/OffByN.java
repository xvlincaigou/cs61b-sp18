public class OffByN implements CharacterComparator{
    private int n;
    public OffByN(int _n){n = _n;}
    @Override
    public boolean equalChars(char x, char y) {
        return (Math.abs(x - y) == n);
    }
}
