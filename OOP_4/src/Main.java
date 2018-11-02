import function.*;
import function.basic.*;

import java.io.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args)
            throws InappropriateFunctionPointException, IOException, ClassNotFoundException{
        Cos cos = new Cos();
        Sin sin = new Sin();
        System.out.println("Значения Sin [0; 2*pi]: ");
        for (double i = 0; i < Math.PI * 2; i += 0.1) {
            System.out.printf ("%.2f %c", sin.getFunctionValue(i), ' ');
        }

        System.out.println("\nЗначения Cos [0; 2*pi]: ");
        for (double i = 0; i < Math.PI * 2; i += 0.1) {
            System.out.printf ("%.2f %c", cos.getFunctionValue(i), ' ');
        }

        TabulatedFunctionImpl tabSin = TabulatedFunctions.tabulate(sin, 0, Math.PI*2, 10);
        TabulatedFunctionImpl tabCos = TabulatedFunctions.tabulate(cos, 0, Math.PI*2, 10);

        System.out.println("\nЗначения sin [0; 2*pi] через табулированную функцию: ");
        for (double i = 0; i < Math.PI * 2; i += 0.1) {
            System.out.printf ("%.2f %c", tabSin.getFunctionValue(i), ' ');
        }

        System.out.println("\nЗначения cos [0; 2*pi] через табулированную функцию: ");
        for (double i = 0; i < Math.PI * 2; i += 0.1) {
            System.out.printf ("%.2f %c", tabCos.getFunctionValue(i), ' ');
        }

        TabulatedFunctionImpl sumSinCos =
                TabulatedFunctions.tabulate(Functions.sum(Functions.power(sin, 2), Functions.power(cos, 2)),
                        0, Math.PI*2, 10);

        System.out.println("\nЗначения sin^2(x) + cos^2(x) [0; 2*pi]: ");

        for (double i = 0; i < Math.PI * 2; i += 0.1) {
            System.out.printf ("%.2f %c", sumSinCos.getFunctionValue(i), ' ');
        }

        TabulatedFunctionImpl tabExp = TabulatedFunctions.tabulate(new Exp(), 0, 10, 11);
        FileWriter fin = new FileWriter("File.txt");
        TabulatedFunctions.writeTabulatedFunction(tabExp, fin);
        FileReader fout = new FileReader("File.txt");
        TabulatedFunctionImpl tabExp2 = TabulatedFunctions.readTabulatedFunction(fout);

        System.out.println("\nЗначения исходной exp [0; 10]:" + tabExp);

        System.out.println("Значения считаной exp [0; 10]: " + tabExp2);

        TabulatedFunctionImpl tabLog = TabulatedFunctions.tabulate(new Log(), 0, 10, 11);
        FileOutputStream fbin = new FileOutputStream("File1.txt");
        TabulatedFunctions.outputTabulatedFunction(tabLog, fbin);
        FileInputStream fbout = new FileInputStream("File1.txt");
        TabulatedFunctionImpl tabLog2 = TabulatedFunctions.inputTabulatedFunction(fbout);

        System.out.println("\nЗначения исходного log [0; 10]: " + tabLog);

        System.out.println("Значения считаного log [0; 10]: " + tabLog2);

        TabulatedFunctionImpl tabLogExp=
                TabulatedFunctions.tabulate(Functions.composition(new Log(), new Exp()), 0,10, 11 );
        FileOutputStream FSerOut = new FileOutputStream("Ser.txt");
        ObjectOutputStream oos = new ObjectOutputStream(FSerOut);
        oos.writeObject(tabLogExp);
        oos.close();
        FileInputStream FSerIn = new FileInputStream("Ser.txt");
        ObjectInputStream oin = new ObjectInputStream(FSerIn);
        TabulatedFunctionImpl tabLogExp2 = (TabulatedFunctionImpl) oin.readObject();
        System.out.println("Исходная сериализумеая функция: " + tabLogExp);
        System.out.println("Исходная сериализуемая функция: " + tabLogExp2);
        oin.close();
    }
}
