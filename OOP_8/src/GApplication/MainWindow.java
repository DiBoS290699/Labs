package GApplication;

import function.Function;
import function.FunctionPoint;
import function.FunctionPointIndexOutOfBoundsException;
import function.InappropriateFunctionPointException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

@SuppressWarnings({"unused", "FieldCanBeLocal", "WeakerAccess"})
public class MainWindow extends JFrame {
    private ParamTabFunDialog paramDialog;
    private TabFunDocument document;
    private FunctionTableModel tableModel;
    private JFileChooser fileChooser;
    private FunctionLoader functionLoader;

    private JMenuBar menuBar;

    private JMenu fileMenu;
    private JMenuItem createItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem saveAsItem;
    private JMenuItem exitItem;

    private JMenu tabulateMenu;
    private JMenuItem tabulateItem;

    private JPanel mainPanel;
    private JLabel PointXLabel;
    private JLabel pointYLabel;
    private JTextField yTextField;
    private JTextField xTextField;
    private JButton addPointButton;
    private JButton deletePointButton;
    private JScrollPane tableScrollPane;
    private JTable tableXY;

    public MainWindow() {
        //Удаление точки кнопкой клавиатуры
        tableXY.registerKeyboardAction(e -> onDelete(), KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        //Добавление точки кнопкой клавиатуры
        mainPanel.registerKeyboardAction(e -> onAdd(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setTitle("Tabulated Functions");        //Заголовок главного окна
        setMinimumSize(new Dimension(400, 450));        //Минимальный размер окна
        setContentPane(mainPanel);                  //Занесение главной панели
        setResizable(true);             //Можно изменять размер
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);  //Просто так не закрыть

        this.document = new TabFunDocument();
        this.document.newFunction(0, 10, 11);
        this.fileChooser = new JFileChooser();

        setUpMenuBar();
        setUpButtons();
        setJMenuBar(menuBar);
        setTableXY();

        pack();
        setVisible(true);               //Сделать окно видимым
    }

    /** Тест главного окна */
    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }

    /** Создание меню-бара */
    private void setUpMenuBar() {
        menuBar = new JMenuBar();       //Создаём меню-бар

        fileMenu = new JMenu("File");         //Создаём меню file

        createItem = new JMenuItem("New...");       //Создаём пункт создания файла
        createItem.addActionListener(new CreateItemActionListener());   //Обработка события

        openItem = new JMenuItem("Open...");        //Создаём пункт открытия файла
        openItem.addActionListener(new OpenItemActionListener());       //Обработка события

        saveItem = new JMenuItem("Save...");        //Создаём пункт сохранения файла
        saveItem.addActionListener(new SaveItemActionListener());       //Обработка события

        saveAsItem = new JMenuItem("Save as...");   //Создаём пункт сохранения файла как
        saveAsItem.addActionListener(new SaveAsItemActionListener());   //Обработка события

        exitItem = new JMenuItem("Exit...");     //Создаём пункт закрытия программы
        exitItem.addActionListener(new ExitItemActionListener());       //Обработка события

        //Занесение в меню File
        fileMenu.add(createItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(exitItem);

        //занесение меню File в меню-бар
        menuBar.add(fileMenu);

        tabulateMenu = new JMenu("Tabulate");   //Создаём меню Tabulate

        tabulateItem = new JMenuItem("Tabulate function...");
        tabulateItem.addActionListener(new TabulateItemActionListener());
        tabulateMenu.add(tabulateItem);
        menuBar.add(tabulateMenu);
    }

    /** Обработка события создания новой функции */
    private class CreateItemActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            paramDialog = new ParamTabFunDialog();
            int paramTabFunDialogStatus = paramDialog.showDialog();

            if (paramTabFunDialogStatus == ParamTabFunDialog.OK) {
                double leftDomainBorder = paramDialog.getLeftDomainBorder();
                double rightDomainBorder = paramDialog.getRightDomainBorder();
                int pointsCount = paramDialog.getPointsCount();

                document.newFunction(leftDomainBorder, rightDomainBorder, pointsCount);
                setTableXY();
                tableXY.revalidate();
                tableXY.repaint();      //перекрасить таблицу (обновить)
            }
        }
    }

    /** Обработка события открытия файла с функцией */
    private class OpenItemActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            int fileChooserStatus = fileChooser.showOpenDialog(MainWindow.this);    //Открываем окно "Открыть"
            if(fileChooserStatus == JFileChooser.APPROVE_OPTION) {      //Если был успешно выбран документ
                try {
                    //Загружаем функцию из выбранного файла
                    document.loadFunction(fileChooser.getSelectedFile().getPath());
                    tableModel = new FunctionTableModel(document.getFunction(), MainWindow.this);
                    tableXY.setModel(tableModel);
                    tableXY.revalidate();
                    tableXY.repaint();      //перекрасить таблицу (обновить)
                } catch (Throwable exc) {
                    JOptionPane.showMessageDialog(MainWindow.this, "Incorrect file!");
                }
            }
        }
    }

    /** Обработка события сохранения функции в файл */
    private class SaveItemActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(!document.isFileNameAssigned()) {        //Если имя НЕ задано, то выбирается файл куда сохранить
                int fileChooserStatus = fileChooser.showSaveDialog(MainWindow.this);
                if(fileChooserStatus == JFileChooser.APPROVE_OPTION) {
                    try {
                        document.saveFunctionAs(fileChooser.getSelectedFile().getPath());
                    } catch (IOException exc) {
                        JOptionPane.showMessageDialog(MainWindow.this, exc.getMessage());
                    }
                }
            }
            else {      //Если имя задано, то сохраняем в этот же файл
                try {
                    document.saveFunction();
                } catch (IOException exc) {
                    JOptionPane.showMessageDialog(MainWindow.this, exc.getMessage());
                }
            }
        }
    }

    /** Обработка события сохранения функции в новый файл */
    private class SaveAsItemActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int fileChooserStatus = fileChooser.showSaveDialog(MainWindow.this);
            if(fileChooserStatus == JFileChooser.APPROVE_OPTION) {
                try {
                    document.saveFunctionAs(fileChooser.getSelectedFile().getPath());
                } catch (IOException exc) {
                    JOptionPane.showMessageDialog(MainWindow.this, exc.getMessage());
                }
            }
        }
    }

    /** Обработка события закрытия окна */
    private class ExitItemActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (document.isModified()) {
                if(JOptionPane.showConfirmDialog(MainWindow.this,
                        "Document is not saved. Are you sure you want to exit?") == JOptionPane.OK_OPTION){
                    dispose();
                }
            }
            else dispose();
        }
    }

    /** Обработка события закрытия окна */
    private class MainWindowCloseListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
            //TODO
        }

        @Override
        public void windowClosing(WindowEvent e) {
            if (document.isModified()) {
                if(JOptionPane.showConfirmDialog(MainWindow.this,
                        "Document is not saved. Are you sure you want to exit?") == JOptionPane.OK_OPTION){
                    dispose();
                }
            }
            else dispose();
        }

        @Override
        public void windowClosed(WindowEvent e) {
            //TODO
        }

        @Override
        public void windowIconified(WindowEvent e) {
            //TODO
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            //TODO
        }

        @Override
        public void windowActivated(WindowEvent e) {
            //TODO
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            //TODO
        }
    }

    /** Обработка события создания функции с помощью класса */
    private class TabulateItemActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int fileChooserStatus = fileChooser.showOpenDialog(MainWindow.this);    //Открываем окно "Открыть"
            if(fileChooserStatus == JFileChooser.APPROVE_OPTION) {      //Если был успешно выбран документ

                paramDialog = new ParamTabFunDialog();
                if (paramDialog.showDialog() == ParamTabFunDialog.OK) {
                    double leftDomainBorder = paramDialog.getLeftDomainBorder();
                    double rightDomainBorder = paramDialog.getRightDomainBorder();
                    int pointsCount = paramDialog.getPointsCount();

                    String classPath = fileChooser.getSelectedFile().getPath();
                    String classNameWithExtension = classPath.split(Pattern.quote("\\"))[classPath.split(Pattern.quote("\\")).length-1];
                    String className = "function." + "basic." + classNameWithExtension.split(Pattern.quote("."))[0];
                    Function fun;
                    try {
                        functionLoader = new FunctionLoader();
                        Object loadFunction = functionLoader.loadFunction(classPath, className);
                        fun = (Function) loadFunction;
                        boolean wrongArg = false;
                        if(leftDomainBorder < fun.getLeftDomainBorder()) {
                            leftDomainBorder = fun.getLeftDomainBorder();
                            wrongArg = true;
                        }
                        if(rightDomainBorder > fun.getRightDomainBorder()) {
                            rightDomainBorder = fun.getRightDomainBorder();
                            wrongArg = true;
                        }
                        if(wrongArg) {
                            JOptionPane.showMessageDialog(MainWindow.this,
                                    "Invalid scope");
                        }
                        document.tabulateFunction(fun, leftDomainBorder, rightDomainBorder, pointsCount);
                        setTableXY();

                        tableXY.revalidate();
                        tableXY.repaint();      //перекрасить таблицу (обновить)
                    }
                    catch (IOException e1) {
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "Class cannot be considered");
                        actionPerformed(e);
                    }
                    catch (ClassFormatError e1) {
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "Invalid format of a class");
                        actionPerformed(e);
                    }
                    catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e1) {
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "You cannot create an instance of the class");
                        actionPerformed(e);
                    } catch (ClassCastException e1) {
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "The class does not implement the Function interface");
                        actionPerformed(e);
                    }
                }
            }
        }
    }

    private void setUpButtons() {
        addPointButton.addActionListener(e -> onAdd());
        deletePointButton.addActionListener(e -> onDelete());
    }

    private void onAdd() {
        if (xTextField.getText().isEmpty() || yTextField.getText().isEmpty()) {
            return;
        }
        try {
            double x = Double.parseDouble(xTextField.getText());
            double y = Double.parseDouble(yTextField.getText());

            xTextField.setText("");
            yTextField.setText("");

            document.getFunction().addPoint(new FunctionPoint(x, y));
            tableXY.revalidate();
            tableXY.repaint();
        }
        catch (NumberFormatException exc) {
            JOptionPane.showMessageDialog(MainWindow.this, "Incorrect values!");
        }
        catch (InappropriateFunctionPointException e) {
            //Точка уже находится в таблице
            JOptionPane.showMessageDialog(MainWindow.this, "Point already is in the table.");
        }
    }

    private void onDelete() {
        int row = tableXY.getSelectedRow();
        if (tableXY.isCellEditable(row, 1)) {
            try {
                document.deletePoint(row);
            } catch (FunctionPointIndexOutOfBoundsException | IllegalStateException e) {
                JOptionPane.showMessageDialog(MainWindow.this, e.getMessage());
            }
            tableXY.revalidate();
            tableXY.repaint();
        }
        else
            JOptionPane.showMessageDialog(MainWindow.this, "You cannot delete a point!");
    }

    private void setTableXY() {
        tableModel = new FunctionTableModel(document.getFunction(), MainWindow.this);
        tableXY.setModel(tableModel);
    }
}
