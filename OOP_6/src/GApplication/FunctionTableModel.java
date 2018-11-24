package GApplication;

import function.InappropriateFunctionPointException;
import function.TabulatedFunction;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;

public class FunctionTableModel implements TableModel {
    private TabulatedFunction function;     //Ссылка на нашу функцию
    private Component parent;       //Ссылка на окно родителя

    /** Кокнструктор инициализации */
    public FunctionTableModel(TabulatedFunction func, Component par) {
        this.function = func;
        this.parent = par;
    }

    /** Гетер функции */
    public TabulatedFunction getFunction() {
        return this.function;
    }

    /** Гетер количества точек (Оно равно количеству строк) */
    @Override
    public int getRowCount() {
        if (function == null)
            return 0;
        return this.function.getPointsCount();
    }

    /** Гетер количества столбцов */
    @Override
    public int getColumnCount() {
        return 2;
    }

    /** Гетер имени столбца по индексу */
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "X";
            case 1:
                return "Y";
            default:
                throw new IllegalArgumentException("Wrong column index.");
        }
    }

    /** Гетер класса значений */
    @Override
    public Class getColumnClass(int columnIndex) {
        return Double.class;
    }

    /** Можно ли изменить указанную точку? (Всегда можно) */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /** Гетер значения выделенной ячейки */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (function == null)
            return null;
        try {
            switch (columnIndex) {
                case 0:
                    return function.getPointX(rowIndex);
                case 1:
                    return function.getPointY(rowIndex);

                default:
                    throw new IllegalArgumentException("Wrong column index.");
            }
        }
        catch (Throwable e) {
            JOptionPane.showMessageDialog(parent, "Invalid index");
            return null;
        }
    }

    /** Сетер значения внутри ячейки по данным координатам */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(!(aValue instanceof Integer) && !(aValue instanceof Double))
            JOptionPane.showMessageDialog(this.parent, "Incorrect value!");

        //Так как изменять можно любую точку, то эта проверка не нужна
        /* if(!isCellEditable(rowIndex, columnIndex))
            JOptionPane.showMessageDialog(this.parent, "Invalid index!"); */

        try {
            switch (columnIndex) {
                case 0:
                    function.setPointX(rowIndex, (double) aValue);
                    break;
                case 1:
                    function.setPointY(rowIndex, (double) aValue);
                    break;

                default:
                    throw new IllegalArgumentException("Wrong column index!");
            }
        }
        catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(parent, "Wrong column index!");
        } catch (InappropriateFunctionPointException e) {
            JOptionPane.showMessageDialog(parent, "Wrong value!");
        }
    }

    /** Данный метод не используется в моей программе */
    @Override
    public void addTableModelListener(TableModelListener l) {
        //TODO
    }

    /** Данный метод не используется в моей программе */
    @Override
    public void removeTableModelListener(TableModelListener l) {
        //TODO
    }
}
