package function;

import java.io.Serializable;

public class LinkedListTabulatedFunction implements TabulatedFunctionImpl, Serializable {
    private FunctionNode head = new FunctionNode();         //Всегда пустая голова
    private int size = 0;                   //Количество объектов в списке

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (leftX >= rightX || pointsCount < 2)
            throw new IllegalArgumentException("Error!");

        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; ++i) {
            this.addNodeToTail().fp = new FunctionPoint(leftX + step * i, 0);
        }

    }

    //Конструктор с заданным массивом точек
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
        if (leftX >= rightX || values.length < 2)
            throw new IllegalArgumentException("Error!");

        int length = values.length;
        double step = (rightX - leftX) / (length - 1);

        for (int i = 0; i < length; ++i) {
            this.addNodeToTail().fp = new FunctionPoint(leftX + step * i, values[i]);
        }
    }

    public LinkedListTabulatedFunction(FunctionPoint[] fpMas) throws IllegalArgumentException {
        int length = fpMas.length;

        if (length < 2)
            throw new IllegalArgumentException("Error!");

        for (int i = 1; i < length; ++i) {
            if (fpMas[i-1].getX() >= fpMas[i].getX())
                throw new IllegalArgumentException("Error!");
        }

        for (int i = 0; i < length; ++i) {
            this.addNodeToTail().fp = fpMas[i];
        }

        this.size = length;
    }

    public double getLeftDomainBorder() {
        return this.head.next.fp.getX();
    }

    public double getRightDomainBorder() {
        return this.head.prev.fp.getX();
    }

    public int getPointsCount() {
        return this.size;
    }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        return this.getNodeByIndex(index).fp;
    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return this.getNodeByIndex(index).fp.getX();
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return this.getNodeByIndex(index).fp.getY();
    }

    //Поиск индекса точки по х (либо соседней точки слева)
    private int searchPoint(double x) {
        int i = 0;
        FunctionNode temp = this.head;

        for (; temp.next.fp.getX() < x; ++i, temp = temp.next);

        if (temp.next.fp.getX() == x) return i;
        else return i - 1;
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        int i = searchPoint(x);
        FunctionNode temp = this.getNodeByIndex(i);

        if (temp.fp.getX() == x) return temp.fp.getY();

        return (x - temp.fp.getX()) * (temp.next.fp.getY() - temp.fp.getY()) / (temp.next.fp.getX() -
                temp.fp.getX()) + temp.fp.getY();
    }

    public void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= this.size)
            throw new FunctionPointIndexOutOfBoundsException("Error!");

        FunctionNode temp = this.getNodeByIndex(index);

        if (temp.prev.fp.getX() > point.getX() || temp.next.fp.getX() < point.getX())
            throw new InappropriateFunctionPointException("Error!");

        temp.fp = new FunctionPoint(point);
    }

    public void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        FunctionNode temp = this.getNodeByIndex(index);

        if (temp.prev.fp.getX() > x || temp.next.fp.getX() < x) {
            throw new InappropriateFunctionPointException("Error!");
        }

        temp.fp.setX(x);
    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        this.getNodeByIndex(index).fp.setY(y);
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (this.size < 3)
            throw new IllegalStateException("Error!");

        deleteNodeByIndex(index);

    }

    private FunctionNode getNodeByIndex(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.size) {
            throw new FunctionPointIndexOutOfBoundsException("Error!");
        }

        if (index > this.size / 2) {
            FunctionNode temp = this.head.prev;

            for (int i = 0; i < this.size - index; ++i) {
                temp = temp.prev;
            }
            return temp;

        } else {
            FunctionNode temp = this.head.next;

            for (int i = 0; i < index; ++i) {
                temp = temp.next;
            }

            return temp;
        }
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (getLeftDomainBorder() > point.getX() || getRightDomainBorder() < point.getX())
            throw new InappropriateFunctionPointException("Error!");

        int i = searchPoint(point.getX());

        if (Double.compare(point.getX(), this.getNodeByIndex(i).fp.getX()) == 0)    //При совпадении значения х выбрасывается исключение
            throw new InappropriateFunctionPointException("Error!");

        addNodeByIndex(i + 1).fp = point;
    }

    /*Сначала сохраняется последний объект в списке,
    после чего следующий элемент от него становится пустым (ранее последний элемент ссылался на первый),
    теперь добавляем связи для нового объекта. Он должен ссылаться на сохранённый старый объект и на первый
    объект в списке. Теперь нужно чтобы голова и первый ссылались на новый элемент. Возвращаем ссылку на него.*/
    private FunctionNode addNodeToTail() {
        if (size == 0) {
            FunctionNode temp = new FunctionNode();
            temp.next = temp;
            temp.prev = temp;
            head.next = temp;
            head.prev = temp;
            size = 1;
            return temp;
        }

        if (size == 1) {
            FunctionNode temp = new FunctionNode(head.next, head.next);
            head.prev = temp;
            head.next.next = temp;
            head.next.prev = temp;
            size = 2;
            return temp;
        }

        FunctionNode temp = new FunctionNode(head.prev, head.next);
        head.next.prev = temp;
        temp.prev.next = temp;
        head.prev = temp;
        ++size;
        return temp;
    }

    private FunctionNode addNodeByIndex(int index) throws ArrayIndexOutOfBoundsException {
        if (this.size == 0 && index == 0 || this.size == 1 && index == 1 || index == this.size) {
            return addNodeToTail();
        }

        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode temp = new FunctionNode(nextNode.prev, nextNode);
        nextNode.prev.next = temp;
        nextNode.prev = temp;
        ++this.size;
        return temp;
    }

    private FunctionNode deleteNodeByIndex(int index) throws ArrayIndexOutOfBoundsException {
        FunctionNode temp = getNodeByIndex(index);
        temp.next.prev = temp.prev;
        temp.prev.next = temp.next;
        --this.size;
        return temp;
    }

    private static class FunctionNode {
        private FunctionPoint fp;
        private FunctionNode next;
        private FunctionNode prev;

        public FunctionNode() {
        }

        public FunctionNode(FunctionNode prev, FunctionNode next) {
            this.prev = prev;
            this.next = next;
        }

        public FunctionNode(FunctionPoint fp, FunctionNode prev, FunctionNode next) {
            this.fp = fp;
            this.prev = prev;
            this.next = next;
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("\nList:");
        FunctionNode temp = this.head.next;

        for (int i = 0; i < this.size; ++i, temp = temp.next) {
            str.append(temp.fp.toString()).append(" ");
        }

        str.append('\n');
        return str.toString();
    }
}
