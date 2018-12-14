package function;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable, Cloneable {
    private FunctionNode head = new FunctionNode();         //Всегда пустая голова
    private int countPoints = 0;                   //Количество объектов в списке

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

        this.countPoints = length;
    }

    public double getLeftDomainBorder() {
        return this.head.next.fp.getX();
    }

    public double getRightDomainBorder() {
        return this.head.prev.fp.getX();
    }

    public int getPointsCount() {
        return this.countPoints;
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
        if (index < 0 || index >= this.countPoints)
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
        if (this.countPoints < 3)
            throw new IllegalStateException("Error!");

        deleteNodeByIndex(index);

    }

    private FunctionNode getNodeByIndex(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.countPoints) {
            throw new FunctionPointIndexOutOfBoundsException("Error!");
        }

        if (index > this.countPoints / 2) {
            FunctionNode temp = this.head.prev;

            for (int i = 0; i < this.countPoints - index - 1; ++i) {
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
        if (countPoints == 0) {
            FunctionNode temp = new FunctionNode();
            temp.next = temp;
            temp.prev = temp;
            head.next = temp;
            head.prev = temp;
            countPoints = 1;
            return temp;
        }

        if (countPoints == 1) {
            FunctionNode temp = new FunctionNode(head.next, head.next);
            head.prev = temp;
            head.next.next = temp;
            head.next.prev = temp;
            countPoints = 2;
            return temp;
        }

        FunctionNode temp = new FunctionNode(head.prev, head.next);
        head.next.prev = temp;
        temp.prev.next = temp;
        head.prev = temp;
        ++countPoints;
        return temp;
    }

    private FunctionNode addNodeByIndex(int index) throws ArrayIndexOutOfBoundsException {
        if (this.countPoints == 0 && index == 0 || this.countPoints == 1 && index == 1 || index == this.countPoints) {
            return addNodeToTail();
        }

        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode temp = new FunctionNode(nextNode.prev, nextNode);
        nextNode.prev.next = temp;
        nextNode.prev = temp;
        ++this.countPoints;
        return temp;
    }

    private FunctionNode deleteNodeByIndex(int index) throws ArrayIndexOutOfBoundsException {
        FunctionNode temp = getNodeByIndex(index);
        temp.next.prev = temp.prev;
        temp.prev.next = temp.next;
        --this.countPoints;
        return temp;
    }



    @SuppressWarnings("unused")
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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("\nList:");
        FunctionNode temp = this.head.next;

        for (int i = 0; i < this.countPoints; ++i, temp = temp.next) {
            str.append(temp.fp.toString()).append(" ");
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

        if (o instanceof LinkedListTabulatedFunction) {  //Если приводится к классу, то происходит проверка
            LinkedListTabulatedFunction temp = (LinkedListTabulatedFunction) o;

            if (temp.countPoints != this.countPoints)
                return false;

            FunctionNode thisNode = this.head.next;
            FunctionNode oNode = temp.head.next;
            for (int i = 0; i < this.countPoints; ++i) {
                if (!thisNode.fp.equals(oNode.fp)) return false;
                thisNode = thisNode.next;
                oNode = oNode.next;
            }

            return true;
        } else {    //Если приводится к другому классу, реализующему интерфейс - то
            //происходит проверка черезе гетеры
            if (((TabulatedFunction) o).getPointsCount() != this.countPoints)
                return false;

            FunctionNode thisNode = this.head.next;
            for (int i = 0; i < this.countPoints; ++i) {
                if (!((TabulatedFunction) o).getPoint(i).equals(thisNode.fp)) return false;

                thisNode = thisNode.next;
            }

            return true;
        }
    }

    @Override
    public int hashCode() {
        int hashPoints = 0;
        FunctionNode temp = this.head.next;
        for (int i = 0; i < countPoints; i++) {
            hashPoints += temp.fp.hashCode();
            temp = temp.next;
        }
        return countPoints + hashPoints;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        int length = this.countPoints;
        FunctionPoint[] fps = new FunctionPoint[length];
        for (int i = 0; i < length; ++i) {
            fps[i] = (FunctionPoint) this.getPoint(i).clone();
        }
        try {
            return new LinkedListTabulatedFunction(fps);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return super.clone();
    }

    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<>() {
            private FunctionNode current = head.next;

            @Override
            public boolean hasNext() {
                return current.next != head.next;
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    //Следующий элемент не существует
                    throw new NoSuchElementException("Next element does not exist.");
                }
                current = current.next;
                return new FunctionPoint(current.fp);
            }

            @Override
            public void remove() {  //Операция remove не поддерживается
                throw new UnsupportedOperationException("Operation remove is unsupported");
            }
        };
    }

    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] fpMas) {
            return new LinkedListTabulatedFunction(fpMas);
        }
    }
}


