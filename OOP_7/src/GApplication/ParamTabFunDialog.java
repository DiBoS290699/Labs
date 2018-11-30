package GApplication;

import javax.swing.*;
import java.awt.event.*;

@SuppressWarnings("ALL")
public class ParamTabFunDialog extends JDialog {
    public static final int OK = 1;
    public static final int CANCEL = 0;
    private int status;

    private JPanel contentPane;             //Главная панель
    private JButton buttonOK;               //Кнопка Ок
    private JButton buttonCancel;              //Кнопка Cancel
    private JSpinner pointsCountSpinner;        //Текстовое поле с прокручиванием значения
    private JTextField rightDBTextField;        //Текстовое поле, содержащее правую границу обл. определения
    private JTextField leftDBTextField;         //Текстовое поле, содержащее правую границу обл. определения
    private JLabel pointsCountLabel;        //Текстовое пояснение поля (Инициализировано в визуальном оформлении)
    private JLabel rightDBLabel;            //Текстовое пояснение поля (Инициализировано в визуальном оформлении)
    private JLabel leftDBLabel;             //Текстовое пояснение поля (Инициализировано в визуальном оформлении)

    public ParamTabFunDialog() {
        setTitle("Function Parameters");        //Заголовок окна
        setContentPane(contentPane);    //Занесение главной панели
        setModal(true);                 //Окно является поверх всех других
        setResizable(false);            //Запрещено изменять размер окна
        //Занесение свойств спиннер-поля (по умолчанию - 11, минимальное значение - 2, макс. - MAX_VALUE, шаг - 1)
        pointsCountSpinner.setModel(new SpinnerNumberModel(11, 2, Integer.MAX_VALUE, 1));

        buttonOK.addActionListener(e -> onOK());    //Добавление события нажатия на кнопку Ok

        buttonCancel.addActionListener(e -> onCancel());    //Добавление события нажатия на кнопку Cancel

        //Нажатие на кнопку закрытия окна
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                status = CANCEL;
                dispose();
            }
        });

        //Нажатие на кнопку ESCAPE клавиатуры
        contentPane.registerKeyboardAction(e ->
                onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /** Обработка события нажатия на кнопку Ok */
    private void onOK() {
        this.status = OK;

        try{
            double leftBorder = Double.parseDouble(leftDBTextField.getText());
            double rightBorder = Double.parseDouble(rightDBTextField.getText());
            if (rightBorder <= leftBorder) {
                throw new NumberFormatException();
            }
            setVisible(false);
            dispose();

        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Incorrect values!");
            dispose();
            showDialog();
        }
    }

    /** Обработка события нажатия на кнопку Cancel */
    private void onCancel() {
        this.status = CANCEL;
        setVisible(false);
        dispose();
    }

    /** Получение левой границы из соответствующего поля */
    public double getLeftDomainBorder() { return Double.parseDouble(leftDBTextField.getText()); }

    /** Получение правой границы из соответствующего поля */
    public double getRightDomainBorder() {
        return Double.parseDouble(rightDBTextField.getText());
    }

    /** Получение количества точек из спиннер-поля */
    public int getPointsCount() {
        return (int) pointsCountSpinner.getValue();
    }

    /**
     * Метод открытия окна
     * @return Статус работы окна
     */
    public int showDialog() {
        this.pack();
        this.setVisible(true);
        return this.status;
    }

    /**
     *  Тест работы диалогового окна
     */
    public static void main(String[] args) {
        ParamTabFunDialog dialog = new ParamTabFunDialog();
        dialog.showDialog();
        System.exit(0);
    }
}
