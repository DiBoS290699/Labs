package function.meta;

import function.FunctionImpl;

public class Scale implements FunctionImpl {

    private FunctionImpl func;
    private double scaleX;
    private double scaleY;

    public Scale(FunctionImpl func, double scaleX, double scaleY) {
        this.func = func;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public double getLeftDomainBorder() {
        return func.getLeftDomainBorder() * scaleX;
    }

    @Override
    public double getRightDomainBorder() {
        return func.getRightDomainBorder() * scaleX;
    }

    @Override
    public double getFunctionValue(double x) {
        return func.getFunctionValue(x * scaleX) * scaleY;
    }
}
