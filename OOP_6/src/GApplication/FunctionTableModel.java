package GApplication;

import function.TabulatedFunction;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;

public class FunctionTableModel implements TableModel {
    TabulatedFunction function;
    Component parent;

    public FunctionTableModel(TabulatedFunction func, Component par) {
        this.function = func;
        this.parent = par;
    }

    public TabulatedFunction getFunction() {
        return this.function;
    }

    @Override
    public int getRowCount() {
        if (function == null)
            return 0;
        return this.function.getPointsCount();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 1:
                return "X";
            case 2:
                return "Y";
            default:
                throw new IllegalArgumentException("Wrong column index.");
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return Double.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (function == null)
            return null;
        try {
            switch (columnIndex) {
                case 1:
                    return function.getPointX(rowIndex - 1);
                case 2:
                    return function.getPointY(rowIndex - 1);

                default:
                    throw new IllegalArgumentException("Wrong column index.");
            }
        }
        catch (Throwable e) {
            JOptionPane.showMessageDialog(parent, "Invalid index");
            return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(!(aValue instanceof Integer) && !(aValue instanceof Double))
            JOptionPane.showMessageDialog(this.parent, "Incorrect value!");

        if(!isCellEditable(rowIndex, columnIndex))
            JOptionPane.showMessageDialog(this.parent, "Invalid index!");
        try {
            switch (columnIndex) {
                case 1:
                    function.setPointX(rowIndex - 1, (double) aValue);
                case 2:
                    function.setPointY(rowIndex - 1, (double) aValue);

                default:
                    throw new IllegalArgumentException("Wrong column index!");
            }
        }
        catch (Throwable e) {
            JOptionPane.showMessageDialog(parent, "Invalid index!");
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        //TODO
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        //TODO
    }
}
