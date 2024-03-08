package boundary;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class ClientMainFrame extends JFrame {
    private ClientMainView mainView;
    private JPanel contentPane;
    private JButton buttonSend;
    private JButton buttonInsert;
    private JTable userList;
    private JTable messageWindow;
    private JTextArea messageTextInput;
    private JPanel buttonPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private ImageIcon messageImage;

    public ClientMainFrame(ClientMainView mainView) {
        this.mainView = mainView;
        setResizable(false);
        setSize(1280, 720);
        add(contentPane);
        setupColors();
        setVisible(true);

        userList.setModel(new DefaultTableModel(0, 2) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        userList.setFont(messageTextInput.getFont());

        messageWindow.setModel(new DefaultTableModel(0, 4) {
            //override needed to render ImageIcons in JTable
            @Override
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        messageWindow.setRowHeight(100);
        messageWindow.setFont(messageTextInput.getFont());

        buttonSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSend();
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
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        // call onSend() on ENTER
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSend();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void addMessageRow(Object[] messageInfo) {
        for (Object object : messageInfo) {
            System.out.println("MainFrame Object Read: " + object.toString());
        }
        DefaultTableModel tableModel = (DefaultTableModel) messageWindow.getModel();
        tableModel.addRow(messageInfo);
    }

    private void onSend() {
        if(!(messageTextInput.getText() == null && messageImage == null)) {
            mainView.createMessage(messageTextInput.getText(), messageImage);
            messageTextInput.setText(null);

            if(messageImage != null) {
                toggleInsertColor();
            }
            messageImage = null;
        }
    }

    private void onInsert() {
        selectMessageImage();
        if(messageImage == null) {
            toggleInsertColor();
        }
    }

    private void onExit() {
        dispose();
    }

    private void toggleInsertColor() {
        Color greenish = new Color(100, 150, 100);

        if(buttonInsert.getBackground() == greenish) {
            buttonInsert.setBackground(new Color(100,95,95));
        } else {
            buttonInsert.setBackground(greenish);
        }
    }

    private void setupColors(){
        Color baseColor = new Color(70,70,70);
        Color mainColor = new Color(45,45,50);
        Color highlightColor = new Color(100,95,95);
        Color textColor = new Color(200,200,200);
        Color buttonBorder = new Color(115,110,110);

        contentPane.setBackground(baseColor);
        topPanel.setBackground(baseColor);
        buttonPanel.setBackground(baseColor);
        bottomPanel.setBackground(baseColor);
        messageWindow.setBackground(mainColor);
        userList.setBackground(mainColor);
        messageTextInput.setBackground(highlightColor);
        buttonInsert.setBackground(highlightColor);
        buttonSend.setBackground(highlightColor);

        buttonSend.setBorder(new MatteBorder(2,2,2,2, buttonBorder));
        buttonInsert.setBorder(new MatteBorder(2,2,2,2, buttonBorder));

        messageTextInput.setForeground(textColor);
        messageWindow.setForeground(textColor);
        userList.setForeground(textColor);
    }

    //TODO mostly copied from LoginFrame, maybe there's a better solution?
    private void selectMessageImage() {
        JFileChooser fileChooser = new JFileChooser();
        while (true) {
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    if (isValidFormat(selectedFile)) {
                        if (messageImage != null) {
                            toggleInsertColor();
                        }
                        messageImage = new ImageIcon(ImageIO.read(selectedFile));

                        break; // Exit the loop if the file is valid
                    } else {
                        JOptionPane.showMessageDialog(this, "Please select a valid image file.");
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                break;
            }
        }
    }

    //TODO copied from LoginFrame, maybe there's a better solution?
    private boolean isValidFormat(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
    }
}
