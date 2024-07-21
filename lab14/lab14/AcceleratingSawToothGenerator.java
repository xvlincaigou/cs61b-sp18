package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {

    private int period;
    private double factor;
    private int state;

    public AcceleratingSawToothGenerator(int period, double factor) {
        this.period = period;
        this.factor = factor;
        this.state = 0;
    }

    private double normalize() {
        return -1.0 + 2.0 * (state % period) / period;
    }

    @Override
    public double next() {
        double next = normalize();
        this.state = (this.state + 1) % this.period;
        if (this.state == 0)
            this.period = (int) (this.period * this.factor);
        return next;
    }
}
