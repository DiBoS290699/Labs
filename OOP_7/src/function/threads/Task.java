package function.threads;

import function.Function;
import function.basic.Log;


public class Task {
    private Function function;
    private double leftX;
    private double rightX;
    private double discreteStep;
    private int numberOfOperation;

    public Task() {

    }

    public Task(Function func, double leftX, double rightX, double discreteStep) {
        this.function = func;
        this.leftX = leftX;
        this.rightX = rightX;
        this.discreteStep = discreteStep;
        this.numberOfOperation = 0;
    }

    public Task(Function func, double leftX, double rightX, double discreteStep, int numberOfOperation) {
        this.function = func;
        this.leftX = leftX;
        this.rightX = rightX;
        this.discreteStep = discreteStep;
        this.numberOfOperation = numberOfOperation;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public double getLeftX() {
        return leftX;
    }

    public void setLeftX(double leftBorder) {
        this.leftX = leftBorder;
    }

    public double getRightX() {
        return rightX;
    }

    public void setRightX(double rightBorder) {
        this.rightX = rightBorder;
    }

    public double getDiscreteStep() {
        return discreteStep;
    }

    public void setDiscreteStep(double step) { this.discreteStep = step; }

    public int getNumberOfOperation() {
        return this.numberOfOperation;
    }

    public void setNumberOfOperation(int number) {
        this.numberOfOperation = number;
    }

    @Override
    public String toString() {
        String className = function.getClass().getName();
        if (className.equals("function.basic.Log")) {
            className += " (" + ((Log)function).getBase() + ")";
        }
        return className + " " + leftX + " " + rightX + " " + discreteStep + '\n';
    }
}
