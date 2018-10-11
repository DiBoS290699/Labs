package functions;

public interface TabulatedFunctionImpl {
    double getLeftDomainBorder();
    double getRightDomainBorder();
    int getPointsCount();
    FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException;
    double getPointX(int index) throws FunctionPointIndexOutOfBoundsException;
    double getPointY(int index) throws FunctionPointIndexOutOfBoundsException;
    double getFunctionValue(double x);
    void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException;
    void deletePoint(int index)
            throws FunctionPointIndexOutOfBoundsException, IllegalStateException;
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
}
