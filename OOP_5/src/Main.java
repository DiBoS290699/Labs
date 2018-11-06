import function.*;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        double[] values = new double[11];
        for(int i = 1; i < 11; ++i)
            values[i] = i;

        ArrayTabulatedFunction arTabFun = new ArrayTabulatedFunction(0, 10, values);
        LinkedListTabulatedFunction linLisTabFun =
                new LinkedListTabulatedFunction(0, 10, values);

        System.out.print("Это объект класса ArrayTabulatedFunction:" + arTabFun);
        System.out.print("Это объект класса LinkedListTabulatedFunction:" + linLisTabFun);

        ArrayTabulatedFunction copyArTabFun = arTabFun;
        ArrayTabulatedFunction newArTabFun = new ArrayTabulatedFunction(0, 10, 11);
        LinkedListTabulatedFunction copyLinLisTabFun = linLisTabFun;
        LinkedListTabulatedFunction newLinLisTabFun =
                new LinkedListTabulatedFunction(0, 10, 11);

        System.out.println("--------------------------------------------------------------------------------------");
        System.out.printf("arTabFun == copyArTabFun?  (%s)\n", arTabFun.equals(copyArTabFun));
        System.out.printf("arTabFun == newArTabFun? (%s)\n", arTabFun.equals(newArTabFun));
        System.out.printf("arTabFun == linLisTabFun?  (%s)\n", arTabFun.equals(linLisTabFun));

        System.out.printf("linLisTabFun == copyLinLisTabFun?  (%s)\n", linLisTabFun.equals(copyLinLisTabFun));
        System.out.printf("linLisTabFun == newLinLisTabFun?  (%s)\n", linLisTabFun.equals(newLinLisTabFun));
        System.out.printf("linLisTabFun == arTabFun?  (%s)\n", linLisTabFun.equals(arTabFun));

        System.out.println("--------------------------------------------------------------------------------------");
        System.out.printf("HashCode объекта arTabFun: %d\n", arTabFun.hashCode());
        System.out.printf("HashCode объекта copyArTabFun: %d\n", copyArTabFun.hashCode());
        System.out.printf("HashCode объекта newArTabFun: %d\n", newArTabFun.hashCode());
        System.out.printf("HashCode объекта linLisTabFun: %d\n", linLisTabFun.hashCode());
        System.out.printf("HashCode объекта copyLinLisTabFun: %d\n", copyLinLisTabFun.hashCode());
        System.out.printf("HashCode объекта newLinLisTabFun: %d\n", newLinLisTabFun.hashCode());
        newArTabFun.setPointY(0, 0.001);
        System.out.printf("Новое значение HashCode объекта newArTabFun: %d\n", newArTabFun.hashCode());
        System.out.println("--------------------------------------------------------------------------------------");

        ArrayTabulatedFunction cloneArTabFun = (ArrayTabulatedFunction) arTabFun.clone();
        LinkedListTabulatedFunction cloneLinLisTabFun = (LinkedListTabulatedFunction) linLisTabFun.clone();

        arTabFun.setPointY(0, 1);
        linLisTabFun.setPointY(0, 2);

        System.out.println("Исходный arTabFun:" + arTabFun);
        System.out.println("Клон arTabFun:" + cloneArTabFun);

        System.out.println("Исходный linLisTabFun:" + linLisTabFun);
        System.out.println("Клон linLisTabFun:" + cloneLinLisTabFun);
    }
}
