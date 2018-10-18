package function;

import function.meta.*;

public final class Functions {

    private Functions() {
        throw new UnsupportedOperationException("Нельзя создать объект класса Functions!");
    }

    public static FunctionImpl shift(FunctionImpl f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static FunctionImpl scale(FunctionImpl f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static FunctionImpl power(FunctionImpl f, double power) {
        return new Power(f, power);
    }

    public static FunctionImpl sum(FunctionImpl f1, FunctionImpl f2) {
        return new Sum(f1, f2);
    }

    public static FunctionImpl mult(FunctionImpl f1, FunctionImpl f2) {
        return new Mult(f1, f2);
    }

    public static FunctionImpl composition(FunctionImpl f1, FunctionImpl f2) {
        return new Composition(f1, f2);
    }
}
