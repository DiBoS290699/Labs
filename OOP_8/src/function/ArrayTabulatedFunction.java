package function;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable, Cloneable {
    private FunctionPoint[] points;         //Массив точек
    private int countPoints;                //количество точек

    // Конструктор с нулевыми y
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (leftX >= rightX || pointsCount < 2)
            throw new IllegalArgumentException("Error!");

        this.points = new FunctionPoint[pointsCount + pointsCount / 2];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; ++i) {
            this.points[i] = new FunctionPoint(leftX + step * i, 0);
        }

        this.countPoints = pointsCount;
    }

    //Конструктор с конкретными y
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
        int length = values.length;

        if (leftX >= rightX || values.length < 2)
            throw new IllegalArgumentException("Error!");

        this.points = new FunctionPoint[length + length / 2];
        double step = (rightX - leftX) / (length - 1);

        for (int i = 0; i < length; ++i) {
            this.points[i] = new FunctionPoint(leftX + step * i, values[i]);
        }

        this.countPoints = length;
    }

    //Конструктор с заданным массивом точек
    public ArrayTabulatedFunction(FunctionPoint[] fpMas) throws IllegalArgumentException {
        int length = fpMas.length;

        if (length < 2)
            throw new IllegalArgumentException("Error!");

        for (int i = 1; i < length; ++i) {
            if (fpMas[i-1].getX() >= fpMas[i].getX())
                throw new IllegalArgumentException("Error!");
        }
        this.points = new FunctionPoint[length + length/2];
        System.arraycopy(fpMas, 0, this.points, 0, length);
        this.countPoints = length;
    }

    //Гетер левой части области определения
    public double getLeftDomainBorder() {
        return this.points[0].getX();
    }

    //Гетер правой части области определения
    public double getRightDomainBorder() {
        return this.points[countPoints - 1].getX();
    }

    //Гетер количества точек
    public int getPointsCount() {
        return this.countPoints;
    }

    //Гетер точки по индексу
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index >= this.countPoints)
            throw new FunctionPointIndexOutOfBoundsException("Error!");

        return this.points[index];
    }

    //Гетер х точки по индексу
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index >= this.countPoints || index < 0)
            throw new FunctionPointIndexOutOfBoundsException("Error!");

        return this.points[index].getX();
    }

    //Гетер у точки по индексу
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index >= this.countPoints || index < 0)
            throw new FunctionPointIndexOutOfBoundsException("Error!");

        return this.points[index].getY();
    }

    //Поиск индекса точки по х (либо соседней точки слева)
    private int searchPoint(double x) {
        int i = 0;

        while (getPointX(i) < x)
            ++i;

        if (getPointX(i) == x) return i;

        else return i - 1;
    }

    //Гетер значения функции в точке по х
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        int i = searchPoint(x);
        if (this.points[i].getX() == x) return this.points[i].getY();

        return (x - this.points[i].getX()) * (this.points[i + 1].getY() - this.points[i].getY()) /
                (this.points[i + 1].getX() - this.points[i].getX()) + this.points[i].getY();
    }

    //Сетер точки по индексу
    public void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index >= this.countPoints)
            throw new FunctionPointIndexOutOfBoundsException("Invalid count points!");

        if (index == 0) {
            if (this.points[1].getX() < point.getX())
                throw new InappropriateFunctionPointException("Violation of the sequence!");    //Нарушение последовательности
            else this.points[index] = new FunctionPoint(point);
        }
        if(index == this.countPoints - 1) {
            if (this.points[index - 1].getX() > point.getX())
                throw new InappropriateFunctionPointException("Violation of the sequence!");    //Нарушение последовательности
            else this.points[index] = new FunctionPoint(point);
        }
        else {
            if (this.points[index - 1].getX() > point.getX() || this.points[index + 1].getX() < point.getX())
                throw new InappropriateFunctionPointException("Violation of the sequence!");    //Нарушение последовательности
            else this.points[index] = new FunctionPoint(point);
        }
    }

    //Сетер х точки по индексу
    public void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index >= this.countPoints)
            throw new FunctionPointIndexOutOfBoundsException("Invalid count points!");

        if (index == 0) {
            if (this.points[1].getX() < x)
                throw new InappropriateFunctionPointException("Violation of the sequence!");    //Нарушение последовательности
            else this.points[index].setX(x);
        }
        if(index == this.countPoints - 1) {
            if (this.points[index - 1].getX() > x)
                throw new InappropriateFunctionPointException("Violation of the sequence!");    //Нарушение последовательности
            else this.points[index].setX(x);
        }
        else {
            if (this.points[index - 1].getX() > x || this.points[index + 1].getX() < x)
                throw new InappropriateFunctionPointException("Violation of the sequence!");    //Нарушение последовательности
            else this.points[index].setX(x);
        }
    }

    //Сетер у точки по индексу
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index >= this.countPoints)
            throw new FunctionPointIndexOutOfBoundsException("Error!");

        this.points[index].setY(y);
    }

    //Удаление точки по индексу
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (this.countPoints < 3)
            throw new IllegalStateException("Deletion not possible! There are two points!");

        if (index >= this.countPoints)
            throw new FunctionPointIndexOutOfBoundsException("Calling a point on an incorrect index!");

        if (this.countPoints - 1 - index >= 0)
            System.arraycopy(points, index + 1, this.points, index, this.countPoints - 1 - index);

        this.points[countPoints] = new FunctionPoint(Double.NaN, Double.NaN);
        --this.countPoints;
    }

    //Добавление точки
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (getLeftDomainBorder() > point.getX() || getRightDomainBorder() < point.getX())
            return;

        int ind = searchPoint(point.getX());

        if (point.getX() == getPointX(ind))    //При совпадении значения х ничего не происходит
            throw new InappropriateFunctionPointException("Error!");

        if (this.points.length == this.countPoints) {     //Если такого х нет и в массиве нехватает памяти, то эта точка вставляется согласно порядку х
            FunctionPoint temp[] = new FunctionPoint[this.countPoints + this.countPoints / 2];
            System.arraycopy(this.points, 0, temp, 0, ind + 1);   //С массива points с 0 индекса в массив temp с 0 индекса ind+1 элементов
            System.arraycopy(this.points, ind + 1, temp, ind + 2, this.countPoints - ind - 1);
            //Добавление остальных точек (temp[ind+1] пустая)
            temp[ind + 1] = point;
            this.points = temp;
        } else {  //Если совпадений с х не найдено и в массиве ещё есть место, то сдвигаем вправо начиная с ind+1

            if (this.countPoints - ind + 1 >= 0)
                System.arraycopy(this.points, ind + 1, this.points, ind + 2, this.countPoints - ind + 1);

            this.points[ind + 1] = point;
        }
        this.countPoints++;
    }

    //Вывод точек из массива
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("\nArray:");

        for (int i = 0; i < this.countPoints; ++i) {
            str.append(getPoint(i).toString()).append(" ");
        }

        str.append('\n');
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)      //Быстрая проверка на идентичность
            return true;

        if (!(o instanceof TabulatedFunction))  //Если не реализует интерфейс - то false
            return false;

        if (o instanceof ArrayTabulatedFunction) {  //Если приводится к классу, то происходит проверка
            ArrayTabulatedFunction temp = (ArrayTabulatedFunction) o;

            if (temp.countPoints != this.countPoints)
                return false;

            for (int i = 0; i < this.countPoints; ++i) {
                if (!temp.points[i].equals(this.points[i]))
                    return false;
            }

            return true;
        } else {    //Если приводится к другому классу, реализующему интерфейс - то
                    //происходит проверка черезе гетеры
            if (((TabulatedFunction) o).getPointsCount() != this.countPoints)
                return false;

            for (int i = 0; i < this.countPoints; ++i) {
                if (!((TabulatedFunction) o).getPoint(i).equals(this.points[i]))
                    return false;
            }

            return true;
        }
    }

    @Override
    public int hashCode() {
        int hashPoints = 0;
        for (int i = 0; i < countPoints; i++) {
            hashPoints += points[i].hashCode();
        }
        return countPoints + hashPoints;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        int length = this.countPoints;
        FunctionPoint[] fps = new FunctionPoint[length];
        for (int i = 0; i < length; ++i) {
            fps[i] = (FunctionPoint) this.points[i].clone();
        }
        try {
            return new ArrayTabulatedFunction(fps);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return super.clone();
    }

    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<>() {
            private int index = -1;

            @Override
            public boolean hasNext() {
                return index + 1 != countPoints;
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    //Следующий элемент не существует
                    throw new NoSuchElementException("Next element does not exist.");
                }
                return new FunctionPoint(points[++index]);
            }

            @Override
            public void remove() {  //Операция remove не поддерживается
                throw new UnsupportedOperationException("Operation remove is unsupported");
            }
        };
    }

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] fpMas) {
            return new ArrayTabulatedFunction(fpMas);
        }
    }
}
