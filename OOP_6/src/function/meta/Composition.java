package function.meta;

import function.Function;

public class Composition implements Function {

    private Function outer;
    private Function inner;

    public Composition(Function outer, Function inner) {
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
