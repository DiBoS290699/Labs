package functions;

public class TabulatedFunction {
    private FunctionPoint points[];         //Массив точек
    private int countPoints;                //количество точек

    // Конструктор с нулевыми y
    public TabulatedFunction(double leftX, double rightX, int pointsCount) {
        this.points = new FunctionPoint[pointsCount + pointsCount/2];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i){
            this.points[i] = new FunctionPoint(leftX + step*i, 0);
        }
        this.countPoints = pointsCount;
    }

    //Конструктор с конкретными y
    public TabulatedFunction(double leftX, double rightX, double[] values) {
        int length = values.length;
        this.points = new FunctionPoint[length + length/2];
        double step = (rightX - leftX) / (length - 1);
        for (int i = 0; i < length; ++i) {
            this.points[i] = new FunctionPoint(leftX + step*i, values[i]);
        }
        this.countPoints = length;
    }

    //Гетер левой части области определения
    public double getLeftDomainBorder() { return this.points[0].getX(); }

    //Гетер правой части области определения
    public double getRightDomainBorder() { return this.points[countPoints - 1].getX();}

    //Гетер количества точек
    public int getPointsCount() { return this.countPoints; }

    //Гетер точки по индексу
    public FunctionPoint getPoint(int index) { return this.points[index]; }

    //Гетер х точки по индексу
    public double getPointX(int index) { return this.points[index].getX(); }

    //Гетер у точки по индексу
    public double getPointY(int index) { return this.points[index].getY(); }

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
        return (x - this.points[i].getX()) * (this.points[i + 1].getY() - this.points[i].getY()) / (this.points[i + 1].getX() - this.points[i].getX())+this.points[i].getY();
    }

    //Сетер точки по индексу
    public void setPoint(int index, FunctionPoint point) {
        if (!(index == 0 || index > this.countPoints - 1 || this.points[index - 1].getX() > point.getX() || this.points[index + 1].getX() < point.getX()))
            this.points[index] = new FunctionPoint(point);
    }

    //Сетер х точки по индексу
    public void setPointX(int index, double x) {
        if (!(index == 0 || index > this.countPoints - 1 || this.points[index - 1].getX() > x || this.points[index + 1].getX() < x))
            this.points[index].setX(x);
    }

    //Сетер у точки по индексу
    public void setPointY(int index, double y) { this.points[index].setY(y); }

    //Удаление точки по индексу
    public void deletePoint(int index) {
        for (int i = index; i < this.countPoints - 1; i++){
            this.points[i]=points[i+1];
        }
        this.points[countPoints]=new FunctionPoint(Double.NaN, Double.NaN);
        --this.countPoints;
    }

    //Добавление точки
    public void addPoint(FunctionPoint point) {
        if (getLeftDomainBorder() > point.getX() || getRightDomainBorder() < point.getX())
            return;
        int ind = searchPoint(point.getX());
        if (point.getX() == getPointX(ind))    //При совпадении значения х ничего не происходит
            return;
        if (this.points.length == this.countPoints) {     //Если такого х нет и в массиве нехватает памяти, то эта точка вставляется согласно порядку х
            FunctionPoint temp[] = new FunctionPoint[this.countPoints + this.countPoints/2];
            System.arraycopy(this.points, 0, temp, 0, ind + 1);   //С массива points с 0 индекса в массив temp с 0 индекса ind+1 элементов
            System.arraycopy(this.points, ind + 1, temp, ind + 2, this.countPoints - ind - 1);  //Добавление остальных точек (temp[ind+1] пустая)
            temp[ind + 1] = point;
            this.points = temp;
        }
        else {  //Если совпадений с х не найдено и в массиве ещё есть место, то сдвигаем вправо начиная с ind+1
            for (int i = this.countPoints; i > ind+1; i--){
                this.points[i] = this.points[i-1];
            }
            this.points[ind + 1] = point;
        }
        this.countPoints++;
    }

    //Вывод точек из массива
    public void print() {
        System.out.print("\nPoints:");
        for (int i = 0; i < this.countPoints; ++i) {
            this.points[i].print();
            System.out.print(" ");
        }
        System.out.println();
    }
}
