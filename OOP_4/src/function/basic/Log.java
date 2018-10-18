package function.basic;

import function.FunctionImpl;

public class Log implements FunctionImpl {

    private double base = Math.E;

    public Log(double base) {
        this.base = base;
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
