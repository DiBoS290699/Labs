package function;

import java.io.Serializable;

public class FunctionPoint implements Serializable, Cloneable {
    private double x;
    private double y;
    public FunctionPoint() {this.x = 0; this.y = 0;}           //Конструктор по умолчанию
    public FunctionPoint(double xx, double yy) {this.x = xx; this.y = yy;}     //Инициализация
    public FunctionPoint(FunctionPoint point) {this.x = point.x; this.y = point.y;}        //Копирование
    public double getX() { return this.x; }                                 //гетер х
    public double getY() { return this.y; }                                 //гетер у
    public void setX(double xx) { this.x=xx; }                           //сетер х
    public void setY(double yy) { this.y=yy; }                           //сетер у
    public void print() { System.out.printf("(%.1f; %.1f)", this.x, this.y); }   //Вывод

    @Override   //координаты точки
    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    @Override   //Метод равенства
    public boolean equals(Object o) {
        return o instanceof FunctionPoint &&
                this.getX() == ((FunctionPoint) o).getX() &&
                this.getY() == ((FunctionPoint) o).getY();
    }

    @Override
    public int hashCode() {
        long v1 = Double.doubleToLongBits(x);
        long v2 = Double.doubleToLongBits(y);
        return (int)(v1^(v1>>>32)) + (int)(v2^(v2>>>32));
       // return Double.hashCode(x) + Double.hashCode(y);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}