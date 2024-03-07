package boundary;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;

public class ClientMainFrame extends JFrame {
    private JPanel contentPane;
    private JButton buttonSend;
    private JButton buttonInsert;
    private JTable userList;
    private JTable messageWindow;
    private JTextArea messageTextInput;

    public ClientMainFrame() {
        setResizable(false);
        setSize(1280, 720);
        add(contentPane);
        setVisible(true);

        messageTextInput.setLineWrap(true);
        messageTextInput.setWrapStyleWord(true);

        messageWindow.setModel(new DefaultTableModel(0, 4) {
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        });

        buttonSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onInsert();
            }
        });

        // call onExit() when cross is clicked
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });

        // call onExit() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void setupMessageWindow() {
        messageWindow.setModel(new DefaultTableModel() {
            @Override
            public int getColumnCount() {
                return 4;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return getValueAt(0, columnIndex).getClass();
            }
        });
    }

    public void addMessageRow(Object[] messageInfo) {
        for (Object object : messageInfo) {
            System.out.println("MainFrame Object Read: " + object.toString());
        }
        DefaultTableModel tableModel = (DefaultTableModel) messageWindow.getModel();
        tableModel.addRow(messageInfo);
        //DefaultTableModel model = (DefaultTableModel) messageWindow.getModel();
        //SwingUtilities.invokeLater(() -> model.addRow(new Object[]{"Column 1", "Column 2", "Column 3"}));

    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onInsert() {
        // add your code here if necessary
        dispose();
    }

    private void onExit() {
        //add code
        dispose();
    }

    public static void main(String[] args) {
        ClientMainFrame MainFrameTest = new ClientMainFrame();
    }
}
