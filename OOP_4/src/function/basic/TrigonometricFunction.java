package function.basic;

import function.FunctionImpl;

public abstract class TrigonometricFunction implements FunctionImpl {

    @Override
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public abstract double getFunctionValue(double x);
}
