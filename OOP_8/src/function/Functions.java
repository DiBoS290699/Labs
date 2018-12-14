package function;

import function.meta.*;

public final class Functions {

    private Functions() {
        throw new UnsupportedOperationException("Нельзя создать объект класса Functions!");
    }

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    public static double integrate(Function fun, double leftX, double rightX, double discreteStep) {
        if(leftX < fun.getLeftDomainBorder() || fun.getRightDomainBorder() < rightX)
            throw new IllegalArgumentException("Boundaries of the integration area are out of function domain boundaries.");

        double integralSum = 0;

        while(leftX + discreteStep < rightX) {
            integralSum += fun.getFunctionValue(leftX) * discreteStep +
                    (fun.getFunctionValue(leftX + discreteStep) -  fun.getFunctionValue(leftX))* discreteStep / 2;
            //integralSum += (fun.getFunctionValue(leftX) + fun.getFunctionValue(leftX + discreteStep)) *
                    //discreteStep / 2;
            leftX += discreteStep;
        }
        double temp = rightX - leftX;
        integralSum += fun.getFunctionValue(leftX) * temp +
                (fun.getFunctionValue(leftX + temp) -  fun.getFunctionValue(leftX))* temp / 2;
        //integralSum += (fun.getFunctionValue(leftX) + fun.getFunctionValue(leftX + temp)) * temp / 2;
        return  integralSum;
    }
}

