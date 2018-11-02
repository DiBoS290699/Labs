package function.meta;

import function.FunctionImpl;

public class Mult implements FunctionImpl {
    private FunctionImpl f1;
    private FunctionImpl f2;

    public Mult(FunctionImpl f1, FunctionImpl f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    public double getLeftDomainBorder() {
        return f1.getLeftDomainBorder() >= f2.getLeftDomainBorder() ?
                f1.getLeftDomainBorder() : f2.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return f1.getRightDomainBorder() <= f2.getRightDomainBorder() ?
                f1.getRightDomainBorder() : f2.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return f1.getFunctionValue(x) * f2.getFunctionValue(x);
    }
}
