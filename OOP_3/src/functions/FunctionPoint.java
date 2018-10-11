package functions;

public class FunctionPoint {
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

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    // координаты точки
}