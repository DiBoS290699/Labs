import function.*;
import function.basic.Cos;
import function.basic.Sin;

public class Main {
    public static void main(String[] args) {
        double[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("The LinkedListTabulatedFunction:");
        LinkedListTabulatedFunction f = new LinkedListTabulatedFunction(0, 9, values);
        for (FunctionPoint p : f) {
            System.out.println(p);
        }

        System.out.println("The ArrayTabulatedFunction:");
        ArrayTabulatedFunction f1 = new ArrayTabulatedFunction(1, 10, values);
        for (FunctionPoint p : f1) {
            System.out.println(p);
        }

        //-------------------------------------------------------------------------------------------
        Function f2 = new Cos();
        TabulatedFunction tf;
        tf = TabulatedFunctions.tabulate(f2, 0, Math.PI, 11);
        System.out.print(tf);
        TabulatedFunctions.setTabulatedFunctionFactory(new
                LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f2, 0, Math.PI, 11);
        System.out.print(tf);
        TabulatedFunctions.setTabulatedFunctionFactory(new
                ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f2, 0, Math.PI, 11);
        System.out.println(tf);

        //-------------------------------------------------------------------------------------------
        System.out.print("Test methods with the parameter Class:");
        TabulatedFunction f3;

        f3 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.print(f3);

        f3 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.print(f3);

        f3 = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(0, 0),
                        new FunctionPoint(10, 10)
                }
        );
        System.out.print(f3);

        f3 = TabulatedFunctions.tabulate(
                LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.print(f3);

    }
}
