package GApplication;

import function.FunctionPoint;
import function.FunctionPointIndexOutOfBoundsException;
import function.InappropriateFunctionPointException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class MainWindow extends JFrame {
    private ParamTabFunDialog paramDialog;
    private TabFunDocument document;
    private FunctionTableModel tableModel;
    private JFileChooser fileChooser;

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
        setTitle("Tabulated Functions");
        setMinimumSize(new Dimension(400, 450));        //Минимальный размер окна
        setContentPane(mainPanel);
        setResizable(true);             //Можно изменять размер
        setDefaultCloseOperation(EXIT_ON_CLOSE);  //Просто так не закрыть
        setVisible(true);               //Сделать окно видимым

        this.paramDialog = new ParamTabFunDialog();
        this.document = new TabFunDocument();
        this.document.newFunction(0, 10, 11);
        this.fileChooser = new JFileChooser();

        setUpMenuBar();
        setUpButtons();
        setJMenuBar(menuBar);
        FunctionTableModel table = new FunctionTableModel(document.getFunction(), MainWindow.this);
        tableXY.setModel(table);

        pack();
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }

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

        fileMenu.add(createItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        tabulateMenu = new JMenu();

        tabulateItem = new JMenuItem("Tabulate function...");
        tabulateItem.addActionListener(new TabulateItemActionListener());
        tabulateMenu.add(tabulateItem);
        menuBar.add(tabulateMenu);
    }

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
                tableModel = new FunctionTableModel(document.getFunction(), MainWindow.this);
                tableXY.setModel(tableModel);
                tableXY.revalidate();
                tableXY.repaint();      //перекрасить таблицу (обновить)
            }
        }
    }

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
                } catch (FileNotFoundException exc) {
                    JOptionPane.showMessageDialog(MainWindow.this, exc.getMessage());
                }
            }
        }
    }

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

    private class TabulateItemActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

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
}
