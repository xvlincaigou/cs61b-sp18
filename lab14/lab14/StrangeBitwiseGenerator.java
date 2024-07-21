package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator{
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        this.state = - 1;
    }

    private double normalize(int state) {
        return - 1.0 + 2.0 * (state % period) / period;
    }

    @Override
    public double next() {
        this.state += 1;
        //int weirdState = (state & (state >> 3)) % period;
        int weirdState = state & (state >> 3) & (state >> 8) % period;
        return normalize(weirdState);
    }
}
