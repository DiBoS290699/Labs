package function;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings({"Duplicates", "unused", "WeakerAccess"})
public final class TabulatedFunctions {

    private static TabulatedFunctionFactory tabFuncFact = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Нельзя создать объект класса TabulatedFunctions!");
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount)
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

        return createTabulatedFunction(fpMas);
    }

    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> aClass,
                                             Function function, double leftX, double rightX, int pointsCount)
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

        return createTabulatedFunction(aClass, fpMas);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) {
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

    public static TabulatedFunction inputTabulatedFunction(InputStream in) {
        try (DataInputStream dis = new DataInputStream(in)) {
            int pointsCount = dis.readInt();
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; ++i) {
                points[i] = new FunctionPoint(dis.readDouble(), dis.readDouble());
            }
            return createTabulatedFunction(points);
        }
        catch (IOException e) {
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }
        return null;
    }

    public static TabulatedFunction inputTabulatedFunction(Class<? extends TabulatedFunction> aClass, InputStream in) {
        try (DataInputStream dis = new DataInputStream(in)) {
            int pointsCount = dis.readInt();
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; ++i) {
                points[i] = new FunctionPoint(dis.readDouble(), dis.readDouble());
            }
            return createTabulatedFunction(aClass, points);
        }
        catch (IOException e) {
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }
        return null;
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) {
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

    public static TabulatedFunction readTabulatedFunction(Reader in) {
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
            return createTabulatedFunction(fpMas);
        }

        catch (IOException e) {
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static TabulatedFunction readTabulatedFunction(Class<? extends TabulatedFunction> aClass, Reader in) {
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
            return createTabulatedFunction(aClass, fpMas);
        }

        catch (IOException e) {
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newTabFuncFact) {
        tabFuncFact = newTabFuncFact;
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return tabFuncFact.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return tabFuncFact.createTabulatedFunction(points);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return tabFuncFact.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> aClass,
                                                            double leftX, double rightX, double[] values)
                                                            throws IllegalArgumentException {
        try {
            Constructor<? extends TabulatedFunction> constructor =
                    aClass.getConstructor(double.class, double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> aClass,
                                                            FunctionPoint[] points)
                                                            throws IllegalArgumentException {
        try {
            Constructor<? extends TabulatedFunction> constructor = aClass.getConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object) points);
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> aClass,
                                                            double leftX, double rightX, int pointsCount)
                                                            throws IllegalArgumentException {
        try {
            Constructor<? extends TabulatedFunction> constructor =
                    aClass.getConstructor(double.class, double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
