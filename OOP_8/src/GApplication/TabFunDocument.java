package GApplication;

import function.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Класс, описывающий в программе документ
 */
@SuppressWarnings("ALL")
public class TabFunDocument implements TabulatedFunction{

    private TabulatedFunction function;     //Функция, хранящаяся в документе
    private String fileName;                //Имя документа
    private boolean modified;               //Изменялась ли функция в процессе работы?
    private boolean fileNameAssigned;       //Задано ли имя файла для функции?

    /** Конструктор по умолчанию */
    public TabFunDocument() {
        this.function = null;
        this.fileName = null;
        this.modified = false;
        this.fileNameAssigned = false;
    }

    /** Конструктор от файла */
    public TabFunDocument(String fileName) throws FileNotFoundException {
        loadFunction(fileName);
        this.modified = false;
    }

    /** Изменение функции на новую */
    public void newFunction(double leftX, double rightX, int pointsCount) {
        this.modified = true;
        this.function = new ArrayTabulatedFunction(leftX, rightX, pointsCount);
    }

    /** Сохранение функции в исходный файл */
    public void saveFunction() throws IOException {
        if(!fileNameAssigned) {
            System.err.print("The document name is not specified");
        }
        this.modified = false;
        TabulatedFunctions.writeTabulatedFunction(function, new FileWriter(fileName));
    }

    /** Сохранение функции в выбранный файл */
    public void saveFunctionAs(String fileName) throws IOException {
        this.fileName = fileName;
        this.fileNameAssigned = true;
        this.modified = false;
        TabulatedFunctions.writeTabulatedFunction(function, new FileWriter(this.fileName));
    }

    /** Загрузка функции из файла */
    public void loadFunction(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        this.fileNameAssigned = true;
        function = TabulatedFunctions.readTabulatedFunction(new FileReader(fileName));
    }

    /** Создание функции на основе класса, наследующего Function */
    public void tabulateFunction(Function func, double leftX, double rightX, int pointsCount) {
        this.function = TabulatedFunctions.tabulate(func, leftX, rightX, pointsCount);
        this.modified = true;
    }

    public boolean isModified() { return modified; }

    public boolean isFileNameAssigned() { return fileNameAssigned; }

    public String getFileName() { return fileName; }

    public TabulatedFunction getFunction() { return function; }

    @Override
    public double getLeftDomainBorder() {
        return function.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return function.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return function.getFunctionValue(x);
    }

    @Override
    public int getPointsCount() {
        return this.function.getPointsCount();
    }

    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        return function.getPoint(index);
    }

    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return function.getPointX(index);
    }

    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return function.getPointY(index);
    }

    @Override
    public void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        function.setPoint(index, point);
        modified = true;
    }

    @Override
    public void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        function.setPointX(index, x);
        modified = true;
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        function.setPointY(index, y);
        modified = true;
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        function.deletePoint(index);
        modified = true;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        function.addPoint(point);
        modified = true;
    }

    /** Вывод точек из массива */
    @Override
    public String toString() {
        return function.toString();
    }

    /** Возвращение хешкода функции */
    @Override
    public int hashCode() {
        return function.hashCode();
    }

    /** Сравнивание функций */
    @Override
    public boolean equals(Object obj) {
        return function.equals(obj);
    }

    /** Создание клона функции */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return function.clone();
    }

    @Override
    public Iterator<FunctionPoint> iterator() {
        //Операция не поддерживается
        throw new UnsupportedOperationException("Operation is unsupported");
    }
}
