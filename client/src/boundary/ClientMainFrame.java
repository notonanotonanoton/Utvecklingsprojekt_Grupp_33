package boundary;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    private JScrollPane userListScrollPane;
    private JScrollPane contactListScrollPane;
    private JTable contactList;
    private ImageIcon messageImage;
    private Color baseColor = new Color(70, 70, 70);
    private Color mainColor = new Color(45, 45, 50);
    private Color highlightColor = new Color(100, 95, 95);
    private Color greenHighlightColor = new Color(100, 150, 100);
    private Color textColor = new Color(200, 200, 200);
    private Color buttonBorder = new Color(115, 110, 110);

    public ClientMainFrame(ClientMainView mainView) {
        this.mainView = mainView;
        setResizable(false);
        setSize(1280, 720);
        add(contentPane);

        setupTables();
        setupTableListeners();
        setupColors();

        setVisible(true);


        buttonSend.addActionListener(e -> onSend());

        buttonInsert.addActionListener(e -> onInsert());

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

        scrollTableToBottom(messageWindow);
    }

    public void scrollTableToBottom(JTable table) {
        SwingUtilities.invokeLater(() ->
                table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true)));
    }

    public void addContactsModel(Object[][] contactsInfo) {
        DefaultTableModel model = (DefaultTableModel) contactList.getModel();
        if (model.getRowCount() > 0) {
            model.setRowCount(0);
        }
        for (Object[] row : contactsInfo) {
            model.addRow(row);
        }
        scrollTableToBottom(contactList);
    }

    public void addUserModel(Object[][] usersInfo) {
        DefaultTableModel model = (DefaultTableModel) userList.getModel();
        if (model.getRowCount() > 0) { // fix for duplicating rows
            model.setRowCount(0);
        }
        for (Object[] row : usersInfo) {
            model.addRow(row);
        }
        scrollTableToBottom(userList);
    }

    public void addContact(String username) {
        mainView.addContact(username);
    }

    public void removeContact(String username) {
        mainView.removeContact(username);
    }

    public void addOrRemoveReceiver(String username) {
        mainView.addOrRemoveReceiver(username);
    }

    private void onSend() {
        if (!(messageTextInput.getText().isBlank() && messageImage == null)) {
            mainView.createMessage(messageTextInput.getText(), messageImage);
            messageTextInput.setText("");
            buttonInsert.setBackground(highlightColor);
        }
    }

    private void onInsert() {
        selectMessageImage();
        if (messageImage != null) {
            buttonInsert.setBackground(greenHighlightColor);
        }
    }

    private void onExit() {
        dispose();
    }

    private void setupColors() {

        contentPane.setBackground(baseColor);
        topPanel.setBackground(baseColor);
        buttonPanel.setBackground(baseColor);
        bottomPanel.setBackground(baseColor);
        messageWindowPanel.setBackground(mainColor);
        messageWindow.setBackground(mainColor);
        userList.setBackground(mainColor);
        contactList.setBackground(mainColor);
        messageTextInput.setBackground(highlightColor);
        buttonInsert.setBackground(highlightColor);
        buttonSend.setBackground(highlightColor);
        buttonSend.setBorder(new MatteBorder(2, 2, 2, 2, buttonBorder));
        buttonInsert.setBorder(new MatteBorder(2, 2, 2, 2, buttonBorder));
        messageTextInput.setForeground(textColor);
        messageWindow.setForeground(textColor);
        userList.setForeground(textColor);
        contactList.setForeground(textColor);

        messageWindowScrollPane.setBackground(mainColor);
        messageWindowScrollPane.getViewport().setBackground(mainColor);
        messageWindowScrollPane.setBorder(new MatteBorder(4, 4, 4, 4, mainColor));
        contactListScrollPane.setBackground(mainColor);
        contactListScrollPane.getViewport().setBackground(mainColor);
        contactListScrollPane.setBorder(new MatteBorder(4, 4, 4, 4, mainColor));
        userListScrollPane.setBackground(mainColor);
        userListScrollPane.getViewport().setBackground(mainColor);
        userListScrollPane.setBorder(new MatteBorder(4, 4, 4, 4, mainColor));
    }

    private void setupTables() {
        messageWindow.setModel(createTableModel(4));
        contactList.setModel(createTableModel(3));
        userList.setModel(createTableModel(3));

        messageWindow.getTableHeader().setUI(null);
        contactList.getTableHeader().setUI(null);
        userList.getTableHeader().setUI(null);
    }

    private void setupTableListeners() {
        contactList.getSelectionModel().addListSelectionListener(e -> {
            if(contactList.getSelectedColumn() == 2) {
                removeContact((String) contactList.getValueAt(contactList.getSelectedRow(), 1));
            } else if (contactList.getSelectedColumn() == 0 || contactList.getSelectedColumn() == 2) {
                addOrRemoveReceiver((String) contactList.getValueAt(contactList.getSelectedRow(), 1));
            }
        });

        userList.getSelectionModel().addListSelectionListener(e -> {
            if(userList.getSelectedColumn() == 2) {
                addContact((String) userList.getValueAt(userList.getSelectedRow(), 1));
            } else if (userList.getSelectedColumn() == 0 || userList.getSelectedColumn() == 2) {
                addOrRemoveReceiver((String) userList.getValueAt(userList.getSelectedRow(), 1));
            }
        });
    }

    private DefaultTableModel createTableModel(int columnNbr) {
        return new DefaultTableModel(0, columnNbr) {
            //override needed to render ImageIcons in JTable
            @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void resetInsert() {
        messageImage = null;
        buttonInsert.setBackground(highlightColor);
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
