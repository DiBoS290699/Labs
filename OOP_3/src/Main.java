import functions.*;

public class Main {
    public static void main(String[] args)
        throws InappropriateFunctionPointException, FunctionPointIndexOutOfBoundsException {
        TabulatedFunctionImpl list = new LinkedListTabulatedFunction(0, 10, 11);
        System.out.print(list);
        System.out.println("Левая граница списка (первый элемент): " + list.getLeftDomainBorder());
        System.out.println("Правая граница списка (последний элемент): " + list.getRightDomainBorder());
        System.out.println("Количество элементов в списке: " + list.getPointsCount());
        try {
            System.out.println("Получение точки с индексом 11: ");
            System.out.println(list.getPoint(11));
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Несуществующий индекс 11.");
        }
        try {
            System.out.println("Получение абсциссы точки с индексом 5:" + list.getPointX(5));
            System.out.println("Получение абсциссы точки с индексом 11:");
            System.out.println(list.getPointX(11));
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Несуществующий индекс 11.");
        }
        try {
            System.out.println("Получение ординаты точки с индексом 5:");
            System.out.println(list.getPointY(5));
            System.out.println("Получение ординаты точки с индексом 11:");
            System.out.println(list.getPointY(11));
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Несуществующий индекс 11.");
        }
        try {
            System.out.println("Замена точки по индексу 12 на (3, 3)");
            list.setPoint(7, new FunctionPoint(3, 3));
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Несушествующий индекс 12");
        }
        try {
            System.out.println("Замена точки по индексу 4 на (3, 3)");
            list.setPoint(6, new FunctionPoint(3, 3));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Нарушение последовательности точек по х");
        }
        System.out.println("Замена точки по индексу 3 на (3.5, 3.5)");
        list.setPoint(3, new FunctionPoint(3.5, 3.5));
        System.out.println("Элемент по индексу 3: " + list.getPoint(3));
        try {
            System.out.println("Удаление последнего элемента по индексу 11");
            list.deletePoint(11);
        } catch(FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Несушествующий индекс 11");
        }
        System.out.println(list);
        System.out.println("Удаление  элемента по индексу 3");
        list.deletePoint(3);
        System.out.println(list);
        try {
            System.out.println("Добавление точки (5, 5)");
            list.addPoint(new FunctionPoint(5, 5));
        } catch(InappropriateFunctionPointException e) {
            System.out.println("Найдено совпадение абсцисс у точек.");
        }
        try {
            System.out.println("Добавление точки (11, 11)");
            list.addPoint(new FunctionPoint(11, 11));
        } catch(InappropriateFunctionPointException e) {
            System.out.println("Выход за границы определения точек");
        }
        try {
            System.out.println("Добавление точки (-1, 11)");
            list.addPoint(new FunctionPoint(-1, 11));
        } catch(InappropriateFunctionPointException e) {
            System.out.println("Выход за границы определения точек");
        }
    }
}
