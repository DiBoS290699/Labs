package function;

import java.io.*;

public final class TabulatedFunctions {

    private TabulatedFunctions() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Нельзя создать объект класса TabulatedFunctions!");
    }

    public static TabulatedFunctionImpl tabulate(FunctionImpl function, double leftX, double rightX, int pointsCount)
            throws IllegalArgumentException {

        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder() || pointsCount < 2) {
            throw new IllegalArgumentException("Некорректные аргументы.");
        }

        FunctionPoint[] fpMas = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i) {
            double x = leftX + step * i;
            fpMas[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }

        return new ArrayTabulatedFunction(fpMas);
    }

    public static void outputTabulatedFunction(TabulatedFunctionImpl function, OutputStream out) {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            int pointsCount = function.getPointsCount();
            dos.writeInt(pointsCount);
            for (int i = 0; i < pointsCount; ++i) {
                dos.writeDouble(function.getPointX(i));
                dos.writeDouble(function.getPointY(i));
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static TabulatedFunctionImpl inputTabulatedFunction(InputStream in) {
        try (DataInputStream dis = new DataInputStream(in)) {
            int pointsCount = dis.readInt();
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; ++i) {
                points[i] = new FunctionPoint(dis.readDouble(), dis.readDouble());
            }
            return new ArrayTabulatedFunction(points);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void writeTabulatedFunction(TabulatedFunctionImpl function, Writer out) {
        try (BufferedWriter w = new BufferedWriter(out)) {
            int pointsCount = function.getPointsCount();
            w.write(pointsCount + " ");

            for (int i = 0; i < pointsCount; ++i) {
                w.write(function.getPointX(i) + " " + function.getPointY(i) + " ");
            }

        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static TabulatedFunctionImpl readTabulatedFunction(Reader in) {

    }

}
