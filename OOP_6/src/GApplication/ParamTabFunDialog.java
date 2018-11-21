package GApplication;

import javax.swing.*;
import java.awt.event.*;

public class ParamTabFunDialog extends JDialog {
    public static final int OK = 1;
    public static final int CANCEL = 0;
    private int status;

    private double leftBorder;
    private double rightBorder;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner pointsCountSpinner;
    private JTextField rightDBTextField;
    private JTextField leftDBTextField;
    private JLabel pointsCountLabel;
    private JLabel rightDBLabel;
    private JLabel leftDBLabel;

    public ParamTabFunDialog() {
        setTitle("Function Parameters");
        setContentPane(contentPane);
        setModal(true);                 //Окно является поверх всех других
        setResizable(false);            //Запрещено изменять размер окна
        getRootPane().setDefaultButton(buttonOK);
        pointsCountSpinner.setModel(new SpinnerNumberModel(11, 2, Integer.MAX_VALUE, 1));

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                status = CANCEL;
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e ->
                onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        this.status = OK;

        try{
            this.leftBorder = Double.parseDouble(leftDBTextField.getText());
            this.rightBorder = Double.parseDouble(rightDBTextField.getText());
            if (rightBorder <= leftBorder) {
                throw new NumberFormatException();
            }
            setVisible(false);

        } catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Incorrect values!");
        }
        dispose();
    }

    private void onCancel() {
        this.status = CANCEL;
        setVisible(false);
        dispose();
    }

    public double getLeftDomainBorder() { return leftBorder; }

    public double getRightDomainBorder() {
        return rightBorder;
    }

    public int getPointsCount() {
        return (int) pointsCountSpinner.getValue();
    }

    public int showDialog() {
        this.pack();
        this.setVisible(true);
        return this.status;
    }

    public static void main(String[] args) {
        ParamTabFunDialog dialog = new ParamTabFunDialog();
        dialog.showDialog();
        System.exit(0);
    }
}
