package function.meta;

import function.FunctionImpl;

public class Composition implements FunctionImpl {

    private FunctionImpl outer;
    private FunctionImpl inner;

    public Composition(FunctionImpl outer, FunctionImpl inner) {
        this.outer = outer;
        this.inner = inner;
    }

    @Override
    public double getLeftDomainBorder() {
        return outer.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return outer.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return outer.getFunctionValue(inner.getFunctionValue(x));
    }
}
