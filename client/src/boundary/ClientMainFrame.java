package boundary;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
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
    private JPanel usersPanel;
    private JTable receiverList;
    private JScrollPane receiverListScrollPane;
    private ImageIcon messageImage;
    private String username;
    private Color baseColor = new Color(70, 70, 70);
    private Color mainColor = new Color(45, 45, 50);
    private Color highlightColor = new Color(100, 95, 95);
    private Color greenHighlightColor = new Color(100, 150, 100);
    private Color textColor = new Color(200, 200, 200);
    private Color buttonBorder = new Color(115, 110, 110);

    public ClientMainFrame(ClientMainView mainView, String username) {
        this.username = username;
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
    }

    public void addUserModel(Object[][] usersInfo) {
        DefaultTableModel model = (DefaultTableModel) userList.getModel();
        if (model.getRowCount() > 0) { // fix for duplicating rows
            model.setRowCount(0);
        }
        for (Object[] row : usersInfo) {
            model.addRow(row);
        }
    }

    public void addReceiverModel(Object[][] receiversInfo) {
        DefaultTableModel model = (DefaultTableModel) receiverList.getModel();
        if (model.getRowCount() > 0) { // fix for duplicating rows
            model.setRowCount(0);
        }
        for (Object[] row : receiversInfo) {
            model.addRow(row);
        }
    }

    public void addContact(String username) {
        mainView.addContact(username);
    }

    public void removeContact(String username) {
        mainView.removeContact(username);
    }

    public void toggleReceiver(String username) {
        mainView.toggleReceiver(username);
    }

    private void onSend() {
        if (!(messageTextInput.getText().isBlank() && messageImage == null)) {
            if(messageTextInput.getText().length() < 250) {
                mainView.createMessage(messageTextInput.getText(), messageImage);
                messageTextInput.setText("");
                buttonInsert.setBackground(highlightColor);
            } else {
                JOptionPane.showMessageDialog(this, "Message too long!");
            }
        }
    }

    private void onInsert() {
        selectMessageImage();
        if (messageImage != null) {
            buttonInsert.setBackground(greenHighlightColor);
        }
    }

    private void onExit() {
        mainView.notifyServerOnExit();
        dispose();
    }

    private void setupColors() {

        contentPane.setBackground(baseColor);
        topPanel.setBackground(baseColor);
        buttonPanel.setBackground(baseColor);
        bottomPanel.setBackground(baseColor);
        usersPanel.setBackground(baseColor);
        messageWindowPanel.setBackground(mainColor);
        messageWindow.setBackground(mainColor);
        userList.setBackground(mainColor);
        contactList.setBackground(mainColor);
        receiverList.setBackground(mainColor);
        messageTextInput.setBackground(highlightColor);
        buttonInsert.setBackground(highlightColor);
        buttonSend.setBackground(highlightColor);
        buttonSend.setBorder(new MatteBorder(2, 2, 2, 2, buttonBorder));
        buttonInsert.setBorder(new MatteBorder(2, 2, 2, 2, buttonBorder));
        messageTextInput.setForeground(textColor);
        messageWindow.setForeground(textColor);
        userList.setForeground(textColor);
        contactList.setForeground(textColor);
        receiverList.setForeground(textColor);

        messageWindowScrollPane.setBackground(mainColor);
        messageWindowScrollPane.getViewport().setBackground(mainColor);
        messageWindowScrollPane.setBorder(new MatteBorder(4, 4, 4, 4, mainColor));
        contactListScrollPane.setBackground(mainColor);
        contactListScrollPane.getViewport().setBackground(mainColor);
        contactListScrollPane.setBorder(new MatteBorder(4, 4, 4, 4, mainColor));
        userListScrollPane.setBackground(mainColor);
        userListScrollPane.getViewport().setBackground(mainColor);
        userListScrollPane.setBorder(new MatteBorder(4, 4, 4, 4, mainColor));
        receiverListScrollPane.setBackground(mainColor);
        receiverListScrollPane.getViewport().setBackground(mainColor);
        receiverListScrollPane.setBorder(new MatteBorder(4, 4, 4, 4, mainColor));

        JTableHeader contactsHeader = contactList.getTableHeader();
        contactsHeader.setBackground(highlightColor);
        contactsHeader.setForeground(textColor);
        contactsHeader.setBorder(new MatteBorder(2,2,2,2, buttonBorder));
        JTableHeader usersHeader = userList.getTableHeader();
        usersHeader.setBackground(highlightColor);
        usersHeader.setForeground(textColor);
        usersHeader.setBorder(new MatteBorder(2, 2, 2, 2, buttonBorder));
        JTableHeader receiversHeader = receiverList.getTableHeader();
        receiversHeader.setBackground(highlightColor);
        receiversHeader.setForeground(textColor);
        receiversHeader.setBorder(new MatteBorder(2, 2, 2, 2, buttonBorder));
    }

    private void setupTables() {
        messageWindow.setModel(createTableModel(4, null));
        TableColumn messageColumn = messageWindow.getColumnModel().getColumn(0);
        messageColumn.setPreferredWidth(500);
        messageColumn.setCellRenderer(new WordWrapRenderer(mainColor,textColor,messageWindow.getFont()));
        contactList.setModel(createTableModel(3, "Contacts"));
        userList.setModel(createTableModel(3, "Online"));
        receiverList.setModel(createTableModel(1, "Receivers"));

        messageWindow.getTableHeader().setUI(null);
    }

    private void setupTableListeners() {
        contactList.getSelectionModel().addListSelectionListener(e -> {
            if(contactList.getSelectedColumn() == 2) {
                removeContact((String) contactList.getValueAt(contactList.getSelectedRow(), 1));
            } else if (contactList.getSelectedColumn() == 0 || contactList.getSelectedColumn() == 2) {
                toggleReceiver((String) contactList.getValueAt(contactList.getSelectedRow(), 1));
            }
        });

        userList.getSelectionModel().addListSelectionListener(e -> {
            if(userList.getSelectedColumn() == 2) {
                addContact((String) userList.getValueAt(userList.getSelectedRow(), 1));
            } else if (userList.getSelectedColumn() == 0 || userList.getSelectedColumn() == 1) {
                toggleReceiver((String) userList.getValueAt(userList.getSelectedRow(), 1));
            }
        });

        receiverList.getSelectionModel().addListSelectionListener(e ->
                        toggleReceiver((String) receiverList.getValueAt(receiverList.getSelectedRow(), 0))
                );
    }

    private DefaultTableModel createTableModel(int columnNbr, String tableName) {
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

            @Override
            public String getColumnName(int column) {
                if(columnNbr == 1) {
                    String[] titles = {tableName};
                    return titles[column];
                }
                String[] titles = {"", tableName, "", ""};
                return titles[column];
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
