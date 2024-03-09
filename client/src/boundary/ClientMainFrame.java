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
    private JScrollPane messageWindowScrollPane;
    private JPanel messageWindowPanel;
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
        messageWindow.getTableHeader().setUI(null);

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
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });

        // call onSend on ENTER
        messageTextInput.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                "sendMessage");
        messageTextInput.getActionMap().put("sendMessage", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSend();
            }
        });
    }

    public void addMessageRow(Object[] messageInfo) {
        //TODO remove test print
        for (Object object : messageInfo) {
            System.out.println("MainFrame Object Read: " + object.toString());
        }
        DefaultTableModel tableModel = (DefaultTableModel) messageWindow.getModel();
        tableModel.addRow(messageInfo);

        //scroll to bottom for every new message
        SwingUtilities.invokeLater(() ->
                messageWindow.scrollRectToVisible(messageWindow.getCellRect(messageWindow.getRowCount()-1, 0, true)));
    }

    private void onSend() {
        if(!(messageTextInput.getText().isBlank() && messageImage == null)) {
            mainView.createMessage(messageTextInput.getText(), messageImage);
            messageTextInput.setText("");
            buttonInsert.setBackground(new Color(100,95,95));
        }
    }

    private void onInsert() {
        selectMessageImage();
        if(messageImage != null) {
            buttonInsert.setBackground(new Color(100, 150, 100));
        }
    }

    private void onExit() {
        dispose();
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
        messageWindowPanel.setBackground(mainColor);
        messageWindow.setBackground(mainColor);
        messageWindowScrollPane.setBackground(mainColor);
        messageWindowScrollPane.getViewport().setBackground(mainColor);
        messageWindowScrollPane.setBorder(new MatteBorder(4,4,4,4, mainColor));
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

    public void resetInsert() {
        messageImage = null;
        buttonInsert.setBackground(new Color(100,95,95));
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
                        messageImage = new ImageIcon(ImageIO.read(selectedFile).getScaledInstance(100, 100, Image.SCALE_SMOOTH));

                        break; // Exit the loop if the file is valid
                    } else {
                        resetInsert();
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
