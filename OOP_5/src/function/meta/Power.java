package function.meta;

import function.FunctionImpl;

public class Power implements FunctionImpl {

    private FunctionImpl base;
    private double degree;

    public Power(FunctionImpl base, double degree) {
        this.base = base;
        this.degree = degree;
    }

    @Override
    public double getLeftDomainBorder() {
        return base.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return base.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return Math.pow(base.getFunctionValue(x), degree);
    }
}
