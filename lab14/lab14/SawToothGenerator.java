package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int period;
    private int state;

    public SawToothGenerator(int period) {
        this.period = period;
        this.state = - 1;
    }

    private double normalize(int state) {
        return - 1.0 + 2.0 * (state % period) / period;
    }

    @Override
    public double next() {
        this.state += 1;
        return normalize(this.state);
    }
}
