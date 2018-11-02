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
            e.printStackTrace();
            //System.out.println(e.getMessage());
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
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }
        return null;
    }

    public static void writeTabulatedFunction(TabulatedFunctionImpl function, Writer out) {
        try (BufferedWriter bw = new BufferedWriter(out)) {
            int pointsCount = function.getPointsCount();
            bw.write(pointsCount + " ");

            for (int i = 0; i < pointsCount; ++i) {
                bw.write(function.getPointX(i) + " " + function.getPointY(i) + " ");
            }

        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static TabulatedFunctionImpl readTabulatedFunction(Reader in) {
        try {
            StreamTokenizer stream = new StreamTokenizer(in);
            stream.nextToken();
            int pointsCount = (int) stream.nval;
            FunctionPoint[] fpMas = new FunctionPoint[pointsCount];
            for (int i = 0; stream.nextToken() != StreamTokenizer.TT_EOF; ++i) {
                double x = stream.nval;
                stream.nextToken();
                double y = stream.nval;
                fpMas[i] = new FunctionPoint(x, y);
            }
            return new ArrayTabulatedFunction(fpMas);
        }

        catch (IOException e) {
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

}
