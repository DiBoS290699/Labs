import functions.*;

public class Main {
    public static void main(String[] args) {
        FunctionPoint p1 = new FunctionPoint();
        p1.print();
        p1 = new FunctionPoint(5,7);
        System.out.println();
        p1.print();
        System.out.println();
        p1.setX(10.1);
        p1.setY(14.5);
        p1.print();
        TabulatedFunction yEquallyX = new TabulatedFunction(-10, 10, 9);    //Функция y = x
        yEquallyX.print();
        for (int i = 0; i < yEquallyX.getPointsCount(); i++)
            yEquallyX.setPointY(i, yEquallyX.getPointX(i));
        yEquallyX.print();
        System.out.printf("The domain of the function: [%.1f; %.1f]", yEquallyX.getLeftDomainBorder(), yEquallyX.getRightDomainBorder());
        System.out.println("\nCount of points: " + yEquallyX.getPointsCount());
        System.out.println("Value at x=0: " + yEquallyX.getFunctionValue(0));
        System.out.println("Value at x=5: " + yEquallyX.getFunctionValue(5));
        System.out.println("Value at x=5.5: " + yEquallyX.getFunctionValue(5.5));
        System.out.println("Value at x=11: " + yEquallyX.getFunctionValue(11));
        System.out.print("Add a points (6; 6), (5; 5), (1; 1), (-11; -11), (9.9; 9.9)");
        yEquallyX.addPoint(new FunctionPoint(6, 6));        //Добавится
        yEquallyX.addPoint(new FunctionPoint(5, 5));        //Ничего не произойдёт (точка уже есть)
        yEquallyX.addPoint(new FunctionPoint(1, 1));        //Добавится
        yEquallyX.addPoint(new FunctionPoint(-11, -11));    //Выходит за пределы обл. опред.
        yEquallyX.addPoint(new FunctionPoint(9.9, 9.9));    //Добавится
        yEquallyX.print();
        double temp[] = {-5, -3, -1, 1, 3, 5};
        yEquallyX = new TabulatedFunction(-5, 5, temp);
        System.out.print("New points:");
        yEquallyX.print();
        yEquallyX.setPoint(2, new FunctionPoint(0,0));  //Заменяем (-1;-1) на (0;0)
        yEquallyX.setPoint(3, new FunctionPoint(6,6));  //Вне обл. опред.
        yEquallyX.setPointY(4, 4);      //Заменяем (3;3) на (3;4)
        System.out.println("Replace (-1; -1) with (0; 0), (1; 1) with (6; 6), (3;3) with (3;4)");
        yEquallyX.print();
        System.out.print("Delete the first and last point:");
        yEquallyX.deletePoint(5);
        yEquallyX.deletePoint(0);
        yEquallyX.print();
    }
}