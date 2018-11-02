package function.basic;

import function.FunctionImpl;

public class Log implements FunctionImpl {

    private double base;

    public Log(double base) {
        this.base = base;
    }

    public Log() {
        this.base = Math.E;
    }

    @Override
    public double getLeftDomainBorder() {
        return 0;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double getFunctionValue(double x) {
        return Math.log(x) / Math.log(base);
    }

}
